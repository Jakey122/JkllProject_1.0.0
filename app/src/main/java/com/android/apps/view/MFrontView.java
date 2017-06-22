package com.android.apps.view;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.apps.helper.PromptHelper;

/**
 * Created by root on 16-8-24.
 */
public class MFrontView extends View {

    private static PromptHelper.ISnackbarHelper mHelper;
    private static Snackbar mSnackbar;

    public MFrontView(Context context) {
        super(context);
    }

    public MFrontView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MFrontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSnackbar != null) mSnackbar.dismiss();
        removeCallback();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mHelper != null) return mHelper.onParentTouch(mSnackbar, this, event);
        removeCallback();
        return super.dispatchTouchEvent(event);
    }

    public void setOnTouchCallback(Snackbar snackbar, PromptHelper.ISnackbarHelper helper) {
        mHelper = helper;
        mSnackbar = snackbar;
    }

    public void removeCallback() {
        mHelper = null;
        mSnackbar = null;
    }
}
