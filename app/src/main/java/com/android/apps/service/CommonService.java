package com.android.apps.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sdk.net.HttpController;

/**
 * Created by root on 16-5-9.
 */
public class CommonService extends IntentService {

    public static final String ACTION_BOOT_COMPLETE = "app_boot_complete";   // 手机开机
    public static final String ACTION_CONNECTIVITY = "app_net_connectivity"; // 网络连接状态变化
    public static final String ACTION_START = "app_app_start";               // 应用启动
    public static final String ACTION_USER_PRESENT = "app_user_present";     // 解屏监听

    public static Intent pendingCommonService(Context context, String action) {
        return pendingCommonService(context, action, null);
    }

    public static Intent pendingCommonService(Context context, String action, Bundle args) {
        Intent sIntent = new Intent(context, CommonService.class);
        sIntent.putExtra("action", action);
        if (args != null) {
            sIntent.putExtras(args);
        }
        return sIntent;
    }

    public static void actionCommonService(Context context, String action) {
        actionCommonService(context, action, null);
    }

    public static void actionCommonService(Context context, String action, Bundle args) {
        Intent sIntent = actionCommonServiceIntent(context, action, args);
        context.startService(sIntent);
    }

    public static Intent actionCommonServiceIntent(Context context, String action, Bundle args) {
        Intent sIntent = new Intent(context, CommonService.class);
        sIntent.putExtra("action", action);
        if (args != null) {
            sIntent.putExtras(args);
        }
        return sIntent;
    }

    public CommonService() {
        super("CommonService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;
        String action = intent.getStringExtra("action");
        if (ACTION_START.equals(action)) {

        } else if (ACTION_CONNECTIVITY.equals(action)) {
//            HttpController.getInstance(this).checkBackgroundAppUpdateSync();
        } else if (ACTION_USER_PRESENT.equals(action)) {
//            HttpController.getInstance(this).checkBackgroundAppUpdateSync();
        }
    }
}
