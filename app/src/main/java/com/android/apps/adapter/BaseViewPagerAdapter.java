package com.android.apps.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.android.apps.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by root on 16-5-19.
 */
public abstract class BaseViewPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private ArrayList<TabInfo> tabs = new ArrayList<>();
    private ViewPager mViewPager;
    private Context mContext;

    public BaseViewPagerAdapter(Activity activity, ViewPager viewPager, FragmentManager fragmentManager) {
        super(fragmentManager);
        // TODO Auto-generated constructor stub
        mContext = activity;
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 处理每页标题
     *
     * @param position
     * @return
     */
    protected abstract String getPageTitleHandler(int position);

    /**
     * 处理Viewpager滑动状态改变
     *
     * @param position
     */
    protected abstract void onPageScrollStateChangedHandler(int position);

    /**
     * 处理Viewpager滑动
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    protected abstract void onPageScrolledHandler(int position, float positionOffset, int positionOffsetPixels);

    /**
     * 处理Viewpager页面切换
     *
     * @param position
     */
    protected abstract void onPageSelectedHandler(int position);

    protected abstract void onChildCreated(BaseFragment baseFragment, int position);

    public void removeViewPagerListener() {
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
        }
    }

    /**
     * 插入子页
     *
     * @param sClass
     * @param args
     */
    public void addItem(Class<?> sClass, Bundle args) {
        TabInfo sTabInfo = new TabInfo();
        sTabInfo.sClass = sClass;
        sTabInfo.args = args;
        tabs.add(sTabInfo);
    }

    public void addItem(Class<?> sClass, Bundle args, int position) {
        TabInfo sTabInfo = new TabInfo();
        sTabInfo.sClass = sClass;
        sTabInfo.args = args;
        tabs.add(position, sTabInfo);
    }

    /**
     * 获得子页中嵌套的Fragment
     *
     * @param position
     * @return
     */
    @Override
    public BaseFragment getItem(int position) {
        // TODO Auto-generated method stub
        if (tabs == null) {
            return null;
        } else {
            if (getCount() > 0 && position < tabs.size()) {
                TabInfo sTabInfo = tabs.get(position);

                if (sTabInfo.sFragment == null) {
                    return (BaseFragment) BaseFragment.instantiate(mContext, sTabInfo.sClass.getName(), sTabInfo.args);
                } else {
                    return sTabInfo.sFragment;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * 获得子页数量
     *
     * @return
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tabs == null ? 0 : tabs.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
//        Object sObject = super.instantiateItem(container, position);
//        TabInfo sTabInfo = tabs.get(position);
//        sTabInfo.sFragment = (BaseFragment) sObject;
//        return sObject;

        TabInfo sTabInfo = tabs.get(position);
        if (sTabInfo != null && sTabInfo.sFragment != null) {
            return sTabInfo.sFragment;
        } else {
            BaseFragment baseFragment = (BaseFragment) super.instantiateItem(container, position);
            sTabInfo.sFragment = baseFragment;
            onChildCreated(baseFragment, position);
            return sTabInfo.sFragment;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        if (object != null) {
            TabInfo sTabInfo = tabs.get(position);
            sTabInfo.sFragment = null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getPageTitleHandler(position);
    }

    @Override
    public void onPageScrollStateChanged(int position) {
        // TODO Auto-generated method stub
        onPageScrollStateChangedHandler(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // TODO Auto-generated method stub
        onPageScrolledHandler(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        onPageSelectedHandler(position);
    }

    class TabInfo {
        public Bundle args;
        public BaseFragment sFragment;
        public Class<?> sClass;
    }
}
