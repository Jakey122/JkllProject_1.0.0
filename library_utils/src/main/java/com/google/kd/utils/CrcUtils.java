package com.google.kd.utils;

public class CrcUtils {
	
	static{
		System.loadLibrary("crc");
	}
	
	public static native String GetFileCRC32(String filePath);

}
