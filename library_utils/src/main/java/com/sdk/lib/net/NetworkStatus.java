
package com.sdk.lib.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import java.util.Locale;

/**
 * 获取当前网络状态工具类
 */
public class NetworkStatus {

    private Context mcontext;
    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;
    private static String mNetworkType;
    private static NetworkStatus mInstance;

    public static NetworkStatus getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkStatus(context.getApplicationContext());
        }
        return mInstance;
    }

    public NetworkStatus(Context context) {
        mcontext = context.getApplicationContext();
        connectivityManager = (ConnectivityManager) mcontext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        telephonyManager = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 是否已连接wifi网络
     * 
     * @return
     */
    public boolean isWiFiConnected() {
        try {
            NetworkInfo netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (netInfo != null) {
                return netInfo.getState() == NetworkInfo.State.CONNECTED;
            } else {
                return false;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 是否连接手机网络（2G、3G等）
     * 
     * @return
     */
    public boolean isMobileConnected() {
        try {
            NetworkInfo netInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (netInfo != null) {
                return netInfo.getState() == NetworkInfo.State.CONNECTED;
            } else {
                return false;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取当前手机网络的类型（2G、3G等）
     * 
     * @return
     */
    private int getMobileNetworkType() {
        int state;
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
                state = ConnectionListener.CONN_2G;
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                state = ConnectionListener.CONN_4G;
                break;
            default:
                state = ConnectionListener.CONN_3G;
                break;
        }
        return state;
    }

    /**
     * 是否已连接网络（未连接、wifi、2G、3G等）
     * 
     * @return
     */
    public boolean isConnected() {
        try {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null) {
                return netInfo.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取网络类型
     * 
     * @return 网络类型
     */
    public static String getNetworkType(Context context) {
        isNetWorking(context);
        return mNetworkType;
    }

    /**
     * 判断网络是否连接
     * 
     * @param context 环境对象
     * @return true 有网络，false 无网络
     */
    public static boolean isNetWorking(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                if (networkInfo.getTypeName().equals("WIFI")) {
                    mNetworkType = networkInfo.getTypeName().toLowerCase(
                            Locale.ENGLISH);
                } else {
                    if (networkInfo.getExtraInfo() == null) {
                        mNetworkType = "";
                    } else {
                        mNetworkType = networkInfo.getExtraInfo().toLowerCase(
                                Locale.ENGLISH);
                    }
                }
                return true;
            } else {
                mNetworkType = "";
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取当前所连接网络类型（未连接、wifi、2G、3G等）
     * 
     * @return
     */
    public int getNetWorkState() {
        if (isConnected()) {
            if (isWiFiConnected()) {
                return ConnectionListener.CONN_WIFI;
            }
            if (isMobileConnected()) {
                return getMobileNetworkType();
            }
        }
        return ConnectionListener.CONN_NONE;
    }

    public boolean isRadioOff() {
        ServiceState serviceState = new ServiceState();
        return serviceState.getState() == ServiceState.STATE_POWER_OFF;
    }

    // public static String getParams(Context context) {
    // // String imei = KingsSystemUtils.imie;
    // // if (imei == null) {
    // // imei = KingsSystemUtils.getIMEI(context);
    // // }
    // String imei=Util.getIMEI(context);
    // // String mc = KingsSystemUtils.macAddress;
    // // if (TextUtils.isEmpty(mc)) {
    // // mc = KingsSystemUtils.getMacAddress(context);
    // // }
    // String mc = Util.getMacAddress();
    // String product = Build.MODEL;
    // String net = NetworkStatus.getNetworkType(context);
    // String apis = "F:SP_V:" + Util.getAppVersionCode();
    // String channel = "";//wss
    // String producer = Build.DEVICE;
    // String sdk = Build.VERSION.SDK;
    // String vcode = String.valueOf(Util.getAppVersionCode());
    // String device_md5 = "";//KingsSystemUtils.getDeviceMd5(context)
    // String params = "&imei=" + imei + "&mc=" + mc + "&product=" + product
    // + "&net=" + net + "&apis=" + apis + "&channel=" + channel
    // + "&producer=" + producer + "&sdk=" + sdk + "&vcode=" + vcode
    // + "&device-md5=" + device_md5;
    // return params;
    // }
}
