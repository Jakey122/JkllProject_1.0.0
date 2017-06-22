package com.android.apps.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 轮播图
 */
public class ScrollingViewPager extends ViewPager {
    private OnChildViewClickListener mChildViewClickListener;
    private boolean mCanAutoScroll = true;
    private float mStartX;
    private float mStartY;

    public ScrollingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ScrollingViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public boolean canAutoScroll() {
        return mCanAutoScroll;
    }

    public void setOnChildViewClickListener(OnChildViewClickListener listener) {
        mChildViewClickListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (arg0.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCanAutoScroll = false;
                mStartX = arg0.getX();
                mStartY = arg0.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCanAutoScroll = false;
                break;
            case MotionEvent.ACTION_UP:
                mCanAutoScroll = true;
                int displayWidth = getResources().getDisplayMetrics().widthPixels;

                if (mChildViewClickListener != null && Math.abs(arg0.getX() - mStartX) < displayWidth / 10 && Math.abs(arg0.getY() - mStartY) < displayWidth / 10) {
                    mChildViewClickListener.onChildViewClick(getCurrentItem());
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(arg0);
    }

    public interface OnChildViewClickListener {
        void onChildViewClick(int position);
    }
}