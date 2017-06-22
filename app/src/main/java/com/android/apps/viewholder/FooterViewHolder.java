package com.android.apps.viewholder;

import android.content.Context;
import android.view.View;

import com.android.apps.view.ListLoadingView;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

/**
 * Created by root on 16-5-16.
 * <p/>
 * 底部加载或拉取结束
 */
public class FooterViewHolder extends BaseViewHolder {

    View mItemView;

    public FooterViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initViewHolder(View itemView, Object... obj) {
        mItemView = itemView;
    }

    @Override
    public void bindViewHolder(Context context, BaseInfo info, View.OnClickListener listener) {

    }

    @Override
    public void onHolderRecycled() {

    }

    public void onDataError() {
        if (mItemView != null && mItemView instanceof ListLoadingView)
            ((ListLoadingView) mItemView).setTitle(R.string.list_loading_dataerror);
    }

    public void onNetError() {
        if (mItemView != null && mItemView instanceof ListLoadingView)
            ((ListLoadingView) mItemView).setTitle(R.string.list_loading_neterror);
    }

    public void onLoading() {
        if (mItemView != null && mItemView instanceof ListLoadingView)
            ((ListLoadingView) mItemView).setTitle(R.string.list_loading_normal);
    }

}
