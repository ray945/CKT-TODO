package com.ckt.ckttodo.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ActivityNewTaskBinding;
import com.ckt.ckttodo.widgt.TaskDateDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.RealmResults;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner mSpinnerTaskKinds;
    private Spinner mSpinnerTaskLevel;
    private Spinner mSpinnerTaskRemind;
    private Spinner mSpinnerTaskPlan;
    private LinearLayout mLinearLayoutScheduleTime;
    private TextView mTextViewScheduleTime;
    private EditText mEditViewPlanTime;
    private TextView mTextViewConent;
    private ArrayAdapter<String> mSpinnerTaskKindsAdapter;
    private ArrayAdapter<String> mSpinnerTaskLevelAdapter;
    private ArrayAdapter<String> mSpinnerTaskRemindAdapter;
    private ArrayAdapter<String> mSpinnerTaskPlanAdapter;
    private ActivityNewTaskBinding mActivityNewTaskBinding;
    private TaskDateDialog mTaskDateDialog;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    private Calendar mCalendar;
    private RealmResults<Plan> mPlanList;
    public static final String GET_PLAN_ID_FROM_PROJECT = "planId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_task));
        getSupportActionBar().setElevation(0);
        mActivityNewTaskBinding = DataBindingUtil.setContentView(NewTaskActivity.this, R.layout.activity_new_task);
        init();
    }

    private void init() {

        // 任务分类的下拉栏
        mSpinnerTaskKinds = mActivityNewTaskBinding.spinnerTaskChoose;
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
                mCalendar = cal;
                mTextViewScheduleTime.setText(mDateFormat.format(cal.getTime()));
                mTaskDateDialog.dismiss();
            }
        });

        mEditViewPlanTime = mActivityNewTaskBinding.newEditPlanTime;
        mTextViewConent = mActivityNewTaskBinding.newEditConent;
//        mPlanList = DatebaseHelper.getInstance(.findAll(Plan.class));

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
                checkAndCommit();
                break;
            case R.id.menu_research:

                break;
            case android.R.id.home:
                finish();
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    private void checkAndCommit() {
        if (TextUtils.isEmpty(mTextViewConent.getText())) {
            return;
        }
        if(mCalendar == null){
            return;
        }
        EventTask task = new EventTask();
        task.setTaskContent(mTextViewConent.getText().toString());
        task.setTaskType(mSpinnerTaskKinds.getSelectedItemPosition() + 1);
        task.setTaskPriority(mSpinnerTaskLevel.getSelectedItemPosition() + 1);
        task.setTaskStartTime(mCalendar.getTimeInMillis());
        if(TextUtils.isEmpty(mEditViewPlanTime.getText())){
            task.setTaskPredictTime(20/60);
        }



        Log.d("KKK", "checkAndCommit: " + task.getTaskType());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_linear_schedule_time:
                mTaskDateDialog.show();
                break;
        }
    }

    /**
     *
     *
     * 重写dispachTochEvent用于编辑EditText时，用户点击非EditText区域，将软件盘隐藏
     *
     * @param ev
     * @return
     */


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断点击时间的位置，是否隐藏软件盘
     * @param v
     * @param event
     * @return
     */

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软件盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
