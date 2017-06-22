package com.android.apps.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.activity.BaseActivity;
import com.android.apps.activity.MainActivity;
import com.android.apps.fragment.BaseFragment;
import com.android.apps.util.AppLogUtil;
import com.android.apps.util.PageId;
import com.android.apps.view.PayView;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.lib.util.UiUtil;
import com.sdk.util.PackageUtil;

/**
 * Created by root on 16-7-2.
 */
public class PaySuccessDialog extends BaseActivity implements View.OnClickListener {


    TextView message;
    View button3;
    Button button1;
    View layout_content;

    private int width;
    private int mType;
    private BaseInfo info = new BaseInfo();
    private BaseInfo extra = new BaseInfo();

    public static void showDialog(Context context, int type, BaseInfo info, BaseInfo extraInfo) {
        try {
            Intent intent = new Intent(context, PaySuccessDialog.class);
            intent.putExtra("type", type);
            intent.putExtra("info", info);
            intent.putExtra("extra", extraInfo);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_pay_success);
        initData();
        initView();
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

    private void initData() {
        mType = getIntent().getIntExtra("type", 0);
        info = getIntent().getParcelableExtra("info");
        extra = getIntent().getParcelableExtra("extra");
        if(extra == null) extra = new BaseInfo();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.button1);
        button3 = findViewById(R.id.button3);
        layout_content = findViewById(R.id.layout_content);
        button1.setOnClickListener(this);
        button3.setOnClickListener(this);
        button1.setText(R.string.go_sorder_detail);
        message = (TextView) findViewById(R.id.message);
        message.setText(getResources().getString(R.string.success_pay_msg, info.getNickname(), extra.getName(), mType == PayDialog.TYPE_PROTECT?extra.getSdate():extra.getExpdate()));

        width = (int) (UiUtil.getScreenSize(this).widthPixels * 4 / 5);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_content.getLayoutParams();
        layoutParams.width = width;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button3) {
            finish();
        } else if (view.getId() == R.id.button1) {
            MainActivity.actionActivity(this, PageId.PAGE_MAIN, mType, 2);
        }
    }

    public void addStatistics(Context mContext) {
        AppLogUtil.addOpenViewLog(mContext, PageId.PageOrder.PAGE_ORDER_DETAIL_PAY_SUCCESS, mType, "-1", "-1");
    }
}
