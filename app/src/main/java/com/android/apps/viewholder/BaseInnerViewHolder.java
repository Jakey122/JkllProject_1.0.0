package com.android.apps.viewholder;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.apps.adapter.BaseRecyclerAdapter;
import com.android.apps.animation.AnimationUtil;
import com.android.apps.recycler.BaseItemDecoration;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

import java.util.List;

/**
 * Created by root on 16-5-17.
 * 一个嵌套 RecyclerView的ViewHolder
 */
public abstract class BaseInnerViewHolder extends BaseViewHolder {

    public static final int LAYOUT_LIST_VERTICAL = 0;
    public static final int LAYOUT_LIST_HORIZONTAL = 1;
    public static final int LAYOUT_GRID_VERTICAL = 2;
    public static final int LAYOUT_GRID_HORIZONTAL = 3;
    public static final int LAYOUT_STAGGERED_VERTICAL = 4;
    public static final int LAYOUT_STAGGERED_HORIZONTAL = 5;

    public static final int DEFAULT_SPANCOUNT = 2 * 3 * 4 * 5 * 6;

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected InnerRecyclerAdapter mInnerAdapter;
    protected BaseViewHolder mInnerViewHolder;

    protected View mTopDivide, mSubjectHeader;
    protected ImageView mSubjectIcon;
    protected TextView mSubjectTitle, mSubjectSummary;
    protected LinearLayout mBottomPanel;

    protected int mLayoutType;
    protected int mSpanCount;
    protected int mScreenWidth;

    protected BaseInfo mParentInfo;
    protected View.OnClickListener mListener;

    public BaseInnerViewHolder(View itemView, int layoutType, int spanCount, BaseRecyclerAdapter adapter) {
        super(itemView, layoutType, spanCount);
        mAdapter = adapter;
    }

