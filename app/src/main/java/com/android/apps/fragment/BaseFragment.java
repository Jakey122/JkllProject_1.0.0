package com.android.apps.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.android.apps.activity.BaseActivity;
import com.android.apps.listener.OnFragmentResumeListener;
import com.android.apps.manager.MFragmentManager;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.RequestResult;
import com.sdk.lib.net.NetworkStatus;
import com.sdk.net.IHttpListener;

/**
 * Created by root on 16-5-9.
 */
public abstract class BaseFragment extends Fragment implements View.OnTouchListener {

    protected BaseActivity mActivity;
    protected MyHandler mHandler;
    protected IHttpListener mHttpListener;
    protected OnFragmentResumeListener mResumeListener;

    protected int screenWidth;
    protected boolean isBannerExpaned = true;
    protected boolean isResume = false;
    protected boolean isNetworkConnect = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mHandler = new MyHandler();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        screenWidth = mActivity.getResources().getDisplayMetrics().widthPixels;
        getView().setOnTouchListener(this);
        MFragmentManager.keepFragment(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mResumeListener != null) mResumeListener.onFragmentResume(this, isResume);
        isResume = true;
        isNetworkConnect = NetworkStatus.getInstance(mActivity).isConnected();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeOnFragmentResumeListener();
        MFragmentManager.removeFragment(this);
        mHttpListener = null;
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
//        mHandler = null;
//        mActivity = null;
    }

    /**
     * 设置Fragment resume 监听
     *
     * @param listener
     */
    public void setOnFragmentResumeListener(OnFragmentResumeListener listener) {
        mResumeListener = listener;
    }

    /**
     * 获取Fragment resume 监听
     *
     * @return
     */
    public OnFragmentResumeListener getOnFragmentResumeListener() {
        return mResumeListener;
    }

    /**
     * 移除Fragment resume 监听
     */
    public void removeOnFragmentResumeListener() {
        mResumeListener = null;
    }


    /**
     * 直接从bundle里面取 避免type没有初始化就调用此方法
     *
     * @return
     */
    public int getFragmentType() {
        Bundle args = getArguments();
        return args == null ? 0 : args.getInt("type", 0);
    }

    /**
     * 获得RecyclerView 显示区域内第一个item的position
     *
     * @return
     */
    public abstract int getFirstItemPosition();

    /**
     * 获得RecyclerView 显示区域内最后一个item的position
     *
     * @return
     */
    public abstract int getLastItemPosition();

    /**
     * mHandler处理message
     *
     * @param msg
     */
    protected abstract void handlerMyMessage(Message msg);

    /**
     * 处理按键事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    public abstract boolean onKeyDown(int keyCode, KeyEvent event);

    /**
     * 处理嵌套View 高度
     */
    public abstract void handlerViewTreeObserver();

    /**
     * 加载外部数据
     */
    public abstract void setData(RequestResult result);

    /**
     * 处理网络状态变化
     *
     * @param state
     */
    public abstract void handleConnectionStateChanged(int state, boolean isConnect, boolean lastState);

    /**
     * 当网络变化时 处理异步网络数据请求
     *
     * @param state
     * @param isConnect
     * @param lastState
     */
    protected abstract void handleHttpListenerOnNetChanged(int state, boolean isConnect, boolean lastState);

    /**
     * 屏蔽Fragment点击事件穿透
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    /**
     * 提交评论
     *
     * @param appId
     * @param star
     * @param name
     * @param comment
     */
    public void postComment(long appId, int star, String name, String comment) {

    }

    public void handleSelfUpdateCallback(BaseInfo baseInfo) {

    }

    /**
     * 处理Viewholder动画
     */
    public void handleViewHolderAnimator() {

    }

    public void refreshFragmentSize() {

    }

    public boolean isLoading() {
        return false;
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlerMyMessage(msg);
        }
    }
}
