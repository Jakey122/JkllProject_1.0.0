package com.android.apps.viewholder;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.apps.adapter.BaseRecyclerAdapter;
import com.android.apps.listener.OnCheckedChangeListener;
import com.sdk.bean.AppInfo;
import com.sdk.bean.BaseInfo;
import com.jkll.app.R;
import com.sdk.bean.QuestionInfo;

/**
 * Created by root on 16-5-16.
 * <p/>
 */
public class QuestionHolder extends BaseViewHolder implements View.OnClickListener {

    int mType, mSpanCount;
    View mItemView;
    OnCheckedChangeListener cclistener;
    private TextView title;
    private ImageView opt;
    private View layout_title;
    private View layout_desc;
    private TextView desc;
    private boolean isOpen;

    public QuestionHolder(View itemView, int appType, BaseRecyclerAdapter adapter) {
        super(itemView, appType);
        mAdapter = adapter;
    }

    @Override
    protected void initViewHolder(View itemView, Object... obj) {
        mType = (obj != null && obj.length > 0) ? (int) obj[0] : AppInfo.ITEM_TYPE_LIST;
        mItemView = itemView;
        title = (TextView)mItemView.findViewById(R.id.title);
        desc = (TextView)mItemView.findViewById(R.id.desc);
        layout_title = mItemView.findViewById(R.id.layout_title);
        layout_desc = mItemView.findViewById(R.id.layout_desc);
        opt = (ImageView)mItemView.findViewById(R.id.opt);
    }

    @Override
    public void bindViewHolder(final Context context, final BaseInfo info, final View.OnClickListener listener) {
        bindViewHolderFromType(context, info, mAdapter.getType());
        mItemView.setTag(info);
    }

    @Override
    public void onHolderRecycled() {
    }

    private void bindViewHolderFromType(Context context, BaseInfo info, int type) {
        if(info == null || !(info instanceof QuestionInfo)) return;
        title.setText(info.getQuestion());
        desc.setText(info.getAnswer());
        layout_title.setOnClickListener(this);
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
        if(v.getId() == R.id.layout_title){
            if(layout_desc.getVisibility() == View.GONE){
                layout_desc.setVisibility(View.VISIBLE);
                opt.setSelected(true);
            } else {
                layout_desc.setVisibility(View.GONE);
                opt.setSelected(false);
            }
        }
    }
}
