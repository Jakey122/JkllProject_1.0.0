package com.android.apps.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.apps.App;
import com.android.apps.adapter.BaseRecyclerAdapter;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.DeviceInfo;
import com.sdk.bean.OrderInfo;
import com.sdk.lib.util.UiUtil;

/**
 * Created by root on 16-5-18.
 */
public class ItemDivideDecoration extends BaseItemDecoration {

    int dividerHeight;

    public ItemDivideDecoration(int space, BaseRecyclerAdapter adapter) {
        super(space, adapter);
        dividerHeight = UiUtil.dip2px(App.getInstance().getApplicationContext(), 10);
    }

    @Override
    protected void handleItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state, int position) {
        if (position < 0) return;
        BaseInfo info = mAdapter.getItem(position);
        if (info instanceof OrderInfo && position == 0) {
            outRect.top = dividerHeight;
        }
        outRect.bottom = dividerHeight/2;
    }
}
