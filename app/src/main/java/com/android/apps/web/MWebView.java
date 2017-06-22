package com.android.apps.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.apps.helper.PromptHelper;
import com.jkll.app.R;
import com.sdk.lib.net.NetworkStatus;

public class MWebView extends WebView {

    private WebViewClient mCustomWebViewClient;
    private ILoadUrlListener mListener;
    private IOverrideUrlHelper mHelper;
    private MJSInerface mJsInerface;
    private int mFrom;
    private long msId;

    public MWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
        initJSSupport(context);
    }

    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
        initJSSupport(context);
    }

    public MWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
        initJSSupport(context);
    }

    public void setFromInfo(int from, long sId, long fatherId) {
        mFrom = from;
        msId = sId;
        if (mJsInerface != null) mJsInerface.setFromInfo(from, sId, fatherId);
    }

    @Override
    public void loadUrl(String url) {
        // TODO Auto-generated method stub
        super.loadUrl(url);
        boolean res = url.startsWith("http://");
        getSettings().setUseWideViewPort(res);
        getSettings().setLoadWithOverviewMode(res);
        //getSettings().setBuiltInZoomControls(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        getSettings().setDefaultTextEncodingName("UTF-8");
        getSettings().setPluginState(PluginState.ON);
        getSettings().setJavaScriptEnabled(true);

        mCustomWebViewClient = new CustomWebViewClient();
        setWebViewClient(mCustomWebViewClient);
        setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        // TODO Auto-generated method stub
        super.setWebViewClient(client);
        mCustomWebViewClient = client;
    }

    @SuppressLint("JavascriptInterface")
    private void initJSSupport(final Context context) {
        mJsInerface = new MJSInerface(context, new MJSInerface.IJSProgressHelper() {

            @Override
            public void sendToJs_Downloadprogress(final long appId, final int progress, final int state, android.os.Handler handler) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadUrl("javascript:setDownloadProgress('" + appId + "','" + progress + "','" + state + "')");
                    }
                });
            }

            @Override
            public boolean preDownload() {
                if (!NetworkStatus.getInstance(context).isConnected()) {
                    PromptHelper.showSnackMessage(MWebView.this, R.string.netstate_unconnect);
                    return false;
                } else if (NetworkStatus.getInstance(context).isMobileConnected()) {
                    PromptHelper.showSnackMessage(MWebView.this, R.string.netstate_mobile);
                }
                return true;
            }
        });
        addJavascriptInterface(mJsInerface, "android");
    }

    @Override
    public void destroy() {
        if (mJsInerface != null) mJsInerface.jsInterfaceDestroy();
        super.destroy();
    }

    public void setLoadUrlListener(ILoadUrlListener listener) {
        mListener = listener;
    }

    public void setOverrideUrlHelper(IOverrideUrlHelper helper) {
        mHelper = helper;
    }

    public class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(url))
                return false;
            if (mHelper != null) return mHelper.handleOverrideUrl(view, url);

            if (!url.contains("://")) {
                url = "http://" + url;
            }
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView wv, String url) {
            if (mListener != null) mListener.onLoadFinish();
        }

        @Override
        public void onPageStarted(WebView wv, String url, Bitmap favicon) {
            if (mListener != null) mListener.onLoadStart();
        }

        @Override
        public void onReceivedError(WebView wv, int errorCode, String description, String failingUrl) {
            if (mListener != null) mListener.onLoadError();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }
}
