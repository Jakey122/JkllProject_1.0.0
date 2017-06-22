
package com.sdk.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import java.io.File;

public class DownloadUtil {

    private WifiLock wifiLock = null;
    private WakeLock mwakeLock = null;
    private int mWiFiFlag = 0;
    private int mPowerFlag = 0;
    private boolean isSdcardAvailable = true;
//    private MyHandler mHandler;
    private Context mContext;
    
    private static DownloadUtil mInstance;

    public static DownloadUtil getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new DownloadUtil(context);
        }
        return mInstance;
    }

    public DownloadUtil(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context.getApplicationContext();
    }

    public String getSdcardCacheDir() {
        File cacheDir = mContext.getExternalCacheDir();
        if (cacheDir == null || !cacheDir.exists()) {
            cacheDir = new File(Environment.getExternalStorageDirectory().toString()
                    + "/Android/data/" + mContext.getPackageName() + "/cache");
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 获取apk下载缓存目录
     */
    public String getApkCacheDir() {
        String strCacheDir;

        if (isSdcardAvailable && Util.isSDCardAvailable()) {
            File cacheFile = new File(getSdcardCacheDir(), "apk");
            cacheFile.mkdirs();
            strCacheDir = cacheFile.getAbsolutePath();
        } else {
            File cacheDir = mContext.getFilesDir();
            strCacheDir = cacheDir.getAbsolutePath();
        }
        return strCacheDir;
    }

    public boolean isSdcardAvailable() {
        return isSdcardAvailable;
    }

    public void setSdcardAvailable(boolean isSdcardAvailable) {
        this.isSdcardAvailable = isSdcardAvailable;
    }

    /**
     * acquire a lock on wake
     */
    public void acquireWakeLock() {
        mPowerFlag++;
        if (mPowerFlag > 1)
            return;
        if (mwakeLock == null) {
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mwakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass()
                    .getCanonicalName());
            mwakeLock.acquire();
        }
    }

    /**
     * release a lock on wake
     */
    public void releaseWakeLock() {
        if (mPowerFlag < 1)
            return;
        mPowerFlag--;
        if (mPowerFlag > 0)
            return;
        if (mwakeLock != null && mwakeLock.isHeld()) {
            mwakeLock.release();
            mwakeLock = null;
        }
    }

    /**
     * Acquires a lock on wifi connection if it is not already locked
     */
    public void acquireWiFiLock() {
        mWiFiFlag++;
        if (mWiFiFlag > 1)
            return;

        if (wifiLock == null) {
            WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "kanbox");
        }

        if (!wifiLock.isHeld()) {
            wifiLock.setReferenceCounted(false);
            wifiLock.acquire();
        }
    }

    /**
     * Releases a lock on wifi
     */
    public void releaseWiFiLock() {
        if (mWiFiFlag < 1)
            return;
        mWiFiFlag--;
        if (mWiFiFlag > 0)
            return;
        if (wifiLock != null && wifiLock.isHeld()) {
            wifiLock.release();
        }
    }

//    /**
//     * 显示Toast
//     * @param msg
//     */
//    public void showToast(int msg) {
//        showToast(getResources().getString(msg));
//    }
//
//    /**
//     * 显示Toast
//     * @param msg
//     */
//    public void showToast(String msg) {
//        Message sMessage = mHandler.obtainMessage(MyHandler.MSG_TOAST);
//        sMessage.obj = msg;
//        mHandler.sendMessage(sMessage);
//    }
//
//    public void showToast(Bitmap bitmap) {
//        if (bitmap != null) {
//            Message sMessage = mHandler.obtainMessage(MyHandler.MSG_IMAGE_TOAST);
//            sMessage.obj = bitmap;
//            mHandler.sendMessage(sMessage);
//        }
//    }
//
//    static class MyHandler extends Handler {
//        private Context mAppContext;
//        public static final int MSG_TOAST = 1;
//        public static final int MSG_IMAGE_TOAST = 2;
//
//        public MyHandler(Context context) {
//            mAppContext = context;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//            case MSG_TOAST:
//                String sToast_Msg = String.valueOf(msg.obj);
//                Toast.makeText(mAppContext, sToast_Msg, Toast.LENGTH_SHORT).show();
//                break;
//            case MSG_IMAGE_TOAST:
//                Bitmap sBitmap = (Bitmap) msg.obj;
//                ImageView sImageView = new ImageView(mAppContext);
//                sImageView.setImageBitmap(sBitmap);
//                Toast sToast = new Toast(mAppContext);
//                sToast.setView(sImageView);
//                sToast.show();
//                break;
//            default:
//                break;
//            }
//        }
//    }
}
