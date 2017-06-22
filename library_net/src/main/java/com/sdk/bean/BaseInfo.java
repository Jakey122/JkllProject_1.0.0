package com.sdk.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.sdk.parse.IParseHelper;
import com.sdk.parse.ParseHelperImp;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有接口构建元素的父类
 * 通过实现 IParseHelper接口从结构化的json数据中读取指定属性值
 * 同时通过 override IParseHelper中的相应方法返回序列化后的属性真实数据
 * <p/>
 * Created by root on 16-5-9.
 */
public class BaseInfo extends ParseHelperImp implements Parcelable {

    /**
     * 接口构建的所有元素在 网格化recyclerview中的位置形态
     */
    public static final int ITEM_TYPE_GRID = 1;
    public static final int ITEM_TYPE_LIST = 0;

    /**
     * 接口构建的所有元素的打开形态
     */
    public static final int OPEN_DEVICE_DETAIL = 1;   //设备详情
    public static final int OPEN_DEVICE_ADD = 2;   //添加设备
    public static final int OPEN_ORDER_DETAIL = 3;  //订单详情

    /**
     * 接口构建的所有最底层元素的展示形态
     */
    public static final int VISIBLE_TYPE_ITEM_BASE = 200;
    public static final int VISIBLE_TYPE_ITEM_DEVICE = VISIBLE_TYPE_ITEM_BASE + 0;   //设备
    public static final int VISIBLE_TYPE_ITEM_ORDER = VISIBLE_TYPE_ITEM_BASE + 1;  //订单
    public static final int VISIBLE_TYPE_ITEM_QUESTION = VISIBLE_TYPE_ITEM_BASE + 2;   //常见问题
    public static final int VISIBLE_TYPE_ITEM_DEVICE_ADD = VISIBLE_TYPE_ITEM_BASE + 3;   //添加设备

    /**
     * 订单状态
     */
    public static final int ORDER_STATE_NOTPAY = 1;
    public static final int ORDER_STATE_PURCHASED = 2;
    public static final int ORDER_STATE_TIMEOUT = 3;
    public static final int ORDER_STATE_CANCELED = 4;



    protected String verify;        //登录验证串
    protected String account;       //账号
    protected String nickname;      //昵称
    protected String logo = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494702843547&di=dbd37de3292ed96aa56c84be8f64479c&imgtype=0&src=http%3A%2F%2Fstatic.9ijf.com%2Fzhm%2Fitem%2F000000%2F201307%2F254667.jpg";          //头像

    protected String cversion;
    protected int cversionCode;
    protected String sdesc;
    protected long fsize;
    protected String apk;

    protected String question;
    protected String answer;
    protected String customerinfo;
    protected String aboutinfo;

    protected String prd_id;
    protected String prd_logo;
    protected String prd_name;

    protected String devieId;
    protected String devieName;
    protected String devieModel;
    protected String devieCode;
    protected String devieProtectMsg;
    protected String devieProtectDate;
    protected int devieProtect;
    protected String devieCustomer;
    protected int status;
    protected int showRealnameAuth;
    protected String realnameMsg;
    protected String realnameUrl;
    protected String trafficPlanEtime;
    protected int buyTrafficPlan;
    protected String buyTrafficPlanMsg;
    protected String buyTrafficBagMsg;
    protected int buyTrafficBag;
    protected int buyTrafficPlan2g;
    protected String buyTrafficPlan2gMsg;
    protected int ptype;
    protected long wmonth;
    protected String trafficPlanPsize;
    protected String trafficBagPsize;
    protected String trafficUsed;
    protected String trafficLeft;

    protected String bx_id;
    protected String name;
    protected String sdate;
    protected String price;
    protected String validity;
    protected String traffic_id;
    protected String expdate;
    protected String charge;


    protected int orderState;
    protected long orderStartTime;
    protected String orderTime;
    protected String orderId;
    protected String orderInfo;

    protected String oname;
    protected String odesc;
    protected String pay_o_id;
    protected int pay_type = 2;
    protected long ctime;

    protected String prepayId;
    protected String sign;



    protected int mType;                             //类型
    protected int position;
    protected long currenPageId = -1;                //下载任务当前所在页面id
    protected long fromPageId = -1;                  //下载任务上一级页面id
    protected long fatherId = -1;
    protected int itemViewType = 0;
    protected int itemSpanCount = 0;
    protected int openType;
    protected int showType;
    protected String shortDesc;                      //简短描述
    protected List<BaseInfo> mList = new ArrayList<>();
    protected BaseInfo info;
    protected int resId = 0;
    protected String packageName;
    protected int versionCode;
    protected String filePath;
    protected String versionName;

    public BaseInfo() {
    }

