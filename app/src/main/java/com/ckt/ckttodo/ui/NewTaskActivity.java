package com.ckt.ckttodo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.databinding.ActivityNewTaskBinding;
import com.ckt.ckttodo.widgt.TaskDateDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner mSpinnerTaskKinds;
    private Spinner mSpinnerTaskLevel;
    private Spinner mSpinnerTaskRemind;
    private Spinner mSpinnerTaskPlan;
    private LinearLayout mLinearLayoutScheduleTime;
    private TextView mTextViewScheduleTime;
    private TextView mTextViewPlanTime;
    private ArrayAdapter<String> mSpinnerTaskKindsAdapter;
    private ArrayAdapter<String> mSpinnerTaskLevelAdapter;
    private ArrayAdapter<String> mSpinnerTaskRemindAdapter;
    private ArrayAdapter<String> mSpinnerTaskPlanAdapter;
    private ActivityNewTaskBinding mActivityNewTaskBinding;
    private TaskDateDialog mTaskDateDialog;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    public static final String GET_PLAN_ID_FROM_PROJECT = "planId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_task));
//        setContentView(R.layout.activity_new_task);
        mActivityNewTaskBinding = DataBindingUtil.setContentView(NewTaskActivity.this, R.layout.activity_new_task);
        init();
//        initTimePicker();
    }

    private void init() {

        // 任务分类的下拉栏
        mSpinnerTaskKinds = mActivityNewTaskBinding.spinnerTaskChoose;
//        mSpinnerTaskKinds = (Spinner) findViewById(R.id.spinner_task_choose);

        String[] arrayTaskKinds = getResources().getStringArray(R.array.kind_list);
        mSpinnerTaskKindsAdapter = new ArrayAdapter<>(this, R.layout.common_text_item, arrayTaskKinds);
        mSpinnerTaskKindsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTaskKinds.setAdapter(mSpinnerTaskKindsAdapter);
        //任务等级的下拉栏

        mSpinnerTaskLevel = mActivityNewTaskBinding.spinnerTaskLevel;
        String[] arrayTaskLevel = getResources().getStringArray(R.array.task_level);
        mSpinnerTaskLevelAdapter = new ArrayAdapter<>(this, R.layout.common_text_item, arrayTaskLevel);
        mSpinnerTaskLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTaskLevel.setAdapter(mSpinnerTaskLevelAdapter);

        // 提醒时间下拉栏
        mSpinnerTaskRemind = mActivityNewTaskBinding.newSpinnerRemind;
        String[] arrayTaskRemind = getResources().getStringArray(R.array.task_remind);
        mSpinnerTaskRemindAdapter = new ArrayAdapter<String>(this, R.layout.common_text_item, arrayTaskRemind);
        mSpinnerTaskRemind.setAdapter(mSpinnerTaskRemindAdapter);


        mTextViewScheduleTime = mActivityNewTaskBinding.newTextScheduleTime;
        mTextViewScheduleTime.setText(mDateFormat.format(Calendar.getInstance().getTime()));

        //计划时间点击域
        mLinearLayoutScheduleTime = mActivityNewTaskBinding.newLinearScheduleTime;
        mLinearLayoutScheduleTime.setOnClickListener(this);

        mTaskDateDialog = new TaskDateDialog(this, new TaskDateDialog.ClickedSureListener() {
            @Override
            public void onClickedSureListener(Calendar cal) {

                mTextViewScheduleTime.setText(mDateFormat.format(cal.getTime()));
                mTaskDateDialog.dismiss();
            }
        });

        mTextViewPlanTime = mActivityNewTaskBinding.newEditPlanTime;


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sure:

                break;
            case R.id.menu_research:

                break;
            case android.R.id.home:
                finish();
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_linear_schedule_time:
                mTaskDateDialog.show();
                break;
        }
    }
}
