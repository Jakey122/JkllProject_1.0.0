package com.android.apps.web;

import android.content.Context;
import android.os.Handler;

/**
 * Native 与 JS 交互接口
 * Created by root on 16-7-6.
 */
public class MJSInerface {

    private Context mContext;
    private int mFrom;
    private long msId;
    private long mFatherId;
    private IJSProgressHelper mHelper;

    public MJSInerface(Context context, IJSProgressHelper helper) {
        mContext = context;
        mHelper = helper;
    }

    public void jsInterfaceDestroy() {

    }

    public void setFromInfo(int from, long sId, long fatherId) {
        mFrom = from;
        msId = sId;
        mFatherId = fatherId;
    }

    public interface IJSProgressHelper {
        void sendToJs_Downloadprogress(long appId, int progress, int state, Handler handler);

        boolean preDownload();
    }
}
