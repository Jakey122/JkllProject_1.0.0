package com.google.kd.utils;

import android.util.Log;

import com.android.utils.Commems;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileUtils {

	/**
	 * 
	 * @param path
	 *            文件夹路径
	 */
	public static boolean isFileExist(String path, String name) {
		File file = new File(path, name);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 读取本地文件 返回
	 * 
	 * @param file
	 *            地址
	 * @return
	 */
	public static String readFile(String file) {

		String res = "";
		try {
			
			if (new File(file).exists()) {
				FileInputStream fin = new FileInputStream(file);
				int length = fin.available();
				byte[] buffer = new byte[length];
				fin.read(buffer);
				res = EncodingUtils.getString(buffer, "UTF-8");
				fin.close();
			}else {
				LogUtils.d("wyy", file+" file is not exists!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;

	}
	
	public static String readFileByReader(String file) {

		FileReader fileReader;
		StringBuffer sb = new StringBuffer();
		try {
			fileReader = new FileReader(file);
			int ch = 0;
			while ((ch = fileReader.read()) != -1) {
				sb.append((char) ch);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	/**
	 * 
	 * @param data
	 * @param filePath
	 */
	public static void writeStringToSdcard(String data, String filePath) {
		
		File file = new File(filePath);
		LogUtils.d("wyy", "writeStringToSdcard"+filePath+" "+file.exists());

		if (!file.exists()) {

			boolean newFile = false;
			try {
				newFile = file.createNewFile();
				LogUtils.e("wyy", filePath + " no exists creatNewFle " + newFile);
				if (newFile) {
					return;
				}
			} catch (IOException e) {
				LogUtils.e("wyy", filePath + " "+e.toString());
				return;
			}
		}

		if (data != null && !(data.equals(""))) {
			FileWriter writer = null;
			try {
				writer = new FileWriter(file);
				writer.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param infostr
	 * @return 1 成功 0 失败
	 */
	public static int delfile(String infostr) {
		int succes = 0;

		File file = new File(infostr);
		if (file.exists()) {
			boolean delete = file.delete();
			succes = delete ? 1 : 0;
		}
		return succes;
	}

	/**
	 * 清除内容
	 * 
	 * @param infostr
	 */
	public static void nulls(String infostr) {

		int succes = 0;
		RandomAccessFile aFile = null;

		File file = new File(infostr);
		if (file.exists()) {
			try {
				aFile = new RandomAccessFile(infostr, "rw");
				FileChannel inChannel = aFile.getChannel();
				inChannel.truncate(0);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 
	 * @param filepath
	 *            路径
	 * @param name
	 *            文件名
	 * @param str
	 *            内容
	 * @param add
	 *            追加模式，ture为追加
	 */
	public static void write2sd(String filepath, String name, String str, boolean add) {

		File path = new File(filepath);

		if (!path.exists()) {
			path.mkdirs();
		}

		String fileName = name;

		File file = new File(filepath + fileName);

		Log.d("wyy", "file path:" + file.getAbsolutePath());

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		Log.d("wyy", "file exists :" + file.exists());

		String command = "chmod 777 " + file.getAbsolutePath();
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(command);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(file, add);
			// writer = new FileWriter(fileName);
			writer.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 读取本地wifi信息
	 * 文件地址{@link Commems.WIFI_STATE}
	 */
	public static String[] readWifiState() {
		
		//长度为2 ，ssid & 密码
		String[] wifiinfo = null;
		String readFile = readFile(Commems.WIFI_STATE);
		
		if (readFile.length() > 0) {
			
			String[] split = readFile.split("#");
			if (split != null && split.length >= 2) {
				wifiinfo = split;
				for (int i = 0; i < split.length; i++) {
					Log.d("wyy", "wifi:"+split[i]);
				}
				
			}else {
				Log.d("wyy", Commems.WIFI_STATE +" file not match");
			}
			
		}else {
			Log.d("wyy", Commems.WIFI_STATE +" file is not exists!");
		}

		return wifiinfo;
	}
}