    @Override
    protected void initViewHolder(View itemView, Object... obj) {
        mScreenWidth = itemView.getContext().getResources().getDisplayMetrics().widthPixels;
        mLayoutType = (obj != null && obj.length >= 1) ? (int) obj[0] : LAYOUT_LIST_VERTICAL;
        mSpanCount = (obj != null && obj.length >= 1) ? (int) obj[1] : 3;
        configLayoutManager(itemView.getContext());

        mTopDivide = itemView.findViewById(R.id.top_divide);
        mSubjectHeader = itemView.findViewById(R.id.subject_header);
        mSubjectIcon = (ImageView) itemView.findViewById(R.id.subject_icon);
        mSubjectTitle = (TextView) itemView.findViewById(R.id.subject_title);
        mSubjectSummary = (TextView) itemView.findViewById(R.id.subject_summary);

        mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mInnerAdapter = new InnerRecyclerAdapter(itemView.getContext(), mRecyclerView);
        mRecyclerView.setAdapter(mInnerAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mBottomPanel = (LinearLayout) itemView.findViewById(R.id.subject_bottom_panel);
    }

    @Override
    public void bindViewHolder(Context context, BaseInfo info, View.OnClickListener listener) {
        if (info == null) return;
        List<? extends BaseInfo> list = null;
        mParentInfo = info;
        mListener = listener;
        setSubjectTitle(info, mSubjectTitle, mSubjectSummary, mSubjectIcon);
        list = info.getInfoList();

        if (list != null && list.size() > 0) {
            mInnerAdapter.addAll(list);
        }

        //initBottomTabView(context, mInnerAdapter.getItemCount());
    }

    @Override
    public void onHolderRecycled() {
        mInnerAdapter.removeAll();
    }

    private void configLayoutManager(Context context) {
        switch (mLayoutType) {
            case LAYOUT_LIST_VERTICAL:
                mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                break;
            case LAYOUT_LIST_HORIZONTAL:
                mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                break;
            case LAYOUT_GRID_VERTICAL:
                mLayoutManager = new GridLayoutManager(context, mSpanCount, GridLayoutManager.VERTICAL, false);
                break;
            case LAYOUT_GRID_HORIZONTAL:
                mLayoutManager = new GridLayoutManager(context, mSpanCount, GridLayoutManager.HORIZONTAL, false);
                break;
            case LAYOUT_STAGGERED_VERTICAL:
                mLayoutManager = new StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.VERTICAL);
                break;
            case LAYOUT_STAGGERED_HORIZONTAL:
                mLayoutManager = new StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.HORIZONTAL);
                break;
        }
    }

    protected BaseViewHolder onCreateInnerViewHolder(Context context, LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        View v = createInnerViewHolder(context, layoutInflater, parent, viewType);
        mInnerViewHolder = new InnerViewHolder(v);
        return mInnerViewHolder;
    }

    protected void addItemDecoration(BaseItemDecoration itemDecoration) {
        if (mRecyclerView != null)
            mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void setupSpanSizeHandler() {
        if (mLayoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(new SpanSizeLookupHelper());
        }
    }

    /**
     * 处理GridLayoutManager item形态变化
     *
     * @param position
     * @return
     */
    protected int spanSizeHandler(int position) {
        if (!(mLayoutManager instanceof GridLayoutManager)) return 0;
        BaseInfo info = mInnerAdapter.getItem(position);
        int spanCount = info.getItemSpanCount();
        if (info.getItemViewType() == BaseInfo.ITEM_TYPE_GRID) {
            if (info.getItemViewType() == BaseInfo.ITEM_TYPE_GRID) {
                return DEFAULT_SPANCOUNT % spanCount == 0 ? DEFAULT_SPANCOUNT / spanCount : 3;
            }
            return DEFAULT_SPANCOUNT % spanCount == 0 ? DEFAULT_SPANCOUNT / spanCount : 3;
        }
        return ((GridLayoutManager) mLayoutManager).getSpanCount();
    }

    private int pageNumSpace = 3;

    protected void initBottomTabView(Context context, int maxIndex) {
        if (!(mLayoutManager instanceof LinearLayoutManager)) return;
        int orientation = ((LinearLayoutManager) mLayoutManager).getOrientation();
        if (orientation != LinearLayoutManager.HORIZONTAL) return;

        mBottomPanel.setVisibility(View.VISIBLE);
        mBottomPanel.setOrientation(LinearLayout.HORIZONTAL);
        mBottomPanel.removeAllViews();

        LinearLayout.LayoutParams sLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = AnimationUtil.dip2px(context, pageNumSpace);
        sLayoutParams.setMargins(margin, margin * 2, margin, margin);

        for (int i = 0; i < maxIndex; i++) {
            createTabSignViewItem(context, sLayoutParams);
        }
        refreshTabSignViewState(((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                refreshTabSignViewState(
                        ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() -
                                ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition());
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    protected void createTabSignViewItem(Context context, LinearLayout.LayoutParams layoutParams) {
        ImageView sImageView = new ImageView(context);
        sImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        sImageView.setLayoutParams(layoutParams);
        sImageView.setImageResource(R.drawable.iv_page_index_circle);
        mBottomPanel.addView(sImageView);
    }

    protected void refreshTabSignViewState(int pageIndex) {
        ImageView sImageView;
        for (int i = 0; i < mBottomPanel.getChildCount(); i++) {
            try {
                sImageView = (ImageView) mBottomPanel.getChildAt(i);
                if (i == pageIndex) {
                    sImageView.setSelected(true);
                } else {
                    sImageView.setSelected(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void setSubjectTitle(BaseInfo info, TextView mSubjectTitle, TextView mSubjectSummary, ImageView titleIcon);

    protected abstract View createInnerViewHolder(Context context, LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    protected abstract void initInnerViewHolder(View itemView);

    protected abstract void bindInnerViewHolder(Context context, BaseInfo info, View.OnClickListener listener);

    class InnerRecyclerAdapter extends BaseRecyclerAdapter {

        public InnerRecyclerAdapter(Context context, RecyclerView recyclerView) {
            super(context, recyclerView);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return onCreateInnerViewHolder(mContext, mLayoutInflater, parent, viewType);
        }

        public void removeAll() {
            mList.clear();
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            holder.bindViewHolder(mContext, getItem(position), this);
        }

        @Override
        protected void onItemClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public int getType() {
            return mAdapter.getType();
        }
    }

    class InnerViewHolder extends BaseViewHolder {

        public InnerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void initViewHolder(View itemView, Object... obj) {
            initInnerViewHolder(itemView);
        }

        @Override
        public void bindViewHolder(Context context, BaseInfo info, View.OnClickListener listener) {
            if (info == null) return;
            bindInnerViewHolder(context, info, listener);
        }

        @Override
        public void onHolderRecycled() {
        }
    }

    /**
     * 处理GridLayoutManager形态变化类
     */
    class SpanSizeLookupHelper extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            return spanSizeHandler(position);
        }
    }
}
