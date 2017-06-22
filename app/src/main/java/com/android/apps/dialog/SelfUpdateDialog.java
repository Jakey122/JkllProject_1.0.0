package com.android.apps.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.apps.App;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppLogUtil;
import com.android.apps.util.PageId;
import com.chiclam.android.updater.Updater;
import com.chiclam.android.updater.UpdaterConfig;
import com.sdk.util.SPUtil;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

import anetwork.channel.download.DownloadManager;

/**
 * Created by root on 16-7-25.
 */
public class SelfUpdateDialog extends CustomAlertDialogBuilder implements DialogInterface.OnClickListener {

    private BaseInfo mBaseInfo;
    private View mCustomView;
    private TextView dialogMessage, dialogVersion;
    private int mVersionCode;
    private Context context;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void showSelfUpdateDialog(Activity context, BaseInfo baseInfo) {
        if (baseInfo == null) return;
        if (context.isFinishing()) return;
        if (context.isDestroyed()) return;
        try {
            Dialog SelfUpdateDialog = new SelfUpdateDialog(context, baseInfo).create();
            SelfUpdateDialog.setCancelable(false);
            SelfUpdateDialog.show();
        } catch (WindowManager.BadTokenException e) {
        } catch (Exception e) {
        }
    }

    public SelfUpdateDialog(Context context, BaseInfo baseInfo) {
        super(context);
        mBaseInfo = baseInfo;
        this.context = context;
        mCustomView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_update, null);
        setView(mCustomView);
        initDialogView(context);
        setTitle(R.string.dialog_title_update);
        setNegativeButton(R.string.btn_later, this);
        setPositiveButton(R.string.btn_update_rightnow, this);
        addStatistics(context);
    }

    private void initDialogView(Context context) {
        dialogMessage = (TextView) mCustomView.findViewById(R.id.message);
        dialogVersion = (TextView) mCustomView.findViewById(R.id.update_info);
        mVersionCode = mBaseInfo.getVersionCode();
        dialogMessage.setText(mBaseInfo.getSdesc());
        dialogVersion.setText(context.getResources().getString(R.string.update_new_version,
                mBaseInfo.getVersionName()));
    }

    public void addStatistics(Context mContext) {
        AppLogUtil.addOpenViewLog(mContext, PageId.PAGE_UPDATE,
                PageId.PAGE_MAIN, mVersionCode + "", "-1");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:  //取消
                SPUtil.getInstant(App.getInstance().getApplicationContext()).save(SPUtil.IsClickLater, true);
                dialog.dismiss();
                break;
            case DialogInterface.BUTTON_POSITIVE:  //复制
                SPUtil.getInstant(App.getInstance().getApplicationContext()).save(SPUtil.UpdateMethod, (int) 1);
                doUpdate(mBaseInfo.getApk());
                SPUtil.getInstant(App.getInstance().getApplicationContext()).save(SPUtil.IsClickLater, false);
                dialog.dismiss();
                break;
        }
    }

    public void doUpdate(String url){
        UpdaterConfig config = new UpdaterConfig.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setDescription(context.getResources().getString(R.string.system_download_description))
                .setFileUrl(url)
                .setCanMediaScanner(true)
                .build();
        Updater.get().showLog(true).download(config);

    }
}
