package com.sdk.lib.util;

import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PullXmlUtil implements BConst{

	public static final String XML_FILE = "/system/etc/EngineX/rombuild.xml";
	public static List<String> whiteList = new ArrayList<String>();

	public static String[] getBlackList(int type) {
		switch (type) {
		case NPL:
			return PullXmlUtil.getPlContent("npl");
		case KPL:
			return PullXmlUtil.getPlContent("kpl");
		case DPL:
			return PullXmlUtil.getPlContent("dpl");
		case ABWL:
			return PullXmlUtil.getPlContent("abwl");
		case UIWL:
			return PullXmlUtil.getPlContent("uiwl");
		case BUILD:
			return PullXmlUtil.getPlContent("build");
		case WIDGETTIME:
			return PullXmlUtil.getPlContent("wastime");
		}
		return null;
	}

	public static String getRomBuild() {
		String[] info = getBlackList(BUILD);
		if (info == null || info.length == 0) {
			return "";
		} else {
			return info[0];
		}
	}

	private static String[] getPlContent(String pl) {
		String[] black = null;
		List<String> result = null;
		try {
			result = getAllXmlContent().get(pl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (result != null && result.size() > 0) {
			black = new String[result.size()];
			for (int i = 0; i < black.length; i++) {
				black[i] = result.get(i);
			}
		}
		return black;
	}

	private static Map<String, List<String>> getAllXmlContent() throws Exception {

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		File xml = new File(XML_FILE);
		if (!xml.exists()) {
			map.clear();
			return map;
		}

		InputStream in = new FileInputStream(xml);
		XmlPullParser parser = Xml.newPullParser();// 得到Pull解析器
		String lastType = null;
		parser.setInput(in, "UTF-8");

		try {
			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlPullParser.START_TAG) {
					String tagName = parser.getName();
					if (tagName.equals("kpl")) {
						lastType = "kpl";
					} else if (tagName.equals("dpl")) {
						lastType = "dpl";
					} else if (tagName.equals("npl")) {
						lastType = "npl";
					} else if (tagName.equals("abwl")) {
						lastType = "abwl";
					} else if (tagName.equals("uiwl")) {
						lastType = "uiwl";
					} else if (tagName.equals("build")) {
						lastType = "build";
					} else if (tagName.equals("wastime")) {
						lastType = "wastime";
					} else if (tagName.equals("name") && !TextUtils.isEmpty(lastType)) {

						String name = parser.nextText();
						if (!isWhiteItem(name) 
								|| lastType.equals("dpl")
								|| lastType.equals("abwl")
								|| lastType.equals("uiwl")
								|| lastType.equals("wastime")) {
							
							if (map.get(lastType) == null) {
								List<String> nameList = new ArrayList<String>();
								nameList.add(name);
								map.put(lastType, nameList);
							} else {
								map.get(lastType).add(name);
							}
						}
					} else {
						lastType = null;
					}
				}
				parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	protected static boolean isWhiteItem(String name){
		if(whiteList != null && whiteList.size() > 0){
			return whiteList.contains(name);
		}
		return false;
	}
	
	public static void setWhiteList(List<String> list){
		if(whiteList == null) whiteList = new ArrayList<String>();
		whiteList.clear();
		whiteList.addAll(list);
	}
	
	/*
	 * public static BuildInfo getBuildInfo(){ String[] args =
	 * getPlContent("build"); if(args == null || args.length < 4) return null;
	 * 
	 * BuildInfo build = new BuildInfo(); build.id = Long.valueOf(args[0]);
	 * build.level = args[1]; build.date = Long.valueOf(args[2]); build.cust =
	 * Integer.valueOf(args[3]); return build; }
	 */
}
