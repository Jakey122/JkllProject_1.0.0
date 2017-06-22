package com.android.apps.viewholder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.apps.activity.DeviceDetailActivity;
import com.android.apps.adapter.BaseRecyclerAdapter;
import com.android.apps.control.BeanClickHelper;
import com.android.apps.dialog.DeleteDialog;
import com.android.apps.dialog.PayDialog;
import com.android.apps.helper.PromptHelper;
import com.android.apps.listener.OnCheckedChangeListener;
import com.android.apps.listener.OnDialogCallbackListener;
import com.android.apps.util.AppUtil;
import com.android.apps.util.PageId;
import com.sdk.bean.AppInfo;
import com.sdk.bean.BaseInfo;
import com.jkll.app.R;
import com.sdk.lib.util.SystemUtil;
import com.sdk.lib.util.Util;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;

/**
 * Created by root on 16-5-16.
 * <p/>
 */
public class OrderHolder extends BaseViewHolder implements View.OnClickListener {

    int mType, mSpanCount;
    View mItemView;
    OnCheckedChangeListener cclistener;
    private Handler mHandler;
    private TextView title;
    private TextView state;
    private TextView name;
    private TextView desc;
    private TextView price;
    private TextView cancel;
    private TextView pay;

    private Context context;
    private BaseInfo info;

    private long maxTime = System.currentTimeMillis() + 30 * 60 * 60 * 1000;

    public OrderHolder(View itemView, int appType, BaseRecyclerAdapter adapter) {
        super(itemView, appType);
        mAdapter = adapter;
        mHandler = new MyHandler();
    }

    @Override
    protected void initViewHolder(View itemView, Object... obj) {
        mType = (obj != null && obj.length > 0) ? (int) obj[0] : AppInfo.ITEM_TYPE_LIST;
        mItemView = itemView;
        title = (TextView) mItemView.findViewById(R.id.title);
        state = (TextView) mItemView.findViewById(R.id.state);
        name = (TextView) mItemView.findViewById(R.id.name);
        desc = (TextView) mItemView.findViewById(R.id.desc);
        price = (TextView) mItemView.findViewById(R.id.price);
        cancel = (TextView) mItemView.findViewById(R.id.cancel);
        pay = (TextView) mItemView.findViewById(R.id.pay);
    }

    @Override
    public void bindViewHolder(final Context context, final BaseInfo info, final View.OnClickListener listener) {
        this.context = context;
        bindViewHolderFromType(context, info, mAdapter.getType());
        this.info = info;
        mItemView.setTag(info);
        mItemView.setOnClickListener(listener);
        cancel.setOnClickListener(this);
        pay.setOnClickListener(this);
    }

    @Override
    public void onHolderRecycled() {
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

    private void bindViewHolderFromType(Context context, BaseInfo info, int type) {
        title.setText(info.getOname());
        name.setText(info.getNickname());
        desc.setText(info.getOdesc());
        price.setText(context.getResources().getString(R.string.order_price, info.getPrice()+""));
        if (info.getStatus() == BaseInfo.ORDER_STATE_NOTPAY){
            cancel.setVisibility(View.VISIBLE);
            pay.setVisibility(View.VISIBLE);
            cancel.setText(R.string.order_cancel);
            pay.setText(R.string.order_paynow);
            state.setTextColor(context.getResources().getColor(R.color.deep_red));
            maxTime = (info.getCtime() + 30 * 60 * 1000) - System.currentTimeMillis();
            if(maxTime > 0)
                startTime();
        } else if (info.getStatus() == BaseInfo.ORDER_STATE_PURCHASED){
            cancel.setVisibility(View.GONE);
            pay.setVisibility(View.VISIBLE);
            pay.setText(R.string.order_detail);
            state.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            state.setText(AppUtil.formatOrderDate(info.getOrderStartTime()));
        } else {
            cancel.setVisibility(View.GONE);
            pay.setVisibility(View.VISIBLE);
            pay.setText(R.string.order_detail);
            state.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            state.setText(AppUtil.formatOrderDate(info.getOrderStartTime()));
        }
    }

    private void startTime() {
        mHandler.removeCallbacks(mTimeRun);
        mHandler.postDelayed(mTimeRun, 1000);
    }

    private Runnable mTimeRun = new Runnable() {
        @Override
        public void run() {
            maxTime -= 1000;
            if(maxTime > 1000){
                state.setText(AppUtil.formatDate(maxTime));
                startTime();
            } else {
                mTimeRun = null;
            }
        }
    };

    public void setOnCheckChangeListener(OnCheckedChangeListener listener) {
        cclistener = listener;
    }

    private boolean isGridType() {
        return mType == AppInfo.ITEM_TYPE_GRID;
    }

    @Override
    public void showHolderAnimation() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            DeleteDialog.showDialog(context, info, new OnDialogCallbackListener() {
                @Override
                public void ok() {
                    doCancel();
                }

                @Override
                public void cancle() {

                }
            });
        } else if (v.getId() == R.id.pay) {
            if (info.getStatus() != BaseInfo.ORDER_STATE_NOTPAY)
                BeanClickHelper.openOrderDetail(context, info);
            else
                PayDialog.showDialog(context, 4, info);
        }
    }

    private void doCancel() {
        if (!com.sdk.util.Util.isNetAvailable(context)) {
            PromptHelper.showToast(R.string.netstate_unconnect);
            return;
        }
        HttpController.getInstance(context).cancelOrder(info.getPayoId(), new IHttpListener() {
            @Override
            public void handleCancelOrder(int statusCode, String orderid) {
                super.handleCancelOrder(statusCode, orderid);
                if(AppUtil.checkResult(context, PageId.PAGE_ORDER, statusCode))
                    PromptHelper.showToast(R.string.success_delete_order);
                else
                    PromptHelper.showToast(R.string.error_delete_order);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }
}
