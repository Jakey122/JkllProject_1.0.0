package com.sdk.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sdk.net.AsyncHttpClientHelper;
import com.sdk.parse.JsonParseUtil;

import cz.msebera.android.httpclient.Header;

/**
 * Created by root on 16-7-21.
 */
public class ResultHandler extends TextHttpResponseHandler {

    public static final int RESULT_TEXT = 0;
    public static final int RESULT_JSON = 1;

    int resultStatus;
    int resultType;
    JSONObject resultJson = null;
    String resultText = "";

    public ResultHandler(int type) {
        resultType = type;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        AsyncHttpClientHelper.removeRequestCount();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        try {
            AsyncHttpClientHelper.removeRequestCount();
            if (TextUtils.isEmpty(responseString)) return;
            resultText = responseString;

            if (resultType != RESULT_JSON) return;
            JSONObject json = JsonParseUtil.parseJSONObject(resultText);
            resultStatus = JsonParseUtil.getInt(json, "ret");
            if (json != null) {
                resultJson = json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJsonResult() {
        return resultJson;
    }

    public String getTextResult() {
        return resultText;
    }

    public int getStatusResult() {
        return resultStatus;
    }

    /**
     * 返回状态值
     */
    public static class ResultCode {
        public final static int CODE_OK = 1; //        操作成功
        public final static int CODE_ERROR_NOMETHOD = -1001; //  接口不存在
        public final static int CODE_ERROR_PARAMS = -1002; //  参数缺失
        public final static int CODE_ERROR_PARAMS_FORMAT = -1003; //  参数格式错误
        public final static int CODE_ERROR_DATABASE = -1004; //  数据库执行错误
        public final static int CODE_ERROR_ACCOUNT = -1005; //  账号/密码错误
        public final static int CODE_ERROR_NOOBJECT = -1006; //  操作对象不存在（例如设备不存在、订单不存在、账号不存在）
        public final static int CODE_ERROR_INVALIDCODE = -1007; //  验证码错误
        public final static int CODE_ERROR_INVALIDCODE_TIMEOUT = -1008; //  验证码超时
        public final static int CODE_ERROR_PASSWORD_SAME = -1009; //  新密码和原密码一样
        public final static int CODE_ERROR_ACCOUNT_EXIST = -1010; //  注册账号已经存在
        public final static int CODE_ERROR_INVALIDCODE_TIMES = -1011; //  超过次数（验证码）
        public final static int CODE_ERROR_SIGN = -1012; //  签名错误
        public final static int CODE_ERROR_NOLOGIN = -1013; //  用户没有登陆
    }
}
