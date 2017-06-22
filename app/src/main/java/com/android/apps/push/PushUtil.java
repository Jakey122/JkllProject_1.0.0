package com.android.apps.push;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by root on 16-7-8.
 */
public class PushUtil {

    /**
     * 通过指定浏览器下载应用
     *
     * @param context
     * @param url
     * @return
     */
    public static boolean downloadApkFromWeb(Context context, String url) {
        return openUrl(context, url);
    }

    public static boolean downloadApkFromWeb(Context context, String url, String pkg) {
        return openUrl(context, url, pkg);
    }

    /**
     * 打开指定包名浏览器
     *
     * @param context
     * @param url
     * @param packageName
     * @return
     */
    public static boolean openUrl(Context context, String url, String packageName) {
        try {
            Intent sIntent = new Intent(Intent.ACTION_VIEW);
            sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sIntent.setData(Uri.parse(url));
            context.startActivity(sIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            return openUrl(context, url);
        }
    }

    /**
     * 抛给系统打开链接
     *
     * @param context
     * @param url
     * @return
     */
    public static boolean openUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        try {
            Intent sIntent = new Intent(Intent.ACTION_VIEW);
            sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sIntent.setData(Uri.parse(url));
            context.startActivity(sIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

}
