package com.android.apps.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.apps.util.AppLogUtil;
import com.android.apps.util.UiUtilities;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;

/**
 */
public class CustomAlertDialogBuilder implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    public Dialog mAlertDialog;
    private Context mContext;
    private ImageView mIcon;
    private FrameLayout mCustomView;
    private View mMainView;
    private boolean[] mCheckedItems;
    private int mCheckedItem;

    private int mListType;

    private static final int LIST_TYPE_SINGLE = 1;
    private static final int LIST_TYPE_MULTIPLY = 2;

    private DialogInterface.OnClickListener mPositiveListener,
            mNegativeListener, mNeutralListener, mListClickListener;

    private DialogInterface.OnMultiChoiceClickListener mMulListClickListener;

    public CustomAlertDialogBuilder(Context context) {
        // super(context);
        mContext = context;
        mMainView = View.inflate(mContext, R.layout.layout_alert_dialog, null);

        mIcon = UiUtilities.getView(mMainView, R.id.icon);
        mCustomView = UiUtilities.getView(mMainView, R.id.custom);

        UiUtilities.getView(mMainView, R.id.button1).setOnClickListener(this);
        UiUtilities.getView(mMainView, R.id.button2).setOnClickListener(this);
        UiUtilities.getView(mMainView, R.id.button3).setOnClickListener(this);

        ViewGroup.LayoutParams slparams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mAlertDialog = new Dialog(mContext, R.style.MCustomDialog);
        mAlertDialog.setContentView(mMainView, slparams);
        // super.setView(mMainView);
    }

    public CustomAlertDialogBuilder setOnCancelListener(
            final DialogInterface.OnCancelListener listener) {
        mAlertDialog.setOnCancelListener(listener);
        return this;
    }

    public CustomAlertDialogBuilder setTitle(int textResId) {
        return setTitle(mContext.getString(textResId));
    }

    public CustomAlertDialogBuilder setTitle(CharSequence text) {
        UiUtilities.setVisibilitySafe(mMainView, R.id.topPanel, View.VISIBLE);
        UiUtilities.setText(mMainView, R.id.alertTitle, text);
        return this;
    }

    public CustomAlertDialogBuilder setMessage(int textResId) {
        return setMessage(mContext.getString(textResId));
    }

    public CustomAlertDialogBuilder setMessage(CharSequence text) {
        TextView sTview = UiUtilities.getView(mMainView, R.id.message);
        UiUtilities.setVisibilitySafe(sTview, View.VISIBLE);
        sTview.setText(Html.fromHtml(text.toString()));
        sTview.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    public CustomAlertDialogBuilder setIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    public CustomAlertDialogBuilder setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }

    public CustomAlertDialogBuilder setView(View view) {
        mCustomView.addView(view);
        return this;
    }

    public CustomAlertDialogBuilder setPositiveButton(int textId,
                                                      DialogInterface.OnClickListener listener) {
        return setPositiveButton(mContext.getString(textId), listener);
    }

    public CustomAlertDialogBuilder setPositiveButton(CharSequence text,
                                                      DialogInterface.OnClickListener listener) {
        UiUtilities
                .setVisibilitySafe(mMainView, R.id.buttonPanel, View.VISIBLE);
        UiUtilities.setVisibilitySafe(mMainView, R.id.button1, View.VISIBLE);
        UiUtilities.setText(mMainView, R.id.button1, text);
        mPositiveListener = listener;
        return this;
    }

    public CustomAlertDialogBuilder setNegativeButton(int textId,
                                                      DialogInterface.OnClickListener listener) {
        return setNegativeButton(mContext.getString(textId), listener);
    }

    public CustomAlertDialogBuilder setNegativeButton(CharSequence text,
                                                      DialogInterface.OnClickListener listener) {
        UiUtilities
                .setVisibilitySafe(mMainView, R.id.buttonPanel, View.VISIBLE);
        mNegativeListener = listener;
        return this;
    }

    public CustomAlertDialogBuilder setNeutralButton(int textId,
                                                     DialogInterface.OnClickListener listener) {
        return setNeutralButton(mContext.getString(textId), listener);
    }

    public CustomAlertDialogBuilder setNeutralButton(CharSequence text,
                                                     DialogInterface.OnClickListener listener) {
        UiUtilities
                .setVisibilitySafe(mMainView, R.id.buttonPanel, View.VISIBLE);
//		UiUtilities.setVisibilitySafe(mMainView, R.id.button2, View.VISIBLE);
//		UiUtilities.setVisibilitySafe(mMainView, R.id.divide2, View.VISIBLE);
        UiUtilities.setText(mMainView, R.id.button2, text);
        mNeutralListener = listener;
        return this;
    }

    public CustomAlertDialogBuilder setSingleChoiceItems(int itemsId,
                                                         int checkedItem, DialogInterface.OnClickListener listener) {
        return setSingleChoiceItems(
                mContext.getResources().getStringArray(itemsId), checkedItem,
                listener);
    }

    public CustomAlertDialogBuilder setSingleChoiceItems(CharSequence[] items,
                                                         int checkedItem, DialogInterface.OnClickListener listener) {
        mListType = LIST_TYPE_SINGLE;
        mListClickListener = listener;
        mCheckedItem = checkedItem;
        ListView sListView = new ListView(mContext);
        sListView.setAdapter(new ListAdapter(mContext, items));
        sListView.setOnItemClickListener(this);
        setView(sListView);
        return this;
    }

    public CustomAlertDialogBuilder setMultiChoiceItems(int itemsId,
                                                        boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
        return setMultiChoiceItems(
                mContext.getResources().getStringArray(itemsId), checkedItems,
                listener);
    }

    public CustomAlertDialogBuilder setMultiChoiceItems(CharSequence[] items,
                                                        boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
        mListType = LIST_TYPE_MULTIPLY;
        mMulListClickListener = listener;
        mCheckedItems = checkedItems;
        ListView sListView = new ListView(mContext);
        sListView.setAdapter(new ListAdapter(mContext, items));
        sListView.setOnItemClickListener(this);
        setView(sListView);
        return this;
    }

    public Dialog create() {
        return mAlertDialog;
    }

    class ListAdapter extends BaseAdapter {
        private int listItemLayout;
        private Context mContext;
        private CharSequence[] mItems;
        private LayoutInflater mInflater;
        private TextView mTextView;

        public ListAdapter(Context context, CharSequence[] items) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mItems = items;

            switch (mListType) {
                case LIST_TYPE_SINGLE:
                    listItemLayout = R.layout.layout_dialog_select_singlechoice;
                    break;
                case LIST_TYPE_MULTIPLY:
                    listItemLayout = R.layout.layout_dialog_select_multichoice;
                    break;
                default:
                    listItemLayout = R.layout.layout_dialog_select_singlechoice;
                    break;
            }
        }

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(listItemLayout, null);
            }
            mTextView = (TextView) convertView.findViewById(android.R.id.text1);
            mTextView.setText(mItems[position]);
            mTextView.setTextSize(13);

            if (mListType == LIST_TYPE_SINGLE) {
                ((CheckedTextView) mTextView)
                        .setChecked(position == mCheckedItem);
            } else {
                ((CheckedTextView) mTextView)
                        .setChecked(mCheckedItems[position]);
            }
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long id) {
        if (mListType == LIST_TYPE_SINGLE) {
            mListClickListener.onClick(mAlertDialog, position);
            mAlertDialog.dismiss();
        } else {
            boolean isChecked = mCheckedItems[position] == true ? false : true;
            mCheckedItems[position] = isChecked;
            ((BaseAdapter) arg0.getAdapter()).notifyDataSetChanged();
            mMulListClickListener.onClick(mAlertDialog, position, isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if (mPositiveListener != null) {
                    mPositiveListener.onClick(mAlertDialog,
                            DialogInterface.BUTTON_POSITIVE);
                }
                break;
            case R.id.button2:
                if (mNeutralListener != null) {
                    mNeutralListener.onClick(mAlertDialog,
                            DialogInterface.BUTTON_NEUTRAL);
                    mAlertDialog.dismiss();
                    mAlertDialog.onDetachedFromWindow();
                }
                break;
            case R.id.button3:
                if (mNegativeListener != null) {
                    mNegativeListener.onClick(mAlertDialog,
                            DialogInterface.BUTTON_NEGATIVE);
                    mAlertDialog.dismiss();
                    mAlertDialog.onDetachedFromWindow();
                }
                break;
            default:
                break;
        }
    }

    protected void addDialogBtnClickLog(int pageId, BaseInfo info) {
        AppLogUtil.addClickViewLog(
                mCustomView.getContext(), 0, pageId, -1,
                info == null ? -1 : info.getOpenType());
    }
}
