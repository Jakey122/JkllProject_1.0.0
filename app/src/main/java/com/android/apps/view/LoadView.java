package com.android.apps.view;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.bean.LoadInfo;
import com.android.apps.fragment.BaseRecyclerFragment;
import com.jkll.app.R;
import com.sdk.lib.util.Util;

/**
 * Created by root on 16-6-21.
 */
public class LoadView extends RelativeLayout {

    public static final int STATE_LOADING = 1;
    public static final int STATE_NONET = 2;
    public static final int STATE_NODATA = 3;
    public static final int STATE_SUCCESS = 4;

    private ProgressBar mProgress;
    private TextView mName;
    private BaseRecyclerFragment mCurrentFragment;

    public LoadView(Context context) {
        super(context);
        initView(context);
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.layout_fragment_loadview, this);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mName = (TextView) view.findViewById(R.id.name);
    }

    public void setCurrentFragment(BaseRecyclerFragment fragment) {
        mCurrentFragment = fragment;
    }

    public void setState(final int state) {
        if (state == STATE_SUCCESS) {
            setVisibility(View.GONE);
            return;
        }

        if (mCurrentFragment != null) {
            if (mProgress == null) return;
            setVisibility(View.VISIBLE);
            LoadInfo mLoadInfo = mCurrentFragment.initLoadingState(state);
            if (mLoadInfo == null) return;
            String title = mLoadInfo.getNickname();
            mName.setVisibility((!TextUtils.isEmpty(title)) ? View.VISIBLE : View.GONE);
            mName.setText(Html.fromHtml(title));

            if (state == STATE_LOADING) {
                mProgress.setVisibility(VISIBLE);
            } else {
                mProgress.setVisibility(GONE);
            }
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnLoadViewClick(state);
                }
            });
        }
    }

    private void handleOnLoadViewClick(int state) {
        switch (state) {
            case STATE_NONET:
                Util.startSettings(getContext());
                break;
            case STATE_NODATA:
                if (mCurrentFragment != null) {
                    setState(STATE_LOADING);
                    mCurrentFragment.handleRefreshLoad();
                }
                break;
        }
    }
}
