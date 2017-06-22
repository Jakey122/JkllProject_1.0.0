package com.android.apps.viewholder;

import android.content.Context;
import android.view.View;

import com.android.apps.adapter.BaseRecyclerAdapter;
import com.android.apps.listener.OnCheckedChangeListener;
import com.sdk.bean.AppInfo;
import com.sdk.bean.BaseInfo;

/**
 * Created by root on 16-5-16.
 * <p/>
 */
public class DeviceAddHolder extends BaseViewHolder implements View.OnClickListener {

    int mType, mSpanCount;
    View mItemView;
    OnCheckedChangeListener cclistener;

    public DeviceAddHolder(View itemView, int appType, BaseRecyclerAdapter adapter) {
        super(itemView, appType);
        mAdapter = adapter;
    }

    @Override
    protected void initViewHolder(View itemView, Object... obj) {
        mType = (obj != null && obj.length > 0) ? (int) obj[0] : AppInfo.ITEM_TYPE_LIST;
        mItemView = itemView;
    }

    @Override
    public void bindViewHolder(final Context context, final BaseInfo info, final View.OnClickListener listener) {
        bindViewHolderFromType(context, info, mAdapter.getType());
        mItemView.setTag(info);
        mItemView.setOnClickListener(listener);
    }

    @Override
    public void onHolderRecycled() {
    }

    private void bindViewHolderFromType(Context context, BaseInfo info, int type) {


    }

    public void setOnCheckChangeListener(OnCheckedChangeListener listener) {
        cclistener = listener;
    }

    private boolean isGridType() {
        return mType == AppInfo.ITEM_TYPE_GRID;
    }

    @Override
    public void showHolderAnimation() {

    }

    @Override
    public void onClick(View v) {
    }
}
