package com.sdk.lib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class SignatureUtil {

	public static final int APP_UNINSTALL = 10;
	public static final int SIGN_SAME = 11;
	public static final int SIGN_DIFFERENT = 12;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getUninstallAPKSignatures(String apkPath) {
		String PATH_PackageParser = "android.content.pm.PackageParser";
		try {

			Class pkgParserCls = Class.forName(PATH_PackageParser);
			Class[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Object pkgParser = pkgParserCt.newInstance(valueArgs);

			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
			valueArgs = new Object[4];
			valueArgs[0] = new File(apkPath);
			valueArgs[1] = apkPath;
			valueArgs[2] = metrics;
			valueArgs[3] = PackageManager.GET_SIGNATURES;
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);

			typeArgs = new Class[2];
			typeArgs[0] = pkgParserPkg.getClass();
			typeArgs[1] = Integer.TYPE;
			Method pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates", typeArgs);
			valueArgs = new Object[2];
			valueArgs[0] = pkgParserPkg;
			valueArgs[1] = PackageManager.GET_SIGNATURES;
			pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);

			Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
			Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);

			return info[0].toCharsString();
		} catch (Exception e) {
			//log.e("getuninstallappsign error is ", e.getMessage());
		}
		return null;
	}

	public static String getInstallAPKSignatures(Context context, String pakname) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
		Iterator<PackageInfo> iter = apps.iterator();

		while (iter.hasNext()) {
			PackageInfo packageinfo = iter.next();
			String packageName = packageinfo.packageName;
			if (packageName.equals(pakname)) {
				return packageinfo.signatures[0].toCharsString();
			}
		}

		return null;
	}

	public static String checkSignInfo(Context mContext, String path) {

		try {
			String pkg = Util.getPackageFromDownPath(new File(path));
			if (!SystemUtil.isInstalledApk(mContext, pkg))
				return null;

			String signa1 = SignatureUtil.getUninstallAPKSignatures(path);
			String signa2 = SignatureUtil.getInstallAPKSignatures(mContext, pkg);
			return signa1.equals(signa2)? null : pkg;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
