package com.android.apps.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.apps.adapter.BaseRecyclerAdapter;

/**
 * Created by root on 16-6-7.
 */
public class InnerDivideDecoration extends BaseItemDecoration {

    IDecorationHelper mHelper;

    public InnerDivideDecoration(int space, BaseRecyclerAdapter adapter, IDecorationHelper helper) {
        super(space, adapter);
        mHelper = helper;
    }

    @Override
    protected void handleItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state, int position) {
        if (mHelper != null)
            mHelper.handleItemOffsets(outRect, view, parent, state, position);
    }
}
