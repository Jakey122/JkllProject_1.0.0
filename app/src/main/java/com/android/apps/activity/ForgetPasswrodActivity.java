package com.android.apps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.apps.fragment.BaseFragment;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppUtil;
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;
import com.sdk.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswrodActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.sendcode)
    TextView sendcode;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.reset)
    TextView reset;
    @Bind(R.id.tip)
    TextView tip;
    @Bind(R.id.ivtip)
    ImageView ivtip;
    @Bind(R.id.layout_tip)
    LinearLayout layoutTip;

    private int mFrom;
    private int mType;

    private IHttpListener mListener;
    private boolean isRequsting;
    private String ctype = "forget";
    private final int MAX_SECOND = 60;
    private int currentSecond = MAX_SECOND;

    public static void actionActivity(Context context, int type, int from) {
        Intent sIntent = new Intent(context, ForgetPasswrodActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_forgetpassword);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);

        mListener = new HttpListener();
    }

    @Override
    protected void handleMyMessage(Message msg) {
    }

    @Override
    protected BaseFragment getFragment() {
        return null;
    }

    @Override
    public BaseInfo getBaseInfo() {
        return null;
    }

    @OnClick({R.id.layout_toolbar_back, R.id.sendcode, R.id.reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_toolbar_back:
                LoginActivity.actionActivity(this, PageId.PageUser.PAGE_FORGET_PASSWORD, PageId.PageUser.PAGE_LOGIN);
                finish();
                break;
            case R.id.sendcode:
                doSendcode();
                break;
            case R.id.reset:
                doReset();
                break;
        }
    }

    private void doReset() {
        String mobile = etMobile.getText().toString();
        String code = etCode.getText().toString();
        if (!isReady(mobile, code)) return;
        ResetPasswordActivity.actionActivity(this, PageId.PageUser.PAGE_RESET_PASSWORD, PageId.PageUser.PAGE_LOGIN, mobile, code);
        finish();
    }

    private void doSendcode() {
        String mobile = etMobile.getText().toString();
        if (!isReady(mobile)) return;
        if (isRequsting) return;
        isRequsting = true;
        HttpController.getInstance(this).sendCode(mobile, ctype, mListener);
    }

    private void checkCodeTimer() {
        if (currentSecond <= 0) {
            currentSecond = MAX_SECOND;
            sendcode.setText(R.string.getcode);
            sendcode.setEnabled(true);
            return;
        }
        sendcode.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentSecond--;
                sendcode.setText(getResources().getString(R.string.getcode_again, currentSecond + ""));
                checkCodeTimer();
            }
        }, 1000);
    }


    private boolean isReady(String mobile, String code) {
        if (TextUtils.isEmpty(mobile)) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_mobile_empty);
            return false;
        }
        if (mobile.trim().length() != Const.MAX_MOBILE_NUMBER_LENGTH) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_mobile_format);
            return false;
        }
        if (!Util.checkMobileNumber(mobile.trim())) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_mobile_format);
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_sendcode_empty);
            return false;
        }
        if (code.trim().length() != Const.MIN_CODE_LENGTH) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_sendcode);
            return false;
        }
        layoutTip.setVisibility(View.INVISIBLE);
        tip.setText("");
        if (!Util.isNetAvailable(this)) {
            PromptHelper.showToast(R.string.netstate_unconnect);
            return false;
        }
        return true;
    }

    private boolean isReady(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_mobile_empty);
            return false;
        }
        if (mobile.trim().length() != Const.MAX_MOBILE_NUMBER_LENGTH) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_mobile_format);
            return false;
        }
        if (!Util.checkMobileNumber(mobile.trim())) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_mobile_format);
            return false;
        }
        if (!Util.isNetAvailable(this)) {
            PromptHelper.showToast(R.string.netstate_unconnect);
            return false;
        }
        layoutTip.setVisibility(View.INVISIBLE);
        tip.setText("");
        return true;
    }

    private class HttpListener extends IHttpListener {

        @Override
        public void handleSendcode(int statusCode) {
            super.handleSendcode(statusCode);
            isRequsting = false;
            if (AppUtil.checkResult(ForgetPasswrodActivity.this, mType, statusCode)) {
                checkCodeTimer();
                PromptHelper.showToast(R.string.success_code_sent);
            } else
                PromptHelper.showToast(R.string.fail_code_sent);
        }
    }
}
