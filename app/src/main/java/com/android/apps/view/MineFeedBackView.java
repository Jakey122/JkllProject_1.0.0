package com.android.apps.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.apps.helper.PromptHelper;
import com.sdk.util.Util;
import com.jkll.app.R;

/**
 * Created by root on 16-8-28.
 */
public class MineFeedBackView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private EditText edit_contact;
    private EditText edit_opinion;
    private TextView opinion_count;
    private TextView btn_submit;
    private IFeedBackHelper mHelper;

    public MineFeedBackView(Context context, IFeedBackHelper helper) {
        super(context);
        mHelper = helper;
        initView(context);
    }

    public MineFeedBackView(Context context) {
        super(context);
    }

    public MineFeedBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineFeedBackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MineFeedBackView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_fragment_mine_advise, this, true);
        edit_contact = (EditText) findViewById(R.id.edit_contact);
        edit_opinion = (EditText) findViewById(R.id.edit_opinion);
        edit_opinion.addTextChangedListener(this);
        opinion_count = (TextView) findViewById(R.id.opinion_count);
        opinion_count.setText(context.getString(R.string.feedback_content_text_count, 0));
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String contact = edit_contact.getText().toString().trim();
        String opinion = edit_opinion.getText().toString().trim();

        if ("".equals(contact)) {
            PromptHelper.showSnackMessage(MineFeedBackView.this,
                    R.string.contact_way);
            return;
        }
        if (!Util.checkMobileNumber(contact) && !Util.checkEmail(contact)) {
            PromptHelper.showSnackMessage(MineFeedBackView.this,
                    R.string.input_ok_contact_way);
            return;
        }
        if ("".equals(opinion)) {
            PromptHelper.showSnackMessage(MineFeedBackView.this,
                    R.string.input_opinion);
            return;
        }
        if (mHelper != null) mHelper.handlePostFeedback(opinion, contact);
        PromptHelper.showSnackMessage(MineFeedBackView.this, R.string.posted_feedback);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) return;
        String content = s.toString();
        int surplusCount = 100 - content.length();
        if (surplusCount < 0) {
            edit_opinion.setText(content.substring(0, content.length() - 1));
            edit_opinion.setSelection(edit_opinion.getText().toString().length());
            opinion_count.setText(opinion_count.getContext().getString(R.string.feedback_content_text_count, 100));
            return;
        }
        opinion_count.setText(opinion_count.getContext().getString(R.string.feedback_content_text_count, content.length()));
    }

    public void removeTextChangedListener() {
        if (edit_opinion != null)
            edit_opinion.removeTextChangedListener(this);
    }

    public interface IFeedBackHelper {

        void handlePostFeedback(String opinion, String contact);
    }
}
