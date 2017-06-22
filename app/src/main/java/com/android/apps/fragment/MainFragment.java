package com.android.apps.fragment;

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.apps.bean.TabInfo;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppLogUtil;
import com.android.apps.util.AppUtil;
import com.android.apps.util.PageId;
import com.android.apps.view.MViewPager;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.helper.ListenerHelper;
import com.sdk.helper.OnItemChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends BaseViewPagerFragment implements View.OnClickListener, OnItemChangeListener {

    private static final int HANDLE_REFRESH_COUNT = 1;

    private int defaultTab;  //默认打开那个tab
    private int mFrom;

    private int lastTabIndex = -1;
    private boolean firstInit = false;

    private List<TabInfo> mTabList;
    private CountDownTimer mCountDownTimer;
    private boolean isAppUpdate;
    private TextView toolbar_title;

    public static Bundle newArgument(int defaultTab, int mFrom) {
        Bundle args = new Bundle();
        args.putInt("defaultTab", defaultTab);
        args.putInt("from", mFrom);
        return args;
    }

    public static MainFragment newInstance(Bundle args) {
        MainFragment gameFragment = new MainFragment();
        gameFragment.setArguments(args);
        return gameFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new NullPointerException("Arguments is null!!!");
        }
        ListenerHelper.addListener(this, getFragmentType() + "");
    }

    @Override
    public void onDestroy() {
        unRegisterCountDbObserver();
        destroyTabList();
        super.onDestroy();
    }

    @Override
    protected void getParams() {
        defaultTab = getArguments().getInt("defaultTab", 0);
        mFrom = getArguments().getInt("from", 0);
    }

    @Override
    protected void initView() {
        mTabLayout = (TabLayout) getView().findViewById(R.id.tab_layout);
        mTabContainer = getView().findViewById(R.id.tab_container);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager = (MViewPager) getView().findViewById(R.id.viewpager);
        toolbar_title = (TextView) getView().findViewById(R.id.toolbar_title);
    }

    private void destroyTabList() {
        if (mTabList != null) mTabList.clear();
        mTabList = null;
    }

    private void initTabList() {
        mTabList = new ArrayList<>();
        mTabList.add(new TabInfo(PageId.PageMain.PAGE_HOME,
                mActivity.getString(R.string.tab_home),
                R.drawable.bg_tab_home));
        mTabList.add(new TabInfo(PageId.PageMain.PAGE_DEVICE,
                mActivity.getString(R.string.tab_device),
                R.drawable.bg_tab_device));
        mTabList.add(new TabInfo(PageId.PageMain.PAGE_ORDER,
                mActivity.getString(R.string.tab_order),
                R.drawable.bg_tab_order));
        mTabList.add(new TabInfo(PageId.PageMain.PAGE_MINE,
                mActivity.getString(R.string.tab_mine),
                R.drawable.bg_tab_mine));
    }

    @Override
    public void onResume() {
        if (!isResume) {
            registerCountDbObserver();
        }
        refreshMineCount();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void setupTab() {
        setupTab(null);
    }

    @Override
    public void setupTab(BaseInfo baseInfo) {
        if (hasSetupTab) return;
        hasSetupTab = true;
        destroyTabList();
        initTabList();
        if (mTabList != null && mTabList.size() > 0) {
            for (TabInfo tabInfo : mTabList) {
                mTitleList.add(new Pair(tabInfo.getNickname(), tabInfo.getTabPageId()));
                setupChildView(tabInfo);
            }
        }
        mViewPagerAdapter.notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabWithIcons(mTabList);
        setOnTabDoubleClickListener(this);
        mViewPager.setCurrentItem(defaultTab);
        if (defaultTab == 0)
            addStatistics(0);
    }

    private void setupChildView(TabInfo tabInfo) {
        Bundle args;
        int tabId = tabInfo.getTabPageId();
        switch (tabId) {
            case PageId.PageMain.PAGE_HOME:
                args = HomeFragment.newArgument(mFrom, tabId, tabId);
                mViewPagerAdapter.addItem(HomeFragment.class, args);
                break;
            case PageId.PageMain.PAGE_DEVICE:
                args = VarietyRecyclerFragment.newArgument(mFrom, tabId,
                        tabId, VarietyRecyclerFragment.LAYOUT_GRID_VERTICAL);
                mViewPagerAdapter.addItem(VarietyRecyclerFragment.class, args);
                break;
            case PageId.PageMain.PAGE_ORDER:
                args = VarietyRecyclerFragment.newArgument(mFrom, tabId,
                        tabId, VarietyRecyclerFragment.LAYOUT_GRID_VERTICAL);
                mViewPagerAdapter.addItem(VarietyRecyclerFragment.class, args);
                break;
            case PageId.PageMain.PAGE_MINE:
                args = MineFragment.newArgument(mFrom, tabId, tabId);
                mViewPagerAdapter.addItem(MineFragment.class, args);
                break;
        }
    }

    private void setupTabWithIcons(List<TabInfo> tabList) {
        if (tabList == null || tabList.size() == 0) return;
        for (int i = 0; i < tabList.size(); i++) {
            TabInfo tabInfo = tabList.get(i);
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tabInfo == null || tab == null) continue;
            tab.setCustomView(createTabView(tabInfo));
        }
        View view = mTabLayout.getTabAt(defaultTab).getCustomView();
        view.setSelected(true);
        view.requestLayout();
    }

    private View createTabView(TabInfo tabInfo) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_item_tab, null);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        iv_icon.setImageResource(tabInfo.getIconRes());
        tv_name.setText(tabInfo.getNickname());
        return view;
    }

    @Override
    protected void handlePageScrollStateChanged(int position) {
    }

    @Override
    protected void handlePageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    protected void handlePageSelected(int position) {
        super.handlePageSelected(position);
        BaseFragment fragment = mViewPagerAdapter.getItem(position);
        addStatistics(position);
        if (fragment != null) fragment.refreshFragmentSize();
    }

    public void addStatistics(int position) {
        if (mTabList != null && mTabList.size() > 0 && position < mTabList.size()) {
            TabInfo tabInfo = mTabList.get(position);
            toolbar_title.setText(tabInfo.getNickname());
            int tabId = tabInfo.getTabPageId();
            if (lastTabIndex != -1)
                AppLogUtil.addOpenViewLog(mActivity, tabId, PageId.PAGE_MAIN);
            else
                AppLogUtil.addOpenViewLog(mActivity, tabId, mFrom);
            lastTabIndex = position;
        }
    }

    @Override
    protected void handlerMyMessage(Message msg) {
        super.handlerMyMessage(msg);
        switch (msg.what) {
            case HANDLE_REFRESH_COUNT:

                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getFragmentType() {
        return PageId.PAGE_MAIN;
    }

    @Override
    public void handleSelfUpdateCallback(BaseInfo baseInfo) {
        super.handleSelfUpdateCallback(baseInfo);
        isAppUpdate = true;
        refreshMineCount();
    }

    private OnDbDataChangeObserver mUpdateDbObserver;

    private void registerCountDbObserver() {
//        Uri downloadUri = DownloadTask.CONTENT_URI;
//        mUpdateDbObserver = new OnDbDataChangeObserver();
//        mActivity.getContentResolver()
//                .registerContentObserver(downloadUri, true, mUpdateDbObserver);
    }

    private void unRegisterCountDbObserver() {
        if (mUpdateDbObserver != null) {
            mActivity.getContentResolver().unregisterContentObserver(mUpdateDbObserver);
        }
    }

    @Override
    public void onChange(int pageId, int status, BaseInfo info) {
        if (pageId == ListenerHelper.TYPE_PAGE_DEVICE_DETAIL) {
            BaseFragment baseFragment = mViewPagerAdapter.getItem(lastTabIndex);
            if (baseFragment instanceof VarietyRecyclerFragment) {
                VarietyRecyclerFragment fragment = (VarietyRecyclerFragment) baseFragment;
                if (fragment != null && fragment.isAdded()) {
                    if (AppUtil.checkResult(mActivity, status, fragment.getFragmentType())) {
                        PromptHelper.showToast(getResources().getString(R.string.success_delete_device));
                    } else {
                        PromptHelper.showToast(getResources().getString(R.string.error_delete_device));
                    }
                    fragment.loadData(false);
                }
            }
        } else if (pageId == ListenerHelper.TYPE_PAGE_DEVICE) {
            BaseFragment baseFragment = mViewPagerAdapter.getItem(lastTabIndex);
            if (baseFragment instanceof VarietyRecyclerFragment) {
                VarietyRecyclerFragment fragment = (VarietyRecyclerFragment) baseFragment;
                if (fragment != null && fragment.isAdded()) {
                    fragment.loadData(false);
                }
            }
        } else if (pageId == ListenerHelper.TYPE_PAGE_ORDER_CANCEL) {
            BaseFragment baseFragment = mViewPagerAdapter.getItem(lastTabIndex);
            if (baseFragment instanceof VarietyRecyclerFragment) {
                VarietyRecyclerFragment fragment = (VarietyRecyclerFragment) baseFragment;
                if (fragment != null && fragment.isAdded()) {
                    fragment.loadData(false);
                }
            }
        }
    }

    public void refreshPage(int i) {
        try {
            BaseFragment baseFragment = mViewPagerAdapter.getItem(i);
            if (baseFragment != null && baseFragment instanceof VarietyRecyclerFragment) {
                MineFragment fragment = (MineFragment) baseFragment;
                if (fragment != null && fragment.isAdded()) {
                    fragment.loadData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class OnDbDataChangeObserver extends ContentObserver {

        public OnDbDataChangeObserver() {
            super(mHandler);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub
            super.onChange(selfChange);
            refreshMineCount();
        }
    }

    private void refreshMineCount() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BaseFragment baseFragment = mViewPagerAdapter.getItem(lastTabIndex);
            if (baseFragment != null && baseFragment instanceof MineFragment) {
                MineFragment mineFragment = (MineFragment) baseFragment;
                return mineFragment.isEditMode();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
