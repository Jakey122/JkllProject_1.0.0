package com.sdk.lib.util;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 格式化操作工具类
 * @author Xsl
 *
 */
public class FormatUtil {
	
	/**
	 * 格式化时间
	 * 
	 * @param times
	 * @param formatType
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDate(long times, String formatType) {
		if (formatType == null) {
			throw new NullPointerException("formatType is null!");
		}

		SimpleDateFormat df = new SimpleDateFormat(formatType);
		return df.format(times);
	}

	/**
	 * 格式化日期1-59分钟前、1-23小时前，1-7天、超过7天显示具体生成时间（精确至秒）
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormatL2S(long ctime) {
		long currentTimeMillis = System.currentTimeMillis();
		long time = currentTimeMillis - ctime;
		time /= 1000;
		if (time == 0 || ctime == 0) {
			return "刚刚";
		} else if (time < 60 && time > 0) {
			return "刚刚";
		} else if (time < 3600 && time >= 60) {// 1-59分钟前
			return (time / 60) + "分钟前";
		} else if (time >= 3600 && time < 24 * 3600) {// 1-23小时前
			return (time / 60 / 60) + "小时前";
		} else if (time >= 24 * 3600 && time < 7 * 24 * 3600) {// 1-7天
			return (time / 60 / 60 / 24) + "天前";
		} else {//
			return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
					.format(new Date(ctime));
		}
	}

	/**
	 * 格式化时间
	 * 
	 * @param times
	 * @param formatType
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static long formatDate(String times, String formatType) {
		try {
			if (formatType == null) {
				throw new NullPointerException("formatType is null!");
			}

			SimpleDateFormat df = new SimpleDateFormat(formatType);
			return df.parse(times).getTime();
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * @param sizeB
	 * @return 格式化文件大小
	 */
	public static final String formatFileSize(long sizeB) {
		double SIZE_KB = 1024.00;
		// int SIZE_MB = SIZE_KB * SIZE_KB;
		String STR_M = "M";

		BigDecimal sizeTotal = null;
		if (sizeB > -1) {
			sizeTotal = new BigDecimal(sizeB / SIZE_KB / SIZE_KB);
			return sizeTotal.setScale(1, BigDecimal.ROUND_UP).doubleValue()
					+ STR_M;
		} else {
			return sizeB + "";
		}
	}
}
