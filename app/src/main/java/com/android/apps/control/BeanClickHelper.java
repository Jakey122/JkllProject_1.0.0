package com.android.apps.control;

import android.content.Context;
import android.view.View;

import com.android.apps.App;
import com.android.apps.activity.DeviceDetailActivity;
import com.android.apps.activity.MainActivity;
import com.android.apps.dialog.OrderDetailDialog;
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.android.apps.util.AppUtil;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.DeviceInfo;
import com.sdk.bean.OrderInfo;

/**
 * Created by root on 16-7-2.
 */
public class BeanClickHelper {

    public static void handleBeanClick(Context mContext, View v, BaseInfo info, int fromPage) {
        if(mContext == null)
            mContext = App.getInstance().getApplicationContext();
        if (info instanceof DeviceInfo) {
            openDeviceDetail(mContext, v, info, fromPage);
            return;
        } else if (info instanceof OrderInfo) {
            openOrderDetail(mContext, info);
            return;
        }
    }

    private static void openDeviceDetail(Context mContext, View v, BaseInfo baseInfo, int fromgPage) {
        if(baseInfo.getOpenType() == BaseInfo.OPEN_DEVICE_ADD){
            if(mContext instanceof MainActivity)
                AppUtil.actionCaptureActivity((MainActivity)mContext, Const.REQUEST_CODE_ADD_DEVICE);
        } else
            DeviceDetailActivity.actionActivity(mContext, PageId.PageDevice.PAGE_DEVICE_DETAIL, fromgPage, baseInfo);
    }

    public static void openOrderDetail(Context mContext, BaseInfo baseInfo) {
        OrderDetailDialog.showDialog(mContext, baseInfo);
    }
}
