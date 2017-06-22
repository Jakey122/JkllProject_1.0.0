package com.android.kd.phone;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.utils.Commems;
import com.google.kd.utils.FileUtils;
import com.google.kd.utils.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.regex.Pattern;

public class NewIMEIGet {

	static Pattern pattern_imei = Pattern.compile("^[0-9A-Fa-f]{13,18}+$");

	Context context;

	public NewIMEIGet(Context context) {
		super();
		this.context = context;
	}

	/***
	 * 获取标准接口的IMEI
	 * 
	 * @param context
	 * @return String OR null
	 */
	public String getIMEI_Stand() {
		
		Log.e("wyy", "getIMEI_Stand:"+(context == null));

		TelephonyManager manager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);

		if (null != manager) {
			// check if has the permission
			String packageName = context.getApplicationContext().getPackageName();
			boolean check = true;
			// check = checkpermission.CheckPermission(context,
			// Manifest.permission.READ_PHONE_STATE)

			if (check) {
				String imei = manager.getDeviceId();
				return manager.getDeviceId();
			} else {
				return "";
			}
		}
		return "";
	}

	/**
	 * 收集获取MEID 去除重复， 如果获取到哦啊meid，则保持imei+meid
	 * 
	 * @param context
	 * @param imeis
	 */
	public void getIMEIOtherAll(Context context, HashSet<String> imeis) {

		String model = Build.MODEL; //机型

		String brand = Build.BRAND; //厂商
		brand = brand.toLowerCase().trim();

		if (brand.contains("huawei") | brand.contains("honor")) {
			getIMEI2_LenovoA3580(context, imeis);
			if (imeis.size() > 0) {
				return;
			}
		}

		try {
			getIMEI_MtkDoubleSim(context, imeis);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			getIMEI_MtkSecondDoubleSim(context, imeis);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			getIMEI_QualcommDoubleSim(context, imeis);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			getIMEI_SpreadDoubleSim(context, imeis);
		} catch (Exception e) {
			// TODO: handle exception
			e.toString();
		}

		// 2015-04-23 11:11 增加meid
		try {
			getMeid_Huawei(context, imeis);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// 2015-05-05 14:30 HUAWEI C8817E 获取IMEI
		try {
			getIMEI_Huawei(context, imeis);
		} catch (Exception e) {
			LogUtils.d("wtyy", e.toString());
		}

		try {
			getMeid_Vivod(context, imeis);
		} catch (Exception e) {
			LogUtils.d("wtyy", e.toString());
		}

		try {

			getIMEI2_SM7000(context, imeis);

		} catch (Exception e) {
			LogUtils.d("wyy", e.toString());
		}

		try {
			
			if (model.contains("WP-S") || model.contains("D5012T") || model.contains("K-Touch K3")) {
				//2016-01-15 获取imei为无效imei
				//不做任何操作
			}else {
				
				getIMEI2_LenovoA3580(context, imeis);
				
			}


		} catch (Exception e) {
			LogUtils.d("wyy", e.toString());
		}

		try {

			getIMEI2_SMG3812(context, imeis);

		} catch (Exception e) {

			LogUtils.d("wyy", e.toString());
		}

		try {
			getMeid_ByLocal(context, imeis);
		} catch (Exception e) {
			LogUtils.d("wtyy", e.toString());
		}

	}

	/**
	 * 
	 * @param context
	 * @param imeis
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static  String getIMEI_CoolpadY89d(Context context, HashSet<String> imeis) throws Exception {

		String Imei = "";

		try {
			
			Class sysProClzz = Class.forName("com.yulong.android.telephony.CPTelephonyManager");
			sysProClzz.getDeclaredConstructor().setAccessible(true);
			
			Method[] declaredMethods = sysProClzz.getDeclaredMethods();
			
			for (int i = 0; i < declaredMethods.length; i++) {
				Method delm= declaredMethods[i];
				StringBuffer sb = new StringBuffer();
				
				sb.append("index:"+i+":"+delm.getName()+" "+delm.getDeclaringClass().getSimpleName()+" ");
				String declaringname = delm.getDeclaringClass().getSimpleName();
				
				sb.append("Parameter:[");
				
				Class<?>[] types = delm.getParameterTypes();
				for (int j = 0; j < types.length; j++) {
					Class typclass =types[j];
					String name = typclass.getSimpleName();
					sb.append(" "+name);
				}
				
				sb.append("]");
				
				Log.d("wyy", ""+sb.toString());
			}
			
			
			
			
			
			
//			Method methodGet = sysProClzz.getDeclaredMethod("getYLDeviceId", Integer.TYPE, Integer.TYPE);
//			if (methodGet != null) {
//				methodGet.setAccessible(true);
//				
//				
//				String simpleName = methodGet.getDeclaringClass().getSimpleName();
//				Log.d("wyy", "simpleName:" + simpleName); // 1
//				
//				String invoke = (String) methodGet.invoke(sysProClzz.newInstance(), Integer.valueOf(1), Integer.valueOf(2));
//				Log.d("wyy", "lteOnCdmaDevice:" + invoke); // 1
//				
//				
////				if (invoke.equals("1")) {
////					Method getNVIMEI = Class.forName("com.huawei.android.hwnv.HWNVFuncation").getMethod("getNVIMEI", null);
////					if (getNVIMEI == null) {
////						return Imei;
////					} else {
////						getNVIMEI.setAccessible(true);
////						Imei = (String) getNVIMEI.invoke(null, null);
////						Log.d("wyy", "Imei:" + Imei); // 1
////
////					}
////				}
//
//			}
			


		} catch (Exception e) {
			e.printStackTrace();
			Log.e("wyy", "CoolpadY89:"+e.toString());
		}


		return Imei;

	}

	/**
	 * LenovoA3580 获取imei2 方式 2015-07-29 增加
	 * 
	 * @param context
	 * @param imeis
	 *            保存imei的map
	 */
	private void getIMEI2_LenovoA3580(Context context, HashSet<String> imeis) {

		try {
			// LenovoA3580 获取到imei2
			TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService("phone");
			int type = telephonymanager.getPhoneType() == 1 ? 2131297790 : 2131297791;
			Class<? extends TelephonyManager> telephonymanagerClazz = telephonymanager.getClass();

			Method getDeviceId = telephonymanagerClazz.getMethod("getDeviceId", int.class);
			if (getDeviceId != null) {

				String meid_0 = (String) getDeviceId.invoke(telephonymanager, 0);

				imeis.add(meid_0); // 这个为meid
				
				LogUtils.d("wyy", "meid_0:"+meid_0);

				String imei_1 = (String) getDeviceId.invoke(telephonymanager, 1);

				imeis.add(imei_1); // imei
				
				LogUtils.d("wyy", "imei_1:"+imei_1);

				String imei_2 = (String) getDeviceId.invoke(telephonymanager, 2);

				imeis.add(imei_2); // 测试没有2，以防万一
				
				LogUtils.d("wyy", "imei_2:"+imei_2);
			}

		} catch (Exception e) {
			LogUtils.d("wyy", e.toString());
		}

	}

	/**
	 * 三星 SMG3812 获取IMEI2 方式
	 * 
	 * @param context
	 * @param imeis
	 */
	private void getIMEI2_SMG3812(Context context, HashSet<String> imeis) {

		try {

			TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
			Class tpm = Class.forName("android.telephony.TelephonyManager");

			Method method_getDeviceIdDs = tpm.getMethod("getDeviceIdDs", int.class);
			if (method_getDeviceIdDs != null) {
				String IMEI1 = (String) method_getDeviceIdDs.invoke(tm, 0);
				String IMEI2 = (String) method_getDeviceIdDs.invoke(tm, 1);

				LogUtils.d("wyy", "SM_G3812_IMEI1:" + IMEI1);
				LogUtils.d("wyy", "SM_G3812_IMEI2:" + IMEI2);

				if (isIMEI(IMEI1)) {
					imeis.add(IMEI1);
				}

				if (isIMEI(IMEI2)) {
					imeis.add(IMEI2);
				}
			}

		} catch (Exception e) {

			LogUtils.e("wyy", "SM_G3812:" + e.toString());
		}

	}

	/**
	 * 2015-05-1816:53 添加 三星A7000 获取imei2 方式
	 * 
	 * @param context
	 * @param imeis
	 */
	private void getIMEI2_SM7000(Context context, HashSet<String> imeis) {

		try {
			// 三星A7000 获取到imei2
			TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService("phone2");
			Class sysClazz = context.getSystemService("phone2").getClass();

			Method method = sysClazz.getMethod("getImeiInCDMAGSMPhone", null);
			if (method != null) {
				String IMEI2 = method.invoke(telephonymanager, null).toString();
				LogUtils.d("wyy", "IMEI2:" + IMEI2);
				if (isIMEI(IMEI2)) {
					imeis.add(IMEI2);
				}

			}

		} catch (Exception e) {
			LogUtils.d("wyy", e.toString());
		}

	}

	/**
	 * 华为C8817E 获取IMEI 方式
	 * 
	 * @param context
	 * @param imeis
	 */
	private void getIMEI_Huawei(Context context, HashSet<String> imeis) {

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");

		Class sysClazz = context.getSystemService("phone").getClass();

		try {
			Method type = sysClazz.getMethod("getCurrentPhoneType", null);
			if (type != null) {
				int correnttype = Integer.parseInt(String.valueOf(type.invoke(telephonyManager, null)));
				Log.d("wyy", "CurrentPhoneType:" + correnttype);

				if (correnttype == 1) {
					// TODO 未分析
				} else if (correnttype == 2) {

					String cmdaImei = getCDMAIMEI();
					if (cmdaImei != null && cmdaImei.length() > 0) {
						imeis.add(cmdaImei);
					}
				}

			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	private String getCDMAIMEI() {

		String Imei = "";

		try {
			Class sysProClzz = Class.forName("android.os.SystemProperties");
			Method methodGet = sysProClzz.getMethod("get", String.class);
			if (methodGet != null) {
				String invoke = (String) methodGet.invoke(sysProClzz.newInstance(), "telephony.lteOnCdmaDevice");
				Log.d("wyy", "lteOnCdmaDevice:" + invoke); // 1
				if (invoke.equals("1")) {

					Method getNVIMEI = Class.forName("com.huawei.android.hwnv.HWNVFuncation").getMethod("getNVIMEI", null);
					if (getNVIMEI == null) {
						return Imei;
					} else {
						getNVIMEI.setAccessible(true);
						Imei = (String) getNVIMEI.invoke(null, null);
						Log.d("wyy", "Imei:" + Imei); // 1

					}
				}

			}

		} catch (Exception e) {
			Log.e("wyy", "getCDMAIMEI:" + e.toString());
		}

		return Imei;
	}

	/**
	 * 装机过程会本地记录，如果有此文件，读取 读取本地为文件获取meid
	 * 
	 * @param context2
	 * @param imeis
	 */
	private void getMeid_ByLocal(Context context2, HashSet<String> imeis) {

		String meid = FileUtils.readFileByReader(Commems.PHONE_MEID);

		if (meid != null && meid.length() > 0) {

			if (imeis != null && !imeis.contains(meid)) {
				if (meid != null && meid.length() > 0 && meid.indexOf("0000000000") == -1) {
					String[] split = meid.split(",");
					for (int i = 0; i < split.length; i++) {
						if (split[i] != null && split[i].length() > 0) {
							LogUtils.d("wyy", "getMeid_split:" + split[i]);
							imeis.add(split[i]);
						}
					}

				}
			}

		}

		LogUtils.d("wyy", "getMeid_ByLocal:" + meid);

	}

	/**
	 * 使用vivo方式获取meid
	 * 
	 * @param context2
	 * @param imeis
	 */
	private void getMeid_Vivod(Context context2, HashSet<String> imeis) {
		if (Build.BRAND.equals("vivo")) {
			String meid = "";
			try {
				// vivo手机发现此方式，测试联想某机型一样可以获取到
				// String model = (String) RefelctionUtils.invoke(null,
				// RefelctionUtils.getMethod(RefelctionUtils.getClass("android.os.SystemProperties"),
				// "get", String.class), "ro.product.model.bbk");
				// Log.d("wyy", "X3V model:" + model);
	
				Class<?> phonegetService = Class.forName("android.os.ServiceManager");
				Method getService = phonegetService.getMethod("getService", String.class);
				android.os.IBinder phonebinder = (android.os.IBinder) getService.invoke(null, "phone");
				Class clazz = Class.forName("com.android.internal.telephony.ITelephony$Stub");
				Method method = clazz.getMethod("asInterface", android.os.IBinder.class);
				Object invoke = method.invoke(null, phonebinder);
	
				try {
					meid = invoke.getClass().getMethod("getMeid").invoke(invoke).toString();
				} catch (Exception e) {
					e.toString();
				}
	
			} catch (Exception e) {
				LogUtils.d("wyy", e.toString());
			}
	
			if (isIMEI(meid)) {
				imeis.add(meid);
			}
	
			LogUtils.d("wyy", "getMeid_Vivod:" + meid);
		}
	}

	/**
	 * 常规方式获取Meid(huawei方式)
	 * 
	 * @param context2
	 * @param imeis
	 */
	private void getMeid_Huawei(Context context2, HashSet<String> imeis) {

		String meid = "";
		try {
			Class<?> HWNVFuncation = Class.forName("com.huawei.android.hwnv.HWNVFuncation");
			Method getNVMEID = HWNVFuncation.getMethod("getNVMEID");
			meid = (String) getNVMEID.invoke(null, null);
		} catch (Exception e) {
			LogUtils.d("wyy", "hwMEID:" + e.toString());
		}

		if (imeis != null && !imeis.contains(meid)) {
			if (meid != null && meid.length() > 0 && meid.indexOf("0000000000") == -1) {
				imeis.add(meid);
			}
		}

		LogUtils.d("wyy", "getMeid_Huawei:" + meid);

	}

	/***
	 * 
	 * 获取双卡imei
	 * 
	 * @param context
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws NumberFormatException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void getIMEI_MtkDoubleSim(Context context, HashSet<String> imeis) throws ClassNotFoundException, NoSuchFieldException, NumberFormatException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		Class<?> c = Class.forName("com.android.internal.telephony.Phone");
		int simId_1, simId_2;
		simId_1 = 0;
		simId_2 = 1;
		try {
			java.lang.reflect.Field fields1 = c.getField("GEMINI_SIM_1");
			fields1.setAccessible(true);
			simId_1 = Integer.parseInt(fields1.get(null).toString());
			java.lang.reflect.Field fields2 = c.getField("GEMINI_SIM_2");
			fields2.setAccessible(true);
			simId_2 = Integer.parseInt(fields2.get(null).toString());
		} catch (Exception ex) {
			simId_1 = 0;
			simId_2 = 1;
		}

		Method m1 = TelephonyManager.class.getDeclaredMethod("getDeviceIdGemini", int.class);
		String imei_1 = ((String) m1.invoke(tm, simId_1)).trim();
		String imei_2 = ((String) m1.invoke(tm, simId_2)).trim();
		if (!imeis.contains(imei_1)) {
			if (imei_1 != null && imei_1.indexOf("0000000000") == -1) {
				imeis.add(imei_1);
			}
			if (imei_1 != null && imei_1.indexOf("111111111") == -1) {
				imeis.add(imei_1);
			}
		}
		if (!imeis.contains(imei_2)) {
			if (imei_2 != null && imei_2.indexOf("0000000000") == -1) {
				imeis.add(imei_2);
			}
			if (imei_2 != null && imei_2.indexOf("111111111") == -1) {
				imeis.add(imei_2);
			}
		}

	}

	/***
	 * 
	 * @param context
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws NumberFormatException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void getIMEI_MtkSecondDoubleSim(Context context, HashSet<String> imeis) throws ClassNotFoundException, NoSuchFieldException, NumberFormatException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		Class<?> c = Class.forName("com.android.internal.telephony.Phone");

		int simId_1, simId_2;
		try {
			java.lang.reflect.Field fields1 = c.getField("GEMINI_SIM_1");
			fields1.setAccessible(true);
			simId_1 = Integer.parseInt(fields1.get(null).toString());
			java.lang.reflect.Field fields2 = c.getField("GEMINI_SIM_2");
			fields2.setAccessible(true);
			simId_2 = Integer.parseInt(fields2.get(null).toString());
		} catch (Exception ex) {
			simId_1 = 0;
			simId_2 = 1;
		}
		Method mx = TelephonyManager.class.getMethod("getDefault", int.class);
		TelephonyManager tm1 = (TelephonyManager) mx.invoke(tm, simId_1);
		TelephonyManager tm2 = (TelephonyManager) mx.invoke(tm, simId_2);
		String imei_1 = (tm1.getDeviceId()).trim();
		String imei_2 = (tm2.getDeviceId()).trim();
		if (!imeis.contains(imei_1)) {
			if (imei_1 != null && imei_1.indexOf("0000000000") == -1)
				imeis.add(imei_1);
		}
		if (!imeis.contains(imei_2)) {
			if (imei_2 != null && imei_2.indexOf("0000000000") == -1)
				imeis.add(imei_2);
		}

	}

	/***
	 * 
	 * @param context
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void getIMEI_SpreadDoubleSim(final Context context, final HashSet<String> imeis) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		try{
			Class<?> c = Class.forName("com.android.internal.telephony.PhoneFactory");
			Method m = c.getMethod("getServiceName", String.class, int.class);
			String spreadTmService = (String) m.invoke(c, Context.TELEPHONY_SERVICE, 1);
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imei_1 = (tm.getDeviceId()).trim();
			TelephonyManager tm1 = (TelephonyManager) context.getSystemService(spreadTmService);
			String imei_2 = (tm1.getDeviceId()).trim();

			if (!imeis.contains(imei_1)) {
				if (imei_1 != null && imei_1.indexOf("0000000000") == -1)
					imeis.add(imei_1);
			}
			if (!imeis.contains(imei_2)) {
				if (imei_2 != null && imei_2.indexOf("0000000000") == -1)
					imeis.add(imei_2);
			}
		}catch(Exception e){
			
		}
	}

	/***
	 * 获取双卡方法1
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void getIMEI_QualcommDoubleSim(Context context, HashSet<String> imeis) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// TelephonyManager tm = (TelephonyManager) context
		// .getSystemService(Context.TELEPHONY_SERVICE);
		Class<?> cx = Class.forName("android.telephony.MSimTelephonyManager");
		Object obj = context.getSystemService("phone_msim");
		int simId_1 = 0;
		int simId_2 = 1;
		Method md = cx.getMethod("getDeviceId", int.class);
		String imei_1 = ((String) md.invoke(obj, simId_1)).trim();
		String imei_2 = ((String) md.invoke(obj, simId_2)).trim();

		if (!imeis.contains(imei_1)) {
			if (imei_1 != null && imei_1.indexOf("0000000000") == -1)
				imeis.add(imei_1);
		}
		if (!imeis.contains(imei_2)) {
			if (imei_2 != null && imei_2.indexOf("0000000000") == -1)
				imeis.add(imei_2);
		}

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
			if (imei.indexOf("000000000") == -1 && imei.indexOf("111111111") == -1 && imei.indexOf("222222222") == -1 && imei.indexOf("333333333") == -1 && imei.indexOf("444444444") == -1 && imei.indexOf("555555555") == -1 && imei.indexOf("666666666") == -1 && imei.indexOf("777777777") == -1 && imei.indexOf("888888888") == -1 && imei.indexOf("999999999") == -1) {
				return true;
			}
		}
		return false;
	}

}
