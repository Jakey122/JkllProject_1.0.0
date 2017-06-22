package com.android.kd.phone;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.android.utils.Commems;
import com.google.kd.utils.FileUtils;
import com.google.kd.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

//import android.os.Looper;

public class PhoneHelper {

	private static final String SPLIT = ",";
	static Pattern pattern_imei = Pattern.compile("^[0-9A-Fa-f]{13,18}+$");

	public static String getPhoneInfo2(Context context) {

		String imeiSIM1 = "";
		String imeiSIM2 = "";

		HashSet<String> imeis = new HashSet<String>();
		NewIMEIGet newimei = new NewIMEIGet(context);

		String imei_Stand = newimei.getIMEI_Stand();
		if (null != imei_Stand) {
			imeis.add(imei_Stand);
		}

		newimei.getIMEIOtherAll(context, imeis);

		String[] imeisarray = set2array(imeis);
		imeiSIM1 = imeisarray[0];
		imeiSIM2 = imeisarray[1];

		// 2015-04-2014:31 获取MEID
		String meid = "";
		meid = getMEID(context);

		Log.e("imei", imeiSIM1 + SPLIT + imeiSIM2 + SPLIT + meid);

		String result = "";

		if (!TextUtils.isEmpty(imeiSIM1)) {
			result += imeiSIM1;
		}

		if (!TextUtils.isEmpty(imeiSIM2)) {
			result += TextUtils.isEmpty(result) ? imeiSIM2 : SPLIT + imeiSIM2;
		}

		if (!TextUtils.isEmpty(meid)) {
			result += TextUtils.isEmpty(result) ? meid : SPLIT + meid;
		}

		if (TextUtils.isEmpty(result)) {
			result = "000000000000000";
		}

		return result;
	}

	public static String getPhoneInfo(Context context) {

		FileUtils.nulls(Commems.PHONE_RESULT_BEGIN);

		// 2014-11-27-修改
		String imeiSIM1 = "";
		String imeiSIM2 = "";

		HashSet<String> imeis = new HashSet<String>();
		NewIMEIGet newimei = new NewIMEIGet(context);

		String imei_Stand = newimei.getIMEI_Stand();
		if (null != imei_Stand) {
			imeis.add(imei_Stand);
		}

		newimei.getIMEIOtherAll(context, imeis);

		String[] imeisarray = set2array(imeis);
		imeiSIM1 = imeisarray[0];
		imeiSIM2 = imeisarray[1];

		// 2015-04-2014:31 获取MEID
		String meid = getMEID(context);

		LogUtils.d("wyy", "meid:" + meid);

		StatFs systemFs = new StatFs("/system");
		long size_system = systemFs.getBlockSize();
		long system_avail = size_system * systemFs.getAvailableBlocks();
		long system_total = size_system * systemFs.getBlockCount();

		StatFs localStatFs = new StatFs("/data");
		long size = localStatFs.getBlockSize();
		long user_avail = size * localStatFs.getAvailableBlocks();
		long user_total = size * localStatFs.getBlockCount();

		long ram_avail = MemInfo.getmem_UNUSED(context.getApplicationContext());
		long ram_total = MemInfo.getmem_TOLAL();

		String mac = "mac_is_null";
		try {
			WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			mac = info.getMacAddress();
		} catch (Exception e) {

			LogUtils.e("wyy", "mac:" + e.toString());
		}

		String reString = "PHONE_RESULT_BEGIN" + "(IMEI:" + imeiSIM1 + ")" + "(IMEI2:" + imeiSIM2 + ")" + "(MODEL:"
				+ Build.MODEL + ")" + "MEID:" + meid + "(OS:" + Build.VERSION.RELEASE + ")" + "(BASEBAND:"
				+ getBaseBand() + ")" + "(BRAND:" + Build.BRAND + ")" + "(MAC:" + mac + ")" + "(SYSTEM_TOTAL:"
				+ system_total + ")" + "(SYSTEM_AVAIL:" + system_avail + ")" + "(USER_TOTAL:" + user_total + ")"
				+ "(USER_AVAIL:" + user_avail + ")" + "(RAM_TOTAL:" + ram_total + ")" + "(RAM_AVAIL:" + ram_avail + ")"
				+ "PHONE_RESULT_END";

		return (reString == null ? "" : reString);
	}

