package com.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 16-5-9.
 */
public class SPUtil {

    public static final String RequestFailCount = "requestFailCount";
    public static final String UserActiveTimeCount = "userActiveTimeCount";
    public static final String SoftUpdatePrompt = "softUpdatePrompt";
    public static final String AutoUpdate = "autoUpdate";
    public static final String DelApkInstalled = "delApkInstalled";
    public static final String ClearInstalledPackage = "clearInstalledPackage";
    public static final String LastUpdateCheck = "lastUpdateCheck";
    public static final String IsClickLater = "isClickLater";
    public static final String UpdateMethod = "updateMethod";
    public static final String ManualUpdate = "manualUpdate";
    public static final String LastUpdatePushTime = "lastUpdatePushTime";
    public static final String UpdatePushCount = "updatePushCount";
    public static final String IsHasSelfNewEdition = "isHasSelfNewEdition";
    public static final String UpdateVersionName = "updateVersionName";
    //    public static final String IsFirstClick = "isFirstClick";
    public static final String IsFirstSettingClick = "IsFirstSettingClick";
    public static final String NewVersionCode = "newVersionCode";
    public static final String LastSoftUpdateAutoCheck = "lastSoftUpdateAutoCheck";
    public static final String LastCloseNoticeTime = "lastCloseNoticeTime";
    public static final String trumpetState = "trumpetState";
    public static final String AutoMiao = "autoMiao";
    public static final String ProvinceTraffic = "provinceTraffic";
    public static final String LastShowSoftUpdatePush = "lastShowSoftUpdatePush";
    public static final String ShowFirstStartPush = "showFirstStartPush";
    public static final String CheckUpdateCount = "checkUpdateCount";
    public static final String FIRST_ENTRY = "firstEntry";

    public static final String DeviceName = "deviceName";
    public static final String SelfChannel = "selfChannel";
    public static final String ResolutionInfo = "resolutionInfo";
    public static final String IMSI = "imsi";
    public static final String AndroidId = "androidId";
    public static final String IMEI = "imei";

    private static final String TAG = "SPUtil";
    public static final String PREF_NAME = "com.android.apps.prefs";
    private static final String PREF_UVERIFY = "uverify";
    private static final String PREF_UACCOUNT = "uaccount";
    private static final String PREF_LOGO = "ulogo";


    private SharedPreferences mPreferences;
    private static SPUtil mInstance;
    private Context mContext;

    private SPUtil(Context context) {
        mContext = context.getApplicationContext();
        mPreferences = mContext.getSharedPreferences(PREF_NAME, 0);
    }

    public static SPUtil getInstant(Context context) {
        if (mInstance == null) {
            mInstance = new SPUtil(context);
        }
        return mInstance;
    }

    public SPUtil save(String key, Object value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, Boolean.valueOf(value.toString()));
        } else if (value instanceof Long) {
            editor.putLong(key, Long.valueOf(value.toString()));
        } else if (value instanceof Float) {
            editor.putFloat(key, Float.valueOf(value.toString()));
        } else if (value instanceof Integer) {
            editor.putInt(key, Integer.valueOf(value.toString()));
        }
        editor.commit();
        return mInstance;
    }

    public Object get(String key, Object value) {
        if (value instanceof String) {
            return mPreferences.getString(key, "");
        } else if (value instanceof Boolean) {
            return mPreferences.getBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            return mPreferences.getLong(key, Long.parseLong(value.toString()));
        } else if (value instanceof Float) {
            return mPreferences.getFloat(key, -1.0f);
        } else if (value instanceof Integer) {
            return mPreferences.getInt(key, Integer.parseInt(value.toString()));
        }
        return null;
    }

    public void setArrayListString(String key, ArrayList<String> list) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (list != null && list.size() > 0) {
            editor.putStringSet(key, new HashSet<>(list));
            editor.commit();
        }
    }

    public ArrayList<String> getArrayListString(String key) {
        Set<String> set = mPreferences.getStringSet(key, null);
        if (set != null) {
            ArrayList<String> setList = new ArrayList<>(set);
            return setList;
        }
        return null;
    }

    public String getString(String key, String strDefault) {
        return mPreferences.getString(key, "");
    }

    public long getLong(String key, long strDefault) {
        return mPreferences.getLong(key, strDefault);
    }

    public boolean getBoolean(String key, boolean blDefault) {
        return mPreferences.getBoolean(key, blDefault);
    }

    public void setVerify(String verfiy){
        save(PREF_UVERIFY, verfiy);
    }

    public String getVerify(){
        return getString(PREF_UVERIFY, "");
    }

    public void setAccount(String account){
        save(PREF_UACCOUNT, account);
    }

    public String getAccount(){
        return getString(PREF_UACCOUNT, "");
    }

    public void setLogo(String logo){
        save(PREF_LOGO, logo);
    }

    public String getLogo(){
        return getString(PREF_LOGO, "");
    }

    public void exit() {
        setVerify("");
        setAccount("");
        setLogo("");
    }
}
