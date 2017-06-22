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
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.sdk.util.SPUtil;
import com.sdk.util.Util;
import com.jkll.app.R;

/**
 * Created by root on 16-7-2.
 */
public class CustomerDialog extends CustomAlertDialogBuilder implements DialogInterface.OnClickListener {

    int MAX_OPINION_TEXT_SIZE = 0;
    View customView;
    TextView message;

    private Context mContext;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void showDialog(Activity context) {
        if (context.isFinishing()) return;
        if (context.isDestroyed()) return;
        try {
            Dialog dialog = new CustomerDialog(context).create();
            dialog.setCancelable(false);
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
        } catch (Exception e) {
        }
    }

    public CustomerDialog(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        customView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_customer, null);
        setView(customView);
        initDialogView(context);
        setNegativeButton(R.string.btn_cancel, this);
        setPositiveButton(R.string.mine_customer_call, this);
        addStatistics(context);
    }

    private void initDialogView(final Context context) {
        message = (TextView) customView.findViewById(R.id.message);
        message.setText(SPUtil.getInstant(context).getString(Const.MINE_CUSTOMER_INFO, context.getResources().getString(R.string.mine_customer_num)));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                Util.doCall(mContext, message.getText().toString().replaceAll("-",""));
                break;
        }
    }

    public void addStatistics(Context mContext) {
        AppLogUtil.addOpenViewLog(mContext, PageId.PageMine.PAGE_MINE_CUSTOMER, PageId.PAGE_MINE, "-1", "-1");
    }
}
