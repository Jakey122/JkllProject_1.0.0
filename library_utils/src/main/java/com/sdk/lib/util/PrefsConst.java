package com.sdk.lib.util;

/**
 * pref文件缓存key
 * @author Xsl
 *
 */
public interface PrefsConst {
	
	public static final String PREF_DEVICE_NAME = "deviceName"; //设备机型
	public static final String PREF_UID = "uid"; //用户uid
	public static final String PREF_LBS_CITY = "lbscity"; //市
	public static final String PREF_LBS_PROVINCE = "lbsprovince"; //省
	public static final String PREF_LBS_DISTRICT = "lbsdistrict"; //县
	public static final String PREF_LAST_GETAUTOINSTALLCONFIG_TIME = "lastGetAutoInstallConfigTime"; //辅助安装获取时间
	public static final String PREF_AUTO_INSTALL_CONFIG = "aotuInstallConfig"; //辅助安装配置
	public static final String PREF_LAST_SHOW_OPEN_ACCESSIBILITY_INSTALL = "lastShowOpenAccessibilityInstall"; //辅助安装框弹出时间
	public static final String PREF_SHOW_ACCESSIBILITY_COUNT = "showAccessibilityCount"; //辅助安装弹出次数
}
