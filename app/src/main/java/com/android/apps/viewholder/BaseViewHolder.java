package com.android.apps.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.apps.adapter.BaseRecyclerAdapter;
import com.sdk.bean.BaseInfo;

/**
 * Created by root on 16-5-16.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    protected int mScreenWidth;
    protected BaseRecyclerAdapter mAdapter;

    public BaseViewHolder(View itemView, Object... obj) {
        super(itemView);
        mScreenWidth = itemView.getContext().getResources().getDisplayMetrics().widthPixels;
        initViewHolder(itemView, obj);
    }

    /**
     * 初始化ViewHolder布局元素
     *
     * @param itemView
     * @param obj
     */
    protected abstract void initViewHolder(View itemView, Object... obj);

    /**
     * 绑定Viewholder数据
     *
     * @param context
     * @param info
     * @param listener
     */
    public abstract void bindViewHolder(Context context, BaseInfo info, View.OnClickListener listener);

    /**
     * 处理Viewholder复用
     */
    public abstract void onHolderRecycled();

    public void onHolderDestroy() {
        mAdapter = null;
    }

    public void viewDetachedFromWindow() {

    }

    public void showHolderAnimation() {

    }

}