	public static String getMEID(Context context) {
		String hwMEID = hwMEID();

		if (TextUtils.isEmpty(hwMEID) || hwMEID.length() <= 3) {
			hwMEID = GetViVoMEID();
		}

		if (TextUtils.isEmpty(hwMEID)|| hwMEID.length() <= 3) {
			hwMEID = getMeid_Oppo(context);
		}

		if (TextUtils.isEmpty(hwMEID)|| hwMEID.length() <= 3) {
			hwMEID = "";
		}
		return hwMEID;
	}

	private static String hwMEID() {
		String MEID = "";
		try {
			Class<?> HWNVFuncation = Class.forName("com.huawei.android.hwnv.HWNVFuncation");
			Method getNVMEID = HWNVFuncation.getMethod("getNVMEID");
			MEID = (String) getNVMEID.invoke(null, null);

		} catch (Exception e) {
			LogUtils.d("wyy", "hwMEID:" + e.toString());
		}
		return MEID;
	}

	private static String GetViVoMEID() {
		String MEID = "";
		if (Build.BRAND.equals("vivo")) {
			try {
				Class<?> phonegetService = Class.forName("android.os.ServiceManager");
				Method getService = phonegetService.getMethod("getService", String.class);
				android.os.IBinder phonebinder = (android.os.IBinder) getService.invoke(null, "phone");
				Class clazz = Class.forName("com.android.internal.telephony.ITelephony$Stub");
				Method method = clazz.getMethod("asInterface", android.os.IBinder.class);
				Object invoke = method.invoke(null, phonebinder);
				try {
					MEID = invoke.getClass().getMethod("getMeid").invoke(invoke).toString();
				} catch (Exception e) {
					e.toString();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("wyy", "vivo:" + MEID);
		}
		return MEID;
	}

	private static String getMeid_Oppo(Context context) {
		Looper mainLooper = Looper.getMainLooper();
		String result = "";
		try {
			Class msim = Class.forName("com.android.internal.telephony.cdma.CDMAPhone");
			if (msim != null) {
				Field mMeid = msim.getDeclaredField("mMeid");
				if (mMeid != null) {
					mMeid.setAccessible(true);
					Object obj = context.getApplicationContext().getSystemService("phone");
					Object aaa = mMeid.get(obj);
					result = aaa.toString();
					Log.d("wyy", "mMeid:" + mMeid);
					if(TextUtils.isEmpty(result)) result = "";
				}
			}
			return (String) result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将set数据整理，取前两位
	 * 
	 * @param imeis
	 */
	private static String[] set2array(HashSet<String> imeis) {
		final List<String> imiesList = new ArrayList<String>();
		for (final String imei : imeis) {
			if (isIMEI(imei)) {
				imiesList.add(imei);
			}
		}

		Collections.sort(imiesList);
		Collections.reverse(imiesList); // 倒序
		// 如果包含字母开头，则字母在前

		LogUtils.d("wyy", "imiesList:" + imiesList.size());
		for (int i = 0; i < imiesList.size(); i++) {
			LogUtils.d("wyy", "imei[" + i + "]:" + imiesList.get(i));
		}

		String[] imiesArray = new String[2];

		if (imiesList.size() == 0) {
			imiesArray[0] = "";
			imiesArray[1] = "";
		} else if (imiesList.size() == 1) {
			imiesArray[0] = imiesList.get(0);
			imiesArray[1] = "";
		} else if (imiesList.size() == 2) {
			imiesArray[0] = imiesList.get(0);
			imiesArray[1] = imiesList.get(1);
		} else if (imiesList.size() == 3) {
			imiesArray[0] = imiesList.get(1);
			imiesArray[1] = imiesList.get(2);
		}

		return imiesArray;
	}

	/**
	 * 2015-05-06 13:17 增加对IMEI判断
	 * 
	 * @param imei
	 * @return
	 */
	public static boolean isIMEI(String imei) {
		if (imei == null || imei.length() == 0)
			return false;
		if (pattern_imei.matcher(imei).matches()) {
			if (imei.indexOf("000000000") == -1 && imei.indexOf("111111111") == -1 && imei.indexOf("222222222") == -1
					&& imei.indexOf("333333333") == -1 && imei.indexOf("444444444") == -1
					&& imei.indexOf("555555555") == -1 && imei.indexOf("666666666") == -1
					&& imei.indexOf("777777777") == -1 && imei.indexOf("888888888") == -1
					&& imei.indexOf("999999999") == -1) {
				return true;
			}
		}
		return false;
	}

	// 以下是获取设备信息！ getPhoneInfo 调用
	private static String getBaseBand() {
		try {
			Class cl = Class.forName("android.os.SystemProperties");
			Object invoker = cl.newInstance();
			Method m = cl.getMethod("get", new Class[] { String.class, String.class });
			Object result = m.invoke(invoker, new Object[] { "gsm.version.baseband", "no message" });
			System.out.println(">>>>>>><<<<<<<" + (String) result);
			return (String) result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "null";
	}

	/*public static String getLuncher() {

		LogUtils.d("wyy", "---getLuncher---start----");

		// 获取launcher信息
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_HOME);
		PackageManager manager = AppContext.getInstance().getApplicationContext().getPackageManager();
		List<ResolveInfo> appList = manager.queryIntentActivities(intent, 0);

		StringBuilder sb = new StringBuilder();

		String rn = "\r\n";
		sb.append("size:[" + appList.size() + "]" + rn);

		for (int i = 0; i < appList.size(); i++) {
			ResolveInfo inf = appList.get(i);
			int matchValue = inf.match;
			String nameValue = inf.activityInfo.name;
			String packageNameValue = inf.activityInfo.packageName;

			sb.append("---------------" + i + "----------");
			sb.append("matchValue:" + matchValue + rn);
			sb.append("nameValue:" + nameValue + rn);
			sb.append("packaName:" + packageNameValue + rn);

		}

		// 写 xml ?
		String xmlStr = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.setXmlVersion("1.0");

			Element info = document.createElement("info");
			document.appendChild(info);

			for (int i = 0; i < appList.size(); i++) {
				ResolveInfo inf = appList.get(i);
				int matchValue = inf.match;
				String nameValue = inf.activityInfo.name;
				String packageNameValue = inf.activityInfo.packageName;

				Element item = document.createElement("item");

				Element match = document.createElement("match");
				match.setTextContent(matchValue + "");
				Element name = document.createElement("name");
				name.setTextContent(nameValue);
				Element packageName = document.createElement("packageName");
				packageName.setTextContent(packageNameValue);

				item.appendChild(match);
				item.appendChild(name);
				item.appendChild(packageName);

				info.appendChild(item);
			}

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transFormer = transFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			// export string
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			transFormer.transform(domSource, new StreamResult(bos));
			xmlStr = bos.toString();

		} catch (Exception e) {
			LogUtils.d("wyy", "Exception:" + e.toString());
		}

		FileUtils.writeStringToSdcard(xmlStr, "/data/local/tmp/match");

		String[] cmd2 = { "chmod", "777", "/data/local/tmp/match" };

		try {
			runCommandS(cmd2, Environment.getExternalStorageDirectory().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.d("wyy", "Exception2:" + e.toString());
		}

		LogUtils.d("wyy", "xmlStr:" + xmlStr);

		return xmlStr;

	}

	public static synchronized String runCommandS(String[] cmd, String workdirectory) throws IOException {
		StringBuffer result = new StringBuffer();
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			// InputStream in = null;
			// 设置一个路径（绝对路径了就不一定需要）
			if (workdirectory != null) {
				// 设置工作目录（同上）
				builder.directory(new File(workdirectory));
				// 合并标准错误和标准输出
				builder.redirectErrorStream(true);
				// 启动一个新进程
				Process process = builder.start();

				// 读取进程标准输出流
				// in = process.getInputStream();
				// byte[] re = new byte[1024];
				// while (in.read(re) != -1) {
				// if (result.length() > 1024 * 1024) {
				// result.setLength(0);
				// }
				// result = result.append(new String(re));
				// break;
				// }
			}
			// 关闭输入流
			// if (in != null) {
			// in.close();
			// }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}*/
}
