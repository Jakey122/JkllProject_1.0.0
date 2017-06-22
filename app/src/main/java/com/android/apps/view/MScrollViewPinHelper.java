package com.android.apps.view;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.jkll.app.R;

/**
 * Created by root on 16-5-27.
 */
public class MScrollViewPinHelper {

    private static final float SCROLL_RATIO = 1f;// 阻尼系数,越小阻力就越大

    private MScrollView mScrollView;
    private View inner;                 // 子View
    private Rect normal = new Rect();   // 矩形(这里只是个形式，只是用于判断是否需要动画.)
    private boolean isCount = false;    // 是否开始计算
    private int mScreenHeight;
    private int mHeaderHeight;
    private float mLastY;

    private MScrollView.OnHeaderScrollListener mListener;

    public MScrollViewPinHelper(MScrollView scrollView) {
        mScrollView = scrollView;
        mScreenHeight = mScrollView.getResources().getDisplayMetrics().heightPixels;
        mHeaderHeight = mScrollView.getResources().getDimensionPixelSize(R.dimen.detail_top_margin);
    }

    public void onFinishInflate() {
        if (mScrollView.getChildCount() > 0) {
            inner = mScrollView.getChildAt(0);
        }
    }

    public void onTouchEvent(MotionEvent ev) {
        if (inner != null) {
            commOnTouchEvent(ev);
        }
    }

    public void setOnHeaderScrollListener(MScrollView.OnHeaderScrollListener listener) {
        mListener = listener;
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                stopScroll();
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int deltaY = (int) (nowY - mLastY);
                layoutView(deltaY);
                mLastY = nowY;
                break;
            default:
                break;
        }
    }

    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
        isCount = false;
    }

    public void animationFinish(int toYDelta, long duration, Animation.AnimationListener listener) {
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                toYDelta);
        ta.setDuration(duration);
        if (listener != null)
            ta.setAnimationListener(listener);
        inner.startAnimation(ta);
    }

    public void setHeaderLayout(int top) {
        inner.layout(normal.left, top, normal.right, normal.bottom);
    }

    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    public boolean isNeedMove() {
        return mScrollView.getScrollY() == 0;
    }

    public void layoutView(int deltaY) {
        if (!isCount) {
            deltaY = 0;
        }

        if (deltaY < 0)
            return;

        if (isNeedMove()) {
            if (normal.isEmpty()) {
                normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
            }
            int newTop = inner.getTop() + (int) (deltaY * SCROLL_RATIO);
            int newBottom = inner.getBottom() + (int) (deltaY * SCROLL_RATIO);
            inner.layout(inner.getLeft(), newTop, inner.getRight(), newBottom);
            if (isEnough(newTop) && mListener != null) {
                mListener.onScrollEnough();
            }
        }
        isCount = true;
    }

    public void stopScroll() {
        if (isNeedAnimation()) {
            animation();
            mLastY = 0;
        }
    }

    public boolean isPin() {
        return !normal.isEmpty() && inner.getTop() != normal.top;
    }

    private boolean isEnough(int top) {
        return top + mHeaderHeight >= mScreenHeight * 2 / 5;
    }
}