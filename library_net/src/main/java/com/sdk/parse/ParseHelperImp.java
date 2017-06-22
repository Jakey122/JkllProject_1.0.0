package com.sdk.parse;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.sdk.bean.BaseInfo;
import com.sdk.bean.JsonInfo;

import java.util.List;

/**
 * 这是一个基础的IParseHelper接口实现类
 * 各个bean只要实现此类中关注的方法就可以返回bean中的真实属性值
 * （如果bean直接实现IParseHelper接口 实现的方法太多，不好看所以藏到这里了！！！）
 * <p/>
 * Created by root on 16-6-21.
 */
public class ParseHelperImp implements IParseHelper {

    @Override
    public <T extends IParseHelper> T getParseHelper(Object obj) {
        if (obj instanceof IParseHelper) {
            return (T) obj;
        } else if (obj instanceof String) {
            return (T) JsonInfo.newInstance(String.valueOf(obj));
        }
        return (T) this;
    }

    @Override
    public String getVerify() {
        return "";
    }

    @Override
    public String getAccount() {
        return "";
    }

    @Override
    public String getNickname() {
        return "";
    }

    @Override
    public String getLogo() {
        return "";
    }

    @Override
    public String getCversion() {
        return "";
    }

    @Override
    public int getCversionCode() {
        return 0;
    }

    @Override
    public String getSdesc() {
        return "";
    }

    @Override
    public long getFsize() {
        return 0;
    }

    @Override
    public String getApk() {
        return "";
    }

    @Override
    public String getQuestion() {
        return "";
    }

    @Override
    public String getAnswer() {
        return "";
    }

    @Override
    public int getResId() {
        return -1;
    }

    @Override
    public String getCustomerInfo() {
        return "";
    }

    @Override
    public String getAboutInfo() {
        return "";
    }


    @Override
    public String getPrdId() {
        return "";
    }

    @Override
    public String getPrdName() {
        return "";
    }

    @Override
    public String getPrdLogo() {
        return "";
    }

    @Override
    public String getDeviceId() {
        return "";
    }

    @Override
    public String getDeviceName() {
        return "";
    }

    @Override
    public String getDeviceModel() {
        return "";
    }

    @Override
    public String getDeviceCode() {
        return "";
    }

    @Override
    public String getDeviceProtectDate() {
        return "";
    }

    @Override
    public String getDeviceCustomer() {
        return "";
    }

    @Override
    public String getDeviceProtectMsg(){
        return "";
    }

    @Override
    public int getDeviceProtect(){
        return 0;
    }

    @Override
    public int getStatus() {
        return 1;
    }

    @Override
    public int getShowRealnameAuth() {
        return 0;
    }

    @Override
    public String getRealnameMsg() {
        return "";
    }

    @Override
    public String getRealnameUrl() {
        return "";
    }

    @Override
    public String getTrafficPlanEtime() {
        return "";
    }

    @Override
    public int getBuyTrafficPlan() {
        return 0;
    }

    @Override
    public String getBuyTrafficPlanMsg() {
        return "";
    }

    @Override
    public int getBuyTrafficBag() {
        return 0;
    }

    @Override
    public String getBuyTrafficBagMsg() {
        return "";
    }

    @Override
    public int getBuyTrafficPlan2g() {
        return 0;
    }

    @Override
    public String getBuyTrafficPlan2gMsg() {
        return "";
    }

    @Override
    public int getPtype() {
        return 1;
    }

    @Override
    public long getWmonth() {
        return 0;
    }

    @Override
    public String getTrafficBagPsize() {
        return "";
    }

    @Override
    public String getTrafficLeft() {
        return "";
    }

    @Override
    public String getTrafficPlanPsize() {
        return "";
    }

    @Override
    public String getTrafficUsed() {
        return "";
    }

    @Override
    public String getBx_id() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getSdate() {
        return "";
    }

    @Override
    public String getPrice() {
        return "0";
    }

    @Override
    public String getValidity() {
        return "";
    }

    @Override
    public String getTraffic_id() {
        return "";
    }

    @Override
    public String getExpdate() {
        return "";
    }

    @Override
    public String getCharge() {
        return "";
    }

    @Override
    public int getOrderState(){
        return 0;
    }

    @Override
    public long getOrderStartTime(){
        return 0;
    }

    @Override
    public String getOrderTime(){
        return "";
    }

    @Override
    public String getOrderId(){
        return "";
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getOpenType() {
        return 0;
    }

    @Override
    public int getShowType() {
        return 0;
    }

    @Override
    public String getBaseShortDescript() {
        return "";
    }

    @Override
    public int getPageTotalItemCount() {
        return 0;
    }

    @Override
    public int getPageTotalPageCount() {
        return 0;
    }

    @Override
    public int getPageEveryPageSize() {
        return 0;
    }

    @Override
    public int getPageCurrentIndex() {
        return 0;
    }

    @Override
    public int getItemViewType() {
        return 0;
    }

    @Override
    public int getItemSpanCount() {
        return 0;
    }

    @Override
    public String getHtmlAddress() {
        return "";
    }

    @Override
    public List<BaseInfo> getInfoList(Object... obj) {
        return null;
    }

    @Override
    public BaseInfo getInfo(Object... obj) {
        return null;
    }

    @Override
    public int getTabPageId() {
        return -1;
    }

    @Override
    public int getTabPagePosition() {
        return 0;
    }

    @Override
    public String getPackageName() {
        return "";
    }

    @Override
    public int getVersionCode() {
        return 0;
    }

    @Override
    public String getFilepath() {
        return "";
    }

    @Override
    public String getVersionName() {
        return "";
    }

    @Override
    public String getOrderInfo(){
        return "";
    }

    @Override
    public String getOname(){
        return "";
    }

    @Override
    public String getOdesc(){
        return "";
    }

    @Override
    public String getPayoId(){
        return "";
    }

    @Override
    public int getPayType(){
        return 1;
    }


    @Override
    public long getCtime(){
        return 0;
    }

    @Override
    public String getPrepayId(){
        return "";
    }

    @Override
    public String getSign(){
        return "";
    }
}
