package com.android.apps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.android.apps.fragment.BaseFragment;
import com.android.apps.util.PageId;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordCompletedActivity extends BaseActivity {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.login)
    TextView login;
    private int mFrom;
    private int mType;

    public static void actionActivity(Context context, int type, int from) {
        Intent sIntent = new Intent(context, ResetPasswordCompletedActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_resetcomplete);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);
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

    @OnClick(R.id.login)
    public void onViewClicked() {
        LoginActivity.actionActivity(this, PageId.PageUser.PAGE_LOGIN, PageId.PageUser.PAGE_RESET_PASSWORD_COMPLETE);
        finish();
    }
}
