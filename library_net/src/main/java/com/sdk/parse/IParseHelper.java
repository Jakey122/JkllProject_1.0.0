package com.sdk.parse;

import com.sdk.bean.BaseInfo;

import java.util.List;

/**
 * 这是一个所有bean元素传递和获取属性值的最外层接口
 * (别问我为啥有这么多方法！！)
 * Created by root on 16-5-9.
 */
public interface IParseHelper {

    <T extends IParseHelper> T getParseHelper(Object obj);

    /**
     * 获取verfify
     * @return
     */
    String getVerify();

    /**
     * 获取账号
     * @return
     */
    String getAccount();

    /**
     * 获取昵称
     * @return
     */
    String getNickname();

    /**
     * 获取头像
     * @return
     */
    String getLogo();

    /**
     * 返回版本名称
     * @return
     */
    String getCversion();

    /**
     * 返回版本code
     * @return
     */
    int getCversionCode();

    /**
     * 软件描述
     * @return
     */
    String getSdesc();

    /**
     * 软件大小
     * @return
     */
    long getFsize();

    int getResId();

    String getQuestion();

    String getAnswer();

    String getCustomerInfo();

    String getAboutInfo();

    public String getPrdId();

    public String getPrdName();

    public String getPrdLogo();

    String getDeviceId();

    String getDeviceName();

    String getDeviceModel();

    String getDeviceCode();

    String getDeviceProtectDate();

    String getDeviceCustomer();

    String getDeviceProtectMsg();

    int getDeviceProtect();

    int getStatus();

    int getShowRealnameAuth();

    String getRealnameMsg();

    String getRealnameUrl();

    String getTrafficPlanEtime();

    int getBuyTrafficPlan();

    String getBuyTrafficPlanMsg();

    String getBuyTrafficBagMsg();

    int getBuyTrafficBag();

    int getBuyTrafficPlan2g();

    String getBuyTrafficPlan2gMsg();

    int getPtype();

    long getWmonth();

    String getTrafficPlanPsize();

    String getTrafficBagPsize();

    String getTrafficUsed();

    String getTrafficLeft();

    String getBx_id();

    String getName();

    String getSdate();

    String getPrice();

    String getValidity();

    String getTraffic_id();

    String getExpdate();

    String getCharge();

    int getOrderState();

    long getOrderStartTime();

    String getOrderTime();

    String getOrderId();

    /**
     * 软件下载地址
     * @return
     */
    String getApk();

    int getType();

    /**
     * 基础信息 元素打开类型
     *
     * @return
     */
    int getOpenType();

    /**
     * 基础信息 元素展示类型
     *
     * @return
     */
    int getShowType();

    /**
     * 短简介
     *
     * @return
     */
    String getBaseShortDescript();


    int getPageTotalItemCount();

    int getPageTotalPageCount();

    int getPageEveryPageSize();

    int getPageCurrentIndex();

    /**
     * 元素展示类型（单行，或者n列宫格）
     *
     * @return
     */
    int getItemViewType();

    /**
     * 宫格展示类型的列数
     *
     * @return
     */
    int getItemSpanCount();

    /**
     * HTML 元素打开链接地址
     *
     * @return
     */
    String getHtmlAddress();

    /**
     * 获得元素内嵌list
     *
     * @param obj
     * @return
     */
    List<BaseInfo> getInfoList(Object... obj);

    /**
     *
     *
     * @param obj
     * @return
     */
    BaseInfo getInfo(Object... obj);


    int getTabPageId();

    int getTabPagePosition();

    String getPackageName();

    int getVersionCode();

    String getFilepath();

    String getVersionName();

    public String getOrderInfo();

    public String getOname();

    public String getOdesc();

    public String getPayoId();

    public int getPayType();

    public long getCtime();

    public String getPrepayId();

    public String getSign();
}
