package com.android.apps.control;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.android.apps.animation.AnimationUtil;

/**
 * Created by root on 16-5-11.
 */
public class TabLayoutBehavior extends CoordinatorLayout.Behavior<View> {

    private int sinceDirectionChange;

    public TabLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && sinceDirectionChange < 0 || dy < 0 && sinceDirectionChange > 0) {
            child.animate().cancel();
            sinceDirectionChange = 0;
        }
        sinceDirectionChange += dy;
        if (sinceDirectionChange > child.getHeight() && child.getVisibility() == View.VISIBLE) {
            AnimationUtil.tabLayoutAnimator(child, View.GONE);
        } else if (sinceDirectionChange < 0 && child.getVisibility() == View.GONE) {
            AnimationUtil.tabLayoutAnimator(child, View.VISIBLE);
        }
    }

//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//        return dependency instanceof AppBarLayout;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        child.setTranslationY(Math.abs(dependency.getTop()));
//        return true;
//    }
}