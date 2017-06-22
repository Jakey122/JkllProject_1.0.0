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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.dialog.PayDialog;
import com.android.apps.fragment.BaseFragment;
import com.android.apps.helper.ImageHelper;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppUtil;
import com.android.apps.util.Const;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.helper.ListenerHelper;
import com.sdk.helper.OnItemChangeListener;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;
import com.sdk.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceDetailActivity extends BaseActivity implements OnItemChangeListener {

    @Bind(R.id.toolbar_back)
    ImageView toolbarBack;
    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.layout_nickname)
    RelativeLayout layoutNickname;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.usemethod)
    TextView usemethod;
    @Bind(R.id.model)
    TextView model;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.layout_info)
    RelativeLayout layoutInfo;
    @Bind(R.id.protect_date)
    TextView protectDate;
    @Bind(R.id.purchase_protect)
    TextView purchaseProtect;
    @Bind(R.id.customer)
    TextView customer;
    @Bind(R.id.layout_protect)
    RelativeLayout layoutProtect;
    @Bind(R.id.tc_traffic)
    TextView tcTraffic;
    @Bind(R.id.purchase_tc_traffic)
    TextView purchaseTcTraffic;
    @Bind(R.id.by_traffic)
    TextView byTraffic;
    @Bind(R.id.purchase_jyb_traffic)
    TextView purchaseJybTraffic;
    @Bind(R.id.layout_traffic)
    RelativeLayout layoutTraffic;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.protect_date_tip)
    TextView protectDateTip;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.tc_traffic_tip)
    TextView tcTrafficTip;
    @Bind(R.id.by_traffic_tip)
    TextView byTrafficTip;
    @Bind(R.id.auth)
    TextView auth;
    @Bind(R.id.layout_icon)
    LinearLayout layoutIcon;
    @Bind(R.id.loading_progress)
    ProgressBar loadingProgress;
    @Bind(R.id.loading_name)
    TextView loadingName;
    @Bind(R.id.layout_loading)
    RelativeLayout layoutLoading;
    @Bind(R.id.nickname)
    TextView nickname;
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.modify)
    TextView modify;
    @Bind(R.id.layout_modify)
    RelativeLayout layoutModify;
    private int mFrom;
    private int mType;

    private IHttpListener mListener;
    private boolean isRequsting;
    private BaseInfo info;

    public static void actionActivity(Context context, int type, int from, BaseInfo info) {
        Intent sIntent = new Intent(context, DeviceDetailActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.putExtra("info", info);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_device_detail);
        ButterKnife.bind(this);
        layoutModify.setVisibility(View.GONE);
        layoutNickname.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.VISIBLE);
        loadingName.setText(R.string.list_loading_normal);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);
        info = getIntent().getParcelableExtra("info");

        mListener = new HttpListener();

        layoutToolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.device_detail));
        ListenerHelper.addListener(this, mType + "");
        loadData(true);
    }

    private void loadData(boolean loading) {
        if (!Util.isNetAvailable(this)) {
            PromptHelper.showToast(getResources().getString(R.string.netstate_unconnect));
            finish();
            return;
        }
        if (loading) {
            layoutLoading.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);
            loadingName.setText(R.string.list_loading_normal);
        } else {
            layoutLoading.setVisibility(View.GONE);
        }
        if (isRequsting) return;
        isRequsting = true;
        HttpController.getInstance(this).getDeviceDetailInfo(info.getDeviceId(), mListener);
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

    @OnClick({R.id.purchase_protect, R.id.purchase_tc_traffic, R.id.purchase_jyb_traffic, R.id.layout_toolbar_back, R.id.layout_nickname, R.id.usemethod, R.id.auth, R.id.layout_loading, R.id.modify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_toolbar_back:
                finish();
                break;
            case R.id.purchase_protect:
                PayDialog.showDialog(DeviceDetailActivity.this, PayDialog.TYPE_PROTECT, info);
                break;
            case R.id.purchase_tc_traffic:
                PayDialog.showDialog(DeviceDetailActivity.this, PayDialog.TYPE_TC, info);
                break;
            case R.id.purchase_jyb_traffic:
                PayDialog.showDialog(DeviceDetailActivity.this, PayDialog.TYPE_JYB, info);
                break;
            case R.id.layout_nickname:
                AppUtil.showIme(etNickname, mHandler);
                layoutModify.setVisibility(View.VISIBLE);
                layoutNickname.setVisibility(View.GONE);
                break;
            case R.id.usemethod:
                WebH5Activity.actionWebH5ActivityActivity(this, toolbarTitle.getText().toString(), Const.DEVICE_USE_METHOD_URL, mType, info);
                break;
            case R.id.auth:
                if (info != null)
                    WebH5Activity.actionWebH5ActivityActivity(this, getString(R.string.purchase_traffic_auth_title), info.getRealnameUrl(), mType, info);
                break;
            case R.id.layout_loading:
                if (Util.isNetAvailable(this))
                    loadData(true);
                else
                    com.sdk.lib.util.Util.startSettings(this);
                break;
            case R.id.modify:
                doEdit();
                break;
        }
    }


    private void doEdit() {
        String nickname = etNickname.getText().toString();
        if (!isReady(nickname)) return;
        HttpController.getInstance(this).updateDevice(info.getDeviceId(), nickname, null);
    }

    public boolean isEditMode() {
        if (layoutModify.getVisibility() == View.VISIBLE) {
            layoutModify.setVisibility(View.GONE);
            layoutNickname.setVisibility(View.VISIBLE);
            AppUtil.dismissIme(etNickname);
            return true;
        }
        return false;
    }

    private boolean isReady(String nickname) {
        if (TextUtils.isEmpty(nickname) || nickname.trim().length() == 0) {
            PromptHelper.showToast(R.string.error_nickname_empty);
            return false;
        }
        if (!Util.isNetAvailable(this)) {
            PromptHelper.showToast(getResources().getString(R.string.netstate_unconnect));
            return false;
        }
        return true;
    }

    @Override
    public void onChange(int pageId, int status, BaseInfo info) {
        if (pageId == ListenerHelper.TYPE_PAGE_DEVICE_DETAIL) {
            if (AppUtil.checkResult(this, status, mType)) {
                this.info.setNickname(info.getNickname());
                refreshView(this.info);
                PromptHelper.showToast(getResources().getString(R.string.success_modify));
            } else {
                PromptHelper.showToast(getResources().getString(R.string.error_modify));
            }
            isRequsting = false;
            layoutModify.setVisibility(View.GONE);
            layoutNickname.setVisibility(View.VISIBLE);
            AppUtil.dismissIme(etNickname);
            loadData(false);
        }
    }

    private class HttpListener extends IHttpListener {
        @Override
        public void handleDeviceDetailInfo(int status, BaseInfo info) {
            super.handleDeviceDetailInfo(status, info);
            isRequsting = false;
            if (AppUtil.checkResult(DeviceDetailActivity.this, mType, status)) {
                if (info != null) {
                    layoutLoading.setVisibility(View.GONE);
                    loadingProgress.setVisibility(View.GONE);
                    refreshView(info.getInfo());
                } else {
                    loadingProgress.setVisibility(View.GONE);
                    layoutLoading.setVisibility(View.VISIBLE);
                    if (Util.isNetAvailable(DeviceDetailActivity.this))
                        loadingName.setText(R.string.list_loading_dataerror);
                    else
                        loadingName.setText("网络已断开,点击设置");
                }
            }
        }
    }

    private void refreshView(BaseInfo info) {
        nickname.setText(info.getNickname());
        name.setText(getResources().getString(R.string.device_name, info.getDeviceName()));
        model.setText(getResources().getString(R.string.device_model, info.getDeviceModel()));
        code.setText(getResources().getString(R.string.device_code, info.getDeviceCode()));
        protectDate.setText(info.getDeviceProtectDate());
        customer.setText(getResources().getString(R.string.device_customer, info.getDeviceCustomer()));
        if (info.getDeviceProtect() == 1) {
            protectDateTip.setVisibility(View.GONE);
            protectDate.setVisibility(View.GONE);
            purchaseProtect.setVisibility(View.GONE);
        } else {
            protectDateTip.setVisibility(View.VISIBLE);
            protectDate.setVisibility(View.VISIBLE);
            purchaseProtect.setVisibility(View.VISIBLE);
            protectDate.setText(info.getDeviceProtectMsg() + "\n" + info.getDeviceProtectDate());
        }

        if (info.getShowRealnameAuth() == 0) {
            if (info.getBuyTrafficBag() == 1) {
                purchaseJybTraffic.setVisibility(View.VISIBLE);
                byTrafficTip.setVisibility(View.VISIBLE);
                byTraffic.setVisibility(View.VISIBLE);
                byTraffic.setText(info.getBuyTrafficBagMsg());
            } else {
                purchaseJybTraffic.setVisibility(View.GONE);
                byTrafficTip.setVisibility(View.GONE);
                byTraffic.setVisibility(View.GONE);
            }

            if (info.getBuyTrafficPlan() == 1 && info.getPtype() == 2) {
                tcTraffic.setText(getResources().getString(R.string.device_detail_tc_traffic_etime, info.getTrafficPlanEtime()));
                purchaseTcTraffic.setText(R.string.device_purchase_tc_traffic);
                purchaseTcTraffic.setVisibility(View.VISIBLE);
                tcTrafficTip.setVisibility(View.VISIBLE);
                tcTraffic.setVisibility(View.VISIBLE);
            } else if (info.getBuyTrafficPlan() == 1 && info.getPtype() == 1) {
                tcTrafficTip.setText(R.string.device_traffic_service);
                tcTraffic.setText(getResources().getString(R.string.device_detail_tc_traffic_etime, info.getTrafficPlanEtime()));
                purchaseTcTraffic.setText(R.string.device_purchase_traffic_service);
                purchaseTcTraffic.setVisibility(View.VISIBLE);
                tcTrafficTip.setVisibility(View.VISIBLE);
                tcTraffic.setVisibility(View.VISIBLE);
                purchaseJybTraffic.setVisibility(View.GONE);
                byTrafficTip.setVisibility(View.GONE);
                byTraffic.setVisibility(View.GONE);
            } else {
                purchaseTcTraffic.setVisibility(View.GONE);
                tcTrafficTip.setVisibility(View.GONE);
                tcTraffic.setVisibility(View.GONE);
            }

        } else {
            if (info.getBuyTrafficBag() == 1) {
                purchaseJybTraffic.setVisibility(View.GONE);
                byTrafficTip.setVisibility(View.VISIBLE);
                byTraffic.setVisibility(View.VISIBLE);
                byTraffic.setText(info.getBuyTrafficBagMsg());
            } else {
                purchaseJybTraffic.setVisibility(View.GONE);
                byTrafficTip.setVisibility(View.GONE);
                byTraffic.setVisibility(View.GONE);
            }

            if (info.getBuyTrafficPlan() == 1 && info.getPtype() == 2) {
                tcTraffic.setText(getResources().getString(R.string.device_detail_tc_traffic_etime, info.getTrafficPlanEtime()));
                purchaseTcTraffic.setText(R.string.device_purchase_tc_traffic);
                purchaseTcTraffic.setVisibility(View.GONE);
                tcTrafficTip.setVisibility(View.VISIBLE);
                tcTraffic.setVisibility(View.VISIBLE);
            } else if (info.getBuyTrafficPlan() == 1 && info.getPtype() == 1) {
                tcTrafficTip.setText(R.string.device_traffic_service);
                tcTraffic.setText(getResources().getString(R.string.device_detail_tc_traffic_etime, info.getTrafficPlanEtime()));
                purchaseTcTraffic.setText(R.string.device_purchase_traffic_service);
                purchaseTcTraffic.setVisibility(View.GONE);
                tcTrafficTip.setVisibility(View.VISIBLE);
                tcTraffic.setVisibility(View.VISIBLE);
                purchaseJybTraffic.setVisibility(View.GONE);
                byTrafficTip.setVisibility(View.GONE);
                byTraffic.setVisibility(View.GONE);
            } else {
                purchaseTcTraffic.setVisibility(View.GONE);
                tcTrafficTip.setVisibility(View.GONE);
                tcTraffic.setVisibility(View.GONE);
            }
        }

        ImageHelper.getInstance(this).loadImage(info.getLogo(), icon);

    }

    @Override
    public void onBackPressed() {
        if (isEditMode())
            return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ListenerHelper.removeListener(this);
        super.onDestroy();
    }
}