    protected BaseInfo(Parcel in) {
        verify = in.readString();
        account = in.readString();
        nickname = in.readString();
        logo = in.readString();

        cversion = in.readString();
        cversionCode = in.readInt();
        sdesc = in.readString();
        fsize = in.readLong();
        apk = in.readString();

        question = in.readString();
        answer = in.readString();

        customerinfo = in.readString();
        aboutinfo = in.readString();

        prd_id = in.readString();
        prd_logo = in.readString();
        prd_name = in.readString();

        devieId = in.readString();
        devieName = in.readString();
        devieModel = in.readString();
        devieCode = in.readString();
        devieProtectMsg = in.readString();
        devieProtectDate = in.readString();
        devieProtect = in.readInt();
        devieCustomer = in.readString();
        status = in.readInt();
        showRealnameAuth = in.readInt();
        realnameMsg = in.readString();
        realnameUrl = in.readString();
        trafficPlanEtime = in.readString();
        buyTrafficPlan = in.readInt();
        buyTrafficPlanMsg = in.readString();
        buyTrafficBag = in.readInt();
        buyTrafficBagMsg = in.readString();
        buyTrafficPlan2g = in.readInt();
        buyTrafficPlan2gMsg = in.readString();
        ptype = in.readInt();
        wmonth = in.readLong();
        trafficPlanPsize = in.readString();
        trafficBagPsize = in.readString();
        trafficUsed = in.readString();
        trafficLeft = in.readString();
        bx_id = in.readString();
        name = in.readString();
        sdate = in.readString();
        price = in.readString();
        validity = in.readString();
        traffic_id = in.readString();
        expdate = in.readString();
        charge = in.readString();
        orderState = in.readInt();
        orderStartTime = in.readLong();
        orderTime = in.readString();
        orderId = in.readString();
        orderInfo = in.readString();
        oname = in.readString();
        odesc = in.readString();
        pay_o_id = in.readString();
        pay_type = in.readInt();
        ctime = in.readLong();
        prepayId = in.readString();
        sign = in.readString();

        mType = in.readInt();
        position = in.readInt();
        currenPageId = in.readLong();
        fromPageId = in.readLong();
        itemViewType = in.readInt();
        itemSpanCount = in.readInt();
        fatherId = in.readLong();
        openType = in.readInt();
        showType = in.readInt();
        shortDesc = in.readString();
        mList = in.readArrayList(BaseInfo.class.getClassLoader());
        info = in.readParcelable(BaseInfo.class.getClassLoader());
        resId = in.readInt();
        packageName = in.readString();
        versionCode = in.readInt();
        filePath = in.readString();
        versionName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(verify);
        dest.writeString(account);
        dest.writeString(nickname);
        dest.writeString(logo);
        dest.writeString(cversion);
        dest.writeInt(cversionCode);
        dest.writeString(sdesc);
        dest.writeLong(fsize);
        dest.writeString(apk);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(customerinfo);
        dest.writeString(aboutinfo);
        dest.writeString(prd_id);
        dest.writeString(prd_logo);
        dest.writeString(prd_name);
        dest.writeString(devieId);
        dest.writeString(devieName);
        dest.writeString(devieModel);
        dest.writeString(devieCode);
        dest.writeString(devieProtectMsg);
        dest.writeString(devieProtectDate);
        dest.writeInt(devieProtect);
        dest.writeString(devieCustomer);
        dest.writeInt(status);
        dest.writeInt(showRealnameAuth);
        dest.writeString(realnameMsg);
        dest.writeString(realnameUrl);
        dest.writeString(trafficPlanEtime);
        dest.writeInt(buyTrafficPlan);
        dest.writeString(buyTrafficPlanMsg);
        dest.writeInt(buyTrafficBag);
        dest.writeString(buyTrafficBagMsg);
        dest.writeInt(buyTrafficPlan2g);
        dest.writeString(buyTrafficPlan2gMsg);
        dest.writeInt(ptype);
        dest.writeLong(wmonth);
        dest.writeString(trafficPlanPsize);
        dest.writeString(trafficBagPsize);
        dest.writeString(trafficUsed);
        dest.writeString(trafficLeft);

        dest.writeString(bx_id);
        dest.writeString(name);
        dest.writeString(sdate);
        dest.writeString(price);
        dest.writeString(validity);
        dest.writeString(traffic_id);
        dest.writeString(expdate);
        dest.writeString(charge);

        dest.writeInt(orderState);
        dest.writeLong(orderStartTime);
        dest.writeString(orderTime);
        dest.writeString(orderId);
        dest.writeString(orderInfo);
        dest.writeString(oname);
        dest.writeString(odesc);
        dest.writeString(pay_o_id);
        dest.writeInt(pay_type);
        dest.writeLong(ctime);
        dest.writeString(prepayId);
        dest.writeString(sign);

        dest.writeInt(mType);
        dest.writeInt(position);
        dest.writeLong(currenPageId);
        dest.writeLong(fromPageId);
        dest.writeInt(itemViewType);
        dest.writeInt(itemSpanCount);
        dest.writeLong(fatherId);

        dest.writeInt(openType);
        dest.writeInt(showType);
        dest.writeString(shortDesc);
        dest.writeList(mList);
        dest.writeParcelable(info, 0);
        dest.writeInt(resId);
        dest.writeString(packageName);
        dest.writeInt(versionCode);
        dest.writeString(filePath);
        dest.writeString(versionName);
    }

