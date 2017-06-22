package com.android.apps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class LoginActivity extends BaseActivity implements OnLoginChangeListener {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.register)
    TextView register;
    @Bind(R.id.forget_password)
    TextView forgetPassword;
    @Bind(R.id.tip)
    TextView tip;
    @Bind(R.id.ivtip)
    ImageView ivtip;
    @Bind(R.id.layout_tip)
    LinearLayout layoutTip;
    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;

    private int mFrom;
    private int mType;

    private IHttpListener mListener;
    private boolean isRequsting;

    public static void actionActivity(Context context, int type, int from) {
        Intent sIntent = new Intent(context, LoginActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_login);
        ButterKnife.bind(this);

        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);

        mListener = new HttpListener();

        LoginHelper.addListener(this, getClass().getSimpleName());

        OnTextChangeListener onTextChangeListener = new OnTextChangeListener();

        etMobile.addTextChangedListener(onTextChangeListener);
        etPassword.addTextChangedListener(onTextChangeListener);
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

    @OnClick({R.id.login, R.id.register, R.id.forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                doLogin();
                break;
            case R.id.register:
                doRegister();
                break;
            case R.id.forget_password:
                doForgetPassword();
                break;
        }
    }

    private void doForgetPassword() {
        ForgetPasswrodActivity.actionActivity(this, PageId.PageUser.PAGE_FORGET_PASSWORD, PageId.PageUser.PAGE_LOGIN);
    }

    private void doRegister() {
        RegisterActivity.actionActivity(this, PageId.PageUser.PAGE_REGISTER, PageId.PageUser.PAGE_LOGIN);
    }

    private void goMain() {
        MainActivity.actionActivity(this, PageId.PageMain.PAGE_HOME, PageId.PageUser.PAGE_LOGIN, 0);
        finish();
    }

    private void doLogin() {
        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();
        if (!isReady(mobile, password)) return;
        if (isRequsting) return;
        isRequsting = true;
//        mobile = "13911821708";
//        password = "123456";
        HttpController.getInstance(this).login(mobile, MD5.getMD5(password + "&showmacapp"), mListener);
    }

    private boolean isReady(String mobile, String password) {
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
        layoutTip.setVisibility(View.INVISIBLE);
        tip.setText("");
        if (!Util.isNetAvailable(this)) {
            PromptHelper.showToast(getResources().getString(R.string.netstate_unconnect));
            return false;
        }

        return true;
    }

    @Override
    public void onLoginChange(BaseInfo info) {
        PromptHelper.showToast(R.string.success_login);
        goMain();
    }

    private class HttpListener extends IHttpListener {
        @Override
        public void handleLogin(int status, BaseInfo info) {
            isRequsting = false;
            if (AppUtil.checkResult(LoginActivity.this, mType, status) && info != null) {
                onLoginChange(info);
            }
        }
    }

    private class OnTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tip.setText("");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
