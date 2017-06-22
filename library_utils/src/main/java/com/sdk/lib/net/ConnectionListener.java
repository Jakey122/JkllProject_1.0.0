package com.sdk.lib.net;

/**
 * 网络连接变化 观察者 注册的listener
 */
public interface ConnectionListener {

	/* 没有任何连线 */
	public final static int CONN_NONE = 0;
	/* WIFI */
	public final static int CONN_WIFI = 1;
	/* 2G：GPRS, CDMA, EDGE */
	public final static int CONN_2G = 2;
	/* 3G */
	public final static int CONN_3G = 3;
	/*4G*/
	public final static int CONN_4G = 4;
	
	public void connectionStateChanged(int state);
	
}
