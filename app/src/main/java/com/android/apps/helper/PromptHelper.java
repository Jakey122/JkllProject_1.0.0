package com.android.apps.helper;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.apps.App;
import com.android.apps.animation.AnimationUtil;
import com.sdk.util.RefInvoke;
import com.android.apps.view.MFrontView;
import com.jkll.app.R;

/**
 * Created by root on 16-8-24.
 */
public class PromptHelper {

    private static MFrontView mCurrentFrontView;
    private static Snackbar mCurrentSnackbar;

    public static void dismissCurrent() {
        if (mCurrentSnackbar != null) mCurrentSnackbar.dismiss();
    }

    public static void showToast(String data) {
        Toast.makeText(App.getInstance().getApplicationContext(), data, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int id) {
        Toast.makeText(App.getInstance().getApplicationContext(), id, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackMessage(View parent, int messageText) {
        showSnackMessage(parent, messageText, -1, false, null, Snackbar.LENGTH_SHORT);
    }

    public static void showSnackMessage(View parent, int messageText, int length) {
        showSnackMessage(parent, messageText, -1, false, null, length);
    }

    public static void showSnackMessage(View parent, int messageText, final ISnackbarHelper helper) {
        showSnackMessage(parent, messageText, -1, false, helper, Snackbar.LENGTH_SHORT);
    }

    public static void showSnackMessage(View parent, int messageText, int actionText, final ISnackbarHelper helper, int length) {
        showSnackMessage(parent, messageText, actionText, false, helper, length);
    }

    public static void showSnackMessage(View parent, int messageText, int actionText, boolean addCutom, final ISnackbarHelper helper,
                                        int length) {
        mCurrentFrontView = null;
        if (parent == null) return;
        Context context = App.getInstance().getApplicationContext();

        final Snackbar snack = Snackbar.make(parent, messageText, length);
        mCurrentSnackbar = snack;
        Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snack.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snack.setActionTextColor(Color.WHITE);
        snack.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (mCurrentSnackbar == snackbar) {
                    if (helper != null) helper.onSnackbarDismissed(snackbar, event);
                    if (mCurrentFrontView != null) mCurrentFrontView.removeCallback();
                    mCurrentFrontView = null;
                    mCurrentSnackbar = null;
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
                if (helper != null) helper.onSnackbarShown(snackbar);
            }
        });
        if (actionText > 0) snack.setAction(actionText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) helper.onActionBtnClick(v, snack);
            }
        });

        ViewGroup mTargetParent = (ViewGroup) RefInvoke.getFieldOjbect(snack.getClass(), snack, "mTargetParent");
        if (mTargetParent != null) {
            try {
                mCurrentFrontView = (MFrontView) mTargetParent.findViewById(R.id.frontView);
            } catch (Exception e) {
            }
        }
        if (mCurrentFrontView != null) {
            mCurrentFrontView.setOnTouchCallback(snack, helper);
        }

        if (addCutom) {
            final TextView message = (TextView) snackbarView.findViewById(R.id.snackbar_text);   //获取textViewView实例
            message.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.text_size_22px));

            final Button customBtn = new Button(context);
            customBtn.setTextColor(Color.WHITE);
            customBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (helper != null) helper.onCustomBtnClick(v, snack);
                }
            });
            customBtn.setBackgroundResource(R.drawable.bg_snackbar_custombtn);
            customBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.text_size_20px));

            int height = AnimationUtil.dip2px(context, 56);
            int width = height / 2;
            Snackbar.SnackbarLayout.LayoutParams customParams =
                    new Snackbar.SnackbarLayout.LayoutParams(height, width);
            customParams.gravity = Gravity.CENTER_VERTICAL;
            customBtn.setLayoutParams(customParams);
            customBtn.setPadding(0, 0, 0, 0);

            final Button actionBtn = (Button) snackbarView.findViewById(R.id.snackbar_action); //获取按钮View实例
            actionBtn.setBackgroundDrawable(null);
            actionBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.text_size_20px));
            if (helper != null) helper.initSnackbarView(snack, message, actionBtn, customBtn);
            snackbarView.addView(customBtn);
        }
        snack.show();
    }

    public static Pair<Snackbar, Button> showBottomDelete(View parent) {
        if (parent == null) return null;
        Context context = App.getInstance().getApplicationContext();
        final Snackbar snack = Snackbar.make(parent, "", Snackbar.LENGTH_INDEFINITE);
        final Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snack.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        snack.setCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
                ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
                if (params != null && params instanceof CoordinatorLayout.LayoutParams) {
                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) params;
                    lp.setBehavior(null);
                }
            }
        });

        final TextView message = (TextView) snackbarView.findViewById(R.id.snackbar_text);   //获取textViewView实例
        Snackbar.SnackbarLayout.LayoutParams messageParams = (Snackbar.SnackbarLayout.LayoutParams) message.getLayoutParams();
        messageParams.height = messageParams.width = 0;
        messageParams.weight = 0f;
        message.setLayoutParams(messageParams);

        final Button mActionView = (Button) snackbarView.findViewById(R.id.snackbar_action);
        mActionView.setVisibility(View.GONE);

        final View mView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_deletebtn, snackbarView, false);
        final Button customBtn = (Button) mView.findViewById(R.id.btn_delete);
        snackbarView.addView(mView);
        Pair<Snackbar, Button> result = new Pair<>(snack, customBtn);
        return result;
    }

    public interface ISnackbarHelper {

        void onActionBtnClick(View v, Snackbar snackbar);

        void onCustomBtnClick(View v, Snackbar snackbar);

        boolean onParentTouch(Snackbar snackbar, View v, MotionEvent event);

        void onSnackbarDismissed(Snackbar snackbar, int event);

        void onSnackbarShown(Snackbar snackbar);

        void initSnackbarView(Snackbar snackbar, TextView snackbarMessage, Button snackBarAction, View customView);
    }
}
