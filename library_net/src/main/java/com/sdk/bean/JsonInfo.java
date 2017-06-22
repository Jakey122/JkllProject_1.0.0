package com.sdk.bean;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdk.parse.IParseHelper;
import com.sdk.parse.JsonParseUtil;
import com.sdk.util.RefInvoke;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个解析结构化json数据的类
 * Created by root on 16-5-10.
 */
public class JsonInfo implements IParseHelper {

    public static final String VERIFY = "verify";
    public static final String ACCOUNT = "account";
    public static final String NICKNAME = "nickname";
    public static final String LOGO = "logo";

    public static final String CVERSION = "cversion";
    public static final String CVERSIONCODE = "cversioncode";
    public static final String SDESC = "sdesc";
    public static final String FSIZE = "fsize";
    public static final String APK = "apk";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String KF_PHONE = "kf_phone";
    public static final String ABOUT_INFO = "about_info";
    public static final String DEVICEPRDID = "prd_id";
    public static final String DEVICEPRDNAME = "prd_name";
    public static final String DEVICEPRDLOGO = "prd_logo";
    public static final String DEVICEID = "device_id";
    public static final String DEVICENAME = "prd_name";
    public static final String DEVICEMODEL = "prd_model";
    public static final String DEVICECODE = "prd_mf_code";
    public static final String DEVICEPROTECTMSG = "bx_msg";
    public static final String DEVICEPROTECTDATE = "bx_date";
    public static final String DEVICECUSTOMER = "kf_phone";
    public static final String DEVICEPROTECT = "device_protect";
    public static final String STATUS = "status";
    public static final String SHOW_REALNAME_AUTH = "show_realname_auth";
    public static final String REALNAME_MSG = "realname_msg";
    public static final String REALNAME_URL = "realname_url";
    public static final String TRAFFIC_PLAN_ETIME = "traffic_plan_etime";
    public static final String BUY_TRAFFIC_PLAN = "buy_traffic_plan";
    public static final String BUY_TRAFFIC_PLAN_MSG = "buy_traffic_plan_msg";
    public static final String BUY_TRAFFIC_BAG_MSG = "buy_traffic_bag_msg";
    public static final String BUY_TRAFFIC_BAG = "buy_traffic_bag";
    public static final String BUY_TRAFFIC_PLAN2G = "buy_traffic_plan2g";
    public static final String BUY_TRAFFIC_PLAN2G_MSG = "buy_traffic_plan2g_msg";
    public static final String PTYPE = "ptype";
    public static final String WMONTH = "wmonth";
    public static final String TRAFFIC_PLAN_PSIZE = "traffic_plan_psize";
    public static final String TRAFFIC_BAG_PSIZE = "traffic_bag_psize";
    public static final String TRAFFIC_USED = "traffic_used";
    public static final String TRAFFIC_LEFT = "traffic_left";
    public static final String BX_ID = "bx_id";
    public static final String NAME = "name";
    public static final String SDATE = "sdate";
    public static final String PRICE = "price";
    public static final String VALIDITY = "validity";
    public static final String TRAFFIC_ID = "traffic_id";
    public static final String EXPDATE = "expdate";
    public static final String CHARGE = "charge";
    public static final String ORDER_STATE = "order_state";
    public static final String ORDER_STARTTIME = "order_starttime";
    public static final String ORDER_TIME = "order_time";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_INFO = "order_info";
    public static final String ONAME = "oname";
    public static final String ODESC = "odesc";
    public static final String PAY_O_ID = "pay_o_id";
    public static final String PAY_TYPE = "pay_type";
    public static final String CTIME = "ctime";
    public static final String PREPAY_ID = "prepay_id";
    public static final String SIGN = "sign";

    public static final String OPEN_TYPE = "openType";
    public static final String SHOW_TYPE = "showType";
    public static final String ITEM_SHORTDESC = "shortDesc";
    public static final String PAGE_ITEMTOTAL = "totalNum";
    public static final String PAGE_TOTAL = "totalPage";
    public static final String PAGE_SIZE = "ps";
    public static final String PAGE_CINDEX = "pi";
    public static final String HTML_ADDRESS = "address";
    public static final String TAB_PAGEID = "tabPageId";
    public static final String TAB_PAGEINDEX = "tabPageIndex";

    private JSONObject mJson;

    public static JsonInfo newInstance(String json) {
        return new JsonInfo(json);
    }

