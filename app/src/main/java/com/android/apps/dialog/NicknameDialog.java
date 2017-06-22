package com.android.apps.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.apps.fragment.HomeFragment;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppLogUtil;
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.sdk.net.HttpController;
import com.sdk.util.Util;
import com.jkll.app.R;

/**
 * Created by root on 16-7-2.
 */
public class NicknameDialog extends CustomAlertDialogBuilder implements DialogInterface.OnClickListener {

    int MAX_OPINION_TEXT_SIZE = 10;
    View customView;
    EditText et_nickname;
    TextView tip;
    private Context mContext;

    public static final int TYPE_USER = 1;
    public static final int TYPE_DEVICE = 2;
    int type = TYPE_USER;
    String extra;
    Context context;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void showDialog(Activity context, int type, String extra) {
        if (context.isFinishing()) return;
        if (context.isDestroyed()) return;
        try {
            Dialog dialog = new NicknameDialog(context, type, extra).create();
            dialog.setCancelable(false);
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
        } catch (Exception e) {
        }
    }

    public NicknameDialog(Context context, int type, String extra) {
        super(context);
        mContext = context.getApplicationContext();
        mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        customView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_nickname, null);
        this.type = type;
        this.extra = extra;
        setView(customView);
        initDialogView(context);
        setNegativeButton(R.string.btn_cancel, this);
        setPositiveButton(R.string.btn_ok, this);
        addStatistics(context);
    }

    private void initDialogView(final Context context) {
        et_nickname = (EditText)customView.findViewById(R.id.et_nickname);
        tip = (TextView)customView.findViewById(R.id.tip);
        if(type == TYPE_USER){
            et_nickname.setHint(R.string.mine_nickname_hint);
        } else {
            et_nickname.setHint(R.string.device_detail_nickname_hint);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                doEdit(dialog);
                break;
        }
    }

    private void doEdit(DialogInterface dialog) {
        String nickname = et_nickname.getText().toString();
        if(!isReady(nickname)) return;
        if(type == TYPE_USER)
            HttpController.getInstance(mContext).updateUserInfo(nickname, null);
        else
            HttpController.getInstance(mContext).updateDevice(extra, nickname, null);
        dialog.dismiss();
    }

    private boolean isReady(String nickname){
        if(TextUtils.isEmpty(nickname) || nickname.trim().length() == 0){
            if(type == TYPE_USER)
                tip.setText(R.string.error_nickname_empty);
            else
                tip.setText(R.string.device_detail_nickname_empty);
            return false;
        }
        tip.setText("");
        if(!Util.isNetAvailable(mContext)){
            PromptHelper.showToast(mContext.getResources().getString(R.string.netstate_unconnect));
            return false;
        }
        return true;
    }

    public void addStatistics(Context mContext) {
        AppLogUtil.addOpenViewLog(mContext, PageId.PageMine.PAGE_MINE_CUSTOMER, PageId.PAGE_MINE, "-1", "-1");
    }
}
