package com.android.apps.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.apps.adapter.BaseRecyclerAdapter;
import com.android.apps.dialog.DeleteDialog;
import com.android.apps.helper.ImageHelper;
import com.android.apps.listener.OnCheckedChangeListener;
import com.android.apps.util.AppUtil;
import com.daimajia.swipe.SwipeLayout;
import com.sdk.bean.AppInfo;
import com.sdk.bean.BaseInfo;
import com.jkll.app.R;
import com.sdk.bean.DeviceInfo;

/**
 * Created by root on 16-5-16.
 * <p/>
 */
public class DeviceHolder extends BaseViewHolder implements View.OnClickListener, SwipeLayout.SwipeListener {

    int mType, mSpanCount;
    View mItemView;
    OnCheckedChangeListener cclistener;
    View.OnClickListener onClickListener;
    TextView name;
    TextView state;
    ImageView icon;
    SwipeLayout swip;
    View layout_delete;

    View mTopDivider;
    View mBottomDivider;
    private boolean isCanClick = true;
    Context mContext;
    BaseInfo info;


    public DeviceHolder(View itemView, int appType, BaseRecyclerAdapter adapter) {
        super(itemView, appType);
        mAdapter = adapter;
    }

    @Override
    protected void initViewHolder(View itemView, Object... obj) {
        mType = (obj != null && obj.length > 0) ? (int) obj[0] : AppInfo.ITEM_TYPE_LIST;
        mItemView = itemView;
        name = (TextView) itemView.findViewById(R.id.name);
        state = (TextView) itemView.findViewById(R.id.state);
        icon = (ImageView) itemView.findViewById(R.id.icon);
        mTopDivider = itemView.findViewById(R.id.iv_top_divide);
        mBottomDivider = itemView.findViewById(R.id.iv_bottom_divide);
        swip = (SwipeLayout) itemView.findViewById(R.id.swip);
        layout_delete = itemView.findViewById(R.id.layout_delete);
    }

    @Override
    public void bindViewHolder(final Context context, final BaseInfo info, final View.OnClickListener listener) {
        mContext = context;
        bindViewHolderFromType(context, info, mAdapter.getType());
        this.info = info;
        mItemView.setTag(info);
        onClickListener = listener;
        layout_delete.setOnClickListener(this);
        swip.setRightSwipeEnabled(true);
        swip.setOnClickListener(this);
        swip.addSwipeListener(this);
    }

    @Override
    public void onHolderRecycled() {
    }

    private void bindViewHolderFromType(Context context, BaseInfo info, int type) {
        if(info instanceof DeviceInfo){
            DeviceInfo deviceInfo = (DeviceInfo)info;
            name.setText(deviceInfo.getNickname());
            state.setText(AppUtil.getDeviceStatus(context, info.getStatus()));
            state.setBackgroundResource(info.getStatus()==1?R.drawable.bg_state_using :R.drawable.bg_state_using);
            ImageHelper.getInstance(context).loadImage(deviceInfo.getLogo(), icon);
        }
        isCanClick = true;
    }

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
        if(v.getId() == R.id.layout_delete){
            DeleteDialog.showDialog(mContext, info, null);
        } else if(v.getId() == R.id.swip && onClickListener != null && isCanClick){
            if(swip.getOpenStatus() == SwipeLayout.Status.Open){
                swip.close(true);
            } else
                onClickListener.onClick(v);
        }
    }

    @Override
    public void onStartOpen(SwipeLayout layout) {
        isCanClick = false;
    }

    @Override
    public void onOpen(SwipeLayout layout) {
    }

    @Override
    public void onStartClose(SwipeLayout layout) {
        isCanClick = false;
    }

    @Override
    public void onClose(SwipeLayout layout) {

    }

    @Override
    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

    }

    @Override
    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
        swip.postDelayed(new Runnable() {
            @Override
            public void run() {
                isCanClick = true;
            }
        }, 100);
    }
}
