package com.android.apps.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;

import com.android.apps.clip.ClipImageActivity;
import com.android.apps.dialog.SelfUpdateDialog;
import com.android.apps.fragment.BaseFragment;
import com.android.apps.fragment.MainFragment;
import com.android.apps.fragment.MineFragment;
import com.android.apps.helper.PromptHelper;
import com.android.apps.listener.OnFragmentResumeListener;
import com.android.apps.listener.OnInitedListener;
import com.android.apps.manager.MFragmentManager;

import com.android.apps.service.CommonService;
import com.android.apps.util.AppUtil;
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.google.zxing.client.android.CaptureActivity;
import com.jkll.app.R;
import com.sdk.bean.AppInfo;
import com.sdk.bean.BaseInfo;
import com.sdk.helper.ImeiHelper;
import com.sdk.net.AsyncHttpClientHelper;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;
import com.sdk.util.Util;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPickerActivity;

public class MainActivity extends BaseActivity implements OnFragmentResumeListener {

    private MainFragment mFragment;         //fragment
    private long mBackTime;

    private int mFrom;
    private int mType;
    private int defaultIndex;
    private boolean isSelfUpdateInstall;
    private OnInitedListener mListener;

    public static void actionActivity(Context context, int type, int from, int defaultIndex) {
        Intent sIntent = new Intent(context, MainActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("from", from);
        sIntent.putExtra("defaultIndex", defaultIndex);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);
        mType = getIntent().getIntExtra("type", 0);
        mFrom = getIntent().getIntExtra("from", 0);
        defaultIndex = getIntent().getIntExtra("defaultIndex", 0);

        Bundle args = MainFragment.newArgument(defaultIndex, mFrom);
        mFragment = MainFragment.newInstance(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mFragment).commit();
        mFragment.setOnFragmentResumeListener(this);
        CommonService.actionCommonService(this, CommonService.ACTION_START);
        initUpdateListener();
        checkAppUpdate();
    }

    private void checkAppUpdate() {
        HttpController.getInstance(this).checkBeforeGroundAppUpdate(mHttpListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MFragmentManager.destroyFragment();
        ImeiHelper.getInstance().destroyHelper();
        AsyncHttpClientHelper.cancleAllRequest();
        mListener = null;
    }

    private void initUpdateListener() {
        if (mHttpListener == null) {
            mHttpListener = new IHttpListener() {
                @Override
                public void handleSelfUpdate(int status, BaseInfo baseInfo) {
                    super.handleSelfUpdate(status, baseInfo);
                    SelfUpdateDialog.showSelfUpdateDialog(MainActivity.this, baseInfo);
                }

                @Override
                public void handleUpdateUserPic(int statusCode) {
                    super.handleUpdateUserPic(statusCode);
                    if(AppUtil.checkResult(MainActivity.this, statusCode, mType))
                        PromptHelper.showToast(R.string.success_modify_icon);
                    if(mFragment != null)
                        mFragment.refreshPage(3);
                }
            };
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFragment.onKeyDown(keyCode, event)) {
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                long sTime = System.currentTimeMillis();
                if (sTime - mBackTime < 3000) {
                    finish();
                    return true;
                } else {
                    mBackTime = sTime;
                    PromptHelper.showToast(R.string.prompt_exit_app);
                    return true;
                }
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void handleMyMessage(Message msg) {
    }

    @Override
    protected BaseFragment getFragment() {
        return mFragment;
    }

    @Override
    public BaseInfo getBaseInfo() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_game, menu);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFragmentResume(BaseFragment baseFragment, boolean isResumed) {
        if (isResumed && mListener != null) {
            mListener.onMainViewInited();
        }
    }

    public void setInitedListener(OnInitedListener listener) {
        mListener = listener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Const.REQUEST_CODE_ADD_DEVICE && resultCode == RESULT_OK && data != null){
            BaseInfo info = data.getParcelableExtra("info");
            CaptureResultActivity.actionActivity(this, PageId.PageDevice.PAGE_DEVICE_CAPTURE_ADD_COMPLETE, PageId.PageDevice.PAGE_DEVICE_CAPTURE, info);
        } else if (resultCode == RESULT_OK && requestCode == MineFragment.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                if(photos != null && photos.size() > 0)
                    startCropImage(photos.get(0));
            }
        } else if (resultCode == RESULT_OK && requestCode == ClipImageActivity.CUT) {
            if (data != null) {
                String path = data.getStringExtra(ClipImageActivity.RESULT_PATH);
                if(!Util.isNetAvailable(this)){
                    PromptHelper.showToast(R.string.netstate_unconnect);
                    return;
                }
                if(!new File(path).exists()){
                    PromptHelper.showToast(R.string.error_modify_icon);
                    return;
                }
                PromptHelper.showToast(R.string.error_modify_uploading);
                HttpController.getInstance(this).updateUserPic(path, mHttpListener);
            }
        }
    }

    private void startCropImage(String path){
        Intent intent = new Intent(this, ClipImageActivity.class);
        intent.putExtra(ClipImageActivity.PASS_PATH, path);
        startActivityForResult(intent, ClipImageActivity.CUT);
    }
}
