package com.android.apps.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.apps.App;
import com.android.apps.activity.BaseActivity;
import com.android.apps.fragment.BaseFragment;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppLogUtil;
import com.android.apps.util.AppUtil;
import com.android.apps.util.PageId;
import com.android.apps.view.PayView;
import com.jkll.app.R;
import com.pingplusplus.android.Pingpp;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.OrderInfo;
import com.sdk.lib.util.UiUtil;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;
import com.sdk.parse.JsonParseUtil;
import com.sdk.util.PackageUtil;
import com.sdk.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-7-2.
 */
public class PayDialog extends BaseActivity implements View.OnClickListener {


    public static final int TYPE_PROTECT = 1;
    public static final int TYPE_TC = 2;
    public static final int TYPE_JYB = 3;
    public static final int TYPE_LLFW = 4;
    public static final int TYPE_AUTH = 5;

    public static final int TYPE_PAY_WX = 1;
    public static final int TYPE_PAY_APLIY = 2;

    PayView payView;
    View left;
    View right;
    ImageView ivLeft;
    ImageView ivRight;

    ImageView iv_apily;
    ImageView iv_wx;
    TextView tv_apliy;
    TextView tv_wx;
    View layout_apliy;
    View layout_wx;
    View button3;
    Button button1;

    View layout_content;

    private int mType;
    private int currentIndex;
    private BaseInfo info = new BaseInfo();
    private String year;
    private String month;
    private int width;
    private List<BaseInfo> mInfos = new ArrayList<BaseInfo>();
    private boolean isRequesting;

    private IHttpListener iHttpListener;

