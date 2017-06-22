package com.android.apps.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.android.apps.fragment.BaseFragment;
import com.sdk.bean.BaseInfo;
import com.sdk.lib.net.NetworkStatus;
import com.sdk.net.ConnectionListener;
import com.sdk.net.ConnectionMonitor;
import com.sdk.net.IHttpListener;
import com.umeng.analytics.MobclickAgent;

/**
 * Activity基类
 * Created by root on 16-5-9.
 */
public abstract class BaseActivity extends AppCompatActivity implements ConnectionListener {

    private boolean isNetworkConnect;
    protected boolean mLastNetworkConnect; // 上次网络连接状态
    protected boolean isResume = false;
    protected MyHandler mHandler;
    protected IHttpListener mHttpListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLastNetworkConnect = NetworkStatus.getInstance(this).isConnected();
        mHandler = new MyHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        ConnectionMonitor.registerConnectionMonitor(this);
        isNetworkConnect = NetworkStatus.getInstance(this).isConnected();
        isResume = true;
        BaseFragment fragment = getFragment();
        if (fragment != null) {
            fragment.handlerViewTreeObserver();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        ConnectionMonitor.unregisterConnectionMonitor(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().beginTransaction().detach(getFragment());
        mHttpListener = null;
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    @Override
    public void connectionStateChanged(final int state) {
        if (!isResume) return;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                isNetworkConnect = (state != ConnectionListener.CONN_NONE);
                if (mLastNetworkConnect != isNetworkConnect)
                    onConnectionStateChanged(state, isNetworkConnect, mLastNetworkConnect);
                mLastNetworkConnect = isNetworkConnect;
            }
        });
    }

    protected abstract void handleMyMessage(Message msg);

    /**
     * 处理网络变化
     *
     * @param state
     * @param isConnect
     * @param lastState
     */
    protected void onConnectionStateChanged(int state, boolean isConnect, boolean lastState) {
        BaseFragment fragment = getFragment();
        if (fragment != null) fragment.handleConnectionStateChanged(state, isConnect, lastState);
    }

    /**
     * 返回Activity主Fragment
     *
     * @return
     */
    protected abstract BaseFragment getFragment();

    public abstract BaseInfo getBaseInfo();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            handleMyMessage(msg);
        }
    }
}
