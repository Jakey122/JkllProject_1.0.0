package com.android.apps.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sdk.util.RefInvoke;
import com.jkll.app.R;

/**
 * Created by root on 16-8-30.
 */
public class LoadRefreshView extends SwipeRefreshLayout {

    private final static float ENDOFFSET = 500f;
    private int mScreenHeight;

    public LoadRefreshView(Context context) {
        super(context);
        initView();
    }

    public LoadRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setColorSchemeResources(R.color.colorPrimary);
        setEnabled(false);
        setNestedScrollingEnabled(false);
        RefInvoke.setFieldOjbect(getClass().getSuperclass(), "mMediumAnimationDuration", this, 0);
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    public void show() {
        setProgressViewOffset(false, 0, mScreenHeight / 3);
        setRefreshing(true);
        showAllProgressRing();
    }

    public void hide() {
        setRefreshing(false);
    }

    private void showAllProgressRing() {
        RefInvoke.invokeMethod(getClass().getSuperclass(), "moveSpinner", this,
                new Class[]{float.class}, new Object[]{ENDOFFSET});
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
