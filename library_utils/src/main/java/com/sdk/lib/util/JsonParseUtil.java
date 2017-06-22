package com.sdk.lib.util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author jiang
 * json解析类
 */
public class JsonParseUtil {
	private static final String TAG = "JsonParseUtil";
	
	public static int getInt(JSONObject json, String key) {
		try {
			if (json.has(key)) {
				return json.getInt(key);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	public static long getLong(JSONObject json, String key) {
		try {
			if (json.has(key)) {
				return json.getLong(key);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	public static String getString(JSONObject json, String key) {
		try {
			if (json.has(key)) {
				return json.getString(key);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public static JSONArray getJsonArray(JSONObject json, String key) {
		try {
			if (json.has(key)) {
				return json.getJSONArray(key);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
