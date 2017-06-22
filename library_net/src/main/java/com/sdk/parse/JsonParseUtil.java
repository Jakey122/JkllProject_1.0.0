package com.sdk.parse;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.DeviceInfo;
import com.sdk.bean.OrderInfo;
import com.sdk.bean.QuestionInfo;
import com.sdk.lib.util.MD5;
import com.sdk.net.NetworkStatus;
import com.sdk.util.RefInvoke;
import com.sdk.util.SPUtil;
import com.sdk.util.Util;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * json解析类，这只是一个安静的json解析工具！！
 */
public class JsonParseUtil {
    private static final String TAG = "JsonParseUtil";

    /**
     * 从JSON中获得int类型参数
     *
     * @param json
     * @param key
     * @return
     */
    public static int getInt(JSONObject json, String key) {
        return json.containsKey(key) ? json.getInteger(key) : 0;
    }

    /**
     * 从JSON中获得long类型参数，可对String日期格式化为时间戳
     *
     * @param json
     * @param key
     * @return
     */
    public static long getLong(JSONObject json, String key) {
        return json.containsKey(key) ? json.getLong(key) : 0;
    }

    /**
     * 从JSON 中获得Double类型参数
     *
     * @param json
     * @param key
     * @return
     */
    public static double getDouble(JSONObject json, String key) {
        return json.containsKey(key) ? json.getDouble(key) : 0;
    }

    /**
     * 从JSON中获得String类型参数并转化为Float类型
     *
     * @param json
     * @param key
     * @return
     */
    public static Float getString2Float(JSONObject json, String key) {
        if (json.containsKey(key)) {
            String value = json.getString(key);
            if (value != null && !TextUtils.isEmpty(value))
                return Float.parseFloat(value);
        }
        return 0f;
    }

    /**
     * 从JSON中获得String类型参数
     *
     * @param json
     * @param key
     * @return
     */
    public static String getString(JSONObject json, String key) {
        return json.containsKey(key) ? json.getString(key) : "";
    }

    /**
     * 从JSON中解析出指定的JSonArray
     *
     * @param json
     * @param key
     * @return
     */
    public static JSONArray getJsonArray(JSONObject json, String key) {
        if (json.containsKey(key)) {
            Object array = json.get(key);
            return array instanceof JSONArray ? (JSONArray) array : null;
        }
        return null;
    }

    /**
     * 从JSON中解析出指定的JSONObject
     *
     * @param json
     * @param key
     * @return
     */
    public static JSONObject getJsonObject(JSONObject json, String key) {
        if (json.containsKey(key)) {
            Object object = json.get(key);
            return object instanceof JSONObject ? (JSONObject) object : null;
        }
        return null;
    }

    /**
     * 将String转为JSONObject
     *
     * @param jsonObject
     * @return
     */
    public static JSONObject parseJSONObject(String jsonObject) {
        return TextUtils.isEmpty(jsonObject) ? null : JSON.parseObject(jsonObject);
    }

    /**
     * 将String转为JSONArray
     *
     * @param jsonArray
     * @return
     */
    public static JSONArray parseJSONArray(String jsonArray) {
        return TextUtils.isEmpty(jsonArray) ? null : JSON.parseArray(jsonArray);
    }

