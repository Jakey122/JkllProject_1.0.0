package com.sdk.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.sdk.bean.AppInfo;
import com.sdk.bean.AppSnippet;
import com.sdk.bean.BaseInfo;
import com.sdk.helper.ImeiHelper;
import com.sdk.net.NetworkStatus;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by root on 16-5-9.
 */
public class Util {

    /**
     * 新建文件夹
     *
     * @param path
     */
    public static void makeDir(String path) {
        if (path == null || path.length() == 0)
            return;
        makeDir(new File(path));
    }

    /**
     * 新建文件夹
     *
     * @param file
     */
    public static void makeDir(File file) {
        if (file == null || file.exists()) {
            return;
        }
        file.mkdirs();
    }

    public static void deleteFile(String path) {
        if (!TextUtils.isEmpty(path))
            deleteFile(new File(path));
    }

    public static void delete(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                file = null;
            }
        }

    }

    public static void deleteFile(File file) {
        try {
            if (file != null && file.exists()) {
                File toFile = new File(file.getAbsolutePath() + ".tmp");
                file.renameTo(toFile);
                toFile.delete();
                toFile = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 判断sdcard是否可用
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        boolean sdcardAvailable = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            sdcardAvailable = true;
        }
        return sdcardAvailable;
    }

    /**
     * 数组转列表
     */
    public static ArrayList<Byte> byteArrayToList(byte[] byteArray) {
        ArrayList<Byte> byteList = new ArrayList<Byte>();

        if (byteArray != null) {
            for (int i = 0; i < byteArray.length; i++) {
                byteList.add(byteArray[i]);
            }
        }
        return byteList;
    }

    /**
     * 数组转列表
     */
    public static byte[] byteListToArray(List<Byte> byteArray) {
        byte[] tempArray = new byte[byteArray.size()];

        if (byteArray != null) {
            for (int i = 0; i < byteArray.size(); i++) {
                tempArray[i] = byteArray.get(i);
            }
        }
        return tempArray;
    }

    /**
     * 加密
     *
     * @param src
     * @return
     */
    public static String encryptString(String src, String seed) {
        byte[] temp = src.getBytes();

        int length = temp.length;
        int count = length / 2 / 2 + (length / 2 % 2 == 0 ? 0 : 1);

        for (int i = 0; i < count; i++) {
            int index = 0;

            if (length % 2 == 0) {
                index = length / 2 + 2 * i;
            } else {
                index = length / 2 + 2 * i + 1;
            }

            byte tempByte = temp[i];
            temp[i] = temp[index];
            temp[index] = tempByte;
        }

        for (int i = 0; i < length / 2 - count; i++) {
            int index = length - 2 * i - 1;
            byte tempByte = temp[i + count];
            temp[i + count] = temp[index];
            temp[index] = tempByte;
        }

        byte[] byteSend = seed.getBytes();
        int timeLength = byteSend.length;

        ArrayList<Byte> result = byteArrayToList(temp);

        for (int i = 0; i < timeLength; i++) {
            if (i < length) {
                result.add(length - i, byteSend[i]);
            } else {
                result.add(0, byteSend[i]);
            }
        }

        byte[] tempResult = Util.byteListToArray(result);
        return new String(tempResult);
    }

    /**
     * 获取单个imei
     */
    public static String getSingleIMEI(Context context) {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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

    public static String getMacAddress(Context sContext) {
        try {
            WifiManager wifi = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
            String macAddress = wifi.getConnectionInfo().getMacAddress();
            macAddress = macAddress.replace(":", "");
            return macAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gzip压缩
     */
    public static byte[] gzipCompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }
        return gzipCompress(str.getBytes());
    }

    /**
     * gzip压缩
     */
    public static byte[] gzipCompress(byte[] source) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(source);
        gzip.close();
        return out.toByteArray();
    }

    /**
     * gzip解压
     */
    public static byte[] gzipDecompress(String str) throws IOException {
        if (TextUtils.isEmpty(str)) {
            return new byte[0];
        }
        return gzipDecompress(str.getBytes());
    }

    /**
     * gzip解压
     */
    public static byte[] gzipDecompress(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
            baos.close();
            gzip.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * gzip解压
     */
    public static byte[] gzipDecompress(InputStream is) {
        byte[] b = null;
        try {
            GZIPInputStream gzip = new GZIPInputStream(is);
            byte[] buf = new byte[1024];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
            baos.close();
            gzip.close();
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    public static final String getDataSize(float size) {
        DecimalFormat formater = new DecimalFormat("####");
        float SIZE_KB = 1024.00f;

        if (size < SIZE_KB) {
            return size + "bytes";

        } else if (size < SIZE_KB * SIZE_KB) {
            float kbsize = size / SIZE_KB;
            return formater.format(kbsize) + "KB";

        } else if (size < SIZE_KB * SIZE_KB * SIZE_KB) {
            float mbsize = size / SIZE_KB / SIZE_KB;
            return formater.format(mbsize) + "MB";

        } else if (size < SIZE_KB * SIZE_KB * SIZE_KB * SIZE_KB) {
            float gbsize = size / SIZE_KB / SIZE_KB / SIZE_KB;
            return formater.format(gbsize) + "GB";

        } else {
            return "size: error";
        }
    }

    /**
     * 计算进度
     *
     * @param min
     * @param total
     * @return
     */
    public static float computeProgress(long min, long total) {
        if (total == 0) {
            return 0;
        } else {
            float douMin = (float) min;
            float douTotal = (float) total;
            DecimalFormat decimalFormat = new DecimalFormat("###.0");
            String p = decimalFormat.format(douMin * 100 / douTotal);
            return Float.valueOf(p);
        }
    }

    /**
     * 计算进度
     *
     * @param min
     * @param total
     * @return
     */
    public static int computeProgress(int min, int total) {
        return total == 0 ? 0 : min * 100 / total;
    }

    /**
     * 计算进度
     *
     * @param min
     * @param total
     * @return
     */
    public static int computeProgressInt(long min, long total) {
        return (int) (total == 0 ? 0 : min * 100 / total);
    }

    public static String getFileNameFromUrl(String url) {
        return getMD5(url);
    }

    public static String getMD5(String val) {
        return val != null ? getMD5(val.getBytes()) : "";
    }

    public static String getMD5(byte[] val) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = e.digest(val);
            StringBuffer hexValue = new StringBuffer();

            for (int i = 0; i < md5Bytes.length; ++i) {
                int temp = md5Bytes[i] & 255;
                if (temp < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(temp));
            }

            return hexValue.toString();
        } catch (Exception var6) {
            var6.printStackTrace();
            return "";
        }
    }

    /**
     * 数组转列表
     */
    public static ArrayList<String> arrayToList(String[] strArray) {
        ArrayList<String> strList = new ArrayList<String>();

        if (strArray != null) {
            for (int i = 0; i < strArray.length; i++) {
                strList.add(strArray[i]);
            }
        }
        return strList;
    }


    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            if (!email.contains("@")) return flag;
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * Attempts to cancel execution of this task
     *
     * @param task
     * @param mayInterruptIfRunning true if the thread executing this task should be interrupted;
     *                              otherwise, in-progress tasks are allowed to complete
     */
    public static void cancelTask(AsyncTask<?, ?, ?> task,
                                  boolean mayInterruptIfRunning) {
        if (task == null)
            return;
        if (task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(mayInterruptIfRunning);
        }
    }

    public static BaseInfo getSelfUpdateApk(final Context sContext, final AppInfo updateInfo) {
        final Context context = sContext.getApplicationContext();
        String apkDir = DownloadUtil.getInstance(context).getApkCacheDir();
        File apkDirFile = new File(apkDir);
        File[] files = null;
        if (apkDirFile != null && apkDirFile.isDirectory()) {
            files = apkDirFile.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (!file.isFile()) return false;
                    if (!file.exists()) return false;
                    if (!file.getName().endsWith(".apk")) return false;

                    AppSnippet mAppSnippet = getAppSnippet(context, Uri.parse(file.getAbsolutePath()));
                    if (mAppSnippet == null) return false;
                    if (mAppSnippet.getPackageName().equals(context.getPackageName()) &&
                            mAppSnippet.getVersionCode() > Util.getAppVersionCode(context)) {
                        updateInfo.parse(mAppSnippet);
                        return true;
                    }
                    return false;
                }
            });
        }
        if (files == null || files.length == 0) return null;
        return updateInfo;
    }

    public static AppSnippet getAppSnippet(Context sContext, Uri uri){
        try{
            Context context = sContext.getApplicationContext();
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(uri.toString(), 0);
            Resources resources = PackageUtil.getUninstalledApkResources(context, uri.toString());
            return new AppSnippet().parse(packageInfo, resources, pm, uri.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static int getAppVersionCode(Context context, String pkg) {
        int versionCode = 10000000;
        try {
            PackageManager sPackageManager = context.getPackageManager();
            PackageInfo sPackageInfo = sPackageManager.getPackageInfo(pkg, 0);
            if (sPackageInfo != null) {
                versionCode = sPackageInfo.versionCode;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 设备名称
     *
     * @return
     */
    public static String getDeviceName(Context sContext) {
        Context context = sContext.getApplicationContext();
        String deviceName = SPUtil.getInstant(context).getString(SPUtil.DeviceName, "");
        if (TextUtils.isEmpty(deviceName)) {
            deviceName = android.os.Build.MODEL;
            if (!TextUtils.isEmpty(deviceName)) {
                SPUtil.getInstant(context).save(SPUtil.DeviceName, deviceName);
            }
        }
        return deviceName;
    }

    /**
     * 软件版本
     *
     * @return
     */
    public static String getAppVersion(Context sContext) {
        try {
            Context context = sContext.getApplicationContext();
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
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
            Context context = sContext.getApplicationContext();
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getIMSI(Context sContext) {
        Context context = sContext.getApplicationContext();
        String imsi = SPUtil.getInstant(context).getString(SPUtil.IMSI, "");
        if (!TextUtils.isEmpty(imsi)) return imsi;
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
            if (!TextUtils.isEmpty(imsi))
                SPUtil.getInstant(context).save(SPUtil.IMSI, imsi);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return imsi;
    }

    /**
     * 获取自身签名信息
     *
     * @return
     */
    public static String getSelfSignature(Context sContext) {
        Context context = sContext.getApplicationContext();
        String strSignature = PackageUtil.getApkSignatureWithPackageName(
                context.getApplicationContext(), context.getPackageName());
        if (TextUtils.isEmpty(strSignature)) {
            return "";
        } else {
            return strSignature;
        }
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
     * 厂商
     *
     * @return
     */
    public static String getCompany() {
        if (isYunOs()) {
            return "yunos";
        } else if (isBaiduOs()) {
            return "baidu";
        } else {
            return android.os.Build.BRAND;
        }
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
     * 是否是阿里os
     */
    public static boolean isYunOs() {
        BufferedReader sReader = null;
        try {
            File sFile = new File("/system/build.prop");
            sReader = new BufferedReader(new InputStreamReader(new FileInputStream(sFile)));
            String strBuffer;

            while ((strBuffer = sReader.readLine()) != null) {
                if (strBuffer.startsWith("ro.sys.vendor") && strBuffer.toLowerCase(Locale.getDefault()).contains("yunos")) {
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
     * 是否是百度os
     *
     * @return
     */
    public static boolean isBaiduOs() {
        try {
            String manufactruer = getManufactruer();

            if (!TextUtils.isEmpty(manufactruer) && manufactruer.toLowerCase(Locale.getDefault()).trim().equals("baidu")) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList listToArrayList(List strArray) {
        ArrayList strList = new ArrayList();

        if (strArray != null) {
            for (int i = 0; i < strArray.size(); i++) {
                strList.add(strArray.get(i));
            }
        }
        return strList;
    }

    public static String getChannel(Context context) {
        String channelNo = SPUtil.getInstant(context).getString(SPUtil.SelfChannel, "");
        if (!TextUtils.isEmpty(channelNo)) return channelNo;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (appInfo != null) {
                channelNo = appInfo.metaData.getInt("CHANNEL") + "";
                if (!TextUtils.isEmpty(channelNo))
                    SPUtil.getInstant(context).save(SPUtil.SelfChannel, channelNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelNo;
    }

    public static int getSdkVersionCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static String getSdkVersion() {
        return "" + android.os.Build.VERSION.SDK_INT;
    }

    public static String getSdkVersionName() {
        return android.os.Build.VERSION.RELEASE + "";
    }

    public static String getAndroidId(Context sContext) {
        Context context = sContext.getApplicationContext();
        String androidId = SPUtil.getInstant(context).getString(SPUtil.AndroidId, "");
        if (!TextUtils.isEmpty(androidId)) return androidId;
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        SPUtil.getInstant(context).save(SPUtil.AndroidId, androidId);
        return androidId;
    }

    public static String getResolution(Context sContext) {
        Context context = sContext.getApplicationContext();
        String resolution = SPUtil.getInstant(context).getString(SPUtil.ResolutionInfo, "");
        if (!TextUtils.isEmpty(resolution)) return resolution;

        DisplayMetrics sDisplayMetrics = context.getResources().getDisplayMetrics();
        resolution = sDisplayMetrics.widthPixels + "*" + sDisplayMetrics.heightPixels;
        SPUtil.getInstant(context).save(SPUtil.ResolutionInfo, resolution);
        return resolution;
    }

    public static String getIMEI(Context sContext) {
        Context context = sContext.getApplicationContext();
        String imei = SPUtil.getInstant(context).getString(SPUtil.IMEI, "");
        if (TextUtils.isEmpty(imei)) {
            ImeiHelper.getInstance().getIMEI(context);
            return getSingleIMEI(context);
        }
        return imei;
    }

    /**
     * 格式化时间
     *
     * @param times
     * @param formatType
     * @return
     */
    public static String formatDate(long times, String formatType) {
        if (formatType == null) {
            throw new NullPointerException("formatType is null!");
        }
        SimpleDateFormat df = new SimpleDateFormat(formatType);
        return df.format(times);
    }

    /**
     * 格式化时间
     *
     * @param times
     * @param formatType
     * @return
     */
    public static long formatDate(String times, String formatType) {
        try {
            if (formatType == null) {
                throw new NullPointerException("formatType is null!");
            }
            SimpleDateFormat df = new SimpleDateFormat(formatType);
            return df.parse(times).getTime();
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isNetAvailable(Context context) {
        return NetworkStatus.getInstance(context).isConnected();
    }

    /**
     * 获取下载文件保存地址
     */
    public static String getDownloadedFilePath(Context context, String downloadUrl) {
        StringBuilder downloadDir = new StringBuilder(DownloadUtil.getInstance(context).getApkCacheDir());
        downloadDir.append(File.separator + getFileNameFromUrl(downloadUrl));
        downloadDir.append(".apk");
        return downloadDir.toString();
    }

    public static void doCall(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void saveUserInfo(Context context, BaseInfo info) {
        SPUtil.getInstant(context).setVerify(info.getVerify());
        SPUtil.getInstant(context).setAccount(info.getAccount());
        SPUtil.getInstant(context).setLogo(info.getLogo());
    }
}
