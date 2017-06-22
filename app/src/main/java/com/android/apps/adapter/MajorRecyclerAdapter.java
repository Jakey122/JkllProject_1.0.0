package com.android.apps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.apps.activity.BaseActivity;
import com.android.apps.viewholder.BaseViewHolder;
import com.android.apps.viewholder.DeviceAddHolder;
import com.android.apps.viewholder.DeviceHolder;
import com.android.apps.viewholder.EmptyViewHolder;
import com.android.apps.viewholder.OrderHolder;
import com.android.apps.viewholder.QuestionHolder;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.DeviceInfo;
import com.sdk.bean.OrderInfo;
import com.sdk.bean.QuestionInfo;

/**
 * Created by root on 16-5-9.
 */
public class MajorRecyclerAdapter extends BaseRecyclerAdapter {

    public MajorRecyclerAdapter(BaseActivity activity, long from, int type, RecyclerView recyclerView) {
        super(activity, recyclerView);
        mFrom = from;
        mType = type;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.layout_item_subject_innerrecycler, parent, false);
        BaseViewHolder holder = super.onCreateViewHolder(parent, viewType);
        if (holder != null) return holder;
        switch (viewType) {
            case BaseInfo.VISIBLE_TYPE_ITEM_ORDER:
                itemView = mLayoutInflater.inflate(R.layout.layout_item_order, parent, false);
                holder = new OrderHolder(itemView, viewType, this);
                break;
            case BaseInfo.VISIBLE_TYPE_ITEM_QUESTION:
                itemView = mLayoutInflater.inflate(R.layout.layout_item_question, parent, false);
                holder = new QuestionHolder(itemView, viewType, this);
                break;
            case BaseInfo.VISIBLE_TYPE_ITEM_DEVICE:
                itemView = mLayoutInflater.inflate(R.layout.layout_item_device, parent, false);
                holder = new DeviceHolder(itemView, viewType, this);
                break;
            case BaseInfo.VISIBLE_TYPE_ITEM_DEVICE_ADD:
                itemView = mLayoutInflater.inflate(R.layout.layout_item_device_add, parent, false);
                holder = new DeviceAddHolder(itemView, viewType, this);
                break;
        }
        if (holder == null) {
            TextView loading = new TextView(parent.getContext());
            holder = new EmptyViewHolder(loading, this);
        }
        keepViewHolder(holder);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        BaseInfo info = getItem(position);
        if (info instanceof DeviceInfo){
            if(info.getOpenType() == BaseInfo.OPEN_DEVICE_ADD)
                return BaseInfo.VISIBLE_TYPE_ITEM_DEVICE_ADD;
            return BaseInfo.VISIBLE_TYPE_ITEM_DEVICE;
        } else if (info instanceof OrderInfo) {
            return BaseInfo.VISIBLE_TYPE_ITEM_ORDER;
        } else if (info instanceof QuestionInfo) {
            return BaseInfo.VISIBLE_TYPE_ITEM_QUESTION;
        }
        return super.getItemViewType(position);
    }

    @Override
    protected void onItemClick(View v) {
        super.onItemClick(v);
    }

    @Override
    public int getType() {
        return mType;
    }

}
