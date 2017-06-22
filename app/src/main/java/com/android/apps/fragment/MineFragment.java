package com.android.apps.fragment;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.apps.activity.AboutActivity;
import com.android.apps.activity.FeedbackActivity;
import com.android.apps.activity.LoginActivity;
import com.android.apps.activity.QuestionActivity;
import com.android.apps.dialog.CustomerDialog;
import com.android.apps.helper.PromptHelper;
import com.android.apps.util.AppUtil;
import com.android.apps.util.Const;
import com.android.apps.util.PageId;
import com.android.apps.view.RoundedImageView;
import com.jkll.app.R;
import com.sdk.bean.BaseInfo;
import com.sdk.bean.RequestResult;
import com.sdk.helper.ListenerHelper;
import com.sdk.helper.OnItemChangeListener;
import com.sdk.net.HttpController;
import com.sdk.net.IHttpListener;
import com.sdk.util.SPUtil;
import com.sdk.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class MineFragment extends BaseFragment implements OnItemChangeListener /*implements MineAdapter.OnRecyclerViewItemClickListener*/ {

    public static final int REQUEST_CODE = 1000;

    @Bind(R.id.icon)
    RoundedImageView icon;
    @Bind(R.id.nickname)
    TextView nickname;
    @Bind(R.id.layout_nickname)
    RelativeLayout layoutNickname;
    @Bind(R.id.layout_mine_question)
    LinearLayout layoutMineQuestion;
    @Bind(R.id.layout_mine_customer)
    LinearLayout layoutMineCustomer;
    @Bind(R.id.layout_mine_feedback)
    LinearLayout layoutMineFeedback;
    @Bind(R.id.layout_mine_about)
    LinearLayout layoutMineAbout;
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.modify)
    TextView modify;
    @Bind(R.id.layout_modify)
    RelativeLayout layoutModify;
    @Bind(R.id.layout_nickname_content)
    RelativeLayout layoutNicknameContent;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.layout_mine_exit)
    LinearLayout layoutMineExit;
    private int mType;
    private int mFrom;

    private IHttpListener mListener;
    private boolean isRequsting;

    public static Bundle newArgument(int type, int from, int layoutType) {
        Bundle args = new Bundle();
        args.putInt("mType", type);
        args.putInt("mFrom", from);
        args.putInt("layoutType", layoutType);
        return args;
    }

    public static MineFragment newInstance(Bundle bundle) {
        MineFragment fragment = new MineFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_mine_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new NullPointerException("arguments is null!!!");
        }
        mType = getArguments().getInt("mType", PageId.PAGE_MINE);
        mFrom = getArguments().getInt("mFrom");
        initView(getView());
        loadData();
    }

    protected void loadData() {
        if (!Util.isNetAvailable(mActivity)) {
            return;
        }
        HttpController.getInstance(mActivity).getMineInfo(mListener);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getFirstItemPosition() {
        return 0;
    }

    @Override
    public int getLastItemPosition() {
        return 0;
    }

    @Override
    protected void handlerMyMessage(Message msg) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void handlerViewTreeObserver() {

    }

    @Override
    public void setData(RequestResult list) {

    }

    @Override
    public void handleConnectionStateChanged(int state, boolean isConnect, boolean lastState) {

    }

    @Override
    protected void handleHttpListenerOnNetChanged(int state, boolean isConnect, boolean lastState) {

    }

    private void initView(View view) {
        mListener = new HttpListener();
        ListenerHelper.addListener(this, getFragmentType() + "");
        nickname.setText(SPUtil.getInstant(mActivity).getString(Const.USER_NICKNAME, getResources().getString(R.string.mine_nickname)));
        layoutModify.setVisibility(View.GONE);
        layoutNickname.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.icon, R.id.layout_nickname, R.id.layout_mine_question, R.id.layout_mine_customer, R.id.layout_mine_feedback, R.id.layout_mine_about, R.id.layout_mine_exit, R.id.modify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon:
                doEditIcon();
                break;
            case R.id.layout_nickname:
                AppUtil.showIme(etNickname, mHandler);
                layoutModify.setVisibility(View.VISIBLE);
                layoutNickname.setVisibility(View.GONE);
                break;
            case R.id.layout_mine_question:
                QuestionActivity.actionActivity(mActivity, PageId.PageMine.PAGE_MINE_QUESTION, PageId.PAGE_MINE);
                break;
            case R.id.layout_mine_customer:
                CustomerDialog.showDialog(mActivity);
                break;
            case R.id.layout_mine_feedback:
                FeedbackActivity.actionActivity(mActivity, PageId.PageMine.PAGE_MINE_FEEDBACK, PageId.PAGE_MINE);
                break;
            case R.id.layout_mine_about:
                AboutActivity.actionActivity(mActivity, PageId.PageMine.PAGE_MINE_ABOUT, PageId.PAGE_MINE);
                break;
            case R.id.layout_mine_exit:
                SPUtil.getInstant(mActivity).exit();
                LoginActivity.actionActivity(mActivity, PageId.PageUser.PAGE_LOGIN, mType);
                PromptHelper.showToast(R.string.success_exit);
                mActivity.finish();
                break;
            case R.id.modify:
                doEdit();
                break;
        }
    }

    private void doEditIcon() {
        PhotoPickerIntent intent = new PhotoPickerIntent(mActivity);
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        mActivity.startActivityForResult(intent, REQUEST_CODE);

    }

    private void doEdit() {
        String nickname = etNickname.getText().toString();
        if (!isReady(nickname)) return;
        HttpController.getInstance(mActivity).updateUserInfo(nickname, null);
    }

    private boolean isReady(String nickname) {
        if (TextUtils.isEmpty(nickname) || nickname.trim().length() == 0) {
            PromptHelper.showToast(R.string.error_nickname_empty);
            return false;
        }
        if (!Util.isNetAvailable(mActivity)) {
            PromptHelper.showToast(getResources().getString(R.string.netstate_unconnect));
            return false;
        }
        return true;
    }

    @Override
    public void onChange(int pageId, int status, BaseInfo info) {
        if (!isAdded()) return;
        if (mActivity == null || mActivity.isFinishing()) return;
        if (pageId == ListenerHelper.TYPE_PAGE_USERINFO) {
            if (status == 1) {
                SPUtil.getInstant(mActivity).save(Const.USER_NICKNAME, info.getNickname());
                nickname.setText(info.getNickname());
                PromptHelper.showToast(R.string.success_modify);
            } else {
                PromptHelper.showToast(R.string.error_modify);
            }
        }
        if (layoutModify != null)
            layoutModify.setVisibility(View.GONE);
        if (layoutNickname != null)
            layoutNickname.setVisibility(View.VISIBLE);
        if (etNickname != null)
            AppUtil.dismissIme(etNickname);
    }

    public boolean isEditMode() {
        if (layoutModify.getVisibility() == View.VISIBLE) {
            layoutModify.setVisibility(View.GONE);
            layoutNickname.setVisibility(View.VISIBLE);
            AppUtil.dismissIme(etNickname);
            return true;
        }
        return false;
    }

    private class HttpListener extends IHttpListener {
        @Override
        public void handleMineInfo(int status, BaseInfo baseInfo) {
            if (AppUtil.checkResult(mActivity, getFragmentType(), status) && baseInfo != null) {
                SPUtil.getInstant(mActivity).save(Const.MINE_CUSTOMER_INFO, baseInfo.getCustomerInfo());
                SPUtil.getInstant(mActivity).save(Const.MINE_ABOUT_INFO, baseInfo.getAboutInfo());
            }
        }
    }
}
