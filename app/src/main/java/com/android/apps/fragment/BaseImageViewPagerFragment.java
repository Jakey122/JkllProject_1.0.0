package com.android.apps.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.sdk.bean.BaseInfo;
import com.sdk.bean.RequestResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-5-20.
 */
public abstract class BaseImageViewPagerFragment extends BaseFragment {

    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected boolean hasSetupTab = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getParams();
        initView();
        setupViewPager();
        setupTab();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mViewPagerAdapter != null) {
            mViewPagerAdapter.removeViewPagerListener();
        }
        super.onDestroy();
    }

    @Override
    protected void handlerMyMessage(Message msg) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void handleConnectionStateChanged(int state, boolean isConnect, boolean lastState) {
        if (!isResume || mViewPagerAdapter == null || mViewPagerAdapter.getCount() == 0) return;
        handleHttpListenerOnNetChanged(state, isConnect, lastState);
    }

    @Override
    protected void handleHttpListenerOnNetChanged(int state, boolean isConnect, boolean lastState) {

    }

    @Override
    public void handlerViewTreeObserver() {

    }

    @Override
    public void setData(RequestResult result) {

    }

    protected void setupViewPager() {
        if (mViewPager != null) {
            mViewPager.setOffscreenPageLimit(1);
            mViewPagerAdapter = new ViewPagerAdapter(mActivity, mViewPager){
                @Override
                protected void onPageSelectedHandler(int position) {
                    super.onPageSelectedHandler(position);
                    handlePageSelected(position);
                }
            };

            mViewPager.setAdapter(mViewPagerAdapter);
        }
    }

    protected abstract void getParams();

    protected abstract void initView();

    protected abstract void setupTab();

    public void setupTab(BaseInfo baseInfo) {

    }

    protected abstract void handlePageScrollStateChanged(int position);

    protected abstract void handlePageScrolled(int position, float positionOffset, int positionOffsetPixels);

    protected void handlePageSelected(int position) {
        if (!isResume || mViewPagerAdapter == null || mViewPagerAdapter.getCount() == 0) return;
    }

    class ViewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener  {

        private ArrayList<View> tabs = new ArrayList<>();
        private ViewPager mViewPager;
        private Context mContext;

        public ViewPagerAdapter(Activity activity, ViewPager viewPager) {
            // TODO Auto-generated constructor stub
            mContext = activity;
            mViewPager = viewPager;
            mViewPager.addOnPageChangeListener(this);
        }

        /**
         * 处理Viewpager滑动状态改变
         *
         * @param position
         */
        protected void onPageScrollStateChangedHandler(int position){};

        /**
         * 处理Viewpager滑动
         *
         * @param position
         * @param positionOffset
         * @param positionOffsetPixels
         */
        protected void onPageScrolledHandler(int position, float positionOffset, int positionOffsetPixels){};

        /**
         * 处理Viewpager页面切换
         *
         * @param position
         */
        protected void onPageSelectedHandler(int position){};

        public void removeViewPagerListener() {
            if (mViewPager != null) {
                mViewPager.removeOnPageChangeListener(this);
            }
        }

        /**
         * 插入子页
         *
         * @param sClass
         */
        public void addItem(View sClass) {
            tabs.add(sClass);
        }

        public void addItem(View sClass, int position) {
            tabs.add(position, sClass);
        }

        public ViewPagerAdapter(List<View> list) {
            this.tabs = tabs;
        }

        @Override
        public int getCount() {
            if(getViewCount() < 2)
                return getViewCount();
            return Integer.MAX_VALUE;
        }

        public int getViewCount() {
            return tabs.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (tabs == null || tabs.size() == 0) return null;
            position %= tabs.size();
            if (position < 0) {
                position = tabs.size() + position;
            }
            View view = tabs.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            //add listeners here if necessary
            return view;

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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

        public void clear() {
            tabs.clear();
            mViewPager.removeAllViews();
        }
    }
}
