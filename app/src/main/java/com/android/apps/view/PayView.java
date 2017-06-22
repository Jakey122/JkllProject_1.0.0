package com.android.apps.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.dialog.PayDialog;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.lib.util.UiUtil;

import java.util.List;

/**
 * Created by root on 16-8-28.
 */
public class PayView extends LinearLayout implements View.OnClickListener{

    ImageView icon;
    TextView title;
    TextView price;
    TextView desc;
    LinearLayout layout_time;
    View line2;

    private int width;
    private BaseInfo info;
    private List<BaseInfo> mInfos;
    private int mType;
    private int index;

    public PayView(Context context) {
        super(context);
        initView(context);
    }

    public PayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_pay_view, this, true);
        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        desc = (TextView) findViewById(R.id.desc);
        line2 = findViewById(R.id.line2);
        layout_time = (LinearLayout) findViewById(R.id.layout_time);
        width = (int)(UiUtil.getScreenSize(getContext()).widthPixels * 4 / 5);
    }

    private void refreshView(){
        if (mType == PayDialog.TYPE_PROTECT) {
            refreshProtectView();
        } else if (mType == PayDialog.TYPE_TC) {
            refreshTcView();
        } else if (mType == PayDialog.TYPE_LLFW) {
            refreshLlfwView();
        }
    }

    private void refreshProtectView(){
        if(mInfos == null || mInfos.size() <= index) return;
        BaseInfo info = mInfos.get(index);
        title.setText(info.getName());
        icon.setImageResource(R.drawable.ic_protect);
        desc.setText(info.getSdate());
        price.setText(getResources().getString(R.string.order_price, info.getPrice()+""));
    }

    private void refreshTcView(){
        if(mInfos == null || mInfos.size() <= index) return;
        BaseInfo info = mInfos.get(index);
        icon.setImageResource(R.drawable.ic_tc);
        title.setText(info.getName());
        desc.setText(info.getExpdate());
        price.setText(getResources().getString(R.string.order_price, info.getPrice()+""));
    }

    private void refreshLlfwView(){
        if(mInfos == null || mInfos.size() <= index) return;
        BaseInfo info = mInfos.get(index);
        icon.setImageResource(R.drawable.ic_llfwf);
        title.setText(info.getName());
        desc.setText(info.getExpdate());
        price.setText(getResources().getString(R.string.order_price, info.getPrice()+""));
    }

    public void refreshData(int mType, BaseInfo info) {
        if(info == null) return;
        this.info = info;
        this.mType = mType;
        this.mInfos = info.getInfoList();
        if (mType == PayDialog.TYPE_PROTECT) {
            refreshProtectView();
            if(mInfos != null){
                createProtectTime(width);
                layout_time.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
            } else {
                layout_time.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
            }
        } else if (mType == PayDialog.TYPE_TC) {
            refreshTcView();
            if(mInfos != null){
                createTcTime(width);
                layout_time.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
            } else {
                layout_time.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
            }
        } else if (mType == PayDialog.TYPE_JYB) {
            icon.setImageResource(R.drawable.ic_jyb);
            layout_time.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            title.setText(info.getName());
            desc.setText(info.getExpdate());
            price.setText(getResources().getString(R.string.order_price, info.getPrice()+""));
        } else if (mType == PayDialog.TYPE_LLFW) {
            refreshLlfwView();
            if(mInfos != null){
                createLlfwTime(width);
                layout_time.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
            } else {
                layout_time.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
            }
        }
    }

    private void createTcTime(int twidth) {
        layout_time.removeAllViews();
        int width = (twidth - UiUtil.dip2px(getContext(), 55)) / 4;
        int height = UiUtil.dip2px(getContext(), 30);
        for (int i = 0; i < mInfos.size(); i++) {
            BaseInfo info = mInfos.get(i);
            layout_time.addView(createTimeView(info.getValidity(), width, height));
        }
        refreshIndex(mInfos.get(index).getValidity());
    }

    private void createProtectTime(int twidth) {
        layout_time.removeAllViews();
        int width = (twidth - UiUtil.dip2px(getContext(), 55)) / 4;
        int height = UiUtil.dip2px(getContext(), 30);
        for (int i = 0; i < mInfos.size(); i++) {
            BaseInfo info = mInfos.get(i);
            layout_time.addView(createTimeView(info.getValidity(), width, height));
        }
        refreshIndex(mInfos.get(index).getValidity());
    }

    private void createLlfwTime(int twidth) {
        layout_time.removeAllViews();
        int width = (twidth - UiUtil.dip2px(getContext(), 55)) / 4;
        int height = UiUtil.dip2px(getContext(), 30);
        for (int i = 0; i < mInfos.size(); i++) {
            BaseInfo info = mInfos.get(i);
            layout_time.addView(createTimeView(info.getValidity(), width, height));
        }
        refreshIndex(mInfos.get(index).getValidity());
    }

    private TextView createTimeView(String date, int width, int height) {
        TextView name = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.leftMargin = UiUtil.dip2px(getContext(), 5);
        name.setLayoutParams(layoutParams);
        name.setTag(date);
        name.setGravity(Gravity.CENTER);
        name.setTextColor(this.getResources().getColor(R.color.grey));
        name.setTextSize(UiUtil.px2sp(getContext(), 40));
        name.setBackgroundResource(R.drawable.bg_button_date);
        name.setText(date);
        name.setOnClickListener(this);
        return name;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            refreshIndex(tag.toString());
            refreshView();
        }
    }

    private void refreshIndex(String tag) {
        for (int i = 0; i < layout_time.getChildCount(); i++) {
            TextView view = (TextView) layout_time.getChildAt(i);
            boolean selected = view.getTag().equals(tag);
            if(selected){
                index = i;
            }
            view.setSelected(selected);
            view.setTextColor(this.getResources().getColor(selected ? R.color.colorPrimary : R.color.grey));
        }
    }

    public String getCurrentTrafficId(){
        if (mType == PayDialog.TYPE_PROTECT) {
            return mInfos.get(index).getBx_id();
        } else if (mType == PayDialog.TYPE_TC) {
            return mInfos.get(index).getTraffic_id();
        } else if (mType == PayDialog.TYPE_JYB) {
            return info.getTraffic_id();
        } else if (mType == PayDialog.TYPE_LLFW) {
            return mInfos.get(index).getTraffic_id();
        }
        return "";
    }

    public String getCurrentTrafficName(){
        if (mType == PayDialog.TYPE_PROTECT) {
            return mInfos.get(index).getName();
        } else if (mType == PayDialog.TYPE_TC) {
            return mInfos.get(index).getName();
        } else if (mType == PayDialog.TYPE_JYB) {
            return info.getName();
        } else if (mType == PayDialog.TYPE_LLFW) {
            return mInfos.get(index).getName();
        }
        return "";
    }

    public BaseInfo getCurrentTrafficInfo(){
        if (mType == PayDialog.TYPE_PROTECT) {
            return mInfos.get(index);
        } else if (mType == PayDialog.TYPE_TC) {
            return mInfos.get(index);
        } else if (mType == PayDialog.TYPE_JYB) {
            return info;
        } else if (mType == PayDialog.TYPE_LLFW) {
            return mInfos.get(index);
        }
        return null;
    }
}
