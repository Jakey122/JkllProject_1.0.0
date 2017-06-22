package com.android.apps.manager;

import com.android.apps.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 16-7-2.
 */
public class MFragmentManager {

    private static ConcurrentHashMap<BaseFragment, String> mFragmentMap = new ConcurrentHashMap();
    public static void keepFragment(BaseFragment fragment) {
        mFragmentMap.put(fragment, fragment.getClass().getName());
    }

    public static void removeFragment(BaseFragment fragment) {
        if (mFragmentMap.containsKey(fragment)) {
            mFragmentMap.remove(fragment);
        }
    }

    public static List<BaseFragment> getFragment(String clazz) {
        List<BaseFragment> list = new ArrayList<>();
        Iterator it = mFragmentMap.keySet().iterator();
        while (it.hasNext()) {
            BaseFragment fragment = (BaseFragment) it.next();
            if (fragment != null && fragment.getClass().getName().equals(clazz)) {
                list.add(fragment);
            }
        }
        return list;
    }

    public static BaseFragment getFragment(long type) {
        Iterator it = mFragmentMap.keySet().iterator();
        while (it.hasNext()) {
            BaseFragment fragment = (BaseFragment) it.next();
            if (fragment != null && fragment.getFragmentType() == type) return fragment;
        }
        return null;
    }

    public static void destroyFragment() {
        Iterator it = mFragmentMap.keySet().iterator();
        while (it.hasNext()) {
            BaseFragment fragment = (BaseFragment) it.next();
            if (fragment != null) {
                try {
                    fragment = null;
                    removeFragment(fragment);
                } catch (Exception e) {
                }
            }
        }
        if (mFragmentMap != null) mFragmentMap.clear();
    }
}
