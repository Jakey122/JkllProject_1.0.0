package com.sdk.helper;

import android.content.Context;
import android.text.TextUtils;

import com.android.kd.phone.PhoneHelper;
import com.sdk.util.SPUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 16-7-21.
 */
public class ImeiHelper {

    private static ImeiHelper mHelper;
    
    private ConcurrentHashMap<String, Future<?>> mTasks = new ConcurrentHashMap<>();
    private ExecutorService mThreadPool;
    private Runnable mRunnable;

    public static ImeiHelper getInstance() {
        if (mHelper == null) {
            mHelper = new ImeiHelper();
        }
        return mHelper;
    }

    public ImeiHelper() {
        mThreadPool = Executors.newSingleThreadExecutor();
    }

    public void destroyHelper() {
        try {
            if (mThreadPool != null) mThreadPool.shutdownNow();
            mThreadPool = null;
            if (mTasks != null) mTasks.clear();
            mRunnable = null;
        } catch (Exception e) {
        }
    }

    public void getIMEI(Context sContext) {
        final Context context = sContext.getApplicationContext();
        String imei = SPUtil.getInstant(context).getString(SPUtil.IMEI, "");
        if (!TextUtils.isEmpty(imei) || mRunnable != null) return;

        mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String allimei = PhoneHelper.getPhoneInfo2(context);
                    if (!TextUtils.isEmpty(allimei) &&
                            !"000000000000000".equals(allimei)) {
                        SPUtil.getInstant(context).save(SPUtil.IMEI, allimei);
                    }
                } catch (Exception e) {
                }
                mRunnable = null;
            }
        };
        execute("getIMEI", mRunnable);
    }

    private void execute(String key, Runnable runnable) {
        try {
            if (mThreadPool != null) {
                Future<?> sFuture = mThreadPool.submit(runnable);
                if (!TextUtils.isEmpty(key)) {
                    if (mTasks.contains(key)) {
                        Future<?> tempFuture = mTasks.get(key);
                        tempFuture.cancel(true);
                    }
                    mTasks.put(key, sFuture);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
