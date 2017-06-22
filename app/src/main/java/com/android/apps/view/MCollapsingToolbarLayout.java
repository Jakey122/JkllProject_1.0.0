package com.android.apps.view;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

import com.sdk.util.RefInvoke;

/**
 * Created by root on 16-9-21.
 */
public class MCollapsingToolbarLayout extends CollapsingToolbarLayout {

    private Object scrimAnimator;

    public MCollapsingToolbarLayout(Context context) {
        super(context);
    }

    public MCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setScrimsShown(boolean shown, boolean animate) {
        scrimAnimator = getScrimAnimator();
        if (scrimAnimator != null && getDuration() > 0) setDuration();
        super.setScrimsShown(shown, animate);
    }

    private Object getScrimAnimator() {
        return RefInvoke.getFieldOjbect(CollapsingToolbarLayout.class,
                this, "mScrimAnimator");
    }

    private void setDuration() {
        RefInvoke.invokeMethod(scrimAnimator.getClass(),
                "setDuration", scrimAnimator,
                new Class[]{int.class},
                new Object[]{0});
    }

    private long getDuration() {
        return (long) RefInvoke.invokeMethod(scrimAnimator.getClass(),
                "getDuration", scrimAnimator, null, null);
    }
}
