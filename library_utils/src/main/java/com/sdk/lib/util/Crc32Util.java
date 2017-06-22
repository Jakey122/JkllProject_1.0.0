package com.sdk.lib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Crc32Util {

	public static String getApkFileSfCrc32ByPkgName(Context context, String packageName) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			return getApkFileSFCrc32(packageInfo.applicationInfo.sourceDir);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "null";
	}

	public static String getApkFileSFCrc32(String ApkFilePath) {
		long crc = 0xffffffff;
		
		try {
			File f = new File(ApkFilePath);
			ZipFile z = new ZipFile(f);
			Enumeration<? extends ZipEntry> zList = z.entries();
			ZipEntry ze = null;
			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();
				if (ze.isDirectory() || ze.getName().toString().indexOf("META-INF") == -1 || ze.getName().toString().indexOf(".SF") == -1) {
					continue;
				} else {
					crc = ze.getCrc();
					break;
				}
			}
			z.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Long.toHexString(crc);
	}
}
