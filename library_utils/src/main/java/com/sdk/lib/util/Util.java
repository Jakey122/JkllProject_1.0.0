package com.sdk.lib.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.regex.PatternSyntaxException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 工具类
 */
public abstract class Util {

	public static String channelNo = "1";

	/**
	 * 是否是用户活跃时间
	 * 
	 * @return
	 */
	public static boolean isActiveTime() {
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTimeInMillis(System.currentTimeMillis());

		if (sCalendar.get(Calendar.HOUR_OF_DAY) > 6
				&& sCalendar.get(Calendar.HOUR_OF_DAY) < 22) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否是忙时
	 * @return
	 */
	public static boolean isBussyTime() {
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTimeInMillis(System.currentTimeMillis());

		if (sCalendar.get(Calendar.HOUR_OF_DAY) >= 7
				&& sCalendar.get(Calendar.HOUR_OF_DAY) <= 8
				|| sCalendar.get(Calendar.HOUR_OF_DAY) >= 17
				&& sCalendar.get(Calendar.HOUR_OF_DAY) <= 18) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前小时
	 * @return
	 */
	public static int getCurrentHour() {
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTimeInMillis(System.currentTimeMillis());
		int currentHour = sCalendar.get(Calendar.HOUR_OF_DAY);

		return currentHour;
	}

	/**
	 * 是否是刷新widget时间
	 * @return
	 */
	public static boolean isTimeUpdateWidget() {
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTimeInMillis(System.currentTimeMillis());
		int currentHour = sCalendar.get(Calendar.HOUR_OF_DAY);
		return currentHour == 2;
	}

	/**
	 * 打开设置界面
	 * 
	 * @param context
	 */
	public static void startSettings(Context context) {
		Intent sIntent = new Intent();
		sIntent.setAction(Settings.ACTION_SETTINGS);
		context.startActivity(sIntent);
	}

	/**
	 * 调用系统浏览器打开url
	 * @param context
	 * @param url
	 * @return
	 */
	public static boolean openUrl(Context context, String url) {
		try {
			Intent sIntent = new Intent(Intent.ACTION_VIEW);
			sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			sIntent.setData(Uri.parse(url));
			context.startActivity(sIntent);
			return true;
		} catch (ActivityNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 是否是url
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		try {
			String strPattern = "^(http://)?(www\\.)\\w+\\.[a-zA-Z]{2,3}$";
			return url.matches(strPattern);
		} catch (PatternSyntaxException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断sdcard是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardAvailable() {
		boolean sdcardAvailable = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			sdcardAvailable = true;
		}
		return sdcardAvailable;
	}

	/**
	 * 获取sdcard可用空间大小
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getSDCardAvailableSpace() {
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.toString());
		// 获取block的SIZE
		long blocSize = statFs.getBlockSize();
		// 己使用的Block的数量
		long availaBlock = statFs.getAvailableBlocks();
		return (availaBlock * blocSize);
	}

	/**
	 * Attempts to cancel execution of this task
	 * 
	 * @param task
	 * @param mayInterruptIfRunning
	 *            true if the thread executing this task should be interrupted;
	 *            otherwise, in-progress tasks are allowed to complete
	 */
	public static void cancelTask(AsyncTask<?, ?, ?> task,
			boolean mayInterruptIfRunning) {
		if (task == null)
			return;
		if (task.getStatus() != AsyncTask.Status.FINISHED) {
			task.cancel(mayInterruptIfRunning);
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
	public static int computeProgress(long min, long total) {
		return (int) (total == 0 ? 0 : min * 100 / total);
	}

	public static String getPackageFromDownPath(File file) {
		return file.getName().substring(file.getName().indexOf("_") + 1,
				file.getName().lastIndexOf("_"));
	}

	public static String getFileNameFromUri(String uri) {
		if (TextUtils.isEmpty(uri) || uri.equals("/"))
			return "/";
		return uri.substring(uri.lastIndexOf("/") + 1);
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

	public static ArrayList<Class<?>> listToArrayList(List<Class<?>> strArray) {
		ArrayList<Class<?>> strList = new ArrayList<Class<?>>();

		if (strArray != null) {
			for (int i = 0; i < strArray.size(); i++) {
				strList.add(strArray.get(i));
			}
		}
		return strList;
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

	public static byte[] logEncryptForTest(byte[] source) {
		int half = source.length / 2;
		byte[] buf1 = new byte[source.length - half];
		byte[] buf2 = new byte[half];
		int j = 0;
		int k = 0;
		for (int i = 0; i < source.length; i++) {
			if (i % 2 == 0) {
				buf1[j++] = source[i];
			} else {
				buf2[k++] = source[i];
			}
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			bos.write(buf1);
			bos.write(buf2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	// @SuppressLint("NewApi")
	public static boolean downloadApk(Context context, String url) {
		return openUrl(context, url);
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
	 * 判断当前手机是否有ROOT权限
	 * 
	 * @return
	 */
	public static boolean checkRoot() {
		boolean bool = false;

		try {
			if ((!new File("/system/bin/su").exists())
					&& (!new File("/system/xbin/su").exists())) {
				bool = false;
			} else {
				bool = true;
			}
		} catch (Exception e) {

		}
		return bool;
	}

	/**
	 * 判断当前手机内存大小
	 * 
	 * @return
	 */
	public static long getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			initial_memory = Long.valueOf(arrayOfString[1]) * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return initial_memory;// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 判断当前手机可用内存大小
	 * 
	 * @return
	 */
	public static long getAvailMem(Context c) {
		// 获得MemoryInfo对象
		MemoryInfo memoryInfo = new MemoryInfo();
		ActivityManager mActivityManager = (ActivityManager) c
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获得系统可用内存，保存在MemoryInfo对象上
		mActivityManager.getMemoryInfo(memoryInfo);
		long memSize = memoryInfo.availMem;
		return memSize;
	}

	/**
	 * 获得SD卡总大小
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getSDTotalSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return blockSize * totalBlocks;
	}

	/**
	 * 获得sd卡剩余容量，即可用大小
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getSDAvailableSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return blockSize * availableBlocks;
	}

	public static String getSDPath() {
		return BConst.SDCARD;
	}

	public static void sendMsgToLauncher(Context sContext, int type, String title,
			String message, String pkg) {
		Intent intent = new Intent(
				"com.mycheering.launcher.ACTION_SHOW_MESSAGE");
		intent.putExtra("type", type);
		intent.putExtra("package", pkg);
		intent.putExtra("title", title);
		intent.putExtra("message", message);
		sContext.getApplicationContext().sendBroadcast(intent);
	}
	
	public static void sendMsgToLauncher(Context sContext, int type, String pkg, int messageCount) {
		Intent intent = new Intent("com.mycheering.launcher.ACTION_SHOW_MESSAGE");
		intent.putExtra("type", type);
		intent.putExtra("package", pkg);
		intent.putExtra("title", "");
		intent.putExtra("message", "");
		intent.putExtra("messageCount", messageCount );
		sContext.getApplicationContext().sendBroadcast(intent);	
	}
	/**
	 * 下载发送桌面下载
	 * @param type
	 * @param pkg
	 * @param progress
	 * @param bmp
	 * @param imageUrl
	 * @param apkPath
	 */
	public static void sendDownloadMsgToLauncher(Context sContext, int type, String pkg, int progress, Bitmap bmp, String imageUrl, String apkPath) {
		sendDownloadMsgToLauncher(sContext, type, pkg, progress, bmp, imageUrl, apkPath, "", "");
	}
	
	/**
	 * 安装完成发送桌面
	 * @param type
	 * @param pkg
	 * @param progress
	 * @param bmp
	 * @param imageUrl
	 * @param apkPath
	 * @param crc32
	 * @param md5
	 */
	public static void sendDownloadMsgToLauncher(Context sContext, int type, String pkg, int progress, Bitmap bmp, String imageUrl, String apkPath, String crc32, String md5) {
		Intent intent = new Intent();
		intent.putExtra("pck", pkg);
		intent.putExtra("progress", progress);
		intent.putExtra("imageurl", imageUrl);
		intent.putExtra("hostpkg", sContext.getApplicationContext().getPackageName());
		String action = "";
		switch (type) {
		case BConst.TYPE_DOWNLOAD_START:
			action = "market.download.start";
			intent.putExtra("icon", bmp);
			break;
		case BConst.TYPE_DOWNLOAD_PAUSE:
			action = "market.download.pause";
			break;
		case BConst.TYPE_DOWNLOAD_PROGRESS:
			action = "market.download.update";
			break;
		case BConst.TYPE_DOWNLOAD_COMPLETE:
			action = "market.download.complete";
			intent.putExtra("apkpath", apkPath);
			break;
		case BConst.TYPE_DOWNLOAD_DELETE:
			action = "market.download.delete";
			break;
		case BConst.TYPE_INSTALL_SUCCESS:
			intent.putExtra("crc32", crc32);
			intent.putExtra("md5", md5);
			action = "market.install.complete";
			break;
		case BConst.TYPE_INSTALL_NOT:
			action = "market.install.fail";
			intent.putExtra("apkpath", apkPath);
			break;
		}
		intent.setAction(action);
		sContext.getApplicationContext().sendBroadcast(intent);
	}
	
	/**
	 * 安装完成发送桌面
	 * @param type
	 * @param pkg
	 * @param progress
	 * @param bmp
	 * @param imageUrl
	 * @param apkPath
	 * @param crc32
	 * @param md5
	 */
	public static void sendCannotInstallMsgToLauncher(Context sContext, int type, String pkg, int progress, Bitmap bmp, String imageUrl, String apkPath, String crc32, String md5) {
		Intent intent = new Intent();
		intent.putExtra("pck", pkg);
		intent.putExtra("progress", progress);
		intent.putExtra("imageurl", imageUrl);
		intent.putExtra("hostpkg", sContext.getApplicationContext().getPackageName());
		String action = "";
		switch (type) {
		case BConst.TYPE_DOWNLOAD_START:
			action = "market.download.start";
			intent.putExtra("icon", bmp);
			break;
		case BConst.TYPE_DOWNLOAD_PAUSE:
			action = "market.download.pause";
			break;
		case BConst.TYPE_DOWNLOAD_PROGRESS:
			action = "market.download.update";
			break;
		case BConst.TYPE_DOWNLOAD_COMPLETE:
			action = "market.download.complete";
			intent.putExtra("apkpath", apkPath);
			break;
		case BConst.TYPE_DOWNLOAD_DELETE:
			action = "market.download.delete";
			break;
		case BConst.TYPE_INSTALL_SUCCESS:
			intent.putExtra("crc32", crc32);
			intent.putExtra("md5", md5);
			action = "market.install.complete";
			break;
		}
		intent.setAction(action);
		sContext.getApplicationContext().sendBroadcast(intent);
	}

	public static String getDIMPath() {
		return getSDPath() + "/DCIM" + "/cheering";
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void copyContent(Context sContext, String content) {
		ClipboardManager cmb = (ClipboardManager) sContext.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	public static int getRandomInt(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
	
	public static int getImageRound(Context sContext) {
		return UiUtil.isSmallScreen(sContext) ? 18 : 24;
	}

	public static boolean isMarketApp(Context sContext, String pkg) {
		PackageManager pm = sContext.getApplicationContext().getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory(Intent.CATEGORY_APP_MARKET);
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.MATCH_DEFAULT_ONLY);

		for (ResolveInfo res : resolveInfos) {
			String spkg = res.resolvePackageName;
			if (spkg == null)
				return false;

			if (res.resolvePackageName.equals(pkg))
				return true;
		}
		return false;
	}

	public static boolean isProcessExist(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.processName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return "";
	}

	public static Properties loadConfig(Context context) {
		Properties properties = new Properties();
		try {
			InputStream open = context.getAssets().open(
					"config/config.properties");
			properties.load(open);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static void saveConfig(Context context, String value) {
		try {
			FileOutputStream outputStream = context.openFileOutput("config",
					Context.MODE_PRIVATE);
			outputStream.write(value.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean openConfig(Context context) {
		try {
			FileInputStream inputStream = context.openFileInput("config");
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			byte[] data = outStream.toByteArray();
			String name = new String(data);
			return !TextUtils.isEmpty(name) && name.equals("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String[] loadConfigWords(Context context) {
		try {
			try {
				InputStream inputStream = context.getAssets().open("config/words.properties");
				StringBuffer sb = new StringBuffer();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
					sb.append(new String(buffer, 0, len));
				}
				inputStream.close();
				String string = sb.toString();
				if(string.contains(","))
					return string.split(",");
				else
					return new String[]{string};
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isArrayContainsString(String key, String[] strArray, boolean ignoreCase) {
		if (strArray == null || strArray.length == 0) return false;
		if (TextUtils.isEmpty(key)) return false;
		if (ignoreCase) key = key.toLowerCase(Locale.getDefault());
		
		for (String string : strArray) {
			if (ignoreCase) {
				if (key.equals(string.toLowerCase(Locale.getDefault()))) return true;
			} else {
				if (key.equals(string)) return true;				
			}
		}
		return false;
	}
	
	public static boolean isAppRunning(Context context, String pkg){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if(Build.VERSION.SDK_INT >= 21){
			return getRunningService(context, pkg);
		} else {
			List<RunningAppProcessInfo> apps = am.getRunningAppProcesses();
			for (int i = 0; i < apps.size(); i++) {
				String appPackStr = apps.get(i).processName.toString();
				if(appPackStr.startsWith(pkg)) return true;
			}
		}
		return false;
	}
	
	public static boolean getRunningService(Context context, String pkg) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo rsi : am
				.getRunningServices(Integer.MAX_VALUE)) {
			String pkgName = rsi.service.getPackageName();
			if(!TextUtils.isEmpty(pkgName) && pkgName.startsWith(pkg)) return true;
		}
		return false;
	}
	
	public static boolean activeService(Context context, String serviceName) {
		try{
			PackageManager pm = context.getPackageManager();
			List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_SERVICES);
			if(installedPackages != null){
				for (PackageInfo packageInfo : installedPackages) {
						if(packageInfo != null) {
							ServiceInfo[] services = packageInfo.services;
							if(services != null && services.length > 0){
								for (ServiceInfo serviceInfo : services) {
									if(serviceInfo.exported && serviceInfo.name.contains(serviceName)){
										Intent intent=new Intent();
										intent.setComponent(new ComponentName(packageInfo.packageName, serviceInfo.name));
										context.startService(intent);
									}
								}
							}
						}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static int R_layout(Context context, String name){
		return getResourceId(context, "layout", name);
	}
	
	public static int R_id(Context context, String name){
		return getResourceId(context, "id", name);
	}
	
	public static int R_string(Context context, String name){
		return getResourceId(context, "string", name);
	}
	
	public static int R_integer(Context context, String name){
		return getResourceId(context, "integer", name);
	}
	
	public static int R_drawable(Context context, String name){
		return getResourceId(context, "mipmap", name);
	}
	
	public static int R_array(Context context, String name){
		return getResourceId(context, "array", name);
	}
	
	public static int getResourceId(Context context, String className, String name) {
		return context.getResources().getIdentifier(name,  
				className, context.getPackageName());  
		
        /*String packageName = context.getApplicationContext().getPackageName();
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return id;*/
    }
}
