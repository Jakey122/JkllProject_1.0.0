package com.sdk.lib.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
	
	public static String getSdcardCacheDir(Context mContext) {
        File cacheDir = mContext.getExternalCacheDir();
        if (cacheDir == null || !cacheDir.exists()) {
            cacheDir = new File(Environment.getExternalStorageDirectory().toString()
                    + "/Android/data/" + mContext.getPackageName() + "/cache");
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

	public static String initCacheDir(Context mContext) {
		String strCacheDir = null;
		try {
		    if (Util.isSDCardAvailable()) {
		        strCacheDir = getSdcardCacheDir(mContext);
		    } else {
		    	File cacheDir = mContext.getFilesDir();
		        strCacheDir = cacheDir.getAbsolutePath();
		    }
		    return strCacheDir;
		} catch (Exception e) {
			if(Util.isSDCardAvailable()){
				strCacheDir = Environment.getExternalStorageDirectory().toString();
			} else {
				File cacheDir = mContext.getFilesDir();
		        strCacheDir = cacheDir.getAbsolutePath();
			}
		}
		return strCacheDir;
	}
	
	/**
	 * 新建文件夹
	 * 
	 * @param path
	 */
	public static void makeDir(String path) {
		if (path == null || path.length() == 0)
			return;
		makeDir(new File(path));
	}

	/**
	 * 新建文件夹
	 * 
	 * @param file
	 */
	public static void makeDir(File file) {
		if (file == null || file.exists())
			return;
		file.mkdirs();
	}



	public static boolean isFileExist(String path) {
		try {
			File sFile = new File(path);
			return sFile.exists();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static void deleteFile(String path) {
		if (!TextUtils.isEmpty(path))
			deleteFile(new File(path));
	}

	public static void deleteFile(File file) {
		try {
			if (file != null && file.exists()) {
				File toFile = new File(file.getAbsolutePath() + ".tmp");
				file.renameTo(toFile);
				toFile.delete();
				toFile = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static String getFileMD5(File file) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			byte[] buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();
			return encryption("MD5", buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fin = null;
		}
		return null;
	}
	
	private static String encryption(String mod, byte[] buffer) {
		try {
			MessageDigest digest = MessageDigest.getInstance(mod);
			digest.update(buffer);
			byte[] messageDigest = digest.digest();
			return byteArrayToHexString(messageDigest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private static String byteArrayToHexString(byte[] byteArray) {
		// Create HEX String
		String hexString = "";
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			hexString = Integer.toHexString(0xFF & byteArray[i]);
			if (hexString.length() == 1) {
				result.append("0");
			}
			result.append(hexString);
		}
		return result.toString();
	}

	/**
	 * gzip压缩
	 */
	public static byte[] gzipCompress(String str) throws IOException {
		// return str;
		if (str == null || str.length() == 0) {
			// return str;
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
	

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	public static File getFile(String baseDir, String absFileName) {
		try {
			String[] dirs = absFileName.split(File.separator);
			File ret = new File(baseDir);
			String substr = null;

			if (dirs.length > 1) {
				for (int i = 0; i < dirs.length - 1; i++) {
					substr = dirs[i];
					ret = new File(ret, substr);
				}
				makeDir(ret);
				substr = dirs[dirs.length - 1];

				ret = new File(ret, substr);
				return ret;
			} else {
				ret = new File(ret, absFileName);
				return ret;
			}
		} catch (Exception e) {
			return null;
		}
	}
	

	/**
	 * 解压缩功能. 将zipFile文件解压到folderPath目录下.
	 * 
	 * @throws Exception
	 */
	public static boolean upZipFile(File zipFile, String folderPath) {
		try {
			File folderFile = new File(folderPath);
			makeDir(folderFile);

			ZipFile zfile = new ZipFile(zipFile);
			Enumeration<?> zList = zfile.entries();
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			OutputStream os;
			InputStream is;

			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();

				if (ze.isDirectory()) {
					String dirPath = folderPath + File.separator + ze.getName();
					makeDir(dirPath);
				} else {
					File destFile = getFile(folderPath, ze.getName());
					deleteFile(destFile);

					os = new FileOutputStream(destFile);
					is = zfile.getInputStream(ze);
					int readLen = 0;

					while ((readLen = is.read(buf, 0, buf.length)) != -1) {
						os.write(buf, 0, readLen);
					}
					is.close();
					os.close();
				}
			}
			zfile.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
