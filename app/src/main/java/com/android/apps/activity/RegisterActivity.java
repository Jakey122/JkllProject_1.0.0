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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.fragment.BaseFragment;
import com.android.apps.helper.LoginHelper;
import com.android.apps.helper.PromptHelper;
import com.android.apps.listener.OnLoginChangeListener;
import com.android.apps.util.AppUtil;
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.lib.util.MD5;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;
import com.sdk.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements OnLoginChangeListener {

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
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_repassword)
    EditText etRepassword;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.register)
    TextView register;
    @Bind(R.id.tip)
    TextView tip;
    @Bind(R.id.ivtip)
    ImageView ivtip;
    @Bind(R.id.layout_tip)
    LinearLayout layoutTip;
    @Bind(R.id.line3)
    View line3;
    @Bind(R.id.line4)
    View line4;
    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;

    private int mFrom;
    private int mType;

    private IHttpListener mListener;
    private boolean isRequsting;
    private String ctype = "reg";
    private final int MAX_SECOND = 60;
    private int currentSecond = MAX_SECOND;

    public static void actionActivity(Context context, int type, int from) {
        Intent sIntent = new Intent(context, RegisterActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_register);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);

        mListener = new HttpListener();
        LoginHelper.addListener(this, getClass().getSimpleName());
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

    private void goLogin() {
        LoginActivity.actionActivity(this, PageId.PageUser.PAGE_LOGIN, PageId.PageUser.PAGE_REGISTER);
        finish();
    }

    private void doRegister() {
        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();
        String repassword = etRepassword.getText().toString();
        String code = etCode.getText().toString();
        if (!isReady(mobile, password, repassword, code)) return;
        if (isRequsting) return;
        isRequsting = true;
        HttpController.getInstance(this).register(mobile, MD5.getMD5(password + "&showmacapp"), code, mListener);
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

    private boolean isReady(String mobile, String password, String repassword, String code) {
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
        if (TextUtils.isEmpty(password)) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_password_empty);
            return false;
        }
        if (password.trim().length() < Const.MIN_PASSWORD_LENGTH) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_password_short);
            return false;
        }
        if (repassword.trim().length() < Const.MIN_PASSWORD_LENGTH) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_repassword_different);
            return false;
        }
        if (!password.trim().equals(repassword.trim())) {
            layoutTip.setVisibility(View.VISIBLE);
            tip.setText(R.string.error_repassword_different);
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
        layoutTip.setVisibility(View.INVISIBLE);
        tip.setText("");
        if (!Util.isNetAvailable(this)) {
            PromptHelper.showToast(R.string.netstate_unconnect);
            return false;
        }
        return true;
    }

    @Override
    public void onLoginChange(BaseInfo info) {

    }

    @OnClick({R.id.layout_toolbar_back, R.id.sendcode, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_toolbar_back:
                LoginActivity.actionActivity(this, PageId.PageUser.PAGE_LOGIN, PageId.PageUser.PAGE_REGISTER);
                finish();
                break;
            case R.id.sendcode:
                doSendcode();
                break;
            case R.id.register:
                doRegister();
                break;
        }
    }

    private class HttpListener extends IHttpListener {
        @Override
        public void handleRegister(int status) {
            isRequsting = false;
            if (AppUtil.checkResult(RegisterActivity.this, mType, status)) {
                PromptHelper.showToast(R.string.success_register);
                goLogin();
            }
        }

        @Override
        public void handleSendcode(int statusCode) {
            super.handleSendcode(statusCode);
            isRequsting = false;
            if (AppUtil.checkResult(RegisterActivity.this, mType, statusCode)) {
                checkCodeTimer();
                PromptHelper.showToast(R.string.success_code_sent);
            } else
                PromptHelper.showToast(R.string.fail_code_sent);
        }
    }
}
