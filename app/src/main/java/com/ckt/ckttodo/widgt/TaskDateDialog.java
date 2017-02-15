package com.ckt.ckttodo.widgt;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private Button mButtonSure;
    private ClickedSureListener mListener;
    private DateTime mDateTime = new DateTime();

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }

    public TaskDateDialog(Context context,ClickedSureListener l) {
        super(context);
        this.mListener = l;
    }

    protected TaskDateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public TaskDateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_task_date);
        init();
    }

    private void init() {
        mTextViewDate = (TextView) findViewById(R.id.dialog_text_date);
        mTextViewTime = (TextView) findViewById(R.id.dialog_text_time);
        mDatePicker = (DatePicker) findViewById(R.id.dialog_date_picker);
        mTimePicker = (TimePicker) findViewById(R.id.dialog_time_picker);
        mButtonSure = (Button) findViewById(R.id.dialog_button_sure);
        mTimePicker.setIs24HourView(true);
        mTextViewTime.setSelected(true);
        mTextViewDate.setSelected(false);
        mTextViewTime.setOnClickListener(this);
        mTextViewDate.setOnClickListener(this);
        mButtonSure.setOnClickListener(this);
        Log.d("KKK", "init: " + mDateTime.getYear() + " "
                + mDateTime.getMonth() + " " + mDateTime.getDay()
                + " " + mDateTime.getHour() + " " + mDateTime.getMinute());
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDateTime.setDay(hourOfDay);
                mDateTime.setMinute(minute);
                Log.d("KKK", "onTimeChanged: " + hourOfDay + " " + minute);
            }
        });
        Calendar cal = Calendar.getInstance();
        mDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDateTime.setYear(year);
                mDateTime.setMonth(monthOfYear+1);
                mDateTime.setDay(dayOfMonth);
                Log.d("KKK", "onDateChanged: " + year + " " + monthOfYear + " " + dayOfMonth);
            }
        });
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
        ca.set(mDateTime.getYear(),mDateTime.getMonth()
                ,mDateTime.getDay(),mDateTime.getHour(),mDateTime.getMinute());
        mListener.onClickedSureListener(ca);

    }

    public interface ClickedSureListener {
        void onClickedSureListener(Calendar cal);
    }
}
