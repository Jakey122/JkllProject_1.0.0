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
import com.android.apps.helper.PromptHelper;
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

public class ResetPasswordActivity extends BaseActivity {
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_repassword)
    EditText etRepassword;
    @Bind(R.id.reset)
    TextView reset;
    @Bind(R.id.tip)
    TextView tip;
    @Bind(R.id.ivtip)
    ImageView ivtip;
    @Bind(R.id.layout_tip)
    LinearLayout layoutTip;
    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    private int mFrom;
    private int mType;

    private IHttpListener mListener;
    private boolean isRequsting;


    private String mobile;
    private String code;

    public static void actionActivity(Context context, int type, int from, String mobile, String code) {
        Intent sIntent = new Intent(context, ResetPasswordActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.putExtra("mobile", mobile);
        sIntent.putExtra("code", code);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_resetpassword);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);
        mobile = getIntent().getStringExtra("mobile");
        code = getIntent().getStringExtra("code");

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

    @OnClick({R.id.layout_toolbar_back, R.id.reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_toolbar_back:
                LoginActivity.actionActivity(this, PageId.PageUser.PAGE_RESET_PASSWORD, PageId.PageUser.PAGE_LOGIN);
                finish();
                break;
            case R.id.reset:
                doReset();
                break;
        }
    }

    private void doReset() {
        String password = etPassword.getText().toString();
        String repassword = etRepassword.getText().toString();
        if (!isReady(mobile, password, repassword, code)) return;
        if (isRequsting) return;
        isRequsting = true;
        HttpController.getInstance(this).reset(mobile, MD5.getMD5(password + "&showmacapp"), code, mListener);
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

    private class HttpListener extends IHttpListener {
        @Override
        public void handleReset(int statusCode) {
            isRequsting = false;
            handleResult(statusCode);
        }
    }

    private void handleResult(int statusCode) {
        if (AppUtil.checkResult(this, mType, statusCode)) {
            PromptHelper.showToast(R.string.success_reset);
            ResetPasswordCompletedActivity.actionActivity(this, PageId.PageUser.PAGE_RESET_PASSWORD_COMPLETE, PageId.PageUser.PAGE_RESET_PASSWORD);
            finish();
        } else {
            PromptHelper.showToast(R.string.fail_reset);
        }
    }
}