    public static final Creator<BaseInfo> CREATOR = new Creator<BaseInfo>() {
        @Override
        public BaseInfo createFromParcel(Parcel in) {
            return new BaseInfo(in);
        }

        @Override
        public BaseInfo[] newArray(int size) {
            return new BaseInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * 通过openType 构建指定的bean
     *
     * @param obj
     * @param <T>
     * @return
     */
    public <T extends BaseInfo> T parse(Object obj) {
        T item = null;
        IParseHelper helper = getParseHelper(obj);
        switch (helper.getOpenType()) {
            case BaseInfo.OPEN_DEVICE_DETAIL:
                item = (T) new DeviceInfo();
                break;
            case BaseInfo.OPEN_ORDER_DETAIL:
                item = (T) new OrderInfo();
                break;
            default:
                item = null;
                break;
        }

        if (item != null) {
            item.parse(helper);
        } else {
            item = (T) this;
            item.parseDefault(helper);
        }
        return item;
    }

    /**
     * 初始化bean中基础数据
     *
     * @param helper
     */
    protected void parseDefault(IParseHelper helper) {
        if (helper == null) return;
        logo = helper.getLogo();
        verify = helper.getVerify();
        account = helper.getAccount();
        nickname = helper.getNickname();
        question = helper.getQuestion();
        answer = helper.getAnswer();
        customerinfo = helper.getCustomerInfo();
        aboutinfo = helper.getAboutInfo();
        status = helper.getStatus();
        showRealnameAuth = helper.getShowRealnameAuth();
        realnameMsg = helper.getRealnameMsg();
        realnameUrl = helper.getRealnameUrl();
        trafficPlanEtime = helper.getTrafficPlanEtime();
        buyTrafficPlan = helper.getBuyTrafficPlan();
        buyTrafficPlanMsg = helper.getBuyTrafficPlanMsg();
        buyTrafficBag = helper.getBuyTrafficBag();
        buyTrafficBagMsg = helper.getBuyTrafficBagMsg();
        buyTrafficPlan2g = helper.getBuyTrafficPlan2g();
        buyTrafficPlan2gMsg = helper.getBuyTrafficPlan2gMsg();
        ptype = helper.getPtype();
        wmonth = helper.getWmonth();
        trafficPlanPsize = helper.getTrafficPlanPsize();
        trafficBagPsize = helper.getTrafficBagPsize();
        trafficUsed = helper.getTrafficUsed();
        trafficLeft = helper.getTrafficLeft();
        bx_id = helper.getBx_id();
        name = helper.getName();
        sdate = helper.getSdate();
        price = helper.getPrice();
        validity = helper.getValidity();
        traffic_id = helper.getTraffic_id();
        expdate = helper.getExpdate();
        charge = helper.getCharge();

        orderStartTime = helper.getOrderStartTime();
        orderTime = helper.getOrderTime();
        orderState = helper.getOrderState();
        orderId = helper.getOrderId();
        orderInfo = helper.getOrderInfo();
        oname = helper.getOname();
        odesc = helper.getOdesc();
        pay_o_id = helper.getPayoId();
        pay_type = helper.getPayType();
        ctime = helper.getCtime();
        prepayId = helper.getPrepayId();
        sign = helper.getSign();
        prd_id = helper.getPrdId();
        prd_name = helper.getPrdName();
        prd_logo = helper.getPrdLogo();
        devieId = helper.getDeviceId();
        devieName = helper.getDeviceName();
        devieModel = helper.getDeviceModel();
        devieCode = helper.getDeviceCode();
        devieProtectMsg = helper.getDeviceProtectMsg();
        devieProtectDate = helper.getDeviceProtectDate();
        devieCustomer = helper.getDeviceCustomer();
        devieProtect = helper.getDeviceProtect();
        shortDesc = helper.getBaseShortDescript();
        openType = helper.getOpenType();
        showType = helper.getShowType();
    }

    /**
     * 解析指定bean元素 List
     *
     * @param obj
     * @param clazz
     * @param key
     */
    public void parseInfoList(Object obj, Class clazz, String key) {
        IParseHelper helper = getParseHelper(obj);
        if (helper != null) mList = helper.getInfoList(clazz, key);
    }

    @Override
    public String getVerify() {
        return verify;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getLogo() {
        return logo;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public String getCustomerInfo() {
        return customerinfo;
    }

    @Override
    public String getAboutInfo() {
        return aboutinfo;
    }

    @Override
    public String getPrdId() {
        return prd_id;
    }

    @Override
    public String getPrdName() {
        return prd_name;
    }

    @Override
    public String getPrdLogo() {
        return prd_logo;
    }

    @Override
    public String getDeviceId() {
        return devieId;
    }

    @Override
    public String getDeviceName() {
        return devieName;
    }

    @Override
    public String getDeviceModel() {
        return devieModel;
    }

    @Override
    public String getDeviceCode() {
        return devieCode;
    }

    @Override
    public String getDeviceProtectMsg() {
        return devieProtectMsg;
    }
    @Override
    public String getDeviceProtectDate() {
        return devieProtectDate;
    }

    @Override
    public int getDeviceProtect() {
        return devieProtect;
    }

    @Override
    public String getDeviceCustomer() {
        return devieCustomer;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public int getShowRealnameAuth() {
        return showRealnameAuth;
    }

    @Override
    public String getRealnameMsg() {
        return realnameMsg;
    }

    @Override
    public String getRealnameUrl() {
        return realnameUrl;
    }

    @Override
    public String getTrafficPlanEtime() {
        return trafficPlanEtime;
    }

    @Override
    public int getBuyTrafficPlan() {
        return buyTrafficPlan;
    }

    @Override
    public String getBuyTrafficPlanMsg() {
        return buyTrafficPlanMsg;
    }

    @Override
    public int getBuyTrafficBag() {
        return buyTrafficBag;
    }

    @Override
    public String getBuyTrafficBagMsg() {
        return buyTrafficBagMsg;
    }

    @Override
    public int getBuyTrafficPlan2g() {
        return buyTrafficPlan2g;

    }

    @Override
    public String getBuyTrafficPlan2gMsg() {
        return buyTrafficPlan2gMsg;
    }

    @Override
    public int getPtype() {
        return ptype;
    }

    @Override
    public long getWmonth() {
        return wmonth;
    }

    @Override
    public String getTrafficPlanPsize() {
        return trafficPlanPsize;
    }

    @Override
    public String getTrafficBagPsize() {
        return trafficBagPsize;
    }

    @Override
    public String getTrafficUsed() {
        return trafficUsed;
    }

    @Override
    public String getTrafficLeft() {
        return trafficLeft;
    }

    @Override
    public String getBx_id() {
        return bx_id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSdate() {
        return sdate;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public String getValidity() {
        return validity;
    }

    @Override
    public String getTraffic_id() {
        return traffic_id;
    }

    @Override
    public String getExpdate() {
        return expdate;
    }

    @Override
    public String getCharge() {
        return charge;
    }

    @Override
    public int getOrderState(){
        return orderState;
    }

    @Override
    public long getOrderStartTime(){
        return orderStartTime;
    }

    @Override
    public String getOrderTime(){
        return orderTime;
    }

    @Override
    public String getOrderId(){
        return orderId;
    }
    @Override
    public String getOrderInfo(){
        return orderInfo;
    }

    @Override
    public String getOname(){
        return oname;
    }

    @Override
    public String getOdesc(){
        return odesc;
    }

    @Override
    public String getPayoId(){
        return pay_o_id;
    }
    @Override
    public int getPayType(){
        return pay_type==0?2:pay_type;
    }

    @Override
    public long getCtime(){
        return ctime * 1000;
    }

    @Override
    public String getPrepayId(){
        return prepayId;
    }

    @Override
    public String getSign(){
        return sign;
    }

    @Override
    public String getBaseShortDescript() {
        return shortDesc;
    }

    @Override
    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType){
        this.openType = openType;
    }

    @Override
    public int getShowType() {
        return showType;
    }

    @Override
    public List<BaseInfo> getInfoList(Object... obj) {
        return mList;
    }

    @Override
    public BaseInfo getInfo(Object... obj) {
        return info;
    }

    @Override
    public int getItemViewType() {
        return itemViewType;
    }

    @Override
    public int getItemSpanCount() {
        return itemSpanCount;
    }

    @Override
    public int getType() {
        return mType;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public int getResId() {
        return resId;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public int getVersionCode() {
        return versionCode;
    }

    @Override
    public String getFilepath() {
        return filePath;
    }

    @Override
    public String getApk() {
        return apk;
    }

    @Override
    public String getSdesc() {
        return sdesc;
    }

    @Override
    public String getCversion() {
        return cversion;
    }

    @Override
    public String getVersionName() {
        return versionName;
    }

    public void setInfoList(List<BaseInfo> mList){
        this.mList = mList;
    }
}
