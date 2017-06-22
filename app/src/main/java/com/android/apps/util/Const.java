package com.android.apps.util;

import android.os.Environment;

import com.jkll.app.BuildConfig;

import java.io.File;

/**
 * Created by root on 16-5-9.
 */
public class Const {
    public static final boolean DEBUG = false;
    public static final String DIR_LOG = ".log"; // 日志文件目录
    public static final String DIR_SDCARD = Environment.getExternalStorageDirectory().getPath();
    public static final String DIR_DCIM = DIR_SDCARD + File.separator + Environment.DIRECTORY_DCIM;
    public static final String HOST = BuildConfig.API_HOST;

    public static final int TYPE_IMAGE_CORNOR_DEGREE = 30;     //本地图片，圆角度

    public static final long TIME_SECOND = 1000;                     // 秒
    public static final long TIME_MINUTE = 60 * TIME_SECOND;         // 分钟
    public static final long TIME_HOUR = 60 * TIME_MINUTE;           // 小时
    public static final long TIME_DAY = 24 * TIME_HOUR;              // 天
    public static final long TIME_WEEK = 7 * TIME_DAY;               // 周

    public static final int MAX_MOBILE_NUMBER_LENGTH = 11;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_CODE_LENGTH = 6;

    //添加设备扫描
    public static final int REQUEST_CODE_ADD_DEVICE = 100;

    public static final String DEVICE_USE_METHOD_URL = "http://www.showmac.cn";
    public static final String MINE_CUSTOMER_INFO = "mineCustomerInfo";
    public static final String MINE_ABOUT_INFO = "mineAboutInfo";
    public static final String USER_NICKNAME = "userNickname";
}
