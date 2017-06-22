package com.android.apps.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.apps.App;
import com.android.apps.adapter.BaseRecyclerAdapter;
import com.android.apps.bean.LoadInfo;
import com.sdk.bean.PageInfo;
import com.android.apps.fragment.BaseRecyclerFragment;
import com.android.apps.view.LoadView;
import com.android.apps.viewholder.BaseViewHolder;
import com.android.apps.viewholder.FooterViewHolder;
import com.jkll.app.R;
import com.sdk.bean.RequestResult;
import com.sdk.lib.net.NetworkStatus;

/**
 * Created by root on 16-8-22.
 */
public class LoadViewHelper {

    public static final int LOAD_STATE_NORMAL = 0;
    public static final int LOAD_STATE_NETERROR = 1;
    public static final int LOAD_STATE_DATAERROR = 2;

    public static void initLoadView(LoadView mLoadView, int type) {
        if (mLoadView != null) {
            if (!NetworkStatus.getInstance(App.getInstance().getApplicationContext()).isConnected()) {
                mLoadView.setState(LoadView.STATE_NONET);
            } else {
                mLoadView.setState(LoadView.STATE_LOADING);
            }
        }
    }

    public static void refreshLoadView(LoadView mLoadView, int state) {
        if (mLoadView == null) return;
        mLoadView.setState(state);
    }

    public static void refreshLoadView(LoadView mLoadView, RequestResult result, BaseRecyclerFragment fragment, boolean showMore) {
        if (mLoadView == null || fragment == null) return;
        int type = fragment.getFragmentType();
        if (result.result != null && result.result.size() > 0) {
            if (result.result.size() == 1 && result.result.get(0) instanceof PageInfo && fragment.isEmpty()) {
                mLoadView.setState(LoadView.STATE_NODATA);
            } else {
                mLoadView.setState(LoadView.STATE_SUCCESS);
            }
        } else if (fragment.isEmpty() || !showMore) {
            if (!NetworkStatus.getInstance(App.getInstance().getApplicationContext()).isConnected()) {
                mLoadView.setState(LoadView.STATE_NONET);
            } else {
                mLoadView.setState(LoadView.STATE_NODATA);
            }
        }
    }

    public static LoadInfo getTargetLoadInfo(int state, BaseRecyclerFragment baseRecyclerFragment) {
        if (baseRecyclerFragment == null) return null;
        Context context = App.getInstance().getApplicationContext();
        switch (state) {
            case LoadView.STATE_LOADING:
                return new LoadInfo(context.getResources().getString(R.string.list_loading_normal));

            case LoadView.STATE_NONET:
                return new LoadInfo("网络已断开,点击设置");

            case LoadView.STATE_NODATA:
                return createNoDataInfo(baseRecyclerFragment);
        }
        return null;
    }

    protected static LoadInfo createNoDataInfo(final BaseRecyclerFragment fragment) {
        Context context = App.getInstance().getApplicationContext();
        return new LoadInfo("暂无数据,点击重试");
    }

    public static void refreshLoadingState(int state, BaseRecyclerFragment fragment) {
        if (fragment == null) return;

        FooterViewHolder footer = null;
        RecyclerView recyclerView = fragment.getRecyclerView();
        if (recyclerView == null) return;

        BaseRecyclerAdapter adapter = fragment.getAdapter();
        if (adapter == null) return;

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) return;

        View child = manager.findViewByPosition(adapter.getItemCount() - 1);
        if (child == null) return;

        BaseViewHolder viewHolder =
                (BaseViewHolder) recyclerView.getChildViewHolder(child);
        if (viewHolder != null && viewHolder instanceof FooterViewHolder) {
            footer = (FooterViewHolder) viewHolder;
        }
        if (footer == null) return;

        switch (state) {
            default:
            case LOAD_STATE_NORMAL:
                footer.onLoading();
                break;
            case LOAD_STATE_NETERROR:
                footer.onNetError();
                break;
            case LOAD_STATE_DATAERROR:
                footer.onDataError();
                break;
        }
    }
}
