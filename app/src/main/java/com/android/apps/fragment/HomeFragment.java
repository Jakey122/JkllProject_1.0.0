package com.android.apps.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.activity.DeviceDetailActivity;
import com.android.apps.activity.WebH5Activity;
import com.android.apps.animation.AnimationUtil;
import com.android.apps.dialog.PayDialog;
import com.android.apps.helper.ImageHelper;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppUtil;
import com.android.apps.util.PageId;
import com.android.apps.view.DeviceView;
import com.android.apps.view.MViewPager;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.DeviceInfo;
import com.sdk.lib.net.NetworkStatus;
import com.sdk.lib.util.UiUtil;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;
import com.sdk.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeFragment extends BaseImageViewPagerFragment implements DeviceView.OnDevicePurchaseItemClickListener, View.OnClickListener /*implements MineAdapter.OnRecyclerViewItemClickListener*/ {

    @Bind(R.id.viewpager)
    MViewPager viewpager;
    @Bind(R.id.layout_indicator)
    LinearLayout layoutIndicator;
    @Bind(R.id.device_view)
    DeviceView deviceView;
    @Bind(R.id.loading_progress)
    ProgressBar loadingProgress;
    @Bind(R.id.loading_name)
    TextView loadingName;
    @Bind(R.id.layout_loading)
    RelativeLayout layoutLoading;

    private int mType;
    private int mFrom;
    private int current;

    private IHttpListener mListener;
    private boolean isRequsting;
    private LayoutInflater mInflater;
    private List<BaseInfo> mList = new ArrayList<BaseInfo>();
    private int bannerItemHeight;

    public static Bundle newArgument(int type, int from, int layoutType) {
        Bundle args = new Bundle();
        args.putInt("mType", type);
        args.putInt("mFrom", from);
        args.putInt("layoutType", layoutType);
        return args;
    }

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);
        ButterKnife.bind(this, view);
        mInflater = mActivity.getLayoutInflater();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutLoading.setVisibility(View.VISIBLE);
        loadingName.setText(R.string.list_loading_normal);
        layoutLoading.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(mList == null || mList.size() == 0);
    }

    public void loadData(boolean loading) {
        if (loading) {
            layoutLoading.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);
            loadingName.setText(R.string.list_loading_normal);
        } else {
            layoutLoading.setVisibility(View.GONE);
        }
        if (isRequsting) return;
        isRequsting = true;
        HttpController.getInstance(mActivity).getHomeSync(mListener);
    }

    @Override
    public int getFirstItemPosition() {
        return 0;
    }

    @Override
    public int getLastItemPosition() {
        return 0;
    }

    @Override
    protected void getParams() {
        mType = getArguments().getInt("mType");
        mFrom = getArguments().getInt("mFrom", PageId.PAGE_MAIN);
    }

    @Override
    protected void initView() {
        viewpager.setScroll(false);
        mViewPager = viewpager;
        bannerItemHeight = UiUtil.getScreenSize(mActivity).widthPixels / 3;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        layoutParams.height = bannerItemHeight + UiUtil.dip2px(mActivity, 90);
        deviceView.setOnDevicePurchaseItemClickListener(this);
    }

    @Override
    protected void setupTab() {
        mListener = new HttpListener();
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
        position = position % mViewPagerAdapter.getViewCount();
        refreshTabSignViewState(position);
        deviceView.refreshData(mList.get(position));
        current = position;
    }

    @Override
    protected void handlerMyMessage(Message msg) {
        super.handlerMyMessage(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void handlerViewTreeObserver() {

    }

    @Override
    public void handleConnectionStateChanged(int state, boolean isConnect, boolean lastState) {
        if(isConnect)
            loadData(mList == null || mList.size() == 0);
    }

    @Override
    protected void handleHttpListenerOnNetChanged(int state, boolean isConnect, boolean lastState) {

    }

    @Override
    public void onDevicePurchaseItemClick(BaseInfo info, int type, String year, String date) {
        if (!Util.isNetAvailable(mActivity)) {
            PromptHelper.showToast(R.string.netstate_unconnect);
            return;
        }
        if (type == PayDialog.TYPE_AUTH) {
            WebH5Activity.actionWebH5ActivityActivity(mActivity, getString(R.string.purchase_traffic_auth_title), info.getRealnameUrl(), mType, info);
            return;
        }
        PayDialog.showDialog(mActivity, type, info, year, date);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.layout_loading) {
            if (Util.isNetAvailable(mActivity))
                loadData(true);
            else
                com.sdk.lib.util.Util.startSettings(mActivity);
            return;
        }
        BaseInfo info = (BaseInfo) view.getTag();
        if (info != null && info instanceof DeviceInfo) {
            DeviceDetailActivity.actionActivity(mActivity, PageId.PageDevice.PAGE_DEVICE_DETAIL, getFragmentType(), info);
        }
    }

    public class HttpListener extends IHttpListener {
        @Override
        public void handleHomeDeviceList(int status, List<BaseInfo> list) {
            super.handleHomeDeviceList(status, list);
            isRequsting = false;
            if(!isAdded()) return;
            if(mActivity == null || mActivity.isFinishing()) return;
            if (AppUtil.checkResult(mActivity, getFragmentType(), status) && list != null && list.size() > 0) {
                layoutLoading.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.GONE);
                handleDeviceInfo(list);
            } else {
                loadingProgress.setVisibility(View.GONE);
                layoutLoading.setVisibility(View.VISIBLE);
                if (Util.isNetAvailable(mActivity))
                    loadingName.setText(R.string.list_loading_dataerror);
                else
                    loadingName.setText("网络已断开,点击设置");
            }
        }
    }

    private void handleDeviceInfo(List<BaseInfo> list) {
        layoutIndicator.removeAllViews();
        LinearLayout.LayoutParams sLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = AnimationUtil.dip2px(mActivity, 4);
        sLayoutParams.setMargins(margin, margin * 2, margin, margin);
        mList.clear();
        mList.addAll(list);
        mViewPagerAdapter.clear();
        for (BaseInfo info : mList) {
            createDeviceItem(info);
            createTabSignViewItem(sLayoutParams);
        }
        mViewPagerAdapter.notifyDataSetChanged();
        refreshTabSignViewState(current);
        handlePageSelected(current);
    }

    private void createDeviceItem(BaseInfo info) {
        View view = mInflater.inflate(R.layout.layout_item_device_banner, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView state = (TextView) view.findViewById(R.id.state);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        name.setText(info.getNickname());
        state.setText(AppUtil.getDeviceStatus(mActivity, info.getStatus()));
        state.setBackgroundResource(info.getStatus() == 1 ? R.drawable.ic_state_using : R.drawable.ic_state_disable);
        view.setTag(info);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) icon.getLayoutParams();
        layoutParams.height = bannerItemHeight;
        ImageHelper.getInstance(mActivity).loadImage(info.getLogo(), icon);
        view.setOnClickListener(this);
        mViewPagerAdapter.addItem(view);
    }

    /**
     * 创建一个第几页标记
     */
    private void createTabSignViewItem(LinearLayout.LayoutParams layoutParams) {
        ImageView sImageView = new ImageView(mActivity);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
