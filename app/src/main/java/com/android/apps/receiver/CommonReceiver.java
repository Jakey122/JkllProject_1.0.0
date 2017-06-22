package com.android.apps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;

import com.android.apps.service.CommonService;
import com.sdk.util.SPUtil;

/**
 * Created by root on 16-5-9.
 */
public class CommonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) { // 开机启动
            CommonService.actionCommonService(context, CommonService.ACTION_BOOT_COMPLETE);

        } else if (Intent.ACTION_SHUTDOWN.equals(action)) { // 关机
            long runtime = (Long) SPUtil.getInstant(context).get(SPUtil.UserActiveTimeCount, Long.valueOf(0));
            SPUtil.getInstant(context).save(SPUtil.UserActiveTimeCount, runtime + SystemClock.elapsedRealtime());

        } else if (Intent.ACTION_PACKAGE_ADDED.equals(action)
                || Intent.ACTION_PACKAGE_REPLACED.equals(action)) { // 应用安装、更新

            String packageName = intent.getDataString();
            if (packageName != null && packageName.contains(":")) {
                packageName = packageName.subSequence(packageName.indexOf(":") + 1, packageName.length()).toString();
            }
            Bundle args = new Bundle();
            args.putString("pkg", packageName);
//            CommonService.actionCommonService(context, CommonService.ACTION_APK_ADDED, args);

        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) { // 应用卸载
            String packageName = intent.getDataString();
            if (packageName != null && packageName.contains(":")) {
                packageName = packageName.subSequence(packageName.indexOf(":") + 1, packageName.length()).toString();
            }
            Bundle args = new Bundle();
            args.putString("pkg", packageName);
//            CommonService.actionCommonService(context, CommonService.ACTION_APK_REMOVED, args);

        } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) { // 联网状态变化
            CommonService.actionCommonService(context, CommonService.ACTION_CONNECTIVITY);
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            CommonService.actionCommonService(context, CommonService.ACTION_USER_PRESENT);
        }
    }

}
