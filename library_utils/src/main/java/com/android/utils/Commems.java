package com.android.utils;

public class Commems {

	public static int width = 0;
	public static int height = 0;

	public static long app_stop_time = 15000 * 60 * 1000; // 15分钟

	public static String PHONE_RESULT_BEGIN = "/data/local/tmp/PHONE_RESULT_BEGIN"; // 信息地址
	
	// wifi本地文件信息，包含wifi名称： ssid 和密码 以#分割
	public static String WIFI_STATE = "/data/local/tmp/WIFI_STATE"; 

	// 本地标记地址，如果内包含数字，读取显示到屏幕端口号
	public static String PHONE_FLAGE = "/data/local/tmp/PHONE_FLAGE";
	public static String APPLIST_CN = "/data/local/tmp/APPLIST_CN"; // 本地标记，如果时间改变就重写list
	public static String PHONE_MEID= "/data/local/tmp/PHONE_MEID"; // 本地imei信息文件

}