    public JsonInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            mJson = JsonParseUtil.parseJSONObject(json);
        }
    }

    /**
     * 解析JsonArray并转换为指定的元素对象
     *
     * @param infoClass
     * @param key
     * @return
     */
    private List<BaseInfo> getList(Class infoClass, String... key) {
        List<BaseInfo> list = new ArrayList<>();
        JSONArray data = JsonParseUtil.getJsonArray(mJson,
                key != null && key.length > 0 ? key[0] : "b");
        if (data == null || data.size() == 0)
            data = JsonParseUtil.getJsonArray(mJson, "list");

        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                try {
                    String source = data.getString(i);
                    if (TextUtils.isEmpty(source)) continue;
                    if(source.startsWith("[")){
                        JSONArray jsonArray = data.getJSONArray(i);
                        List<BaseInfo> infos = new ArrayList<>();
                        BaseInfo object = (BaseInfo)infoClass.newInstance();
                        for (int j = 0; j < jsonArray.size(); j++) {
                            String dataSource = jsonArray.getString(j);
                            Object info = infoClass.newInstance();
                            BaseInfo baseInfo = (BaseInfo) RefInvoke.invokeMethod(infoClass, "parse", info, new Class[]{Object.class}, new Object[]{dataSource});
                            infos.add(baseInfo);
                        }
                        object.setInfoList(infos);
                        list.add(object);
                    } else {
                        Object info = infoClass.newInstance();
                        BaseInfo baseInfo = (BaseInfo) RefInvoke.invokeMethod(infoClass, "parse", info, new Class[]{Object.class}, new Object[]{source});
                        list.add(baseInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public <T extends IParseHelper> T getParseHelper(Object obj) {
        return (T) this;
    }

    @Override
    public int getType() {
        int type = JsonParseUtil.getInt(mJson, "pp");
        return type > 0 ? type : JsonParseUtil.getInt(mJson, "p");
    }

    @Override
    public String getVerify() {
        return JsonParseUtil.getString(mJson, VERIFY);
    }

    @Override
    public String getAccount() {
        return JsonParseUtil.getString(mJson, ACCOUNT);
    }

    @Override
    public String getNickname() {
        return JsonParseUtil.getString(mJson, NICKNAME);
    }

    @Override
    public String getLogo() {
        return JsonParseUtil.getString(mJson, LOGO);
    }

    @Override
    public String getCversion() {
        return JsonParseUtil.getString(mJson, CVERSION);
    }

    @Override
    public int getCversionCode() {
        return JsonParseUtil.getInt(mJson, CVERSIONCODE);
    }

    @Override
    public String getSdesc() {
        return JsonParseUtil.getString(mJson, SDESC);
    }

    @Override
    public long getFsize() {
        return JsonParseUtil.getLong(mJson, FSIZE);
    }

    @Override
    public String getApk() {
        return JsonParseUtil.getString(mJson, APK);
    }

    @Override
    public String getQuestion() {
        return JsonParseUtil.getString(mJson, QUESTION);
    }

    @Override
    public String getAnswer() {
        return JsonParseUtil.getString(mJson, ANSWER);
    }

    @Override
    public String getCustomerInfo() {
        return JsonParseUtil.getString(mJson, KF_PHONE);
    }

    @Override
    public String getAboutInfo() {
        return JsonParseUtil.getString(mJson, ABOUT_INFO);
    }

    @Override
    public int getStatus() {
        return JsonParseUtil.getInt(mJson, STATUS);
    }


    @Override
    public String getPrdId() {
        return JsonParseUtil.getString(mJson, DEVICEPRDID);
    }

    @Override
    public String getPrdName() {
        return JsonParseUtil.getString(mJson, DEVICEPRDNAME);
    }

    @Override
    public String getPrdLogo() {
        return JsonParseUtil.getString(mJson, DEVICEPRDLOGO);
    }

    @Override
    public String getDeviceId() {
        return JsonParseUtil.getString(mJson, DEVICEID);
    }

    @Override
    public String getDeviceName() {
        return JsonParseUtil.getString(mJson, DEVICENAME);
    }

    @Override
    public String getDeviceModel() {
        return JsonParseUtil.getString(mJson, DEVICEMODEL);
    }

    @Override
    public String getDeviceCode() {
        return JsonParseUtil.getString(mJson, DEVICECODE);
    }

    @Override
    public String getDeviceProtectDate() {
        return JsonParseUtil.getString(mJson, DEVICEPROTECTDATE);
    }

    @Override
    public String getDeviceProtectMsg() {
        return JsonParseUtil.getString(mJson, DEVICEPROTECTMSG);
    }

    @Override
    public String getDeviceCustomer() {
        return JsonParseUtil.getString(mJson, DEVICECUSTOMER);
    }


    @Override
    public int getDeviceProtect() {
        return JsonParseUtil.getInt(mJson, DEVICEPROTECT);
    }

    @Override
    public int getShowRealnameAuth() {
        return JsonParseUtil.getInt(mJson, SHOW_REALNAME_AUTH);
    }

    @Override
    public String getRealnameMsg() {
        return JsonParseUtil.getString(mJson, REALNAME_MSG);
    }

    @Override
    public String getRealnameUrl() {
        return JsonParseUtil.getString(mJson, REALNAME_URL);
    }

    @Override
    public String getTrafficPlanEtime() {
        return JsonParseUtil.getString(mJson, TRAFFIC_PLAN_ETIME);
    }

    @Override
    public int getBuyTrafficPlan() {
        return JsonParseUtil.getInt(mJson, BUY_TRAFFIC_PLAN);
    }

    @Override
    public String getBuyTrafficPlanMsg() {
        return JsonParseUtil.getString(mJson, BUY_TRAFFIC_PLAN_MSG);
    }

    @Override
    public int getBuyTrafficBag() {
        return JsonParseUtil.getInt(mJson, BUY_TRAFFIC_BAG);
    }

    @Override
    public String getBuyTrafficBagMsg() {
        return JsonParseUtil.getString(mJson, BUY_TRAFFIC_BAG_MSG);
    }

    @Override
    public int getBuyTrafficPlan2g() {
        return JsonParseUtil.getInt(mJson, BUY_TRAFFIC_PLAN2G);
    }

    @Override
    public String getBuyTrafficPlan2gMsg() {
        return JsonParseUtil.getString(mJson, BUY_TRAFFIC_PLAN2G_MSG);
    }

    @Override
    public int getPtype() {
        return JsonParseUtil.getInt(mJson, PTYPE);
    }

    @Override
    public long getWmonth() {
        return JsonParseUtil.getLong(mJson, WMONTH);
    }

    @Override
    public String getTrafficPlanPsize() {
        return JsonParseUtil.getString(mJson, TRAFFIC_PLAN_PSIZE);
    }

    @Override
    public String getTrafficBagPsize() {
        return JsonParseUtil.getString(mJson, TRAFFIC_BAG_PSIZE);
    }

    @Override
    public String getTrafficUsed() {
        return JsonParseUtil.getString(mJson, TRAFFIC_USED);
    }

    @Override
    public String getTrafficLeft() {
        return JsonParseUtil.getString(mJson, TRAFFIC_LEFT);
    }

    @Override
    public String getBx_id() {
        return JsonParseUtil.getString(mJson, BX_ID);
    }

    @Override
    public String getName() {
        return JsonParseUtil.getString(mJson, NAME);
    }

    @Override
    public String getSdate() {
        return JsonParseUtil.getString(mJson, SDATE);
    }

    @Override
    public String getPrice() {
        return JsonParseUtil.getString(mJson, PRICE);
    }

    @Override
    public String getValidity() {
        return JsonParseUtil.getString(mJson, VALIDITY);
    }

    @Override
    public String getTraffic_id() {
        return JsonParseUtil.getString(mJson, TRAFFIC_ID);
    }

    @Override
    public String getExpdate() {
        return JsonParseUtil.getString(mJson, EXPDATE);
    }

    @Override
    public String getCharge() {
        return JsonParseUtil.getString(mJson, CHARGE);
    }

    @Override
    public int getOrderState() {
        return JsonParseUtil.getInt(mJson, ORDER_STATE);
    }

    @Override
    public long getOrderStartTime() {
        return JsonParseUtil.getLong(mJson, ORDER_STARTTIME);
    }

    @Override
    public String getOrderTime() {
        return JsonParseUtil.getString(mJson, ORDER_TIME);
    }

    @Override
    public String getOrderId() {
        return JsonParseUtil.getString(mJson, ORDER_ID);
    }

    @Override
    public String getOrderInfo() {
        return JsonParseUtil.getString(mJson, ORDER_INFO);
    }


    @Override
    public String getOname(){
        return JsonParseUtil.getString(mJson, ONAME);
    }

    @Override
    public String getOdesc(){
        return JsonParseUtil.getString(mJson, ODESC);
    }

    @Override
    public String getPayoId(){
        return JsonParseUtil.getString(mJson, PAY_O_ID);
    }

    @Override
    public int getPayType(){
        return JsonParseUtil.getInt(mJson, PAY_TYPE);
    }

    @Override
    public long getCtime(){
        return JsonParseUtil.getLong(mJson, CTIME);
    }

    /**
     * 基础信息 元素打开类型
     *
     * @return
     */
    @Override
    public int getOpenType() {
        return JsonParseUtil.getInt(mJson, OPEN_TYPE);
    }

    /**
     * 基础信息 元素展示类型
     *
     * @return
     */
    @Override
    public int getShowType() {
        return JsonParseUtil.getInt(mJson, SHOW_TYPE);
    }

    /**
     * 软件简短描述
     *
     * @return
     */
    @Override
    public String getBaseShortDescript() {
        return JsonParseUtil.getString(mJson, ITEM_SHORTDESC);
    }

    /**
     * 元素总数
     *
     * @return
     */
    @Override
    public int getPageTotalItemCount() {
        if (mJson.containsKey(PAGE_ITEMTOTAL)) {
            return JsonParseUtil.getInt(mJson, PAGE_ITEMTOTAL);
        }
        return 1;
    }

    /**
     * 总页数
     *
     * @return
     */
    @Override
    public int getPageTotalPageCount() {
        if (mJson.containsKey(PAGE_TOTAL)) {
            return JsonParseUtil.getInt(mJson, PAGE_TOTAL);
        }
        return 0;
    }

    /**
     * 每页数量
     *
     * @return
     */
    @Override
    public int getPageEveryPageSize() {
        if (mJson.containsKey(PAGE_SIZE)) {
            return JsonParseUtil.getInt(mJson, PAGE_SIZE);
        }
        return 20;
    }

    /**
     * 当前页码
     *
     * @return
     */
    @Override
    public int getPageCurrentIndex() {
        if (mJson.containsKey(PAGE_CINDEX)) {
            return JsonParseUtil.getInt(mJson, PAGE_CINDEX) + 1;
        }
        return 1;
    }

    /**
     * 获取元素展示类型（一行，宫格）
     *
     * @return
     */
    @Override
    public int getItemViewType() {
        return BaseInfo.ITEM_TYPE_LIST;
    }

    /**
     * 获取元素宫格类型 列数
     *
     * @return
     */
    @Override
    public int getItemSpanCount() {
        return 0;
    }

    /**
     * html 元素打开地址
     *
     * @return
     */
    @Override
    public String getHtmlAddress() {
        return JsonParseUtil.getString(mJson, HTML_ADDRESS);
    }


    /**
     * 元素嵌套list
     *
     * @param obj
     * @return
     */
    @Override
    public List<BaseInfo> getInfoList(Object... obj) {
        if (obj != null && obj.length == 2) {
            Class clazz = (Class) obj[0];
            String key = String.valueOf(obj[1]);
            return getList(clazz, key);
        }
        return null;
    }

    /**
     * 元素嵌套list
     *
     * @param obj
     * @return
     */
    @Override
    public BaseInfo getInfo(Object... obj) {
        if (obj != null && obj.length == 2) {
            Class clazz = (Class) obj[0];
            String key = String.valueOf(obj[1]);
            try {
                JSONObject data = JsonParseUtil.getJsonObject(mJson, key);
                Object info = clazz.newInstance();
                return (BaseInfo) RefInvoke.invokeMethod(clazz, "parse", info, new Class[]{Object.class}, new Object[]{data.toJSONString()});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int getTabPageId() {
        if (mJson.containsKey(TAB_PAGEID))
            return JsonParseUtil.getInt(mJson, TAB_PAGEID);
        return -1;
    }

    @Override
    public int getTabPagePosition() {
        if (mJson.containsKey(TAB_PAGEINDEX)) {
            return JsonParseUtil.getInt(mJson, TAB_PAGEINDEX);
        }
        return 2;
    }

    @Override
    public int getResId() {
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
    public String getPrepayId() {
        return JsonParseUtil.getString(mJson, PREPAY_ID);
    }

    @Override
    public String getSign() {
        return JsonParseUtil.getString(mJson, SIGN);
    }
}
