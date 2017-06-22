package com.sdk.net;

import android.content.Context;

/**
 * Created by xiesongrin on 17/5/12.
 */

public class NetLib {

    private static NetLib mInstance;
    private Context mContext;
    private  static NetConfig config;

    private NetLib(){}

    public static NetLib getInstance() {
        if(mInstance == null){
            synchronized (NetLib.class){
                if(mInstance == null)
                    mInstance = new NetLib();
            }
        }
        return mInstance;
    }

    public String getHost(){
        checkConfig();
        return config.getHost();
    }

    public Context getContext(){
        return mContext;
    }

    private void checkConfig(){
        if(config == null)
            config = new NetConfig();
    }

    public void build(Context context, NetConfig config){
        mContext = context.getApplicationContext();
        this.config = config;
    }

    public static class NetConfig{

        private String host = "mapp.showmac.cn";

        public NetConfig build(){
            return new NetConfig();
        }

        public NetConfig host(String host){
            this.host = host;
            return this;
        }

        public String getHost(){
            return host;
        }
    }
}
