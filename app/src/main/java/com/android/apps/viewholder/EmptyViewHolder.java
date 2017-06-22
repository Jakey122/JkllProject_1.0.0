package com.android.apps.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.apps.adapter.BaseRecyclerAdapter;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

/**
 * Created by root on 16-8-23.
 */
public class EmptyViewHolder extends BaseViewHolder {

    View mItemView;

    public EmptyViewHolder(View itemView, BaseRecyclerAdapter adapter) {
        super(itemView);
        mAdapter = adapter;
    }

    @Override
    protected void initViewHolder(View itemView, Object... obj) {
        mItemView = itemView;
    }

    @Override
    public void bindViewHolder(Context context, BaseInfo info, View.OnClickListener listener) {
        int resId = R.color.white;
        /*int type = mAdapter.getType();
        int resId = R.color.divide;
        switch (type) {
            case PageId.Page_Download.PAGE_DOWNLOAD_COMPLETED:
            case PageId.PageGift.PAGE_GIFT_MINE:
                resId = R.color.white;
                break;
        }*/
//        mItemView.setBackgroundColor(ContextCompat.getColor(context, resId));
    }

    @Override
    public void onHolderRecycled() {

    }
}
