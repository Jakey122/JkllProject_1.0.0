
package com.sdk.lib.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.kd.phone.PhoneHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SystemUtil {
    /**
     * @return 是否已插入sim卡
     */
    public static boolean isSimAvailable(Context sContext) {
        try {
            TelephonyManager sTelephonyManager = (TelephonyManager) sContext
                    .getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            int simState = sTelephonyManager.getSimState();
            return simState > TelephonyManager.SIM_STATE_ABSENT;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }

    public static String getIMEI(Context sContext) {
        return PhoneHelper.getPhoneInfo2(sContext.getApplicationContext());
    }

    public static String getIMSI(Context sContext) {
        try {
            TelephonyManager tm = (TelephonyManager) sContext.getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static String getMAC(Context sContext) {
        try {
            WifiManager wifi = (WifiManager) sContext.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static String getSdkVersion() {
        return "" + android.os.Build.VERSION.SDK_INT;
    }

    public static String getBrandName() {
        String normal = android.os.Build.BRAND;
        return normal;
    }

    public static String getDeviceName(Context mContext) {
        String deviceName = (String) SPUtil.getInstance(mContext).get(PrefsConst.PREF_DEVICE_NAME,
                null);
        if (TextUtils.isEmpty(deviceName)) {
            deviceName = android.os.Build.MODEL;
            if (!TextUtils.isEmpty(deviceName)) {
                SPUtil.getInstance(mContext).save(PrefsConst.PREF_DEVICE_NAME, deviceName);
            }
        }
        return deviceName;
    }

    /**
     * @return：0：未知、1：移动、2：联通、3：电信 运营商
     */
    public static int getTMobileName(Context sContext) {
        try {
            String imsi = getIMSI(sContext.getApplicationContext());

            if (imsi != null) {
                if (imsi.startsWith("46000") || imsi.startsWith("46002")
                        || imsi.startsWith("46007") || imsi.startsWith("46020")) {
                    return 1;
                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
                    return 2;
                } else if (imsi.startsWith("46003") || imsi.startsWith("46005")) {
                    return 3;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 软件版本
     * 
     * @return
     */
    public static String getAppVersion(Context sContext) {
        try {
            PackageManager manager = sContext.getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(sContext.getApplicationContext()
                    .getPackageName(), 0);
            String sVersion = String.valueOf(info.versionName);
            return sVersion;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 软件版本号
     * 
     * @return
     */
    public static int getAppVersionCode(Context sContext) {
        try {
            PackageManager manager = sContext.getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(sContext.getApplicationContext()
                    .getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取渠道号
     * 
     * @return
     */
    public static String getChannel(Context sContext) {
        String channelNo = "1";
        PackageManager pm = sContext.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(sContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (appInfo != null) {
                channelNo = appInfo.metaData.getInt("MYCHEERING_GAME_CHANNEL") + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelNo;
        /*
         * String channelNo = "1"; if (channelNo == null || channelNo.equals("1")) { InputStream is
         * = null; BufferedReader sReader = null; try { is = sContext.getAssets().open("c"); sReader
         * = new BufferedReader(new InputStreamReader(is)); String temp = sReader.readLine();
         * channelNo = temp.split("_")[0]; } catch (Exception e) { } finally { try { if (sReader !=
         * null) { sReader.close(); sReader = null; } if (is != null) { is.close(); sReader = null;
         * } } catch (Exception e2) { } if (channelNo != null && !channelNo.equals("1"))
         * Util.channelNo = channelNo; } } else { channelNo = Util.channelNo; } return channelNo;
         */
    }

    /**
     * get UMENG_APPKEY
     * 
     * @return
     */
    public static String getUmengKey(Context sContext, String defaultUK) {
        try {
            ApplicationInfo ai = sContext.getPackageManager()
                    .getApplicationInfo(sContext.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object b = ai.metaData.get("UMENG_APPKEY");
            String romid = b.toString();
            return romid;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultUK;
        }
    }

    /**
     * 刷机渠道
     * 
     * @return
     */
    public static String getShopId() {
        String channel = "0";
        try {
            File sFile = new File("/system/etc/EngineX/build.prop");

            if (sFile != null && sFile.exists()) {
                BufferedReader sReader = new BufferedReader(new FileReader(
                        sFile));
                channel = sReader.readLine();
                sReader.close();
                channel = channel.split("_")[0];
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(channel))
            channel = "0";
        return channel;
    }

    /**
     * 刷机档口
     * 
     * @return
     */
    public static String getMarketId() {
        String dangkou = "0";
        try {
            File sFile = new File("/system/etc/EngineX/build.prop");

            if (sFile != null && sFile.exists()) {
                BufferedReader sReader = new BufferedReader(new FileReader(
                        sFile));
                dangkou = sReader.readLine();
                sReader.close();
                dangkou = dangkou.split("_")[1];
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(dangkou))
            dangkou = "0";
        return dangkou;
    }

    /**
     * 打开未知来源开关
     * 
     * @param context
     */
    @SuppressWarnings("deprecation")
    public static void openSecure(Context sContext) {
        try {
            ContentResolver sResolver = sContext.getApplicationContext().getContentResolver();
            int value = Settings.Secure.getInt(sResolver,
                    Settings.Secure.INSTALL_NON_MARKET_APPS);

            if (0 == value) {
                Settings.Secure.putInt(sResolver,
                        Settings.Secure.INSTALL_NON_MARKET_APPS, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是阿里os
     */
    public static boolean isYunOs() {
        BufferedReader sReader = null;
        try {
            File sFile = new File("/system/build.prop");
            sReader = new BufferedReader(new InputStreamReader(new FileInputStream(sFile)));
            String strBuffer;

            while ((strBuffer = sReader.readLine()) != null) {
                if (strBuffer.startsWith("ro.sys.vendor")
                        && strBuffer.toLowerCase(Locale.getDefault()).contains("yunos")
                        || strBuffer.startsWith("ro.yunos.version")
                        && strBuffer.toLowerCase(Locale.getDefault()).contains("yunos")) {
                    try {
                        if (sReader != null) {
                            sReader.close();
                            sReader = null;
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (sReader != null) {
                try {
                    sReader.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    /**
     * 制造商
     * 
     * @return
     */
    public static String getManufactruer() {
        return android.os.Build.MANUFACTURER + "";
    }

    /**
     * 是否是百度os
     * 
     * @return
     */
    public static boolean isBaiduOs() {
        try {
            String manufactruer = getManufactruer();

            if (!TextUtils.isEmpty(manufactruer)
                    && manufactruer.toLowerCase(Locale.getDefault()).trim().equals("baidu")) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否是厂商app
     * 
     * @param context
     * @param pkg
     * @return
     */
    private static boolean isBrandApp(Context context, String pkg) {
        String[] filterPkg = context.getResources().getStringArray(
                Util.R_array(context, "filter_package_app_update"));

        for (String string : filterPkg) {
            if (string.equals(pkg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 厂商
     * 
     * @return
     */
    public static String getCompany() {
        if (isBaiduOs()) {
            return "baidu";
        } else {
            return android.os.Build.BRAND;
        }
    }

    /**
     * 是否是ViVo
     */
    public static boolean isViVo() {
        String strCompany = getCompany();
        if (strCompany != null) {
            return "vivo".equals(strCompany.toLowerCase(Locale.getDefault()));
        }
        return false;
    }

    public static boolean isMeiZu() {
        String strCompany = getCompany();
        if (strCompany != null) {
            return "Meizu".equals(strCompany.toLowerCase(Locale.getDefault()));
        }
        return false;
    }

    /**
     * 是否是Miui
     */
    public static boolean isMiui() {
        String strCompany = getCompany();
        if (strCompany != null) {
            return "xiaomi".equals(strCompany.toLowerCase(Locale.getDefault()));
        }
        return false;
    }

    /**
     * 是否是Miui
     */
    public static boolean isHMNote() {
        String mobile = android.os.Build.MODEL;
        if (mobile != null) {
            return mobile.toLowerCase(Locale.getDefault()).startsWith("hm note");
        }
        return false;
    }

    /**
     * 判断当前界面是否是桌面
     */
    public static boolean isHome(Context sContext) {
        ActivityManager mActivityManager = (ActivityManager) sContext.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes(sContext.getApplicationContext()).contains(
                rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     * 
     * @return 返回包含所有包名的字符串列表
     */
    private static List<String> getHomes(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    public static String getMacAddress(Context sContext) {
        try {
            WifiManager wifi = (WifiManager) sContext.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            String macAddress = wifi.getConnectionInfo().getMacAddress();
            macAddress = macAddress.replace(":", "");
            return macAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取单个imei
     */
    public static String getSingleIMEI(Context sContext) {
        String imei = "";

        try {
            TelephonyManager tm = (TelephonyManager) sContext.getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(imei)) {
            if (imei.trim().length() < 15) {
                int count = 15 - imei.trim().length();
                imei = imei.trim();

                for (int i = 0; i < count; i++) {
                    imei += "0";
                }
            }
        }
        return imei;
    }

    public static int getStatusBarHeight(Context sContext) {
        Resources res = sContext.getApplicationContext().getResources();
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判断是否本机已安装了packageName指定的应用
     * 
     * @param packageName
     * @return
     */
    public static boolean isInstalledApk(Context sContext, String packageName) {
        try {
            if (TextUtils.isEmpty(packageName)) {
                return false;
            }
            PackageInfo sPackageInfo = sContext.getPackageManager().getPackageInfo(packageName, 0);
            if (sPackageInfo != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @return the lbscity
     */
    public static String getLbscity(Context sContext) {
        return (String) SPUtil.getInstance(sContext).get(PrefsConst.PREF_LBS_CITY, "-1");
    }

    /**
     * @param lbscity the lbscity to set
     */
    public static void setLbscity(Context sContext, String lbscity) {
        SPUtil.getInstance(sContext).save(PrefsConst.PREF_LBS_CITY, lbscity);
    }

    /**
     * @return the lbsprovince
     */
    public static String getLbsprovince(Context sContext) {
        return (String) SPUtil.getInstance(sContext).get(PrefsConst.PREF_LBS_PROVINCE, "-1");
    }

    /**
     * @param lbsprovince the lbsprovince to set
     */
    public static void setLbsprovince(Context sContext, String lbsprovince) {
        SPUtil.getInstance(sContext).save(PrefsConst.PREF_LBS_PROVINCE, lbsprovince);
    }

    /**
     * @return the lbsdistrict
     */
    public static String getLbsdistrict(Context sContext) {
        return (String) SPUtil.getInstance(sContext).get(PrefsConst.PREF_LBS_DISTRICT, "-1");
    }

    /**
     * @param lbsdistrict the lbsdistrict to set
     */
    public static void setLbsdistrict(Context sContext, String lbsdistrict) {
        SPUtil.getInstance(sContext).save(PrefsConst.PREF_LBS_DISTRICT, lbsdistrict);
    }

    public static String getUID(Context sContext) {
        return (String) SPUtil.getInstance(sContext).getString(PrefsConst.PREF_UID, null);
    }

    public static void setUID(Context sContext, String uid) {
        SPUtil.getInstance(sContext).save(PrefsConst.PREF_UID, uid);
    }

    public static String getUid(Context sContext) {
        String strUid = null;
        String uid = getUID(sContext);
        if (TextUtils.isEmpty(uid)) {
            try {
                File sFile = new File("/data/local/tmp/cheering");

                if (sFile.exists() && sFile.length() > 0) {
                    BufferedReader sReader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(sFile)));
                    strUid = sReader.readLine();
                    sReader.close();
                }
            } catch (FileNotFoundException e) {
            } catch (Exception e) {
            }

            if (TextUtils.isEmpty(strUid)) {
                try {
                    File sFile = new File(BConst.DIR_DCIM + File.separator + "cheering");

                    if (sFile.exists() && sFile.length() > 0) {
                        BufferedReader sReader = new BufferedReader(new InputStreamReader(
                                new FileInputStream(sFile)));
                        strUid = sReader.readLine();
                        sReader.close();
                    }
                } catch (FileNotFoundException e) {
                } catch (Exception e) {
                }
            }
        } else {
            strUid = uid;
        }

        if (TextUtils.isEmpty(strUid) || strUid.length() != 49) {
            strUid = null;
            String imei = getSingleIMEI(sContext);
            String mac = getMacAddress(sContext);

            if (!TextUtils.isEmpty(imei) && !TextUtils.isEmpty(mac)) {
                strUid = imei + mac;
                strUid = Base64.encodeToString(strUid.trim().getBytes(), Base64.NO_WRAP);
                strUid = Util.encryptString(strUid, String.valueOf(System.currentTimeMillis()));
                setUid(sContext, strUid);
            } else {
                setUid(sContext, null);
            }
        } else {
            setUid(sContext, strUid);
        }
        return strUid;
    }

    public static void setUid(Context sContext, String sUid) {
        try {
            if (!TextUtils.isEmpty(sUid) && sUid.trim().length() > 0) {
                setUID(sContext, sUid.trim());
                try {
                    FileOutputStream os = new FileOutputStream("/data/local/tmp/cheering");
                    os.write(sUid.trim().getBytes());
                    os.flush();
                    os.close();
                    os = null;
                } catch (FileNotFoundException e) {
                } catch (Exception e) {
                }

                try {
                    if (Util.isSDCardAvailable()) {
                        File dirFile = new File(BConst.DIR_DCIM);
                        if (!dirFile.exists()) {
                            FileUtil.makeDir(dirFile);
                        }

                        FileOutputStream os = new FileOutputStream(BConst.DIR_DCIM + File.separator
                                + "cheering");
                        os.write(sUid.trim().getBytes());
                        os.flush();
                        os.close();
                        os = null;
                    }
                } catch (FileNotFoundException e) {
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 卸载应用
     * 
     * @param packageName
     */
    public static void uninstallApp(Context context, String packageName) {
        try {
            if (isInstalledApk(context, packageName)) {
                Uri packageUri = Uri.parse("package:" + packageName);
                Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void installFromPath(Context sContext, File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("android.intent.extra.INSTALLER_PACKAGE_NAME", sContext.getPackageName());
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        sContext.startActivity(intent);
    }

    /**
     * 获取系统签名
     * 
     * @param context
     * @return
     */
    public static String getSystemSignature(Context context) {
        try {
            File dirFile = new File("/system/framework");
            Signature[] signatures;
            if (dirFile != null && dirFile.isDirectory()) {
                File[] files = dirFile.listFiles();

                for (File file : files) {
                    if (file.getName().endsWith(".apk")) {
                        signatures = getSignature(context, file.getAbsolutePath());

                        if (signatures != null && signatures.length > 0) {
                            return getSignatureMD5(signatures[0].toByteArray());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Signature[] getSignature(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_SIGNATURES);
        return packageInfo.signatures;
    }

    private static String getSignatureMD5(byte[] signature) {
        try {
            return MD5.getMD5(signature);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 是否是需要过滤的app
     * 
     * @param context
     * @param pkg
     * @return
     */
    public static boolean filterUpdateApp(Context context,
            PackageInfo packageInfo, String systemSignature) {
        try {
            PackageManager sPackageManager = context.getPackageManager();
            Intent sIntent = sPackageManager
                    .getLaunchIntentForPackage(packageInfo.packageName);

            if (sIntent == null) {
                return true;
            } else {
                if (systemSignature == null) {
                    if (isBrandApp(context, packageInfo.packageName)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (systemSignature.equals(getApkSignatureWithPackageName(context,
                            packageInfo.applicationInfo.packageName))) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取apk签名
     */
    public static String getApkSignatureWithPackageName(Context context, String pkg) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES);

            if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                return getSignatureMD5(packageInfo.signatures[0].toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
