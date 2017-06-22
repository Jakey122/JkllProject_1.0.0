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

import com.android.apps.helper.PromptHelper;
import com.android.apps.listener.OnDialogCallbackListener;
import com.android.apps.util.AppLogUtil;
import com.android.apps.util.PageId;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.DeviceInfo;
import com.sdk.bean.OrderInfo;
import com.sdk.net.HttpController;
import com.sdk.util.Util;

/**
 * Created by root on 16-7-2.
 */
public class DeleteDialog extends CustomAlertDialogBuilder implements DialogInterface.OnClickListener {

    int MAX_OPINION_TEXT_SIZE = 0;
    View customView;
    TextView message;
    BaseInfo info;

    private Context mContext;
    private OnDialogCallbackListener listener;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void showDialog(Context context, BaseInfo info, OnDialogCallbackListener listener) {
        if (((Activity)context).isFinishing()) return;
        if (((Activity)context).isDestroyed()) return;
        try {
            Dialog dialog = new DeleteDialog(context, info, listener).create();
            dialog.setCancelable(false);
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
        } catch (Exception e) {
        }
    }

    public DeleteDialog(Context context, BaseInfo info, OnDialogCallbackListener listener) {
        super(context);
        mContext = context.getApplicationContext();
        customView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_customer, null);
        this.info = info;
        this.listener = listener;
        setView(customView);
        initDialogView(context);
        setNegativeButton(R.string.btn_cancel, this);
        setPositiveButton(R.string.btn_ok, this);
        addStatistics(context);
    }

    private void initDialogView(final Context context) {
        message = (TextView) customView.findViewById(R.id.message);
        message.setText(mContext.getResources().getString(R.string.device_delete, info != null?info.getNickname():""));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                if(listener != null)
                    listener.cancle();
                dialog.dismiss();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                doDelete(dialog);
                break;
        }
    }

    private void doDelete(DialogInterface dialog) {
        if(info == null) return;
        if(!Util.isNetAvailable(mContext)){
            PromptHelper.showToast(mContext.getString(R.string.netstate_unconnect));
            return;
        }
        if(listener != null)
            listener.ok();
        if(info instanceof DeviceInfo)
            HttpController.getInstance(mContext).deleteDevice(info.getDeviceId(), null);
        dialog.dismiss();
    }

    public void addStatistics(Context mContext) {
        AppLogUtil.addOpenViewLog(mContext, PageId.PageMine.PAGE_MINE_CUSTOMER, PageId.PAGE_MINE, "-1", "-1");
    }
}
