package com.android.apps.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.apps.dialog.FeedbackDialog;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppUtil;
import com.sdk.bean.RequestResult;
import com.sdk.net.HttpController;

import com.android.apps.util.PageId;
import com.android.apps.view.MineFeedBackView;
import com.jkll.app.R;
import com.sdk.net.IHttpListener;
import com.sdk.util.Util;

/**
 * 设置 建议
 * Created by huangyongbiao on 2016-08-26 12:02.
 */
public class MineSettingAdviseFragment extends BaseFragment implements MineFeedBackView.IFeedBackHelper {

    public static final int MIN_CLICK_DELAY_TIME = 3000;
    private int mType;
    private int mFrom;
    private MineFeedBackView mFeedbackView;

    public static Bundle newArgument(int type, int from) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("from", from);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_base, container, false);
        return view;
    }

    public static MineSettingAdviseFragment newInstance(Bundle args) {
        MineSettingAdviseFragment fragment = new MineSettingAdviseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new NullPointerException("Arguments is null!!!");
        }
        mType = getArguments().getInt("type");
        mFrom = getArguments().getInt("from");
        mHttpListener = new HttpListener();
        initView();
    }

    private void initView() {
        ViewGroup parent = (ViewGroup) getView();
        parent.removeAllViews();
        switch (mType) {
            case PageId.PageMine.PAGE_MINE_FEEDBACK:
                mFeedbackView = new MineFeedBackView(mActivity, this);
                parent.addView(mFeedbackView);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFeedbackView != null)
            mFeedbackView.removeTextChangedListener();
    }

    @Override
    public void handlePostFeedback(String opinion, String contact) {
        HttpController.getInstance(mActivity).postFeedback(opinion, contact, mHttpListener);
    }

    @Override
    protected void handlerMyMessage(Message msg) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void handlerViewTreeObserver() {

    }

    @Override
    public void setData(RequestResult result) {

    }

    @Override
    public void handleConnectionStateChanged(int state, boolean isConnect, boolean lastState) {

    }

    @Override
    protected void handleHttpListenerOnNetChanged(int state, boolean isConnect,
                                                  boolean lastState) {
    }

    @Override
    public int getFirstItemPosition() {
        return 0;
    }

    @Override
    public int getLastItemPosition() {
        return 0;
    }

    class HttpListener extends IHttpListener {

        @Override
        public void handlePostFeedback(final int state) {
            super.handlePostFeedback(state);
            if(AppUtil.checkResult(mActivity, mType, state)){
                FeedbackDialog.showDialog(mActivity);
            } else {
                PromptHelper.showToast(R.string.error_feedback);
            }
        }
    }
}
