package com.sdk.bean;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.sdk.parse.ParseHelperImp;

import java.io.File;


/**
 */
public class AppSnippet extends BaseInfo {

    public AppSnippet() {
    }

    public AppSnippet parse(PackageInfo packageInfo, Resources sResources, PackageManager pm, String apkPath) {
        if (packageInfo == null) return this;
        File file;
        try {
            packageName = packageInfo.packageName;
            versionCode = packageInfo.versionCode;
            try {
                nickname = sResources.getText(packageInfo.applicationInfo.labelRes).toString();
            } catch (Exception var10) {
                try {
                    nickname = pm.getApplicationLabel(packageInfo.applicationInfo).toString();
                } catch (Exception var9) {

                }
            }
            if (!TextUtils.isEmpty(apkPath)) {
                file = new File(apkPath);
            } else {
                file = new File(packageInfo.applicationInfo.sourceDir);
            }
            filePath = (file == null || !file.exists()) ? "" : file.getAbsolutePath();
        } catch (Exception e) {

        }
        return this;
    }
}
