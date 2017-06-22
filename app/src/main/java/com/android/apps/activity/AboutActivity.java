package com.android.apps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.fragment.BaseFragment;
import com.android.apps.util.Const;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.util.SPUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    @Bind(R.id.desc)
    TextView desc;
    private int mFrom;
    private int mType;

    private boolean isRequsting;

    public static void actionActivity(Context context, int type, int from) {
        Intent sIntent = new Intent(context, AboutActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_about);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);
        layoutToolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getResources().getString(R.string.mine_about));
        desc.setText(SPUtil.getInstant(this).getString(Const.MINE_ABOUT_INFO, getResources().getString(R.string.mine_question_num)));
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

    @OnClick({R.id.layout_toolbar_back, R.id.toolbar_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_toolbar_back:
                finish();
                break;
        }
    }
}
