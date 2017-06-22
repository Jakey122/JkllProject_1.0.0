package com.android.apps.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TextView;

/**
 */
public class UiUtilities {private UiUtilities() {
}

    @SuppressWarnings("unchecked")
    public static <T extends View> T getViewOrNull(Activity parent, int viewId) {
        return (T) parent.findViewById(viewId);
    }


    @SuppressWarnings("unchecked")
    public static <T extends View> T getViewOrNull(View parent, int viewId) {
        return (T) parent.findViewById(viewId);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(Activity parent, int viewId) {
        return (T) checkView(parent.findViewById(viewId));
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(View parent, int viewId) {
        return (T) checkView(parent.findViewById(viewId));
    }

    private static View checkView(View v) {
        if (v == null) {
            throw new IllegalArgumentException("View doesn't exist");
        }
        return v;
    }

    public static boolean setText(Activity parent, int id, CharSequence text) {
        TextView textView = getView(parent, id);
        if (textView == null)
            return false;

        textView.setText(text);
        return true;
    }

    public static boolean setText(TextView view, CharSequence text) {
        if(view == null) return false;
        view.setText(text);
        return true;
    }

    public static boolean setText(View view, int id, CharSequence text) {
        TextView textView = getView(view, id);
        if (textView == null)
            return false;

        textView.setText(text);
        return true;
    }

    public static void setVisibilitySafe(View v, int visibility) {
        if (v != null) {
            v.setVisibility(visibility);
        }
    }

    public static void setVisibilitySafe(Activity parent, int viewId, int visibility) {
        setVisibilitySafe(parent.findViewById(viewId), visibility);
    }

    public static void setVisibilitySafe(View parent, int viewId, int visibility) {
        setVisibilitySafe(parent.findViewById(viewId), visibility);
    }

    public static void setEnable(Activity parent, int viewId, boolean enabled) {
        setEnable(parent.findViewById(viewId), enabled);
    }

    public static void setEnable(View parent, int viewId, boolean enabled) {
        setEnable(parent.findViewById(viewId), enabled);
    }

    public static void setEnable(View v, boolean enabled) {
        if(v != null) {
            v.setEnabled(enabled);
        }
    }

    public static void setEnabledScreenRotation(boolean enabled, Activity activity) {
        if(enabled) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        else{
            int currOrientation = activity.getResources().getConfiguration().orientation;
            int requestedOrientation;

            if(android.os.Build.VERSION.SDK_INT > 8) {
                requestedOrientation = currOrientation == Configuration.ORIENTATION_PORTRAIT ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
            } else {
                requestedOrientation = currOrientation == Configuration.ORIENTATION_PORTRAIT ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
            activity.setRequestedOrientation(requestedOrientation);
        }
    }

    public static final int TYPE_UNLOCK = 0;
    public static final int TYPE_LOCK_PORTRAIT = 1;
    public static final int TYPE_LOCK_LANDSCAPE = 2;

    public static void lockScreenRotation(int orientationType, Activity activity) {
        int requestedOrientation;

        switch (orientationType) {
            case TYPE_UNLOCK:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case TYPE_LOCK_PORTRAIT:
                requestedOrientation = android.os.Build.VERSION.SDK_INT > 8 ?
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                activity.setRequestedOrientation(requestedOrientation);
                break;
            case TYPE_LOCK_LANDSCAPE:
                requestedOrientation = android.os.Build.VERSION.SDK_INT > 8 ?
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                activity.setRequestedOrientation(requestedOrientation);
                break;
            default:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
        }
    }
}
