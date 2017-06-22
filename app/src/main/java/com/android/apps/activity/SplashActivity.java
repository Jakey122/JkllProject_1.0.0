package com.android.apps.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.animation.AnimationUtil;
import com.android.apps.fragment.BaseFragment;
import com.android.apps.helper.ImageHelper;
import com.android.apps.util.PageId;
import com.android.apps.view.AutoScrollViewPager;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.lib.util.UiUtil;
import com.sdk.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 16-9-8.
 */
public class SplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.viewpager)
    AutoScrollViewPager viewpager;

    @Bind(R.id.start)
    TextView start;
    @Bind(R.id.layout_indicator)
    LinearLayout layoutIndicator;

    private static final int DURATION = 3000;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.layout_guide)
    RelativeLayout layoutGuide;
    @Bind(R.id.layout_welcom)
    RelativeLayout layoutWelcom;
    private SplashPagerAdapter mPagerAdapter;
    private int index;
    private ViewGroup.LayoutParams mLayoutParams;

    public static void actionActivity(Context context) {
        Intent sIntent = new Intent(context, SplashActivity.class);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_splash);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        boolean firstStart = SPUtil.getInstant(this).getBoolean(SPUtil.FIRST_ENTRY, true);
        if (firstStart) {
            layoutGuide.setVisibility(View.VISIBLE);
            layoutWelcom.setVisibility(View.GONE);
            initViewPager();
            initTabSignView();
            return;
        }
        layoutGuide.setVisibility(View.GONE);
        layoutWelcom.setVisibility(View.VISIBLE);
        String verify = SPUtil.getInstant(this).getVerify();
        if (TextUtils.isEmpty(verify)) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goLogin();
                }
            }, DURATION);
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goMain();
                }
            }, DURATION);
        }
    }

    private void initViewPager() {
        mPagerAdapter = new SplashPagerAdapter(this);
        viewpager.setAdapter(mPagerAdapter);
        viewpager.addOnPageChangeListener(this);
        viewpager.setStopScrollWhenTouch(true);
        viewpager.setCycle(true);
        viewpager.setInterval(DURATION);
        viewpager.setCurrentItem(index);
        viewpager.startAutoScroll(DURATION);
        toolbarTitle.setText(R.string.app_name);

        List<ImageView> mList = new ArrayList<ImageView>();
        ImageView screenAd1 = new ImageView(this);
        screenAd1.setImageResource(R.drawable.ic_splash_1);
        ImageView screenAd2 = new ImageView(this);
        screenAd2.setImageResource(R.drawable.ic_splash_2);
        ImageView screenAd3 = new ImageView(this);
        screenAd3.setImageResource(R.drawable.ic_splash_3);
        mList.add(screenAd1);
        mList.add(screenAd2);
        mList.add(screenAd3);
        mPagerAdapter.addAll(mList);

        int currentItem = Integer.MAX_VALUE / 2;
        currentItem = currentItem - ((Integer.MAX_VALUE / 2) % mList.size());
        viewpager.setCurrentItem(currentItem);

    }

    @OnClick(R.id.start)
    public void onViewClicked() {
        SPUtil.getInstant(this).save(SPUtil.FIRST_ENTRY, false);
        goLogin();
    }

    private void goLogin() {
        LoginActivity.actionActivity(this, PageId.PageMain.PAGE_HOME, PageId.PageAD.PAGE_AD_SPLASH);
        finish();
    }

    private void goMain() {
        MainActivity.actionActivity(this, PageId.PAGE_MAIN, PageId.PageAD.PAGE_AD_SPLASH, 0);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        refreshTabSignViewState(position % mPagerAdapter.getViewCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class SplashPagerAdapter extends PagerAdapter {

        private Context mContext;
        private List<ImageView> mList = new ArrayList<ImageView>();

        public SplashPagerAdapter(Context context) {
            mContext = context;
        }

        public void addAll(List<ImageView> list) {
            mList.clear();
            mList.addAll(list.size() > 3 ? list.subList(0, 3) : list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        public int getViewCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求模取出View列表中要显示的项
            if (mList == null || mList.size() == 0) return null;
            position %= mList.size();
            if (position < 0) {
                position = mList.size() + position;
            }
            View view = mList.get(position);
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
    }

    /**
     * 初始化页面标记
     */
    private void initTabSignView() {
        layoutIndicator.removeAllViews();
        LinearLayout.LayoutParams sLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = AnimationUtil.dip2px(this, 4);
        sLayoutParams.setMargins(margin, margin * 2, margin, margin);

        for (int i = 0; i < mPagerAdapter.getViewCount(); i++) {
            createTabSignViewItem(sLayoutParams);
        }
        refreshTabSignViewState(viewpager.getCurrentItem()%mPagerAdapter.getViewCount());
    }

    /**
     * 创建一个第几页标记
     */
    private void createTabSignViewItem(LinearLayout.LayoutParams layoutParams) {
        ImageView sImageView = new ImageView(this);
        sImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        sImageView.setLayoutParams(layoutParams);
        sImageView.setImageResource(R.drawable.iv_page_index_circle);
        layoutIndicator.addView(sImageView);
    }

    /**
     * 刷新第几页标记
     *
     * @param pageIndex
     */
    private void refreshTabSignViewState(int pageIndex) {
        ImageView sImageView;

        for (int i = 0; i < layoutIndicator.getChildCount(); i++) {
            try {
                sImageView = (ImageView) layoutIndicator.getChildAt(i);

                if (i == pageIndex) {
                    sImageView.setSelected(true);
                } else {
                    sImageView.setSelected(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewpager != null)
            viewpager.cancle();

    }

    @Override
    protected void handleMyMessage(Message msg) {

    }

    @Override
    protected BaseFragment getFragment() {
        return null;
    }

    @Override
    public BaseInfo getBaseInfo() {
        return null;
    }
}
