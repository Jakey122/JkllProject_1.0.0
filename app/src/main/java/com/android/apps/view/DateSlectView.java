package com.android.apps.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.apps.animation.AnimationUtil;
import com.android.apps.fragment.HomeFragment;
import com.jkll.app.R;
import com.sdk.lib.util.UiUtil;

import java.util.Calendar;

/**
 * Created by root on 16-5-27.
 */
public class DateSlectView extends LinearLayout implements View.OnClickListener, View.OnScrollChangeListener {

    private OnDateChangeListener listener;
    private TextView year;
    private View month_left;
    private View month_right;
    private View year_left;
    private View year_right;
    private HorizontalScrollView scrollView;
    private LinearLayout layout_month;
    private int currentIndex;

    LinearLayoutCompat.LayoutParams monthParams;
    private int screenWitdth;
    private int itemWidth;
    private int currentYear;


    private LinearLayout mImages, mPoints;
    private int count, width, childWidth, margins, scrollSpace;

    public DateSlectView(Context context) {
        super(context);
        initView(context);
    }

    public DateSlectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DateSlectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateSlectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_date_select, this, true);
        year = (TextView) findViewById(R.id.year);
        year_left = findViewById(R.id.year_left);
        year_right = findViewById(R.id.year_right);
        month_left = findViewById(R.id.month_left);
        month_right = findViewById(R.id.month_right);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        layout_month = (LinearLayout) findViewById(R.id.layout_month);

        month_left.setOnClickListener(this);
        month_right.setOnClickListener(this);
        year_left.setOnClickListener(this);
        year_right.setOnClickListener(this);
        scrollView.setOnScrollChangeListener(this);
        scrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        screenWitdth = UiUtil.getScreenSize(getContext()).widthPixels - UiUtil.dip2px(getContext(), 45) * 2;
        itemWidth = screenWitdth/7;
        monthParams = new LinearLayoutCompat.LayoutParams(itemWidth, UiUtil.dip2px(getContext(), 20));
    }

    private void refreshYearView(){
        year.setText(getResources().getString(R.string.year, currentYear));
    }

    private void refreshMonthView(long delay){
        layout_month.removeAllViews();
        for (int i=0; i < 36; i++){
            createMonthView(i);
        }
        currentIndex = 12 + Calendar.getInstance().get(Calendar.MONTH);
        layout_month.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectChildView(currentIndex, true);
            }
        }, delay);
    }

    private void resetMonthView(){
        int count =  36;
        if(layout_month.getChildCount() > 0)
            count += 12;
        layout_month.removeAllViews();
        for (int i=0; i < count; i++){
            createMonthView(i);
        }
    }

    private void refreshMonthViewTag(){
        for (int i=0; i < layout_month.getChildCount(); i++){
            layout_month.getChildAt(i).setTag(i);
        }
    }

    public void addNewMonthView(int position){
        if(position == 0){
            for (int i=12; i >=0; i--){
                createMonthView(i);
            }
        } else {
            for (int i=0; i < 12; i++){
                createMonthView(i);
            }
        }

    }

    private void insertMonthView(int index){
        if(index <= 3){
            resetMonthView();
            selectChildView(index + 12, false);
        } else if(index >= layout_month.getChildCount()-4){
            resetMonthView();
            selectChildView(index - 12, false);
        } else {
            if(index % 12 == 0 && index > currentIndex){
                currentYear++;
                refreshYearView();
            } else if((index+1) % 12 == 0 && index < currentIndex){
                currentYear--;
                refreshYearView();
            }
            selectChildView(index, true);
        }
    }

    private void insertYearView(int index){
        if(index <= 3){
            resetMonthView();
            selectChildView(index + 12, false);
        } else if(index >= layout_month.getChildCount()-4){
            resetMonthView();
            selectChildView(index - 12, false);
        } else {
            selectChildView(index, true);
        }
    }

    private void createMonthView(int index){
        int month = index%12;
        TextView name = new TextView(getContext());
        name.setLayoutParams(monthParams);
        name.setText(getResources().getString(R.string.month, month+1));
        name.setGravity(Gravity.CENTER);
        name.setTag(index);
        name.setOnClickListener(this);
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, UiUtil.sp2px(getContext(), 14));
        name.setBackgroundResource(R.drawable.bg_button_month);
        layout_month.addView(name);
    }

    public void setOnDateChangeListener(OnDateChangeListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.month_left){
            insertMonthView(currentIndex - 1);
            return;
        } else if(view.getId() == R.id.month_right){
            insertMonthView(currentIndex + 1);
            return;
        } else if(view.getTag() != null && view.getTag() instanceof Integer){
            int index = (int)view.getTag();
            insertMonthView(index);
            return;
        }
        if(view.getId() == R.id.year_left){
            currentYear--;
            insertYearView(currentIndex-12);
        } else if(view.getId() == R.id.year_right){
            currentYear++;
            insertYearView(currentIndex+12);
        }
        refreshYearView();
    }

    //对点击选中的子控件进行设置
    private void selectChildView(int position, boolean smooth){
        if(currentIndex > layout_month.getChildCount()) return;
        TextView lastChild = (TextView)layout_month.getChildAt(currentIndex);
        lastChild.setSelected(false);
        lastChild.setTextSize(TypedValue.COMPLEX_UNIT_PX, UiUtil.sp2px(getContext(), 14));
        currentIndex = position;
        TextView currentView = (TextView)layout_month.getChildAt(currentIndex);
        currentView.setSelected(true);
        currentView.setGravity(Gravity.CENTER);
        currentView.setTextColor(R.color.colorPrimary);
        currentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, UiUtil.sp2px(getContext(), 16));
        if(smooth){
            int left=currentView.getLeft();     //获取点击控件与父控件左侧的距离
            int width=currentView.getMeasuredWidth();   //获得控件本身宽度
            int toX=left+width/2-screenWitdth/2;
            scrollView.smoothScrollTo(toX, 0);
        } else{
            int left=position * itemWidth;     //获取点击控件与父控件左侧的距离
            int width=currentView.getMeasuredWidth();   //获得控件本身宽度
            int toX = left - screenWitdth/2 + UiUtil.dip2px(getContext(), 45)/2;
            scrollView.scrollTo(toX, 0);
        }
    }

    @Override
    public void onScrollChange(View view, int x, int y, int ox, int oy) {
    }

    public void resetView() {
        refreshYearView();
        refreshMonthView(0l);
        if(listener != null)
            listener.onDateChange(currentYear+"", (currentIndex%12)+1 + "");
    }

    public interface OnDateChangeListener{
        public void onDateChange(String year, String date);
    }
}
