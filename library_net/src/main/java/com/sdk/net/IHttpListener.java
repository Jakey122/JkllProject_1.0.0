package com.sdk.net;

import com.sdk.bean.BaseInfo;

import java.util.List;

/**
 * Created by root on 16-6-2.
 */
public abstract class IHttpListener {

    public void handleLogin(int status, BaseInfo info) {
    }

    public void handleRegister(int status) {
    }

    public void handleSendcode(int statusCode) {
    }

    public void handleReset(int statusCode) {
    }

    public void handleUpdateUserInfo(int statusCode) {
    }

    public void handleUpdateUserPic(int statusCode) {
    }

    public void handleHomeDeviceList(int status, List<BaseInfo> list) {
    }

    public void handleGetDeviceList(int status, List<BaseInfo> list) {
    }

    public void handleDeviceDetailInfo(int status, BaseInfo info) {
    }

    public void handleDeviceTrafficInfo(int status, BaseInfo info) {
    }

    public void handleAddDevice(int status, BaseInfo info) {
    }

    public void handleDeleteDevice(int statusCode) {
    }

    public void handleUpdateDevice(int statusCode) {
    }

    public void handleGetTrafficInfo(int statusCode, String deviceId, String qt, BaseInfo info) {
    }

    public void handlePurchase(int statusCode, String deviceId, int type, BaseInfo info) {
    }

    public void handleCancelOrder(int statusCode, String orderid) {
    }

    public void handleSelfUpdate(int status, BaseInfo baseInfo) {
    }

    public void handleMineInfo(int status, BaseInfo baseInfo) {
    }

    public void handlePostFeedback(int state) {
    }

}
