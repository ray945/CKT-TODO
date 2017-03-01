package com.ckt.ckttodo.widgt;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DateTime;

import java.util.Calendar;
import java.util.List;

/**
 * Created by mozre on 2/8/17.
 */

public class TaskDateDialog extends Dialog implements View.OnClickListener {

    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private TextView mTextViewPlan;
    private LinearLayout mLinearLayoutTask;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private Button mButtonSure;
    private ClickedSureListener mListener;
    private DateTime mDateTime = new DateTime();

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }

    public TaskDateDialog(Context context, ClickedSureListener l) {
        super(context);

        this.mListener = l;
    }

    protected TaskDateDialog(Context context, boolean cancelable, OnCancelListener cancelListener, ClickedSureListener l) {
        super(context, cancelable, cancelListener);
        this.mListener = l;
    }

    public TaskDateDialog(Context context, int themeResId, ClickedSureListener l) {
        super(context, themeResId);
        this.mListener = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_task_date);
        init();
    }

    private void init() {
        mTextViewDate = (TextView) findViewById(R.id.dialog_text_date);
        mTextViewTime = (TextView) findViewById(R.id.dialog_text_time);
        mDatePicker = (DatePicker) findViewById(R.id.dialog_date_picker);
        mTimePicker = (TimePicker) findViewById(R.id.dialog_time_picker);
        mButtonSure = (Button) findViewById(R.id.dialog_button_sure);
        mTextViewPlan = (TextView) findViewById(R.id.dialog_text_time_plan);
        mLinearLayoutTask = (LinearLayout) findViewById(R.id.dialog_linear_title);
        mTimePicker.setIs24HourView(true);
        mTextViewTime.setSelected(true);
        mTextViewDate.setSelected(false);
        mTextViewTime.setOnClickListener(this);
        mTextViewDate.setOnClickListener(this);
        mButtonSure.setOnClickListener(this);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDateTime.setHour(hourOfDay);
                mDateTime.setMinute(minute);
            }
        });
        Calendar cal = Calendar.getInstance();
        mDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDateTime.setYear(year);
                mDateTime.setMonth(monthOfYear);
                mDateTime.setDay(dayOfMonth);
            }
        });
    }


    public void show(boolean isOnlyShowDate) {
        super.show();
        if (isOnlyShowDate) {
            mTextViewPlan.setVisibility(View.VISIBLE);
            mLinearLayoutTask.setVisibility(View.GONE);
            mTimePicker.setVisibility(View.GONE);
            mDatePicker.setVisibility(View.VISIBLE);
        }
    }

    public void show(long dateMills){
        super.show();
        mTextViewPlan.setVisibility(View.VISIBLE);
        mLinearLayoutTask.setVisibility(View.GONE);
        mTimePicker.setVisibility(View.GONE);
        mDatePicker.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMills);
        mDatePicker.updateDate(calendar.YEAR, calendar.MONTH, calendar.WEEK_OF_MONTH);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_text_date:
                mTextViewDate.setSelected(true);
                mTextViewTime.setSelected(false);
                mTimePicker.setVisibility(View.INVISIBLE);
                mDatePicker.setVisibility(View.VISIBLE);
                break;
            case R.id.dialog_text_time:
                mTextViewDate.setSelected(false);
                mTextViewTime.setSelected(true);
                mTimePicker.setVisibility(View.VISIBLE);
                mDatePicker.setVisibility(View.INVISIBLE);
                break;
            case R.id.dialog_button_sure:
                passClickEvent();
                break;

        }
    }

    private void passClickEvent() {
        Calendar ca = Calendar.getInstance();
        ca.set(mDateTime.getYear(), mDateTime.getMonth()
                , mDateTime.getDay(), mDateTime.getHour(), mDateTime.getMinute());
        mListener.onClickedSureListener(ca);

    }

    public interface ClickedSureListener {
        void onClickedSureListener(Calendar cal);
    }
}
