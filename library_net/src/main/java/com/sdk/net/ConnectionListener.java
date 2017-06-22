package com.sdk.net;

/**
 * 网络连接变化 观察者 所需注册的listener
 */
public interface ConnectionListener {

    /* 没有任何连线 */
    public final static int CONN_NONE = -1;
    /* 来自未知网络的不明连线 */
    public final static int CONN_UNKNOWN = 0;
    /* WIFI */
    public final static int CONN_WIFI = 1;
    /* 2G：GPRS, CDMA, EDGE */
    public final static int CONN_2G = 2;
    /* 3G */
    public final static int CONN_3G = 3;
    /*4G*/
    public final static int CONN_4G = 4;

    /**
     * 继承用
     *
     * @param state
     */
    public void connectionStateChanged(int state);
}
