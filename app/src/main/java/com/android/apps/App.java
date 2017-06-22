package com.android.apps;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.android.apps.activity.MainActivity;
import com.android.apps.activity.SplashActivity;
import com.android.apps.log.CrashExceptionHandler;

import com.android.apps.receiver.CommonReceiver;
import com.android.apps.util.Const;
import com.sdk.helper.ImeiHelper;
import com.sdk.net.ConnectionMonitor;
import com.sdk.net.NetLib;
import com.sdk.util.Util;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import java.io.File;

/**
 * Created by root on 16-5-9.
 */
public class App extends Application {

    private static App mInstance;
    private ConnectionMonitor mConnectionMonitor;
    public String mUninstallingPkg; // 要卸载的软件包名
    private IUmengRegisterCallback registerCallback;
    private MyHandler mHandler = new MyHandler();

    public static App getInstance() {
        if (mInstance == null) {
            mInstance = new App();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        registerScreenListener();
        registerConnectionMonitor();
        CrashExceptionHandler.getInstance().init(getApplicationContext());     //崩溃捕获
        ImeiHelper.getInstance().getIMEI(getApplicationContext());                                    //多imei获取
        NetLib.getInstance().build(getApplicationContext(), new NetLib.NetConfig());
        registerUmengPush();
    }

    /**
     * 注册push
     */
    public void registerUmengPush() {
        try {
            if(!Util.isNetAvailable(this)){
                return;
            }
            final PushAgent pushAgent = PushAgent.getInstance(this);
            pushAgent.setDebugMode(false);
            getUMengPushTokenId(null);
            pushAgent.setMessageHandler(messageHandler);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    pushAgent.register(new IUmengRegisterCallback() {

                        @Override
                        public void onSuccess(String registrationId) {
                            getUMengPushTokenId(registrationId);
                        }

                        @Override
                        public void onFailure(String arg0, String arg1) {
                            // TODO Auto-generated method stub
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void getUMengPushTokenId(String registrationId){
        try {
            if(!TextUtils.isEmpty(registrationId)){
                try{
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(registrationId);
                } catch(Exception e){}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该Handler是在IntentService中被调用，故
     * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
     *        或者可以直接启动Service
     * */
    UmengMessageHandler messageHandler = new UmengMessageHandler() {
        @Override
        public void dealWithCustomMessage(final Context context, final UMessage msg) {
            new Handler(getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    SplashActivity.actionActivity(context);
                }
            });
        }
    };


    /**
     * 监听网络变化
     */
    private void registerConnectionMonitor() {
        if (mConnectionMonitor == null) {
            mConnectionMonitor = new ConnectionMonitor();
            IntentFilter filter = new IntentFilter();
            filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mConnectionMonitor, filter);
        }
    }

    /**
     * 启动screen状态广播接收器
     */
    private void registerScreenListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new CommonReceiver(), filter);
    }

    public String getLogCacheDir() {
        String path = getSdcardCacheDir() + File.separator + Const.DIR_LOG;
        File logDir = new File(path);

        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        return path;
    }

    public String getSdcardCacheDir() {
        File cacheDir = getExternalCacheDir();
        if (cacheDir == null || !cacheDir.exists()) {
            cacheDir = new File(Environment.getExternalStorageDirectory().toString()
                    + "/Android/data/" + getPackageName() + "/cache");
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    public MyHandler getAppHandler() {
        return mHandler;
    }

    class MyHandler extends Handler {

        public MyHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
