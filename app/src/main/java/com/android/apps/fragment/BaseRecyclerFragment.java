package com.android.apps.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.android.apps.activity.LoginActivity;
import com.android.apps.adapter.BaseRecyclerAdapter;
import com.android.apps.bean.LoadInfo;
import com.android.apps.util.AppUtil;
import com.sdk.bean.PageInfo;
import com.android.apps.helper.LoadViewHelper;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.PageId;
import com.android.apps.view.LoadView;
import com.android.apps.viewholder.BaseViewHolder;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.RequestResult;
import com.sdk.net.ResultHandler;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 16-5-19.
 */
public abstract class BaseRecyclerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final int LAYOUT_LIST_VERTICAL = 0;
    public static final int LAYOUT_LIST_HORIZONTAL = 1;
    public static final int LAYOUT_GRID_VERTICAL = 2;
    public static final int LAYOUT_GRID_HORIZONTAL = 3;
    public static final int LAYOUT_STAGGERED_VERTICAL = 4;
    public static final int LAYOUT_STAGGERED_HORIZONTAL = 5;
    public static final int DEFAULT_SPANCOUNT = 2 * 3 * 4 * 5 * 6;

    private static final int MSG_REFRESH_LIST = -1000;
    private static final int MSG_REFRESH_FOOTER = -1001;

    protected SwipeRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseRecyclerAdapter mAdapter;
    protected LoadView mLoadView;

    private RecyclerView.LayoutManager mLayoutManager;
    private ScrollHelper mScrollHelper;
    private SpanSizeLookupHelper mSpanHelper;
    private FragmentDataLoader mDataLoader;
    private PageInfo mPageInfo;
    private boolean isLoading;

    private int pageIndex = 1;
    private int pageSize = 20;
    private int beginLastVisibleItem;
    private int lastVisibleItem;
    private int firstVisibleItem;
    private int scrollDy;
    private boolean showed = false;

    private ConcurrentHashMap<String, Future<?>> mTasks = new ConcurrentHashMap<>();
    private ExecutorService mThreadPool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScrollHelper = new ScrollHelper();
        mSpanHelper = new SpanSizeLookupHelper();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mThreadPool = Executors.newFixedThreadPool(3);
        initLoadView();
        initRefreshLayout();
    }

    @Override
    public void onResume() {
        if (!isResume) {
            setupScrollHelper(); //设置滑动监听
            if (mAdapter != null) mAdapter.onAdapterResume(String.valueOf(getFragmentType()));
        }
        super.onResume();
//        if (isEmpty() && NetworkStatus.getInstance().isConnected() && !isNetworkConnect)
//            handleConnectionStateChanged(0, true, isNetworkConnect);
    }

    @Override
    public void onDestroy() {
        removeScrollHelper(); //取消滑动监听
        if (mAdapter != null) mAdapter.onAdapterDestroy();
        if (mThreadPool != null) mThreadPool.shutdownNow();
        mDataLoader = null;
        mScrollHelper = null;
        mSpanHelper = null;
        mDataLoader = null;
        super.onDestroy();
    }

    public BaseRecyclerAdapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onRefresh() {
        onMyRefresh();
    }

    /**
     * 处理Fragment内view高度
     */
    @Override
    public void handlerViewTreeObserver() {

    }

    @Override
    public void setData(RequestResult result) {
        pageIndex = 1;
        final boolean nullList = result == null || result.result == null || result.result.size() == 0;
        refreshDataList(false, nullList, result);
        if (nullList && isNetworkConnect)
            refreshFooterView(LoadViewHelper.LOAD_STATE_DATAERROR);
    }

    @Override
    public int getFirstItemPosition() {
        RecyclerView.LayoutManager manager = getLayoutManager();
        if (manager != null) {
            if (manager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
            } else if (manager instanceof GridLayoutManager) {
                return ((GridLayoutManager) manager).findFirstVisibleItemPosition();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int[] indexs = ((StaggeredGridLayoutManager) manager).findFirstVisibleItemPositions(null);
                return indexs[0];
            }
        }
        return -1;
    }

    @Override
    public int getLastItemPosition() {
        RecyclerView.LayoutManager manager = getLayoutManager();
        if (manager != null) {
            if (manager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) manager).findLastVisibleItemPosition();
            } else if (manager instanceof GridLayoutManager) {
                return ((GridLayoutManager) manager).findLastVisibleItemPosition();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int[] indexs = ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(null);
                return indexs[indexs.length - 1];
            }
        }
        return -1;
    }

    @Override
    public void handleViewHolderAnimator() {
        if (mAdapter != null) {
            final ConcurrentHashMap<BaseViewHolder, Object> holderMap = mAdapter.getKeepedHolder();
            if (holderMap != null && holderMap.size() > 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Iterator it = holderMap.keySet().iterator();
                        while (it.hasNext()) {
                            BaseViewHolder viewHolder = (BaseViewHolder) it.next();
                            if (viewHolder != null) viewHolder.showHolderAnimation();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void handleConnectionStateChanged(int state, boolean isConnect, boolean lastState) {
        isNetworkConnect = isConnect;
        if (isResume) {
            if (isConnect && !lastState && isEmpty()) {
                if (mLoadView != null) {
                    mLoadView.setState(LoadView.STATE_LOADING);
                }
                isLoading = false;
                loadData(false);
            }
            refreshFooterView(isNetworkConnect ?
                    LoadViewHelper.LOAD_STATE_NORMAL : LoadViewHelper.LOAD_STATE_NETERROR);
        }
        handleHttpListenerOnNetChanged(state, isConnect, lastState);
    }

    @Override
    protected void handleHttpListenerOnNetChanged(int state, boolean isConnect, boolean lastState) {

    }

    /**
     * 设置RecyclerView LayoutManager类型
     * 如果是GridLayoutManager类型则设置一个spanSizeLookup
     *
     * @param layoutType
     */
    protected void setLayoutType(int layoutType, long pageType) {
        switch (layoutType) {
            case LAYOUT_LIST_VERTICAL:
                mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
                break;
            case LAYOUT_LIST_HORIZONTAL:
                mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
                break;
            case LAYOUT_GRID_VERTICAL:
                mLayoutManager = new GridLayoutManager(mActivity, DEFAULT_SPANCOUNT, GridLayoutManager.VERTICAL, false);
                break;
            case LAYOUT_GRID_HORIZONTAL:
                mLayoutManager = new GridLayoutManager(mActivity, DEFAULT_SPANCOUNT, GridLayoutManager.HORIZONTAL, false);
                break;
        }

        if (mRecyclerView != null) {
            if (mLayoutManager instanceof GridLayoutManager) {
                ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(mSpanHelper);
            }
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }

    /**
     * 获得当前LayoutManager
     *
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /**
     * 获取数据如果正在加载或已经全部加载完跳出
     *
     * @param showMore
     */
    protected void loadData(boolean showMore) {
        if (isLoading) return;
        if (!continueLoadData(showMore)) return;
        mDataLoader = new FragmentDataLoader(showMore);
        execute("loadData", mDataLoader);
    }

    /**
     * 设置分页信息元素
     *
     * @param page
     */
    protected void setPageInfo(PageInfo page) {
        mPageInfo = page;
    }

    /**
     * 获得当前页码
     *
     * @return
     */
    protected int getPageIndex() {
        return pageIndex;
    }

    /**
     * 获得当前一页元素数量
     *
     * @return
     */
    protected int getPageSize() {
        return pageSize;
    }

    /**
     * 判断是否已经全部加载完毕
     *
     * @param showMore
     * @return
     */
    protected boolean continueLoadData(boolean showMore) {
        if (mPageInfo != null) {
            refreshFooterView(isNetworkConnect ?
                    LoadViewHelper.LOAD_STATE_NORMAL : LoadViewHelper.LOAD_STATE_NETERROR);
            pageSize = mPageInfo.getPageEveryPageSize();
            if (showMore && getPageIndex() >= mPageInfo.getPageTotalPageCount()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 设置RecyclerView滑动监听
     */
    private void setupScrollHelper() {
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(mScrollHelper);
        }
    }

    /**
     * 取消RecyclerView滑动监听
     */
    private void removeScrollHelper() {
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(mScrollHelper);
        }
    }

    /**
     * 处理GridLayoutManager item形态变化
     *
     * @param position
     * @return
     */
    protected int spanSizeHandler(int position) {
        if (!(getLayoutManager() instanceof GridLayoutManager)) return 0;
        BaseInfo info = mAdapter.getItem(position);
        if (info == null) return 0;
        int spanCount = info.getItemSpanCount();
        if (info.getItemViewType() == BaseInfo.ITEM_TYPE_GRID) {
            return DEFAULT_SPANCOUNT % spanCount == 0 ? DEFAULT_SPANCOUNT / spanCount : 0;
        }
        return ((GridLayoutManager) getLayoutManager()).getSpanCount();
    }

    /**
     * 处理RecyclerView滑动事件
     *
     * @param recyclerView
     * @param dx
     * @param dy
     */
    protected void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        lastVisibleItem = getLastItemPosition();
        firstVisibleItem = getFirstItemPosition();

        if (scrollDy > 0 && dy < 0 || scrollDy < 0 && dy > 0) {
            beginLastVisibleItem = getLastItemPosition();
            scrollDy = 0;
        } else {
            scrollDy = beginLastVisibleItem - lastVisibleItem == 0 ? 0 : dy;
        }

        /*if (lastVisibleItem == 20 && !showed) {
            showed = true;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    BaseFragment fragment = MFragmentManager.getFragment(getFragmentType());
                    if (fragment != null && fragment instanceof BaseRecyclerFragment)
                        FloatingManager.getInstance(mActivity).showBoxAnimation((BaseRecyclerFragment) fragment);
                }
            });
        }*/
    }

    /**
     * 处理RecyclerView滑动状态变化
     *
     * @param recyclerView
     * @param newState
     */
    protected void onRecyclerViewScrollStateChanged(RecyclerView recyclerView, int newState) {
         /*newState == RecyclerView.SCROLL_STATE_IDLE && */
        if (lastVisibleItem + 1 == mAdapter.getItemCount()) {
            loadData(true);
        }

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            beginLastVisibleItem = getLastItemPosition();
            scrollDy = 0;
        }
    }

    /**
     * 处理SwipeRefreshLayout 刷新事件
     */
    protected abstract void onMyRefresh();

    /**
     * 处理请求网络数据
     *
     * @return
     */
    protected abstract RequestResult requestData(boolean showMore);

    /**
     * 处理解析请求回来的数据
     *
     * @param list
     * @param showMore
     */
    protected abstract void parseData(List<? extends BaseInfo> list, boolean showMore);

    private void execute(String key, FragmentDataLoader runnable) {
        if (mThreadPool == null || runnable == null) return;
        try {
            Future<?> sFuture = mThreadPool.submit(runnable);
            if (!TextUtils.isEmpty(key)) {
                if (mTasks.contains(key)) {
                    Future<?> tempFuture = mTasks.get(key);
                    tempFuture.cancel(true);
                }
                mTasks.put(key, sFuture);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * RecyclerView滑动监听类
     */
    class ScrollHelper extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            onRecyclerViewScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            onRecyclerViewScrollStateChanged(recyclerView, newState);
        }
    }

    /**
     * 处理GridLayoutManager形态变化类
     */
    class SpanSizeLookupHelper extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            return spanSizeHandler(position);
        }
    }

    /**
     * 数据请求线程类
     */
    class FragmentDataLoader implements Runnable {

        boolean showMore = false;

        public FragmentDataLoader(boolean showmore) {
            isLoading = true;
            showMore = showmore;
        }

        @Override
        public void run() {
            try {
                pageIndex = showMore ? pageIndex + 1 : 1;
                final RequestResult result = requestData(showMore);
                if (result != null) {
                    final boolean nullList = result.resultStatus < 0 || result.result == null || result.result.size() == 0;
                    pageIndex = nullList && showMore ? pageIndex - 1 : pageIndex;
                    refreshDataList(showMore, nullList, result);
                    if (nullList && isNetworkConnect)
                        refreshFooterView(LoadViewHelper.LOAD_STATE_DATAERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void handlerMyMessage(Message msg) {
        switch (msg.what) {
            case MSG_REFRESH_LIST:
                boolean showMore = msg.getData().getBoolean("showMore", false);
                RequestResult result = (RequestResult) msg.obj;
                refreshLoadView(result, showMore);
                if(AppUtil.checkResult(mActivity, getFragmentType(), result.resultStatus))
                    parseData(result.result, showMore);
                isLoading = false;
                break;
            case MSG_REFRESH_FOOTER:
                int state = msg.getData().getInt("state", LoadViewHelper.LOAD_STATE_NORMAL);
                LoadViewHelper.refreshLoadingState(state, this);
                break;
        }
    }

    protected void refreshDataList(boolean showMore, boolean nullList, RequestResult result) {
        mHandler.removeMessages(MSG_REFRESH_LIST);
        Message msg = mHandler.obtainMessage(MSG_REFRESH_LIST);
        msg.getData().putBoolean("showMore", showMore);
        msg.getData().putBoolean("nullList", nullList);
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    protected void refreshFooterView(int state) {
        mHandler.removeMessages(MSG_REFRESH_FOOTER);
        Message msg = mHandler.obtainMessage(MSG_REFRESH_FOOTER);
        msg.getData().putInt("state", state);
        mHandler.sendMessage(msg);
    }

    /**
     * 初始化loadview
     */
    protected void initLoadView() {
        int type = getFragmentType();
        if (type == PageId.PageMine.PAGE_MINE_FEEDBACK) return;
        try {
            mLoadView = (LoadView) getView().findViewById(R.id.loadview);
            mLoadView.setCurrentFragment(this);
        } catch (Exception e) {
        }
        LoadViewHelper.initLoadView(mLoadView, type);
    }

    protected void initRefreshLayout() {
        try {
            mRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh);
        } catch (Exception e) {
        }
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            mRefreshLayout.setOnRefreshListener(this);
            mRefreshLayout.setEnabled(false);
            mRefreshLayout.setNestedScrollingEnabled(false);
        }
    }

    /**
     * 刷新loadview
     *
     * @param result
     */
    protected void refreshLoadView(RequestResult result, boolean showMore) {
        LoadViewHelper.refreshLoadView(mLoadView, result, this, showMore);
    }

    public boolean isEmpty() {
        return mAdapter == null || (mAdapter != null && mAdapter.getItemCount() == 0);
    }

    public void handleRefreshLoad() {
//        mRefreshLayout.setRefreshing(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData(false);
            }
        }, 800);
    }

    public LoadInfo initLoadingState(int state) {
        return LoadViewHelper.getTargetLoadInfo(state, this);
    }

    @Override
    public boolean isLoading() {
        return mLoadView.getVisibility() != View.GONE;
    }

    public LoadView getLoadView() {
        return mLoadView;
    }
}
