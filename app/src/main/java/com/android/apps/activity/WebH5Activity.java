package com.android.apps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.apps.fragment.BaseFragment;
import com.android.apps.util.AppLogUtil;
import com.android.apps.util.PageId;
import com.android.apps.web.ILoadUrlListener;
import com.android.apps.web.MWebView;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 16-5-30.
 */
public class WebH5Activity extends BaseActivity implements ILoadUrlListener {

    @Bind(R.id.layout_toolbar_back)
    LinearLayout layoutToolbarBack;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.webview)
    MWebView webview;
    private String mTitle;
    private String mUrl;
    private String mData;
    private int mFrom;
    private long mSId;

    private MWebView sView;
    private boolean hasError = false;
    private BaseInfo mBaseInfo;

    public static void actionWebH5ActivityActivity(Context context, String title, String url, int from, BaseInfo baseInfo) {
        Intent sIntent = new Intent(context, WebH5Activity.class);
        sIntent.putExtra("title", title);
        sIntent.putExtra("url", url);
        sIntent.putExtra("data", "");
        sIntent.putExtra("from", from);
        sIntent.putExtra("baseInfo", baseInfo);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Intent sIntent = getIntent();
        mTitle = sIntent.getStringExtra("title");
        mUrl = sIntent.getStringExtra("url");
        mData = sIntent.getStringExtra("data");
        mSId = sIntent.getLongExtra("sid", 0);
        mFrom = getIntent().getIntExtra("from", 0);
        mBaseInfo = getIntent().getParcelableExtra("baseInfo");

        setContentView(R.layout.layout_activity_webh5);
        ButterKnife.bind(this);
        sView = (MWebView) findViewById(R.id.webview);
        layoutToolbarBack.setVisibility(View.VISIBLE);
        sView.setLoadUrlListener(this);
        sView.setFromInfo(mFrom, mSId, 0);
        toolbarTitle.setText(mTitle);
        addStatistics();
        if (TextUtils.isEmpty(mUrl)) {
            sView.loadDataWithBaseURL(null, mData, "text/html", "UTF-8", null);
        } else {
            sView.loadUrl(mUrl);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (sView.canGoBack() && !sView.getUrl().equals(mUrl)) {
                sView.goBack();
                return true;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLoadFinish() {
        // TODO Auto-generated method stub
        if (hasError) {
            sView.setVisibility(View.GONE);
        } else {
            sView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadError() {
        // TODO Auto-generated method stub
        hasError = true;
    }

    @Override
    public void onLoadStart() {
        // TODO Auto-generated method stub
        hasError = false;
    }

    @Override
    protected void onResume() {
        if (sView != null) {
            sView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (sView != null)
            sView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (sView != null)
            sView.onPause();
        super.onPause();

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
        return mBaseInfo;
    }

    public void addStatistics() {
        AppLogUtil.addOpenViewLog(this, PageId.PAGE_WEB, mFrom, "-1", "-1");
    }

    @OnClick(R.id.layout_toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
