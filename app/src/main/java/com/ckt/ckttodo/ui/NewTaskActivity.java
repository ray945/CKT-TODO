package com.ckt.ckttodo.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ActivityNewTaskBinding;
import com.ckt.ckttodo.util.Constants;
import com.ckt.ckttodo.widgt.TaskDateDialog;
import com.iflytek.cloud.thirdparty.V;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner mSpinnerTaskKinds;
    private Spinner mSpinnerTaskLevel;
    private Spinner mSpinnerTaskRemind;
    private AutoCompleteTextView mAutoCompleteTextViewTaskPlan;
    private EditText mTextViewTaskPlan;
    private LinearLayout mLinearLayoutInput;
    private LinearLayout mLinearLayoutScheduleTime;
    private ScrollView mScrollViewTaskListContainer;
    private TextView mTextViewScheduleTime;
    private EditText mEditViewPlanTime;
    private TextView mTextViewContent;
    private TextView mTextViewTitle;
    private ArrayAdapter<String> mSpinnerTaskKindsAdapter;
    private ArrayAdapter<String> mSpinnerTaskLevelAdapter;
    private ArrayAdapter<String> mSpinnerTaskRemindAdapter;
    private ActivityNewTaskBinding mActivityNewTaskBinding;
    private TaskDateDialog mTaskDateDialog;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private Calendar mCalendar = Calendar.getInstance();
    private Map<String, String> mPlanList = new HashMap<>();
    private String[] mPlans;
    private EventTask mTask;
    public static final String GET_PLAN_ID_FROM_PROJECT = "planId";
    private static final String TASK_BELONG_NONE = "plan";
    public static final String PASS_TASK_ID = "task_id";
    public static final String VOICE_INPUT = "voice_input";
    public static final int MODIFY_TASK_RESULT_CODE = 40;
    private boolean isEditMode = false;

    DatebaseHelper mHelper;
    private String mTaskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_task));
        mActivityNewTaskBinding = DataBindingUtil.setContentView(NewTaskActivity.this, R.layout.activity_new_task);
        mHelper = DatebaseHelper.getInstance(this);
        getPlanData();
        init();
        getIntentData();
    }


    private void init() {


        mScrollViewTaskListContainer = mActivityNewTaskBinding.taskListContainer;

        // 任务分类的下拉栏
        mSpinnerTaskKinds = mActivityNewTaskBinding.spinnerTaskChoose;
        String[] arrayTaskKinds = getResources().getStringArray(R.array.kind_list);
        mSpinnerTaskKindsAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, arrayTaskKinds);
        mSpinnerTaskKindsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTaskKinds.setAdapter(mSpinnerTaskKindsAdapter);
        //任务等级的下拉栏
        mSpinnerTaskLevel = mActivityNewTaskBinding.spinnerTaskLevel;
        String[] arrayTaskLevel = getResources().getStringArray(R.array.task_level);
        mSpinnerTaskLevelAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, arrayTaskLevel);
        mSpinnerTaskLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTaskLevel.setAdapter(mSpinnerTaskLevelAdapter);
        // 提醒时间下拉栏
        mSpinnerTaskRemind = mActivityNewTaskBinding.newSpinnerRemind;
        String[] arrayTaskRemind = getResources().getStringArray(R.array.task_remind);
        mSpinnerTaskRemindAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, arrayTaskRemind);
        mSpinnerTaskRemind.setAdapter(mSpinnerTaskRemindAdapter);

        mEditViewPlanTime = mActivityNewTaskBinding.newEditPlanTime;
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

        mAutoCompleteTextViewTaskPlan = mActivityNewTaskBinding.newTextPlan;
        mAutoCompleteTextViewTaskPlan.setDropDownHeight(700);
        mAutoCompleteTextViewTaskPlan.setThreshold(1);


        mAutoCompleteTextViewTaskPlan.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mPlans));
        mTextViewTaskPlan = mActivityNewTaskBinding.newTextShowPlan;
        mLinearLayoutInput = mActivityNewTaskBinding.linearInputPlan;
        mTextViewTaskPlan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mLinearLayoutInput.setVisibility(View.VISIBLE);
                    mScrollViewTaskListContainer.setVisibility(View.INVISIBLE);
                    mAutoCompleteTextViewTaskPlan.showDropDown();
                }

            }
        });

        mTextViewContent = mActivityNewTaskBinding.newEditConent;
        mTextViewTitle = mActivityNewTaskBinding.newEditTitle;


    }

    /**
     * get plan list form database
     */
    private void getPlanData() {
        RealmResults<Plan> plans = mHelper.findAll(Plan.class);
        Plan plan;
        mPlans = new String[plans.size()];
        for (int i = 0; i < plans.size(); ++i) {
            plan = plans.get(i);
            mPlanList.put(plan.getPlanName(), plan.getPlanId());
            mPlans[i] = plan.getPlanName();
        }

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
                if (mLinearLayoutInput.getVisibility() == View.VISIBLE) {
                    finishInputPlan();
                } else {
                    checkAndCommit();
                }
                break;
            case android.R.id.home:
                if (mLinearLayoutInput.getVisibility() == View.VISIBLE) {
                    finishInputPlan();
                } else {
                    finish();
                }
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * commit new task
     */

    private void checkAndCommit() {
        if (TextUtils.isEmpty(mTextViewTitle.getText())) {
            Toast.makeText(this, getResources().getString(R.string.task_not_empty_content), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mEditViewPlanTime.getText())) {
            Toast.makeText(this, getResources().getString(R.string.task_not_empty_plan_time), Toast.LENGTH_SHORT).show();
            return;
        }
        if (Float.valueOf(mEditViewPlanTime.getText().toString()).compareTo(new Float(0)) <= 0) {
            Toast.makeText(this, getResources().getString(R.string.task_not_empty_plan_time), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCalendar == null) {
            return;
        }
        EventTask task = new EventTask();
        if (mTaskID == null) {
            String taskID = UUID.randomUUID().toString();
            task.setTaskId(taskID);
        } else {
            task.setTaskId(mTaskID);
            Realm realm = mHelper.getRealm();
            EventTask deleteTask = realm.where(EventTask.class).contains(EventTask.TASK_ID, mTaskID).findFirst();
            realm.beginTransaction();
            deleteTask.removeFromRealm();
            realm.commitTransaction();
        }
        task.setTaskStatus(EventTask.NOT_START);
        task.setTaskTitle(mTextViewTitle.getText().toString());
        task.setTaskContent(TextUtils.isEmpty(mTextViewContent.getText()) ? "" : mTextViewContent.getText().toString());
        task.setTaskType(mSpinnerTaskKinds.getSelectedItemPosition() + 1);
        task.setTaskPriority(mSpinnerTaskLevel.getSelectedItemPosition() + 1);
        task.setTaskStartTime(mCalendar.getTimeInMillis());
        task.setTaskPredictTime(Float.valueOf(mEditViewPlanTime.getText().toString()));
        task.setTaskRemindTime(getRemindTime(mSpinnerTaskRemind.getSelectedItemPosition()));
        if (TextUtils.isEmpty(mTextViewTaskPlan.getText()) || mTextViewTaskPlan.getText().toString().replace(" ", "").length() < 1) {
            task.setPlanId(TASK_BELONG_NONE);
        } else {
            String planID;
            String planName = mTextViewTaskPlan.getText().toString();
            planName = planName.replace(" ", "");
            if (mPlanList.containsKey(planName)) {
                planID = mPlanList.get(planName);
                task.setPlanId(planID);
            } else {
                // new plan
                planID = makeNewPlan(planName);
                task.setPlanId(planID);
            }


            Plan plan = mHelper.getRealm().where(Plan.class).equalTo("planId", planID).findFirst();
            mHelper.getRealm().beginTransaction();
            task.setPlan(plan);
            plan.getEventTasks().add(task);
            mHelper.getRealm().commitTransaction();
            if (isEditMode) {
                Toast.makeText(this, getResources().getString(R.string.task_modify_successful), Toast.LENGTH_SHORT).show();
                setResult(MODIFY_TASK_RESULT_CODE);
            } else {
                Toast.makeText(this, getResources().getString(R.string.new_task_successful), Toast.LENGTH_SHORT).show();
            }

            finish();
        }
        mHelper.insert(task);
        Toast.makeText(this, getResources().getString(R.string.new_task_successful), Toast.LENGTH_SHORT).show();
        finish();
    }


    /**
     * Create a new Plan in Database
     *
     * @param planName
     * @return
     */

    private String makeNewPlan(String planName) {

        String planID;
        Plan newPlan = new Plan();
        planID = UUID.randomUUID().toString();
        newPlan.setPlanId(planID);
        newPlan.setPlanName(planName);
        newPlan.setCreateTime(Calendar.getInstance().getTimeInMillis());
        mHelper.insert(newPlan);
        return planID;
    }

    /**
     * turn text to time
     *
     * @param position
     * @return
     */

    private long getRemindTime(int position) {
        switch (position) {
            case 0:
                return Constants.TEN_MIN_TO_SEC;
            case 1:
                return Constants.TWENTY_MIN_TO_SEC;
            case 2:
                return Constants.HALF_HOUR_TO_SEC;
            case 3:
                return Constants.ONE_HOUR_TO_SEC;
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_linear_schedule_time:
                mTaskDateDialog.show();
                break;
        }
    }

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


    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void finishInputPlan() {
        if (!TextUtils.isEmpty(mAutoCompleteTextViewTaskPlan.getText())) {
            mTextViewTaskPlan.setText(mAutoCompleteTextViewTaskPlan.getText().toString());
        }
        mLinearLayoutInput.setVisibility(View.GONE);
        mScrollViewTaskListContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLinearLayoutInput.getVisibility() == View.VISIBLE) {
                finishInputPlan();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void getIntentData() {

        Intent intent = getIntent();
        String content = intent.getStringExtra(VOICE_INPUT);
        if (content != null && content.length() > 0) {
            mTask = new EventTask();
            mActivityNewTaskBinding.setTask(mTask);
            mActivityNewTaskBinding.executePendingBindings();
            mTextViewTitle.setText(content);
        }
        mTaskID = intent.getStringExtra(PASS_TASK_ID);
        if (mTaskID != null) {
            mHelper = DatebaseHelper.getInstance(this);
            mTask = mHelper.getRealm().where(EventTask.class).contains(EventTask.TASK_ID, mTaskID).findFirst();
            mSpinnerTaskKinds.setSelection(mTask.getTaskType() - 1);
            mSpinnerTaskLevel.setSelection(mTask.getTaskPriority() - 1);
            mSpinnerTaskRemind.setSelection(transRemind(mTask.getTaskRemindTime()));
            mActivityNewTaskBinding.setTask(mTask);
            mActivityNewTaskBinding.executePendingBindings();
            isEditMode = true;
        }


    }

    private int transRemind(long i) {
        if (i == Constants.TEN_MIN_TO_SEC) {

            return Constants.AHEAD_SCHEDULE_TIME_TEM_MIN;

        } else if (i == Constants.TWENTY_MIN_TO_SEC) {

            return Constants.AHEAD_SCHEDULE_TIME_THENTY_MIN;

        } else if (i == Constants.HALF_HOUR_TO_SEC) {

            return Constants.AHEAD_SCHEDULE_TIME_HALF_HOUR;

        } else if (i == Constants.ONE_HOUR_TO_SEC) {

            return Constants.AHEAD_SCHEDULE_TIME_ONE_HOUR;

        } else {

            return Constants.AHEAD_SCHEDULE_TIME_TEM_MIN;

        }
    }

}
