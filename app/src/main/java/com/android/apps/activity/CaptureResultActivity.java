package com.android.apps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.fragment.BaseFragment;
import com.android.apps.helper.ImageHelper;
import com.android.apps.helper.LoginHelper;
import com.android.apps.helper.PromptHelper;
import com.android.apps.listener.OnLoginChangeListener;
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.net.IHttpListener;
import com.sdk.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptureResultActivity extends BaseActivity {

    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.iv_tip)
    ImageView ivTip;
    @Bind(R.id.tip)
    TextView tip;
    @Bind(R.id.complete)
    TextView complete;
    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    private int mFrom;
    private int mType;
    private BaseInfo info;

    public static void actionActivity(Context context, int type, int from, BaseInfo info) {
        Intent sIntent = new Intent(context, CaptureResultActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.putExtra("info", info);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_captureresult);
        ButterKnife.bind(this);

        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);
        info = getIntent().getParcelableExtra("info");

        refreshView();
    }

    private void refreshView() {
        layoutToolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.tab_device);
        if(info == null) return;
        ImageHelper.getInstance(this).loadImage(info.getPrdLogo(), ivTip);
        tip.setText(getResources().getString(R.string.device_add_tip, info.getPrdName(), info.getDeviceModel(), info.getPrdId()));
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

    @OnClick({R.id.complete, R.id.layout_toolbar_back})
    public void onViewClicked() {
        finish();
    }
}
