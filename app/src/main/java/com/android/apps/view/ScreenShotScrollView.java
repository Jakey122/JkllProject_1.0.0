package com.android.apps.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.android.apps.animation.AnimationUtil;
import com.jkll.app.R;

/**
 * Created by root on 16-5-27.
 */
public class ScreenShotScrollView extends HorizontalScrollView {

    private LinearLayout mImages, mPoints;
    private int count, width, childWidth, margins, scrollSpace;

    public ScreenShotScrollView(Context context) {
        super(context);
    }

    public ScreenShotScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScreenShotScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScreenShotScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setChildView(LinearLayout images, LinearLayout point) {
        mImages = images;
        mPoints = point;
        View image = mImages.getChildAt(0);

        count = mImages.getChildCount();
        margins = AnimationUtil.dip2px(getContext(), 7) * 2;
        childWidth = image.getLayoutParams().width + margins;
        width = getResources().getDisplayMetrics().widthPixels - margins;
        scrollSpace = childWidth * count - width;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);

        int x = getScrollX();
        int position = x / (scrollSpace / count);

        for (int i = 0; i < mPoints.getChildCount(); i++) {
            if (i == position) {
                mPoints.getChildAt(i).setBackgroundResource(R.drawable.point_select);
            } else {
                mPoints.getChildAt(i).setBackgroundResource(R.drawable.point_normal);
            }
        }
    }
}
