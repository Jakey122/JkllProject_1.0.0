package com.sdk.net;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.sdk.parse.JsonParseUtil;

import cz.msebera.android.httpclient.client.HttpClient;

/**
 * Created by root on 16-7-18.
 */
public class AsyncHttpClientHelper {

    private static int taskCount = 0;
    private static SyncHttpClient mSyncHttpClient = new SyncHttpClient();
    private static AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();

    public static JSONObject doGetSync(String url, JSONObject params) {
        addRequestCount();
//        RequestParams requestParams = null;
        ResultHandler handler = new ResultHandler(ResultHandler.RESULT_JSON);
        if (params != null) {
            url += JsonParseUtil.toRequestJsonParams(params);
        }
//        if (requestParams != null) {
//            mSyncHttpClient.get(url, requestParams, handler);
//        } else {
            mSyncHttpClient.get(url, handler);
//        }
        return handler.getJsonResult();
    }

    public static String doGetSyncText(String url, JSONObject params) {
        addRequestCount();
//        RequestParams requestParams = null;
        ResultHandler handler = new ResultHandler(ResultHandler.RESULT_TEXT);
        if (params != null) {
//            requestParams = new RequestParams();
//            requestParams.put("data", params.toString());
            url += JsonParseUtil.toRequestJsonParams(params);
        }
//        if (requestParams != null) {
//            mSyncHttpClient.get(url, requestParams, handler);
//        } else {
            mSyncHttpClient.get(url, handler);
//        }
        return handler.getTextResult();
    }

    public static void doGet(String url, JSONObject params, ResultHandler resultHandler) {
        addRequestCount();
//        RequestParams requestParams = null;
        if (params != null) {
//            requestParams = new RequestParams();
//            requestParams.put("data", params.toString());
            url += JsonParseUtil.toRequestJsonParams(params);
        }
        mAsyncHttpClient.get(url, resultHandler);
//        if (requestParams != null) {
//            mAsyncHttpClient.get(url, requestParams, resultHandler);
//        } else {
//            mAsyncHttpClient.get(url, resultHandler);
//        }
    }

    public static void doPost(String url, JSONObject headerparams, JSONObject params, AsyncHttpResponseHandler resultHandler) {
        addRequestCount();
        RequestParams requestParams = null;
        if (headerparams != null) {
            url += JsonParseUtil.toRequestJsonParams(headerparams);
        }
        if(params != null){
            requestParams = JsonParseUtil.toRequestParams(params);
        }
        if (requestParams != null) {
            mAsyncHttpClient.post(url, requestParams, resultHandler);
        } else {
            mAsyncHttpClient.post(url, resultHandler);
        }
    }

    public synchronized static void addRequestCount() {
        taskCount++;
    }

    public synchronized static void removeRequestCount() {
        taskCount--;
    }

    public synchronized static boolean isIdle() {
        return taskCount == 0;
    }

    public static void cancleAllRequest() {
        taskCount = 0;
        if (mSyncHttpClient != null) mSyncHttpClient.cancelAllRequests(true);
        if (mAsyncHttpClient != null) mAsyncHttpClient.cancelAllRequests(true);
    }

    public static HttpClient getHttpClient() {
        return mAsyncHttpClient.getHttpClient();
    }
}
