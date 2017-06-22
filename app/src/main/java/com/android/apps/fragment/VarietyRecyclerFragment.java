package com.android.apps.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.apps.adapter.MajorRecyclerAdapter;
import com.android.apps.animation.AnimationUtil;
import com.sdk.bean.PageInfo;
import com.android.apps.listener.OnAdapterItemClickListener;
import com.android.apps.listener.OnCheckedChangeListener;
import com.android.apps.listener.OnLoginChangeListener;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.RequestResult;
import com.sdk.net.HttpController;
import com.android.apps.recycler.ItemDivideDecoration;
import com.android.apps.util.PageId;
import com.jkll.app.R;

import java.util.List;

/**
 * Created by root on 16-5-9.
 */
public class VarietyRecyclerFragment extends BaseRecyclerFragment implements OnCheckedChangeListener, OnAdapterItemClickListener, OnLoginChangeListener {

    private long mSid;
    private int mType, mFrom;
    private int mLayoutType;
    private BaseInfo mBaseInfo;

    public static Bundle newArgument(long sid, int from, int type, int layoutType, BaseInfo baseInfo) {
        Bundle args = new Bundle();
        args.putLong("sid", sid);
        args.putInt("from", from);
        args.putInt("type", type);
        args.putInt("layoutType", layoutType);
        args.putParcelable("baseInfo", baseInfo);
        return args;
    }

    public static Bundle newArgument(long sid, int from, int type, int layoutType) {
        Bundle args = new Bundle();
        args.putLong("sid", sid);
        args.putInt("from", from);
        args.putInt("type", type);
        args.putInt("layoutType", layoutType);
        args.putParcelable("baseInfo", new BaseInfo());
        return args;
    }

    public static VarietyRecyclerFragment newInstance(Bundle args) {
        VarietyRecyclerFragment varietyRecyclerFragment = new VarietyRecyclerFragment();
        varietyRecyclerFragment.setArguments(args);
        return varietyRecyclerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_recycler, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new NullPointerException("Arguments is null!!!");
        }
        mSid = getArguments().getLong("sid", 0);
        mFrom = getArguments().getInt("from", 0);
        mType = getArguments().getInt("type", 0);
        mLayoutType = getArguments().getInt("layoutType", LAYOUT_LIST_VERTICAL);
        mBaseInfo = getArguments().getParcelable("baseInfo");

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MajorRecyclerAdapter(mActivity, mFrom, mType, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new ItemDivideDecoration(AnimationUtil.dip2px(mActivity, 7), mAdapter));
        setLayoutType(mLayoutType, mType);
        if(PageId.PageMain.PAGE_ORDER == getFragmentType()){
            mRecyclerView.setBackgroundColor(getResources().getColor(R.color.background));
        } else {
            mRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        }
//        这句话是为了，第一次进入页面的时候显示加载进度条
//        mRefreshLayout.setProgressViewOffset(false, 0,
//                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
//                getResources().getDisplayMetrics()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(false);
    }

    @Override
    protected void initRefreshLayout() {
        super.initRefreshLayout();
//        mRefreshLayout.setEnabled(true);
//        mRefreshLayout.setNestedScrollingEnabled(true);
    }

    @Override
    public void handlerViewTreeObserver() {

    }

    @Override
    protected void onRecyclerViewScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onRecyclerViewScrollStateChanged(recyclerView, newState);
    }

    @Override
    protected RequestResult requestData(boolean showMore) {
        RequestResult result = new RequestResult();
        switch (mType) {
            case PageId.PageMain.PAGE_DEVICE:
                result = HttpController.getInstance(mActivity).getDeviceListSync(getPageIndex());
                break;
            case PageId.PageMain.PAGE_ORDER:
                result = HttpController.getInstance(mActivity).getOrderSync(getPageIndex());
                break;
            case PageId.PageMine.PAGE_MINE_QUESTION:
                result = HttpController.getInstance(mActivity).getQuestionSync(getPageIndex());
                break;
        }
        if (result.result != null && result.result.size() > 0) {
            BaseInfo info = result.result.get(result.result.size() - 1);
            if (info instanceof PageInfo) {
                setPageInfo((PageInfo) info);
            }
        }
        return result;
    }

    @Override
    protected void parseData(List<? extends BaseInfo> list, boolean showMore) {
        if (showMore) {
            mAdapter.addItems(list);
        } else {
            mAdapter.addAll(list);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    protected boolean continueLoadData(boolean showMore) {
        boolean result = super.continueLoadData(showMore);
        if (!result) {
            mRefreshLayout.setRefreshing(false);
        }
        return result;
    }

    @Override
    protected void onMyRefresh() {
        loadData(false);
    }

    @Override
    protected void handlerMyMessage(Message msg) {
        super.handlerMyMessage(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onAdapterItemClick(View v) {
        int id = v.getId();
    }

    @Override
    public void onLoginChange(BaseInfo info) {
        loadData(false);
    }
}
