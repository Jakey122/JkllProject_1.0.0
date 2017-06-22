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

import com.android.apps.util.AppLogUtil;
import com.android.apps.util.PageId;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

/**
 * Created by root on 16-7-2.
 */
public class DeviceUseMethodDialog extends CustomAlertDialogBuilder implements DialogInterface.OnClickListener {

    View customView;
    TextView desc;

    private Context mContext;
    private BaseInfo info;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void showDialog(Context context, BaseInfo info) {
        if (((Activity)context).isFinishing()) return;
        if (((Activity)context).isDestroyed()) return;
        try {
            Dialog dialog = new DeviceUseMethodDialog(context, info).create();
            dialog.setCancelable(false);
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
        } catch (Exception e) {
        }
    }

    public DeviceUseMethodDialog(Context context, BaseInfo info) {
        super(context);
        mContext = context.getApplicationContext();
        this.info = info;
        customView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_deviceusemethod, null);
        setView(customView);
        initDialogView(context);
        setNegativeButton(R.string.btn_cancel, this);
        setPositiveButton(R.string.btn_ok, this);
        addStatistics(context);
    }

    private void initDialogView(final Context context) {
        desc = (TextView) customView.findViewById(R.id.desc);
        desc.setText(info.getBaseShortDescript());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                dialog.dismiss();
                break;
        }
    }

    public void addStatistics(Context mContext) {
        AppLogUtil.addOpenViewLog(mContext, PageId.PageOrder.PAGE_ORDER_DETAIL, PageId.PAGE_MINE, "-1", "-1");
    }
}
