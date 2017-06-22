package com.android.apps.util;

import android.content.Context;

/**
 * Created by root on 16-5-9.
 */
public class AppLogUtil {

    /**
     * 浏览页面
     *
     * @param mContext
     * @param currentPageId
     * @param fromPageId
     */
    public static void addOpenViewLog(Context mContext, int currentPageId, int fromPageId) {
//        addOpenViewLog(mContext, StatisticsLib.TYPE_MARKET, currentPageId, fromPageId, "-1", "-1");
    }

    /**
     * 浏览页面
     *
     * @param mContext
     * @param currentPageId
     * @param fromPageId
     * @param extraSid
     * @param extraSubjectId
     */
    public static void addOpenViewLog(Context mContext, int currentPageId, int fromPageId, String extraSid, String extraSubjectId) {
//       addOpenViewLog(mContext, StatisticsLib.TYPE_MARKET, currentPageId, fromPageId, extraSid, extraSubjectId );
    }

    /**
     * 浏览页面
     * @param type
     * @param mContext
     * @param currentPageId
     * @param fromPageId
     * @param extraSid
     * @param extraSubjectId
     */
    public static void addOpenViewLog(Context mContext, int type, int currentPageId, int fromPageId, String extraSid, String extraSubjectId){
        /*List<Object> params = StatisticsLib.generateHeader(type, AppLogAction.ACTION_OPEN_VIEW);
        params.add(currentPageId);
        params.add(fromPageId);
        params.add(extraSid);
        params.add(extraSubjectId);
        StatisticsLib.addLog(mContext.getApplicationContext(), params);*/
    }

    /**
     * 点击view 非页面跳转可以加该日志
     * @param mContext
     * @param type
     * @param currentPageId
     * @param extraSid
     * @param extraSubjectId
     */
    public static void addClickViewLog(Context mContext, int type, int currentPageId, long extraSid, long extraSubjectId){
        /*List<Object> params = StatisticsLib.generateHeader(type, AppLogAction.ACTION_CLICK_VIEW);
        params.add(currentPageId);
        params.add(extraSid);
        params.add(extraSubjectId);
        StatisticsLib.addLog(mContext.getApplicationContext(), params);*/
    }

}
