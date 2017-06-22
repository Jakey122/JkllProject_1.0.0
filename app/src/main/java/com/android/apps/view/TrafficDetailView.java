package com.android.apps.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.apps.activity.WebH5Activity;
import com.android.apps.dialog.PayDialog;
import com.android.apps.util.AppUtil;
import com.android.apps.util.PageId;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;
import com.sdk.util.Util;

/**
 * Created by root on 16-5-27.
 */
public class TrafficDetailView extends FrameLayout implements View.OnClickListener {

    String year, date;
    private BaseInfo baseInfo;
    private OnPurchaseClickListener listener;

    private TextView tc_traffic;
    private TextView jyb_traffic;
    private TextView yy_traffic;
    private TextView sy_traffic;
    private TextView traffic_tip;
    private TextView tip;
    private TextView purchase;

    private LinearLayout mImages, mPoints;
    private int count, width, childWidth, margins, scrollSpace;

    private IHttpListener mHttpListener;

    public TrafficDetailView(Context context) {
        super(context);
        initView(context);
    }

    public TrafficDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TrafficDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TrafficDetailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_home_device_info_traffic, this, true);
        tc_traffic = (TextView) findViewById(R.id.tc_traffic);
        jyb_traffic = (TextView) findViewById(R.id.jyb_traffic);
        yy_traffic = (TextView) findViewById(R.id.yy_traffic);
        sy_traffic = (TextView) findViewById(R.id.sy_traffic);
        traffic_tip = (TextView) findViewById(R.id.traffic_tip);
        tip = (TextView) findViewById(R.id.tip);
        purchase = (TextView) findViewById(R.id.purchase);
        purchase.setOnClickListener(this);
        mHttpListener = new HttpListener();
    }

    private void refreshView(BaseInfo info) {
        if (baseInfo.getShowRealnameAuth() == 1) {
            traffic_tip.setVisibility(VISIBLE);
            tc_traffic.setVisibility(INVISIBLE);
            jyb_traffic.setVisibility(INVISIBLE);
            yy_traffic.setVisibility(INVISIBLE);
            sy_traffic.setVisibility(INVISIBLE);
            traffic_tip.setText(getResources().getString(R.string.purchase_traffic_auth_title));
            tip.setText(getResources().getString(R.string.purchase_traffic_auth));
            purchase.setText(getResources().getString(R.string.purchase_traffic_auth_start));
        } else if (baseInfo.getBuyTrafficPlan() == 1 && baseInfo.getPtype() == 1) {
            traffic_tip.setVisibility(VISIBLE);
            tc_traffic.setVisibility(INVISIBLE);
            jyb_traffic.setVisibility(INVISIBLE);
            yy_traffic.setVisibility(INVISIBLE);
            sy_traffic.setVisibility(INVISIBLE);
            traffic_tip.setText(getResources().getString(R.string.home_device_traffic_title, baseInfo.getTrafficPlanEtime()));
            tip.setText(getResources().getString(R.string.purchase_traffic_2g_tip));
            purchase.setText(getResources().getString(R.string.purchase_traffic_2g));
        } else {
            traffic_tip.setVisibility(INVISIBLE);
            tc_traffic.setVisibility(VISIBLE);
            jyb_traffic.setVisibility(VISIBLE);
            yy_traffic.setVisibility(VISIBLE);
            sy_traffic.setVisibility(VISIBLE);
            if (info.getInfo() != null) {
                tc_traffic.setText(getResources().getString(R.string.tc_traffic, info.getInfo().getTrafficPlanPsize() + "MB"));
                jyb_traffic.setText(getResources().getString(R.string.jyb_traffic, info.getInfo().getTrafficBagPsize() + "MB"));
                yy_traffic.setText(getResources().getString(R.string.yy_traffic, info.getInfo().getTrafficUsed() + "MB"));
                sy_traffic.setText(getResources().getString(R.string.sy_traffic, info.getInfo().getTrafficLeft() + "MB"));
            }

            purchase.setVisibility(GONE);

            if (baseInfo.getBuyTrafficPlan() == 1 && baseInfo.getPtype() == 2) {
                tip.setText(baseInfo.getBuyTrafficPlanMsg());
                purchase.setVisibility(VISIBLE);
                purchase.setText(getResources().getString(R.string.purchase_tc_traffic));
            }

            if (baseInfo.getBuyTrafficBag() == 1) {
                tip.setText(baseInfo.getBuyTrafficBagMsg());
                purchase.setVisibility(VISIBLE);
                purchase.setText(getResources().getString(R.string.purchase_jyb_traffic));
            }
        }
    }

    public void setDate(String year, String date) {
        this.year = year;
        this.date = date;
        loadData();
    }

    public void loadData() {
        if (baseInfo == null) return;
        if (!Util.isNetAvailable(getContext())) return;
        HttpController.getInstance(getContext()).getDeviceTrafficInfo(baseInfo.getDeviceId(), year, date, mHttpListener);

    }

    public void setOnPurchaseClickListener(OnPurchaseClickListener listener) {
        this.listener = listener;
    }

    public void setData(BaseInfo baseInfo) {
        this.baseInfo = baseInfo;
        refreshView(baseInfo);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.purchase && listener != null) {
            if(baseInfo.getShowRealnameAuth() == 1){
                listener.onPurchaseClick(PayDialog.TYPE_AUTH, year, date);
            } else if (baseInfo.getBuyTrafficPlan() == 1 && baseInfo.getPtype() == 1)
                listener.onPurchaseClick(PayDialog.TYPE_LLFW, year, date);
            else if (baseInfo.getBuyTrafficPlan() == 1 && baseInfo.getPtype() == 2)
                listener.onPurchaseClick(PayDialog.TYPE_TC, year, date);
            else if (baseInfo.getBuyTrafficBag() == 1)
                listener.onPurchaseClick(PayDialog.TYPE_JYB, year, date);
        }
    }

    public interface OnPurchaseClickListener {
        public void onPurchaseClick(int type, String year, String date);
    }

    private class HttpListener extends IHttpListener {
        @Override
        public void handleDeviceTrafficInfo(int status, BaseInfo info) {
            super.handleDeviceTrafficInfo(status, info);
            if (AppUtil.checkResult(getContext(), PageId.PageMain.PAGE_HOME, status)) {
                refreshView(info);
            }
        }
    }
}
