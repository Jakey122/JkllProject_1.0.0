package com.android.apps.helper;

import com.android.apps.listener.OnLoginChangeListener;
import com.sdk.bean.BaseInfo;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016-08-17.
 */
public class LoginHelper {

    private static ConcurrentHashMap<OnLoginChangeListener, String> mListenersMap = new ConcurrentHashMap();

    public static void addListener(OnLoginChangeListener listener, String tag) {
        if (!mListenersMap.containsKey(listener)) {
            mListenersMap.put(listener, tag);
        }
    }

    public static void removeListener(OnLoginChangeListener listener) {
        if (mListenersMap.containsKey(listener)) {
            mListenersMap.remove(listener);
        }
    }

    public static void handleLoginChangeState(BaseInfo info) {
        Iterator it = mListenersMap.keySet().iterator();
        while (it.hasNext()) {
            OnLoginChangeListener listener = (OnLoginChangeListener) it.next();
            if (listener != null) {
                listener.onLoginChange(info);
            }
        }
    }
}
