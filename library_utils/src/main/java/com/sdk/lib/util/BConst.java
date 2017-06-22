package com.sdk.lib.util;

import android.os.Environment;

/**
 * 常量类（记录一些常用的常量 比如：配置信息中的key、文件路径等）
 */
public interface BConst {

	public final static int TYPE_DOWNLOAD_START = 1; // 下载开始
	public final static int TYPE_DOWNLOAD_PAUSE = 2; // 下载暂停
	public final static int TYPE_DOWNLOAD_PROGRESS = 3; // 下载进度更新
	public final static int TYPE_DOWNLOAD_COMPLETE = 4; // 下载完成
	public final static int TYPE_DOWNLOAD_DELETE = 5; // 删除下载
	public final static int TYPE_INSTALL_SUCCESS = 6; // 安装完成
	public final static int TYPE_INSTALL_NOT = 7; // 无辅助功能安装发送桌面
	
	/* sdcard根目录 */
	public static final String SDCARD = Environment.getExternalStorageDirectory().toString();
	public static final String DIR_DCIM = SDCARD + "/DCIM";

	public static final long TIME_SECOND = 1000; // 秒
	public static final long TIME_MINUTE = 60 * TIME_SECOND; // 分钟
	public static final long TIME_HOUR = 60 * TIME_MINUTE; // 小时
	public static final long TIME_DAY = 24 * TIME_HOUR; // 天
	public static final long TIME_WEEK = 7 * TIME_DAY; // 周
	public static final long TIME_MONTH = 30 * TIME_DAY; // 月
	
	public static final String AES_KEY = "9618433740985479"; // 加解密密钥
	public static final String secret = "7a48be950ac1746fc2b3ea781bd23daf";

	public static final int NPL = 1;
	public static final int KPL = 2;
	public static final int DPL = 3;
	public static final int ABWL = 4;
	public static final int UIWL = 5;
	public static final int BUILD = 6;
	public static final int WIDGETTIME = 7;
}