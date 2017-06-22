package com.android.apps.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by root on 16-5-26.
 */
public class MScrollView extends NestedScrollView {

    MScrollViewPinHelper mScrollPinHelper;
    boolean isLocked = false;

    public MScrollView(Context context) {
        super(context);
        init();
    }

    public MScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSmoothScrollingEnabled(true);
        mScrollPinHelper = new MScrollViewPinHelper(this);
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            }
        });
    }

    //public void setHeaderView(ImageView header) {
    //    mScrollPinHelper.setHeaderView(header);
    //}

    public void setOnHeaderScrollListener(OnHeaderScrollListener listener) {
        mScrollPinHelper.setOnHeaderScrollListener(listener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mScrollPinHelper.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isLocked) mScrollPinHelper.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //mScrollPinHelper.initLayoutParams();
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
        mScrollPinHelper.stopScroll();
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //dy < 0 手指向下  dy > 0 手指向上
//        refreshToolBar(dy);
        if (shouldIntercept(dx, dy)) {
            consumed[1] = dy;
            smoothScrollBy(dx, dy);
        }
        layoutView(dx, dy);
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        refreshToolBar((int) velocityY);
        if (shouldIntercept((int) velocityX, (int) velocityY)) {
            consumed = false;
        }
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    private boolean shouldIntercept(int dx, int dy) {
        return Math.abs(dy) > Math.abs(dx) && (dy > 0 && getScrollY() + getHeight() != getChildAt(0).getMeasuredHeight()
                || dy < 0 && getScrollY() + getHeight() < getChildAt(0).getMeasuredHeight());
    }

    private void layoutView(int dx, int dy) {
        if (dy < 0 && getScrollY() == 0
                && Math.abs(dy) > Math.abs(dx)) {
            if (!isLocked) mScrollPinHelper.layoutView(-dy);
        }
    }

    public void showFinishAnimation(int toYDelta, long duration, Animation.AnimationListener listener) {
        if (mScrollPinHelper != null) {
            mScrollPinHelper.animationFinish(toYDelta, duration, listener);
        }
    }

    public void setHeaderTop(int top) {
        if (mScrollPinHelper != null) {
            mScrollPinHelper.setHeaderLayout(top);
        }
    }

    public void lockScrollView(boolean lock) {
        isLocked = lock;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public interface OnHeaderScrollListener {
        void onScrollEnough();
    }
}
