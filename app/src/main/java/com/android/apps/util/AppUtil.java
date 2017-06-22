package com.android.apps.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.apps.App;
import com.android.apps.activity.LoginActivity;
import com.android.apps.activity.MainActivity;
import com.android.apps.helper.PromptHelper;
import com.google.zxing.client.android.CaptureActivity;
import com.jkll.app.R;
import com.sdk.net.ResultHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 16-5-9.
 */
public class AppUtil {

    /**
     * 扫描二维码
     *
     * @param context
     */
    public static void actionCaptureActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivityForResult(intent, requestCode);
    }


    public static boolean checkResult(Context mContext, int mType, int resultStatus) {
        switch (resultStatus) {
            case ResultHandler.ResultCode.CODE_OK:
                return true;
            //  接口不存在 
            case ResultHandler.ResultCode.CODE_ERROR_NOMETHOD: {
                PromptHelper.showToast(R.string.errcode_nomethod);
                return false;
            }
            //  参数缺失 
            case ResultHandler.ResultCode.CODE_ERROR_PARAMS: {
                PromptHelper.showToast(R.string.errcode_params);
                return false;
            }
            //  参数格式错误 
            case ResultHandler.ResultCode.CODE_ERROR_PARAMS_FORMAT: {
                PromptHelper.showToast(R.string.errcode_params_format);
                return false;
            }
            //  数据库执行错误 
            case ResultHandler.ResultCode.CODE_ERROR_DATABASE: {
                PromptHelper.showToast(R.string.errcode_database);
                return false;
            }
            //  账号/密码错误 
            case ResultHandler.ResultCode.CODE_ERROR_ACCOUNT: {
                PromptHelper.showToast(R.string.errcode_account);
                return false;
            }
            //  操作对象不存在（例如设备不存在、订单不存在、账号不存在） 
            case ResultHandler.ResultCode.CODE_ERROR_NOOBJECT: {
                PromptHelper.showToast(R.string.errcode_noobject);
                return false;
            }
            //  验证码错误 
            case ResultHandler.ResultCode.CODE_ERROR_INVALIDCODE: {
                PromptHelper.showToast(R.string.errcode_invalidcode);
                return false;
            }
            //  验证码超时 
            case ResultHandler.ResultCode.CODE_ERROR_INVALIDCODE_TIMEOUT: {
                PromptHelper.showToast(R.string.errcode_invalidcode_timeout);
                return false;
            }
            //  新密码和原密码一样 
            case ResultHandler.ResultCode.CODE_ERROR_PASSWORD_SAME: {
                PromptHelper.showToast(R.string.errcode_password_same);
                return false;
            }
            //  注册账号已经存在 
            case ResultHandler.ResultCode.CODE_ERROR_ACCOUNT_EXIST: {
                PromptHelper.showToast(R.string.errcode_account_exist);
                return false;
            }
            //  超过次数（验证码） 
            case ResultHandler.ResultCode.CODE_ERROR_INVALIDCODE_TIMES: {
                PromptHelper.showToast(R.string.errcode_invalidcode_times);
                return false;
            }
            //  签名错误 
            case ResultHandler.ResultCode.CODE_ERROR_SIGN: {
                PromptHelper.showToast(R.string.errcode_sign);
                return false;
            }
            //  用户没有登陆
            case ResultHandler.ResultCode.CODE_ERROR_NOLOGIN: {
                PromptHelper.showToast(R.string.errcode_nologin);
                LoginActivity.actionActivity(mContext, PageId.PageUser.PAGE_LOGIN, mType);
                if(mContext instanceof MainActivity){
                    ((MainActivity)mContext).finish();
                }
                return false;
            }

        }
        return true;
    }

    public static String formatDate(long maxTime) {
        long min = (maxTime % (1000 * 60 * 60)) / (1000 * 60);
        long scond = (maxTime % (1000 * 60)) / 1000;
        return App.getInstance().getApplicationContext().getResources().getString(R.string.order_state_timer, min + "", scond + "");
    }

    public static String formatOrderDate(long maxTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static String getDeviceStatus(Context context, int status) {
        if (status == 1)
            return context.getResources().getString(R.string.device_state_using);
        else
            return context.getResources().getString(R.string.device_state_disable);
    }

    /**
     * 隐藏键盘
     */
    public static void dismissIme(final EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 显示键盘
     */
    public static void showIme(final EditText editText, Handler mHandler) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				//显示键盘
                m.showSoftInput(editText, 0);
            }
        }, 100);
    }
}