    /**
     * 请求接口的头信息
     *
     * @return
     */
    public static JSONObject getHeaderJson(Context sContext) {
        Context context = sContext.getApplicationContext();
        JSONObject data = new JSONObject();
        try {
            data.put("channel", Util.getChannel(context));                                   //渠道号
            data.put("pmodel", URLEncoder.encode(Util.getDeviceName(context)));                   //设备名称
            data.put("resolution", Util.getResolution(context));                                 //分辨率
            data.put("cversion", Util.getAppVersionCode(context));                             //自身版本
            data.put("sdk", Util.getSdkVersionCode());                                    //androuid sdk版本
            if (!TextUtils.isEmpty(SPUtil.getInstant(context).getVerify()))
                data.put("verify", SPUtil.getInstant(context).getVerify());                                //uid
            data.put("packageName", context != null ? context.getPackageName() : "com.jkll.app");
            data.put("pnet", NetworkStatus.getInstance(context).getNetWorkState());
            data.put("imsi", Util.getIMSI(context));
            data.put("dusid", Util.getIMEI(context));
            data.put("androidid", Util.getAndroidId(context));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return data;
    }

    /**
     * 解析设备列表
     *
     * @param list
     * @param baseInfo
     * @param jsonData
     * @return
     */
    public static boolean parseSubject(List<BaseInfo> list, BaseInfo baseInfo, String jsonData) {
        boolean catched = false;
        List<BaseInfo> innerList;

        if (baseInfo.getShowType() == BaseInfo.VISIBLE_TYPE_ITEM_DEVICE) {
            catched = true;
            DeviceInfo deviceInfo = new DeviceInfo().parse(jsonData);
            list.add(deviceInfo);
        } else if (baseInfo.getShowType() == BaseInfo.VISIBLE_TYPE_ITEM_ORDER) {
            catched = true;
            OrderInfo orderInfo = new OrderInfo().parse(jsonData);
            list.add(orderInfo);
        } else if (baseInfo.getShowType() == BaseInfo.VISIBLE_TYPE_ITEM_QUESTION) {
            catched = true;
            QuestionInfo questionInfo = new QuestionInfo().parse(jsonData);
            list.add(questionInfo);
        }
        return catched;
    }

    /**
     * 将MAP格式化为JSON结构
     *
     * @param dataMap
     * @return
     */
    public static String getTargetJsonString(Map<String, Object> dataMap) {
        String result = "";
        if (dataMap == null || dataMap.size() == 0) return result;
        JSONObject data = new JSONObject();
        try {
            Iterator it = dataMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!TextUtils.isEmpty(key)) {
                    Object value = dataMap.get(key);
                    data.put(key, value);
                }
            }
            result = data.toString();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 将Json 转为 BeanList
     *
     * @param json
     * @param listName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends BaseInfo> List<T> jsonToBeanList(JSONObject json, String listName,
                                                              Class<T> clazz) {
        return jsonToBeanList(json, listName, clazz, null);
    }

    /**
     * 将Json 转为 BeanList
     *
     * @param json
     * @param listName
     * @param clazz
     * @param helper
     * @param <T>
     * @return
     */
    public static <T extends BaseInfo> List<T> jsonToBeanList(JSONObject json, String listName,
                                                              Class<T> clazz, IBeanHelper helper) {
        if (TextUtils.isEmpty(listName)) return null;
        if (!json.containsKey(listName)) return null;

        List<T> list = new ArrayList<>();
        JSONArray data = JsonParseUtil.getJsonArray(json, listName);
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                try {
                    String source = data.getString(i);
                    if (TextUtils.isEmpty(source)) continue;

                    BaseInfo baseInfo = null;
                    if (helper != null) baseInfo = helper.createBean(source);

                    if (baseInfo == null) {
                        Object info = clazz.newInstance();
                        baseInfo = (BaseInfo) RefInvoke.invokeMethod(
                                clazz, "parse", info,
                                new Class[]{Object.class},
                                new Object[]{source});
                    }

                    boolean add = true;
                    if (helper != null) add = helper.computeBean(baseInfo);
                    if (add) list.add((T) baseInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static String toRequestJsonParams(JSONObject params) {
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue() + "";
            stringBuilder.append("&" + key + "=" + value);
        }
        return stringBuilder.toString();
    }

    public static RequestParams toRequestParams(JSONObject params) {
        try {
            RequestParams requestParams = new RequestParams();
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                if (value instanceof File)
                    requestParams.put(key, (File) value);
                else
                    requestParams.put(key, value);
            }
            return requestParams;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转换 beanlist 单独处理 bean的接口
     */
    public interface IBeanHelper {
        /**
         * 自己处理生成bean
         *
         * @param <T>
         * @param jsonSource
         * @return
         */
        <T extends BaseInfo> T createBean(String jsonSource);

        /**
         * 处理bean
         *
         * @param t
         * @param <T>
         * @return
         */
        <T extends BaseInfo> boolean computeBean(T t);
    }

}