    public static void showDialog(Context context, int type, BaseInfo info) {
        try {
            Intent intent = new Intent(context, PayDialog.class);
            intent.putExtra("type", type);
            intent.putExtra("info", info);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public static void showDialog(Context context, int type, BaseInfo info, String year, String month) {
        try {
            Intent intent = new Intent(context, PayDialog.class);
            intent.putExtra("type", type);
            intent.putExtra("info", info);
            intent.putExtra("year", year);
            intent.putExtra("month", month);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_pay);
        initData();
        initView();
        iHttpListener = new HttpListener();
        addStatistics();
        loadData();
    }

    private void initData() {
        mType = getIntent().getIntExtra("type", 0);
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        info = getIntent().getParcelableExtra("info");
    }

    private void initView() {
        payView = (PayView) findViewById(R.id.payview);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        ivLeft = (ImageView) findViewById(R.id.ivleft);
        ivRight = (ImageView) findViewById(R.id.ivright);
        iv_apily = (ImageView) findViewById(R.id.iv_apliy);
        iv_wx = (ImageView) findViewById(R.id.iv_wx);
        tv_apliy = (TextView) findViewById(R.id.tv_apliy);
        tv_wx = (TextView) findViewById(R.id.tv_wx);
        layout_apliy = findViewById(R.id.layout_apliy);
        layout_wx = findViewById(R.id.layout_wx);
        button1 = (Button) findViewById(R.id.button1);
        button3 = findViewById(R.id.button3);

        layout_content = findViewById(R.id.layout_content);
        layout_apliy.setOnClickListener(this);
        layout_wx.setOnClickListener(this);
        button1.setOnClickListener(this);
        button3.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        iv_apily.setSelected(true);
        tv_apliy.setSelected(true);
        button1.setText(R.string.btn_ok);

        width = (int) (UiUtil.getScreenSize(this).widthPixels * 4 / 5);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_content.getLayoutParams();
        layoutParams.width = width;

        iv_apily.setSelected(info.getPayType() == TYPE_PAY_APLIY);
        iv_wx.setSelected(info.getPayType() == TYPE_PAY_WX);
        tv_apliy.setSelected(info.getPayType() == TYPE_PAY_APLIY);
        tv_wx.setSelected(info.getPayType() == TYPE_PAY_WX);

        layout_wx.setVisibility(!PackageUtil.isInstalledApk(this, "com.tencent.mm") ? View.GONE : View.VISIBLE);
    }

    private void loadData() {
        if (!Util.isNetAvailable(this)) {
            PromptHelper.showToast(R.string.netstate_unconnect);
            return;
        }
        String qt = "";
        if (mType == TYPE_PROTECT) {
            qt = "get_buy_bx";
        } else if (mType == TYPE_TC) {
            qt = "get_buy_traffic_plan";
        } else if (mType == TYPE_JYB) {
            qt = "get_buy_traffic_bag";
        } else if (mType == TYPE_LLFW) {
            qt = "get_buy_traffic_plan";
        }
        if (isRequesting) return;
        isRequesting = true;
        HttpController.getInstance(this).getTrafficInfo(qt, info.getDeviceId(), iHttpListener);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_apliy) {
            iv_apily.setSelected(true);
            iv_wx.setSelected(false);
            tv_apliy.setSelected(true);
            tv_wx.setSelected(false);
        } else if (v.getId() == R.id.layout_wx) {
            iv_apily.setSelected(false);
            iv_wx.setSelected(true);
            tv_apliy.setSelected(false);
            tv_wx.setSelected(true);
        } else if (v.getId() == R.id.button1) {
            doPay();
        } else if (v.getId() == R.id.button3) {
            finish();
        } else if (v.getId() == R.id.left) {
            ivRight.setEnabled(true);
            if (currentIndex - 1 >= 0 && currentIndex - 1 < mInfos.size()) {
                currentIndex--;
                switchView();
            }
            ivLeft.setEnabled(currentIndex - 1 >= 0 && currentIndex - 1 < mInfos.size());
        } else if (v.getId() == R.id.right) {
            ivLeft.setEnabled(true);
            if (currentIndex + 1 >= 0 && currentIndex + 1 < mInfos.size()) {
                currentIndex++;
                switchView();
            }
            ivRight.setEnabled(currentIndex + 1 >= 0 && currentIndex + 1 < mInfos.size());
        }
    }

    private void doPay() {
        if (!Util.isNetAvailable(this)) {
            PromptHelper.showToast(R.string.netstate_unconnect);
            return;
        }
        if (iv_apily.isSelected()) {
            doApliyPay();
        } else if (iv_wx.isSelected()) {
            doWxPay();
        }
    }

    private void pay(String data) {
        Pingpp.createPayment(this, data);
    }

    private void doWxPay() {
        if(info instanceof OrderInfo && !TextUtils.isEmpty(info.getPayoId())){
            HttpController.getInstance(this).rePayOrder(TYPE_PAY_WX, info.getDeviceId(), info.getPayoId(), iHttpListener);
        } else {
            HttpController.getInstance(this).purchase(TYPE_PAY_WX, mType, info.getDeviceId(), payView.getCurrentTrafficName(), payView.getCurrentTrafficId(), iHttpListener);
        }
    }

    private void doApliyPay() {
        if(info instanceof OrderInfo && !TextUtils.isEmpty(info.getPayoId())){
            HttpController.getInstance(this).rePayOrder(TYPE_PAY_APLIY, info.getDeviceId(), info.getPayoId(), iHttpListener);
        } else {
            HttpController.getInstance(this).purchase(TYPE_PAY_APLIY, mType, info.getDeviceId(), payView.getCurrentTrafficName(), payView.getCurrentTrafficId(), iHttpListener);
        }
    }

    public void addStatistics() {
        AppLogUtil.addOpenViewLog(this, PageId.PageDevice.PAGE_DEVICE_PAY, PageId.PAGE_MINE, "-1", "-1");
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
        return info;
    }

    private class HttpListener extends IHttpListener {

        @Override
        public void handleGetTrafficInfo(int statusCode, String deviceId, String qt, BaseInfo info) {
            isRequesting = false;
            if (AppUtil.checkResult(PayDialog.this, PageId.PageDevice.PAGE_DEVICE_PAY, statusCode)) {
                if (mType == TYPE_PROTECT) {
                    mInfos.add(info);
                } else if (mType == TYPE_TC) {
                    mInfos = info.getInfoList();
                } else if (mType == TYPE_JYB) {
                    mInfos = info.getInfoList();
                } else if (mType == TYPE_LLFW) {
                    mInfos = info.getInfoList();
                }
                refreshView();
            } else {
                PromptHelper.showToast(R.string.no_content);
            }
        }

        @Override
        public void handlePurchase(int statusCode, String deviceId, int type, BaseInfo info) {
            super.handlePurchase(statusCode, deviceId, type, info);
            if (AppUtil.checkResult(PayDialog.this, PageId.PageDevice.PAGE_DEVICE_PAY, statusCode) && info != null && !TextUtils.isEmpty(info.getCharge())) {
                pay(info.getCharge());
            } else {
                PromptHelper.showToast(R.string.error_pay_result);
            }
        }
    }

    private void refreshView() {
        if (mInfos != null && mInfos.size() <= 1) {
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        }
        currentIndex = 0;
        switchView();
    }

    private void switchView() {
        if (mInfos != null && mInfos.size() > currentIndex) {
            payView.refreshData(mType, mInfos.get(currentIndex));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
            /* 处理返回值
             * "success" - 支付成功
             * "fail"    - 支付失败
             * "cancel"  - 取消支付
             * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
             * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
             */

                String msg = null;
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                if (!TextUtils.isEmpty(errorMsg))
                    msg += errorMsg;
                if (!TextUtils.isEmpty(extraMsg))
                    msg += extraMsg;
                doResult(result, msg);
            }
        }
    }

    private void doResult(String result, String errorMsg) {
        if (isFinishing()) return;
        if (!TextUtils.isEmpty(result)) {
            if (result.equals("success")) {
                PaySuccessDialog.showDialog(this, mType, info, payView.getCurrentTrafficInfo());
                finish();
            } else if (result.equals("fail")) {
                PayFailDialog.showDialog(this, mType, info);
                finish();
            } else if (result.equals("cancel")) {
                PayFailDialog.showDialog(this, mType, info);
                finish();
            } else if (result.equals("invalid")) {
                if(!TextUtils.isEmpty(errorMsg))
                    PromptHelper.showToast(errorMsg);
            } else if (result.equals("unknown")) {
                if(!TextUtils.isEmpty(errorMsg))
                    PromptHelper.showToast(errorMsg);
            }
        } else {
            if(!TextUtils.isEmpty(errorMsg))
                PromptHelper.showToast(errorMsg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRequesting = false;
    }
}
