package com.sdk.net;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.sdk.bean.AppInfo;
import com.sdk.bean.AppSnippet;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.DeviceDetailInfo;
import com.sdk.bean.DeviceInfo;
import com.sdk.bean.OrderInfo;
import com.sdk.bean.PageInfo;
import com.sdk.bean.QuestionInfo;
import com.sdk.bean.RequestResult;
import com.sdk.bean.TrafficInfo;
import com.sdk.bean.UserInfo;
import com.sdk.helper.ListenerHelper;
import com.sdk.lib.util.BConst;
import com.sdk.lib.util.SystemUtil;
import com.sdk.parse.JsonParseUtil;
import com.sdk.util.SPUtil;
import com.sdk.util.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sdk.lib.util.MD5;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.Header;

/**
 * Created by root on 16-5-9.
 */
public class HttpController {

    private static String TAG = "HttpController";
    private static HttpController mInstance;
    private Context mContext;

    private HttpController() {
    }

    private HttpController(Context context) {
        if (context == null)
            mContext = NetLib.getInstance().getContext();
        else
            mContext = context.getApplicationContext();
    }

    public static HttpController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpController(context);
        }
        return mInstance;
    }

    public String getBaseUrl(String qt) {
        return "http://" + NetLib.getInstance().getHost() + "/mapi/apienter.php?qt=" + qt;
    }

    /**
     * 获取签名
     *
     * @param account
     * @param pwd
     * @return
     */
    private static String getSign(String account, String pwd) {
        String pubsec = "hello this login check syscode";
        return MD5.getMD5(account + "_" + pwd.toUpperCase() + "_" + pubsec);
    }

    /**
     * 登录
     * <p>
     * http://mapp.showmac.cn/mapi/apienter.php?qt=login&
     * account=13911821708&pwd=111111&ccode=181818&sign=39846e9c854699fd049cdce63ac0467e&dusid=&cversion=&pmodel=&pnet=
     *
     * @param mobile
     * @param password
     * @param listener
     */
    public void login(final String mobile, String password, final IHttpListener listener) {
        if (listener == null) return;
        try {
            String url = getBaseUrl("login");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("account", mobile);
            data.put("pwd", password);
            data.put("sign", getSign(mobile, password));
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject jsonObject = getJsonResult();
                    BaseInfo info = null;
                    if (jsonObject != null) {
                        String json = jsonObject.toString();
                        if (!TextUtils.isEmpty(json))
                            info = new UserInfo().parse(json.toString());
                    }
                    if (getStatusResult() == 1) {
                        Util.saveUserInfo(mContext, info);
                    }
                    listener.handleLogin(getStatusResult(), info);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    listener.handleLogin(statusCode, null);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 注册
     * <p>
     * http://mapp.showmac.cn/mapi/apienter.php?qt=reg&account=13911821708&pwd=111111&ccode=181818&dusid=&cversion=&pmodel=&pnet=
     *
     * @param mobile
     * @param password
     * @param code
     * @param listener
     */
    public void register(final String mobile, String password, String code, final IHttpListener listener) {
        if (listener == null) return;
        try {
            String url = getBaseUrl("reg");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("account", mobile);
            data.put("pwd", password);
            data.put("ccode", code);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    listener.handleRegister(getStatusResult());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    listener.handleRegister(statusCode);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 发送验证码
     *
     * @param mobile
     * @param listener
     */
    public void sendCode(final String mobile, String ctype, final IHttpListener listener) {
        if (listener == null) return;
        try {
            String url = getBaseUrl("get_ccode");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("account", mobile);
            data.put("ctype", ctype);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    listener.handleSendcode(getStatusResult());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    listener.handleSendcode(statusCode);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 验证验证码
     *
     * @param mobile
     * @param listener
     */
    public void checkCode(final String mobile, String code, final IHttpListener listener) {
        if (listener == null) return;
        try {
            String url = getBaseUrl("gcode");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("account", mobile);
            data.put("ccode", code);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    listener.handleSendcode(getStatusResult());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    listener.handleSendcode(statusCode);
                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * 重置密码
     *
     * @param mobile
     * @param password
     * @param code
     * @param listener
     */
    public void reset(final String mobile, String password, String code, final IHttpListener listener) {
        if (listener == null) return;
        try {
            String url = getBaseUrl("forget");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("account", mobile);
            data.put("pwd", password);
            data.put("ccode", code);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    listener.handleReset(getStatusResult());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    listener.handleReset(statusCode);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 修改用户昵称 异步
     *
     * @param userName
     * @param listener
     */
    public void updateUserInfo(final String userName, final IHttpListener listener) {
        try {
            String url = getBaseUrl("modify_user_nickname");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("nickname", userName);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    AsyncHttpClientHelper.removeRequestCount();
                    int state = 0;
                    if (!TextUtils.isEmpty(responseString)) {
                        JSONObject json = JsonParseUtil.parseJSONObject(responseString);
                        if (json != null) {
                            state = JsonParseUtil.getInt(json, "ret");
                        }
                    }
                    BaseInfo info = new BaseInfo();
                    info.setNickname(userName);
                    ListenerHelper.handleChange(ListenerHelper.TYPE_PAGE_USERINFO, state, info);
                    if (listener != null)
                        listener.handleUpdateUserInfo(state);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ListenerHelper.handleChange(ListenerHelper.TYPE_PAGE_USERINFO, 0, null);
                    if (listener != null)
                        listener.handleUpdateUserInfo(0);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 修改用户头像 异步
     *
     * @param userPicpath
     * @param listener
     */
    public void updateUserPic(String userPicpath, final IHttpListener listener) {
        if (listener == null) return;
        try {
            String url = getBaseUrl("modify_user_logo");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            File file = new File(userPicpath);
            if (!file.exists() || file.length() <= 0) {
                listener.handleUpdateUserPic(0);
                return;
            }
            JSONObject value = new JSONObject();
            value.put("file", file);
            AsyncHttpClientHelper.doPost(url, data, value, new AsyncHttpResponseHandler(true) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    AsyncHttpClientHelper.removeRequestCount();
                    int state = 0;
                    String responseString = new String(responseBody);
                    if (!TextUtils.isEmpty(responseString)) {
                        JSONObject json = JsonParseUtil.parseJSONObject(responseString);
                        if (json != null) {
                            state = JsonParseUtil.getInt(json, "ret");
                        }
                    }
                    listener.handleUpdateUserPic(state);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    listener.handleUpdateUserPic(statusCode);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 获取设备列表 异步
     *
     * @param listener
     */
    public void getDeviceList(final IHttpListener listener) {
        try {
            String url = getBaseUrl("get_dev_list");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject jsonObject = getJsonResult();
                    List<BaseInfo> list = new ArrayList<>();
                    if (jsonObject != null) {
                        String json = jsonObject.toString();
                        if (!TextUtils.isEmpty(json)) {
                            DeviceInfo deviceInfo = new DeviceInfo().parse(responseString.toString());
                            if (deviceInfo.getInfoList() != null)
                                list.addAll(deviceInfo.getInfoList());
                        }
                    }
                    if (listener != null)
                        listener.handleGetDeviceList(getStatusResult(), list);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (listener != null)
                        listener.handleGetDeviceList(0, null);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 获取首页列表
     */
    public void getHomeSync(final IHttpListener listener) {
        try {
            String url = getBaseUrl("index");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject jsonObject = getJsonResult();
                    List<BaseInfo> list = new ArrayList<>();
                    if (jsonObject != null) {
                        String json = jsonObject.toString();
                        if (!TextUtils.isEmpty(json)) {
                            DeviceInfo deviceInfo = new DeviceInfo().parse(responseString.toString());
                            if (deviceInfo.getInfoList() != null)
                                list.addAll(deviceInfo.getInfoList());
                        }
                    }
                    if (listener != null)
                        listener.handleHomeDeviceList(getStatusResult(), list);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (listener != null)
                        listener.handleHomeDeviceList(0, null);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 获取设备列表
     *
     * @param pi
     */
    public RequestResult getDeviceListSync(int pi) {
        try {
            String url = getBaseUrl("get_dev_list");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            JSONObject jsonObject = AsyncHttpClientHelper.doGetSync(url, data);
            if (jsonObject == null) return new RequestResult();
            String responseString = jsonObject.toString();
            if (TextUtils.isEmpty(responseString)) return new RequestResult();
            JSONObject json = JsonParseUtil.parseJSONObject(responseString);
            int resultStatus = JsonParseUtil.getInt(json, "ret");
            List<BaseInfo> list = new ArrayList<BaseInfo>();
            DeviceInfo deviceInfo = new DeviceInfo().parse(responseString.toString());
            if (deviceInfo.getInfoList() != null)
                list.addAll(deviceInfo.getInfoList());
            DeviceInfo deviceInfoAdd = new DeviceInfo();
            deviceInfoAdd.setOpenType(BaseInfo.OPEN_DEVICE_ADD);
            list.add(deviceInfoAdd);
            PageInfo pageInfo = new PageInfo();
            list.add(pageInfo);
            return new RequestResult(resultStatus, list);
        } catch (Exception e) {
        }
        return new RequestResult();
    }

    /**
     * 获取订单列表
     */
    public RequestResult getOrderSync(int pi) {
        try {
            String url = getBaseUrl("get_order_list");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("pg", pi);
            JSONObject jsonObject = AsyncHttpClientHelper.doGetSync(url, data);
            if (jsonObject == null) return new RequestResult();
            String responseString = jsonObject.toString();
            if (TextUtils.isEmpty(responseString)) return new RequestResult();
            JSONObject json = JsonParseUtil.parseJSONObject(responseString);
            int resultStatus = JsonParseUtil.getInt(json, "ret");
            List<BaseInfo> list = new ArrayList<BaseInfo>();
            OrderInfo orderInfo = new OrderInfo().parse(responseString.toString());
            if (orderInfo.getInfoList() != null && orderInfo.getInfoList().size() > 0){
                list.addAll(orderInfo.getInfoList());
                PageInfo appInfo2 = new PageInfo(pi++);
                list.add(appInfo2);
            } else {
                PageInfo appInfo2 = new PageInfo(pi);
                list.add(appInfo2);
            }

            return new RequestResult(resultStatus, list);
        } catch (Exception e) {
        }
        return new RequestResult();
    }

    /**
     * 获取设备详情 异步
     *
     * @param deiveId
     * @param listener
     */
    public void getDeviceDetailInfo(final String deiveId, final IHttpListener listener) {
        try {
            String url = getBaseUrl("get_dev_detail");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("device_id", deiveId);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject json = getJsonResult();
                    DeviceDetailInfo deviceDetailInfo = null;
                    if (json != null) {
                        deviceDetailInfo = new DeviceDetailInfo().parse(json.toString());
                    }
                    listener.handleDeviceDetailInfo(getStatusResult(), deviceDetailInfo);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 获取设备详情 异步
     *
     * @param deiveId
     * @param date
     * @param listener
     */
    public void getDeviceTrafficInfo(final String deiveId, String year, String date, final IHttpListener listener) {
        try {
            String url = getBaseUrl("get_dev_traffic");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("device_id", deiveId);
            if (!TextUtils.isEmpty(year))
                data.put("year", year);
            if (!TextUtils.isEmpty(date))
                data.put("month", date);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject json = getJsonResult();
                    DeviceDetailInfo deviceDetailInfo = new DeviceDetailInfo();
                    if (json != null) {
                        deviceDetailInfo = new DeviceDetailInfo().parse(json.toString());
                    }
                    if (listener != null)
                        listener.handleDeviceTrafficInfo(getStatusResult(), deviceDetailInfo);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (listener != null)
                        listener.handleDeviceTrafficInfo(statusCode, null);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 添加 异步
     *
     * @param deiveCode
     * @param listener
     */
    public void addDevice(final String deiveCode, final IHttpListener listener) {
        if (listener == null) return;
        try {
            String url = getBaseUrl("bind_device");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("prd_mf_code", deiveCode);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject json = getJsonResult();
                    DeviceInfo deviceInfo = null;
                    if (json != null) {
                        deviceInfo = new DeviceInfo().parse(json.toString());
                    }
                    listener.handleAddDevice(getStatusResult(), deviceInfo);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    listener.handleAddDevice(statusCode, null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除设备 异步
     *
     * @param deiveId
     * @param listener
     */
    public void deleteDevice(final String deiveId, final IHttpListener listener) {
        try {
            String url = getBaseUrl("del_device");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("device_id", deiveId);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    AsyncHttpClientHelper.removeRequestCount();
                    int state = 0;
                    if (!TextUtils.isEmpty(responseString)) {
                        JSONObject json = JsonParseUtil.parseJSONObject(responseString);
                        if (json != null) {
                            state = JsonParseUtil.getInt(json, "ret");
                        }
                    }
                    ListenerHelper.handleChange(ListenerHelper.TYPE_PAGE_DEVICE, state, null);
                    if (listener != null)
                        listener.handleDeleteDevice(state);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ListenerHelper.handleChange(ListenerHelper.TYPE_PAGE_DEVICE, 0, null);
                    if (listener != null)
                        listener.handleDeleteDevice(0);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 修改设备名称 异步
     *
     * @param deiveId
     * @param deviceName
     * @param listener
     */
    public void updateDevice(final String deiveId, final String deviceName, final IHttpListener listener) {
        try {
            String url = getBaseUrl("modify_dev_nickname");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("device_id", deiveId);
            data.put("nickname", deviceName);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    int state = 0;
                    if (!TextUtils.isEmpty(responseString)) {
                        JSONObject json = JsonParseUtil.parseJSONObject(responseString);
                        if (json != null) {
                            state = JsonParseUtil.getInt(json, "ret");
                        }
                    }
                    DeviceInfo deviceInfo = new DeviceInfo(deiveId, deviceName);
                    ListenerHelper.handleChange(ListenerHelper.TYPE_PAGE_DEVICE_DETAIL, state, deviceInfo);
                    if (listener != null)
                        listener.handleUpdateDevice(state);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ListenerHelper.handleChange(ListenerHelper.TYPE_PAGE_DEVICE_DETAIL, 0, null);
                    if (listener != null)
                        listener.handleUpdateDevice(0);
                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * 获取购买流量信息 异步
     *
     * @param deiveId
     * @param qt     购买流量类型
     * @param listener
     */
    public void getTrafficInfo(final String qt, final String deiveId, final IHttpListener listener) {
        try {
            String url = getBaseUrl(qt);
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("device_id", deiveId);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject json = getJsonResult();
                    TrafficInfo info = null;
                    if (json != null) {
                        info = new TrafficInfo().parse(json.toString());
                    }
                    if (listener != null)
                        listener.handleGetTrafficInfo(getStatusResult(), deiveId, qt, info);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (listener != null)
                        listener.handleGetTrafficInfo(0, deiveId, qt, null);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 购买流量 异步
     * @param type
     * @param otype
     * @param deiveId
     * @param oname
     * @param objid
     * @param listener
     */
    public void purchase(final int type, final int otype, final String deiveId, String oname, String objid, final IHttpListener listener) {
        try {
            String url = getBaseUrl("post_order");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("device_id", deiveId);
            data.put("objid", objid);
            data.put("otype", otype);
            data.put("pay_type", type);
            data.put("oname", oname);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject json = getJsonResult();
                    BaseInfo info = null;
                    if (json != null) {
                        info = new BaseInfo().parse(json.toString());
                    }
                    if (listener != null)
                        listener.handlePurchase(getStatusResult(), deiveId, type, info);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (listener != null)
                        listener.handlePurchase(0, deiveId, type, null);
                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * 订单中购买流量 异步
     * @param type
     * @param deiveId
     * @param orderid
     * @param listener
     */
    public void rePayOrder(final int type, final String deiveId, String orderid, final IHttpListener listener) {
        try {
            String url = getBaseUrl("repay_order");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("device_id", deiveId);
            data.put("pay_o_id", orderid);
            data.put("pay_type", type);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    JSONObject json = getJsonResult();
                    BaseInfo info = null;
                    if (json != null) {
                        info = new BaseInfo().parse(json.toString());
                    }
                    if (listener != null)
                        listener.handlePurchase(getStatusResult(), deiveId, type, info);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (listener != null)
                        listener.handlePurchase(0, deiveId, type, null);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 取消订单 异步
     *
     * @param orderid
     * @param listener
     */
    public void cancelOrder(final String orderid, final IHttpListener listener) {
        try {
            String url = getBaseUrl("cancel_order");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("pay_o_id", orderid);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    int state = 0;
                    if (!TextUtils.isEmpty(responseString)) {
                        JSONObject json = JsonParseUtil.parseJSONObject(responseString);
                        if (json != null) {
                            state = JsonParseUtil.getInt(json, "ret");
                        }
                    }
                    ListenerHelper.handleChange(ListenerHelper.TYPE_PAGE_ORDER_CANCEL, state, null);
                    if (listener != null)
                        listener.handleCancelOrder(state, orderid);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ListenerHelper.handleChange(ListenerHelper.TYPE_PAGE_ORDER_CANCEL, statusCode, null);
                    if (listener != null)
                        listener.handleCancelOrder(statusCode, orderid);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 获取关于信息 异步
     *
     * @param listener
     */
    public void getMineInfo(final IHttpListener listener) {
        try {
            String url = getBaseUrl("get_mykf");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_JSON) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    int state = 0;
                    JSONObject json = JsonParseUtil.parseJSONObject(responseString);
                    int resultStatus = JsonParseUtil.getInt(json, "ret");
                    BaseInfo info = new BaseInfo().parse(responseString.toString());
                    if (listener != null)
                        listener.handleMineInfo(resultStatus, info);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (listener != null)
                        listener.handleMineInfo(statusCode, null);
                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * 获取问题列表
     */
    public RequestResult getQuestionSync(int pi) {
        try {
            String url = getBaseUrl("get_myqa");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            JSONObject jsonObject = AsyncHttpClientHelper.doGetSync(url, data);
            if (jsonObject == null) return new RequestResult();
            String responseString = jsonObject.toString();
            if (TextUtils.isEmpty(responseString)) return new RequestResult();
            JSONObject json = JsonParseUtil.parseJSONObject(responseString);
            int resultStatus = JsonParseUtil.getInt(json, "ret");
            QuestionInfo appInfo = new QuestionInfo().parse(responseString.toString());
            List<BaseInfo> list = appInfo.getInfoList();
            if(list != null)
                list.add(new PageInfo(pi));
            return new RequestResult(resultStatus, list);
        } catch (Exception e) {
        }
        return new RequestResult();
    }

    /**
     * 反馈 异步
     *
     * @param content
     * @param contact
     * @param listener
     */
    public void postFeedback(final String content, final String contact, final IHttpListener listener) {
        try {
            String url = getBaseUrl("feedback");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("memo", URLEncoder.encode(content, "utf-8"));
            data.put("contacts", URLEncoder.encode(contact, "utf-8"));
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_TEXT) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    AsyncHttpClientHelper.removeRequestCount();
                    int state = 0;
                    if (!TextUtils.isEmpty(responseString)) {
                        JSONObject json = JsonParseUtil.parseJSONObject(responseString);
                        if (json != null) {
                            state = JsonParseUtil.getInt(json, "ret");
                        }
                    }
                    if (listener != null)
                        listener.handlePostFeedback(state);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (listener != null)
                        listener.handlePostFeedback(statusCode);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 前台升级
     */
    public void checkBeforeGroundAppUpdate(final IHttpListener listener) {
        if (listener == null || !NetworkStatus.getInstance(mContext).isConnected()) return;
        try {
            if(System.currentTimeMillis() - SPUtil.getInstant(mContext).getLong(SPUtil.AutoUpdate, System.currentTimeMillis()) < BConst.TIME_DAY) return;
            SPUtil.getInstant(mContext).save(SPUtil.AutoUpdate, System.currentTimeMillis());
            String url = getBaseUrl("check_version");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("signature", Util.getSelfSignature(mContext));
            AsyncHttpClientHelper.doGet(url, data, new ResultHandler(ResultHandler.RESULT_TEXT) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    AsyncHttpClientHelper.removeRequestCount();
                    if (TextUtils.isEmpty(responseString)) return;

                    JSONObject json = JsonParseUtil.parseJSONObject(responseString);
                    if (json != null && JsonParseUtil.getInt(json, "ret") == 1) {
                        SPUtil.getInstant(mContext).save(SPUtil.IsHasSelfNewEdition, true);
                        AppInfo appInfo = new AppInfo().parse(json.toString());
                        //直接弹 升级提示框
                        SPUtil.getInstant(mContext).save(SPUtil.LastUpdateCheck, (long) System.currentTimeMillis());
                        listener.handleSelfUpdate(getStatusResult(), appInfo);
                    } else {
                        SPUtil.getInstant(mContext).save(SPUtil.IsHasSelfNewEdition, false);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 检测自身更新 数据接口 同步
     *
     * @return
     */
    public synchronized void checkBackgroundAppUpdateSync() {
        if (!NetworkStatus.getInstance(mContext).isConnected()) return;
        long updateInterval = System.currentTimeMillis()
                - (Long) SPUtil.getInstant(mContext).get(SPUtil.LastUpdateCheck, (long) 0);
        if (updateInterval < 12 * 60 * 60 * 1000)
            return;
        SPUtil.getInstant(mContext).save(SPUtil.LastUpdateCheck, (long) System.currentTimeMillis());

        try {
            String url = getBaseUrl("check_version");
            JSONObject data = JsonParseUtil.getHeaderJson(mContext);
            data.put("signature", Util.getSelfSignature(mContext));
            String result = AsyncHttpClientHelper.doGetSyncText(url, data);
            if (TextUtils.isEmpty(result)) return;

            JSONObject json = JsonParseUtil.parseJSONObject(result);
            if (json != null && JsonParseUtil.getInt(json, "ret") == 1) {
                SPUtil.getInstant(mContext).save(SPUtil.IsHasSelfNewEdition, true);
                AppInfo appInfo = new AppInfo().parse(json.toString());
            } else {//没有新版本
                SPUtil.getInstant(mContext).save(SPUtil.IsHasSelfNewEdition, false);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 判断更新安装包是否已下载， 如果已经下载则返回其所在路径
     *
     * @param appInfo
     * @return
     */
    public String getUpdateAppExistPath(AppInfo appInfo) {
        if (appInfo == null || TextUtils.isEmpty(appInfo.getApk())) return "";
        String filePath = Util.getDownloadedFilePath(mContext, appInfo.getApk());
        if (TextUtils.isEmpty(filePath)) return "";

        if (new File(filePath).exists()) {
            AppSnippet sAppSnippet = Util.getAppSnippet(mContext,
                    Uri.parse(filePath));
            if (sAppSnippet == null) return "";
            return filePath;
        }
        return "";
    }
}
