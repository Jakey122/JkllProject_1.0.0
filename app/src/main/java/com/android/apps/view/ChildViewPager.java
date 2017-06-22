package com.android.apps.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 */
public class ChildViewPager extends ViewPager {

    private int mWindowWidth;
    private int mScrollWidth;

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setOffscreenPageLimit(0);
        mWindowWidth = context.getResources().getDisplayMetrics().widthPixels;
        mScrollWidth = mWindowWidth / 4;
    }

    public ChildViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setOffscreenPageLimit(0);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        if (item < 0 || item >= getAdapter().getCount()) return;
        super.setCurrentItem(item);
    }

}