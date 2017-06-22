package com.sdk.lib.util;

import android.util.Log;

import java.util.Date;

/**
 * 耗时测试工具类
 * Xsl
 */
public class TestUtil {

	private long startTime;
	private String tag;
	
	public TestUtil(String tag){
		this.tag = tag;
	}
    
    public void start(){
    	startTime = System.currentTimeMillis();
    	Log.e(getClass().getSimpleName(), tag + "start :"+ new Date().toLocaleString());
    }
    
    public void end(){
    	Log.e(getClass().getSimpleName(), tag + "end :"+new Date().toLocaleString()+ " use time :"+((System.currentTimeMillis()-startTime)/1000.0f) + "秒");
    }
    
    public static TestUtil getTest(String tag){
    	return new TestUtil(tag);
    }
}
