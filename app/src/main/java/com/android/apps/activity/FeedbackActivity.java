package com.android.apps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.fragment.BaseFragment;
import com.android.apps.fragment.MineSettingAdviseFragment;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends BaseActivity {

    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    @Bind(R.id.content_frame)
    FrameLayout contentFrame;

    private int mFrom;
    private int mType;

    private boolean isRequsting;
    private MineSettingAdviseFragment mFragment;

    public static void actionActivity(Context context, int type, int from) {
        Intent sIntent = new Intent(context, FeedbackActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_feedback);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);

        Bundle args = MineSettingAdviseFragment.newArgument(mType, mFrom);
        mFragment = MineSettingAdviseFragment.newInstance(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mFragment).commit();
        layoutToolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getResources().getString(R.string.mine_feedback));
    }

    @Override
    protected void handleMyMessage(Message msg) {
    }

    @Override
    protected BaseFragment getFragment() {
        return mFragment;
    }

    @Override
    public BaseInfo getBaseInfo() {
        return null;
    }

    @OnClick(R.id.layout_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
