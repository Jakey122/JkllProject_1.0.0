package com.android.apps.adapter;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdk.bean.PageInfo;
import com.android.apps.control.BeanClickHelper;
import com.android.apps.listener.OnAdapterItemClickListener;
import com.android.apps.util.PageId;
import com.android.apps.view.ListLoadingView;
import com.android.apps.viewholder.BaseViewHolder;
import com.android.apps.viewholder.EmptyViewHolder;
import com.android.apps.viewholder.FooterViewHolder;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 16-5-17.
 */
public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener {

    private final int VIEW_TYPE_BASE = 30;
    protected final int VIEW_TYPE_EMPTY = VIEW_TYPE_BASE;               //空
    protected final int VIEW_TYPE_LOADING = VIEW_TYPE_BASE + 1;               //上滑正在加载
    protected final int VIEW_TYPE_END = VIEW_TYPE_BASE + 2;                   //分页结束

    protected final int VIEW_TYPE_DEVICE = VIEW_TYPE_BASE + 3;                   //设备
    protected final int VIEW_TYPE_ORDER = VIEW_TYPE_BASE + 4;                   //订单
    protected final int VIEW_TYPE_QUESTION = VIEW_TYPE_BASE + 5;                   //常见问题

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<BaseInfo> mList;
    protected RecyclerView mRecyclerView;
    protected long mFrom;
    protected int mType;
    protected OnAdapterItemClickListener mItemListener;

    private ConcurrentHashMap<BaseViewHolder, Object> mHolderMap = new ConcurrentHashMap();

    public BaseRecyclerAdapter(final Context context, RecyclerView recyclerView) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRecyclerView = recyclerView;
        mList = new ArrayList<>();
    }

    /**
     * 绑定ViewHolder布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        View itemView = null;
        int height = 50;
        switch (viewType) {
            case VIEW_TYPE_LOADING:
                holder = new FooterViewHolder(new ListLoadingView(mContext, height, R.string.list_loading_normal));
                break;
            case VIEW_TYPE_EMPTY:
                holder = new EmptyViewHolder(new View(mContext), this);
                break;
            case VIEW_TYPE_END:
                holder = new EmptyViewHolder(new ListLoadingView(mContext, height, R.string.list_end), this);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        BaseInfo info = getItem(position);
        if (info == null) return VIEW_TYPE_EMPTY;
        if (info instanceof PageInfo) {
            if (isNoEndPage()) return VIEW_TYPE_EMPTY;

            if (info.getPageCurrentIndex() < info.getPageTotalPageCount() + 1) {
                return VIEW_TYPE_LOADING;
            } else /*if (info.getPageCurrentIndex() == info.getPageTotalPageCount() + 1)*/ {
                return VIEW_TYPE_END;
            }
        }
        return VIEW_TYPE_EMPTY;
    }

    /**
     * viewHolder绑定数据在ViewHolder的bindViewHolder方法中处理
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        holder.bindViewHolder(mContext, getItem(position), this);
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.viewDetachedFromWindow();
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.showHolderAnimation();
    }

    /**
     * 处理ViewHolder复用
     *
     * @param holder
     */
    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onHolderRecycled();
    }

    /**
     * 获取列表元素个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    /**
     * 处理点击事件 正常逻辑在子类实现的onItemClick方法中处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        onItemClick(v);
        if (mItemListener != null) mItemListener.onAdapterItemClick(v);
    }

    public void setOnAdapterItemClickListener(OnAdapterItemClickListener listener) {
        mItemListener = listener;
    }

    public OnAdapterItemClickListener getOnAdapterItemClickListener() {
        return mItemListener;
    }

    protected void removeOnAdapterItemClickListener() {
        mItemListener = null;
    }

    protected boolean isNoEndPage() {
        switch (mType) {
            case PageId.PageMain.PAGE_DEVICE:
                if (getItemCount() < 10) return true;
                break;
            case PageId.PageMain.PAGE_ORDER:
                if (getItemCount() < 10) return true;
                break;
        }
        return false;
    }

    /**
     * 将自己加入下载监听map
     *
     * @param tag
     */
    public void onAdapterResume(String tag) {

    }

    /**
     * 销毁自己持有的viewholder，将自己从下载监听map 移除
     */
    public void onAdapterDestroy() {
        if (mHolderMap != null && mHolderMap.size() > 0) {
            Iterator it = mHolderMap.keySet().iterator();
            while (it.hasNext()) {
                BaseViewHolder holder = (BaseViewHolder) it.next();
                if (holder != null) holder.onHolderDestroy();
                mHolderMap.remove(holder);
            }
        }
//        mHandler = null;
        if (mList != null) mList.clear();
//        mList = null;
        mContext = null;
        mLayoutInflater = null;
        removeOnAdapterItemClickListener();
    }

    public void keepViewHolder(BaseViewHolder holder) {
        if (holder != null && !mHolderMap.containsKey(holder)) {
            mHolderMap.put(holder, this);
        }
    }

    public ConcurrentHashMap<BaseViewHolder, Object> getKeepedHolder() {
        return mHolderMap;
    }

    /**
     * 获得素材元素
     *
     * @param position
     * @return
     */
    public BaseInfo getItem(int position) {
        if (position < 0 || mList == null || position >= mList.size()) return new BaseInfo();
        return mList.get(position);
    }

    public BaseInfo getItem(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        return getItem(position);
    }

    public int getItemPosition(BaseInfo info) {
        if (mList.contains(info)) {
            return mList.indexOf(info);
        }
        return -1;
    }

    public void updateListItem(int position, BaseInfo newInfo) {
        if (mList != null && position < mList.size()) {
            mList.set(position, newInfo);
        }
    }

    /**
     * 插入一个素材元素并只刷新插入项
     *
     * @param info
     */
    public void addItem(BaseInfo info) {
        if (info != null) {
            mList.add(info);
            notifyDataSetChanged();
//            int position = mList.indexOf(info);
//            notifyItemRangeChanged(position - 1, 2);
//            notifyItemInserted(position);
        }
    }

    /**
     * 指定位置插入一个素材元素并只刷新插入项
     *
     * @param info
     */
    public synchronized void addItem(int position, BaseInfo info) {
        if (info != null) {
            mList.add(position, info);
            notifyItemRangeChanged(position, 2);
        }
    }

    /**
     * 重新插入全部素材元素刷新整个列表
     *
     * @param list
     */
    public void addAll(List<? extends BaseInfo> list) {
        if (list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
        } else {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 插入一组素材元素
     *
     * @param list
     */
    public void addItems(List<? extends BaseInfo> list) {
        if (list != null && list.size() > 0) {
            removeEndInfo();
            for (BaseInfo info : list) {
                addItem(info);
            }
        }
    }

    public void removeItem(int position) {
        removeItem(getItem(position));
    }

    /**
     * 删除一个素材元素
     *
     * @param baseInfo
     */
    public void removeItem(BaseInfo baseInfo) {
        if (baseInfo != null && mList.contains(baseInfo)) {
            int position = mList.indexOf(baseInfo);
            mList.remove(baseInfo);
            notifyItemRemoved(position);
        }
    }

    /**
     * 删除分页元素
     */
    protected void removeEndInfo() {
        int index = getItemCount() - 1;
        if (index < 0) return;
        BaseInfo end = mList.get(index);
        if (end instanceof PageInfo) {
            removeItem(end);
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    protected void handleMyMessage(Message msg) {

    }

    /**
     * 处理点击事件
     *
     * @param v
     */
    protected void onItemClick(View v) {
        Object obj = v.getTag();
        if (obj == null || !(obj instanceof BaseInfo)) return;
        BaseInfo info = (BaseInfo) obj;
        BeanClickHelper.handleBeanClick(mContext, v, info, getType());
    }

    public int getType() {
        return mType;
    }
}
