package com.sdk.helper;

import com.sdk.bean.BaseInfo;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 17/5/3.
 */

public class ListenerHelper {

    public static final int TYPE_PAGE_DEVICE = 1;
    public static final int TYPE_PAGE_USERINFO = 2;
    public static final int TYPE_PAGE_DEVICE_DETAIL = 3;
    public static final int TYPE_PAGE_ORDER_CANCEL = 4;

    private static ConcurrentHashMap<OnItemChangeListener, String> mListenersMap = new ConcurrentHashMap();

    public static void addListener(OnItemChangeListener listener, String tag) {
        if (!mListenersMap.containsKey(listener)) {
            mListenersMap.put(listener, tag);
        }
    }

    public static void removeListener(OnItemChangeListener listener) {
        if (mListenersMap.containsKey(listener)) {
            mListenersMap.remove(listener);
        }
    }

    public static void handleChange(int pageId, int status, BaseInfo info) {
        Iterator it = mListenersMap.keySet().iterator();
        while (it.hasNext()) {
            OnItemChangeListener listener = (OnItemChangeListener) it.next();
            if (listener != null) {
                listener.onChange(pageId, status, info);
            }
        }
    }
}
