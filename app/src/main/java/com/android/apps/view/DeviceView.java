package com.android.apps.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

import butterknife.Bind;

/**
 * Created by root on 16-8-28.
 */
public class DeviceView extends LinearLayout implements TrafficDetailView.OnPurchaseClickListener, DateSlectView.OnDateChangeListener {

    DateSlectView date;
    TrafficDetailView traffic;
    private BaseInfo baseInfo;
    private OnDevicePurchaseItemClickListener listener;

    public DeviceView(Context context) {
        super(context);
        initView(context);
    }

    public DeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DeviceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_home_device_info, this, true);
        date = (DateSlectView)findViewById(R.id.date);
        traffic = (TrafficDetailView)findViewById(R.id.traffic);
        date.setOnDateChangeListener(this);
        traffic.setOnPurchaseClickListener(this);
    }

    @Override
    public void onPurchaseClick(int type, String year, String date) {
        if(listener != null)
            listener.onDevicePurchaseItemClick(baseInfo, type, year, date);
    }

    @Override
    public void onDateChange(String year, String date) {
        traffic.setDate(year, date);
    }

    public void refreshData(BaseInfo baseInfo) {
        this.baseInfo = baseInfo;
        if(baseInfo.getBuyTrafficPlan() == 1 && baseInfo.getPtype() == 1){
            date.setVisibility(INVISIBLE);
        } else {
            date.setVisibility(VISIBLE);
            date.resetView();
        }
        traffic.setData(baseInfo);
    }

    public void setOnDevicePurchaseItemClickListener(OnDevicePurchaseItemClickListener listener){
        this.listener = listener;
    }

    public interface OnDevicePurchaseItemClickListener{
        public void onDevicePurchaseItemClick(BaseInfo info, int type, String year, String date);
    }
}
