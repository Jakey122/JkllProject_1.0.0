package com.sdk.lib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class UiUtil {

	/**
	 * 获取屏幕的分辨率
	 * @return
	 */
	public static Display getScreenDisplay(Context sContext){
		WindowManager wm = (WindowManager) sContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay();
	}
	
	public static DisplayMetrics getScreenSize(Context c){
		DisplayMetrics dm = new DisplayMetrics();
		dm =c. getResources().getDisplayMetrics();
		return dm;
	}

	/**
	 * 将dip转成px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context sContext, float dip) {
		float scale = sContext.getApplicationContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * px转成dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context sContext, float pxValue) {
		float scale = sContext.getApplicationContext().getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context sContext, float pxValue) {
		final float fontScale = sContext.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context sContext, float spValue) {
		final float fontScale = sContext.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isSmallScreen(Context sContext) {
		WindowManager wm = (WindowManager) sContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		int W = wm.getDefaultDisplay().getWidth();
		return W <= 720;
	}
	
	@SuppressWarnings("deprecation")
	public static String getResolution(Context sContext) {
		WindowManager wm = (WindowManager) sContext.getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		int W = wm.getDefaultDisplay().getWidth();
		int H = wm.getDefaultDisplay().getHeight();
		return W + "*" + H;
	}

	@SuppressLint("NewApi")
	public static void releaseAssets(Context sContext, String desName, String targetName) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			File targetFile = new File(targetName);
			if (targetFile.exists()) {
				targetFile.delete();
			}

			in = new BufferedInputStream(sContext.getApplicationContext().getAssets().open(desName));
			out = new BufferedOutputStream(new FileOutputStream(targetFile,
					false));
			byte[] buffer = new byte[4096];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			out.flush();
			targetFile.setReadable(true, false);
			targetFile.setWritable(true, false);
			targetFile.setExecutable(true, false);
		} catch (Throwable e) {
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
