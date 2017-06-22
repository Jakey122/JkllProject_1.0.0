package com.android.apps.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;

import com.android.apps.adapter.BaseViewPagerAdapter;
import com.android.apps.helper.PromptHelper;
import com.android.apps.listener.OnTabDoubleClickListener;
import com.android.apps.listener.OnViewPagerChildCreateListener;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.RequestResult;
import com.sdk.lib.net.NetworkStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 16-5-20.
 */
public abstract class BaseViewPagerFragment extends BaseFragment implements OnTabDoubleClickListener {

    protected TabLayout mTabLayout;
    protected View mTabContainer;
    protected List<Pair<String, Integer>> mTitleList;
    protected ViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected OnViewPagerChildCreateListener mListener;
    private Map<TabLayout.Tab, Long> mTabSelectTime = new HashMap<>();
    protected boolean hasSetupTab = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitleList = new ArrayList<>();
        getParams();
        initView();
        setupViewPager();
        setupTab();
        setOnTabDoubleClickListener(this);
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
        removeOnChildCreatedListener();
        mTabSelectTime.clear();
        mTitleList.clear();
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
//        if (isConnect && !lastState) {
        for (int position = 0; position < mViewPagerAdapter.getCount(); position++) {
            BaseFragment fragment = mViewPagerAdapter.getItem(position);
            if (fragment != null && fragment.isResume)
                fragment.handleConnectionStateChanged(state, isConnect, lastState);
        }
//        }
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

    @Override
    public int getFirstItemPosition() {
        return -1;
    }

    @Override
    public int getLastItemPosition() {
        return -1;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (mViewPager != null) mViewPager.setCurrentItem(tab.getPosition());
        mTabSelectTime.put(tab, System.currentTimeMillis());
        PromptHelper.dismissCurrent();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        long lastTime = mTabSelectTime.containsKey(tab) ? mTabSelectTime.get(tab) : 0;
        long sTime = System.currentTimeMillis();
        if (sTime - lastTime < 1000) {
            onTabDoubleClick(tab);
        }
        mTabSelectTime.put(tab, System.currentTimeMillis());
        PromptHelper.dismissCurrent();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabDoubleClick(TabLayout.Tab tab) {
        BaseFragment baseFragment = null;
        if (mViewPagerAdapter != null && tab != null) {
            baseFragment = mViewPagerAdapter.getItem(tab.getPosition());
        }
        if (baseFragment != null) {
            if (baseFragment instanceof BaseRecyclerFragment) {
                BaseRecyclerFragment recyclerFragment = (BaseRecyclerFragment) baseFragment;
                if (recyclerFragment.isEmpty()) return;
                RecyclerView recyclerView = recyclerFragment.getRecyclerView();
                if (recyclerView != null) recyclerView.scrollToPosition(0);
            }
        }
    }

    public void setOnTabDoubleClickListener(OnTabDoubleClickListener listener) {
        if (mTabLayout != null) {
            mTabLayout.setOnTabSelectedListener(listener);
        }
    }

    public void setOnChildCreatedListener(OnViewPagerChildCreateListener listener) {
        mListener = listener;
    }

    public void removeOnChildCreatedListener() {
        mListener = null;
    }

    protected void setupViewPager() {
        if (mViewPager != null) {
            mViewPager.setOffscreenPageLimit(1);
            mViewPagerAdapter = new ViewPagerAdapter(mActivity, mViewPager, getChildFragmentManager());
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
        BaseFragment fragment = mViewPagerAdapter.getItem(position);
        if (fragment != null) {
            if (!fragment.isNetworkConnect || (fragment instanceof BaseRecyclerFragment && ((BaseRecyclerFragment) fragment).isEmpty())) {
                int netState = NetworkStatus.getInstance(mActivity).getNetWorkState();
                boolean isConnect = NetworkStatus.getInstance(mActivity).isConnected();
                fragment.handleConnectionStateChanged(netState, isConnect, isNetworkConnect);
            }
            fragment.handleViewHolderAnimator();
        }
    }

    class ViewPagerAdapter extends BaseViewPagerAdapter {

        public ViewPagerAdapter(Activity activity, ViewPager viewPager, FragmentManager fragmentManager) {
            super(activity, viewPager, fragmentManager);
        }

        @Override
        protected String getPageTitleHandler(int position) {
            Pair<String, Integer> pair = mTitleList.get(position % mTitleList.size());
            return pair.first;
        }

        @Override
        protected void onPageScrollStateChangedHandler(int position) {
            PromptHelper.dismissCurrent();
            handlePageScrollStateChanged(position);
        }

        @Override
        protected void onPageScrolledHandler(int position, float positionOffset, int positionOffsetPixels) {
            handlePageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        protected void onPageSelectedHandler(int position) {
            handlePageSelected(position);
        }

        @Override
        protected void onChildCreated(BaseFragment baseFragment, int position) {
            if (mListener != null) mListener.onChildCreated(baseFragment, position);
        }
    }
}
