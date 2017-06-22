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
import com.android.apps.fragment.VarietyRecyclerFragment;
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.net.IHttpListener;
import com.sdk.util.SPUtil;
import com.sdk.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionActivity extends BaseActivity {

    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    @Bind(R.id.content_frame)
    FrameLayout contentFrame;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.layout_num)
    RelativeLayout layoutNum;
    private int mFrom;
    private int mType;

    private IHttpListener mListener;
    private boolean isRequsting;
    private VarietyRecyclerFragment mFragment;

    public static void actionActivity(Context context, int type, int from) {
        Intent sIntent = new Intent(context, QuestionActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_question);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);
        mFragment = VarietyRecyclerFragment.newInstance(VarietyRecyclerFragment.newArgument(mFrom, mType,
                mType, VarietyRecyclerFragment.LAYOUT_GRID_VERTICAL));
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mFragment).commit();
        layoutToolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getResources().getString(R.string.mine_question));
        phone.setText(SPUtil.getInstant(this).getString(Const.MINE_CUSTOMER_INFO, getResources().getString(R.string.mine_question_num)));
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

    @OnClick({R.id.layout_toolbar_back, R.id.layout_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_toolbar_back:
                finish();
                break;
            case R.id.layout_num:
                Util.doCall(this, phone.getText().toString().replaceAll("-", ""));
                break;
        }
    }
}
