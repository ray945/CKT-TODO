package com.ckt.ckttodo.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.ActivityTaskDetailBinding;
import com.ckt.ckttodo.util.Constants;
import com.ckt.ckttodo.util.TranserverUtil;

import org.w3c.dom.Text;

public class TaskDetailActivity extends AppCompatActivity {

    public final static String EVENT_TASK_ID = "event_task";
    public final static int TASK_DETAIL_MAIN_RESULT_CODE = 20;
    public final static int TASK_DETAIL_TO_EDIT_TASK_REQUEST_CODE = 300;
    public final static String IS_TASK_DETAIL_MODIFY = "is_task_detail_modify";
    private ActivityTaskDetailBinding mActivityTaskDetailBinding;
    private TextView mTextViewKinds;
    private TextView mTextViewLevels;
    private TextView mTextViewRemind;
    private EditText mEditTextSpendTime;
    private TextView mTextViewContent;
    private DatebaseHelper mHelper;
    private EventTask mTask;
    private boolean isModify = false;
    private String mTaskID;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NewTaskActivity.MODIFY_TASK_RESULT_CODE) {
            mTask = mHelper.getRealm().where(EventTask.class).contains(EventTask.TASK_ID, mTaskID).findFirst();
            mActivityTaskDetailBinding.setTask(mTask);
            mActivityTaskDetailBinding.executePendingBindings();
            if (mTask.getTaskContent() == null || mTask.getTaskContent().replace(" ", "").length() < 1) {
                mTextViewContent.setVisibility(View.GONE);
            } else {
                mTextViewContent.setVisibility(View.VISIBLE);
            }
            isModify = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.task_detail));
        actionBar.setDisplayHomeAsUpEnabled(true);
        mActivityTaskDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
            case R.id.menu_edit:
                Intent intent = new Intent(this, NewTaskActivity.class);
                intent.putExtra(NewTaskActivity.PASS_TASK_ID, mTask.getTaskId());
                intent.putExtra("isEdit", true);
                startActivityForResult(intent, TASK_DETAIL_TO_EDIT_TASK_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init() {
        mTextViewKinds = mActivityTaskDetailBinding.detailTextKinds;
        mTextViewLevels = mActivityTaskDetailBinding.detailTextLevel;
        mTextViewRemind = mActivityTaskDetailBinding.detailTextRemind;
        mEditTextSpendTime = mActivityTaskDetailBinding.detailEditSendTime;
        mTextViewContent = mActivityTaskDetailBinding.detailTextContent;
        setData();
    }

    private void setData() {
        mTaskID = getIntent().getStringExtra(EVENT_TASK_ID);
        if (mTaskID != null) {
            mHelper = DatebaseHelper.getInstance(this);
            mTask = mHelper.getRealm().where(EventTask.class).contains(EventTask.TASK_ID, mTaskID).findFirst();
            mActivityTaskDetailBinding.setTask(mTask);
            mActivityTaskDetailBinding.executePendingBindings();
            mTextViewKinds.setText(transKinds(mTask.getTaskType()));
            mTextViewLevels.setText(transLevel(mTask.getTaskPriority()));
            mTextViewRemind.setText(transRemind(mTask.getTaskRemindTime()));
            if (mTask.getTaskContent() == null || mTask.getTaskContent().replace(" ", "").length() < 1) {
                mTextViewContent.setVisibility(View.GONE);
            }
            Float zero = new Float(0f);
            if (zero.compareTo(mTask.getTaskRealSpendTime()) == 0) {
                mEditTextSpendTime.setText("");
            }

        }
    }

    private String transLevel(int i) {
        String[] levels = getResources().getStringArray(R.array.task_level);
        return levels[i - 1];
    }

    private String transKinds(int i) {
        String[] kinds = getResources().getStringArray(R.array.kind_list);
        return kinds[i - 1];
    }

    private String transRemind(long i) {
        String[] reminds = getResources().getStringArray(R.array.task_remind);
        if (i == Constants.TEN_MIN_TO_SEC) {
            return reminds[0];
        } else if (i == Constants.TWENTY_MIN_TO_SEC) {
            return reminds[1];
        } else if (i == Constants.HALF_HOUR_TO_SEC) {
            return reminds[2];
        } else if (i == Constants.ONE_HOUR_TO_SEC) {
            return reminds[3];
        } else {
            return "";
        }
    }

    private void finishActivity() {
        if (!TextUtils.isEmpty(mEditTextSpendTime.getText())) {

            String planTime = mEditTextSpendTime.getText().toString();
            if (!TranserverUtil.isLegalNum(planTime) || !TranserverUtil.isNumber(planTime)) {
                Toast.makeText(this, getResources().getString(R.string.task_plan_time_illegality), Toast.LENGTH_SHORT).show();
                return;
            }
            String content = mEditTextSpendTime.getText().toString().replace(" ", "");
            Float update = Float.valueOf(content);
            if (update.compareTo(new Float(0)) > 0
                    && update.compareTo(mTask.getTaskRealSpendTime()) != 0) {
                Dialog dialog = initDialog(update);
                dialog.show();
            } else {
                setResult(TASK_DETAIL_MAIN_RESULT_CODE);
                finish();
            }
        } else {
            setResult(TASK_DETAIL_MAIN_RESULT_CODE);
            finish();
        }

    }

    private Dialog initDialog(final float update) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.remind_save))
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isModify = true;
                        EventTask task = new EventTask();
                        task.setTaskId(mTask.getTaskId());
                        task.setPlan(mTask.getPlan());
                        task.setCreateUerId(mTask.getCreateUerId());
                        task.setTaskContent(mTask.getTaskContent());
                        task.setTaskPredictTime(mTask.getTaskPredictTime());
                        task.setTaskPriority(mTask.getTaskPriority());
                        task.setTaskStatus(mTask.getTaskStatus());
                        task.setTaskTitle(mTask.getTaskTitle());
                        task.setTaskRealSpendTime(update);
                        task.setTaskUpdateTime(mTask.getTaskUpdateTime());
                        task.setExecUserId(mTask.getExecUserId());
                        task.setPlanId(mTask.getPlanId());
                        task.setTaskRemindTime(mTask.getTaskRemindTime());
                        task.setTaskStartTime(mTask.getTaskStartTime());
                        task.setTopNumber(mTask.getTopNumber());
                        task.setTaskType(mTask.getTaskType());
                        mHelper.update(task);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setResult(TASK_DETAIL_MAIN_RESULT_CODE);
                        finish();
                    }
                }).create();

        return dialog;
    }


    @Override
    public void finish() {
        if (isModify) {
            Intent intent = new Intent();
            intent.putExtra(IS_TASK_DETAIL_MODIFY, isModify);
            setResult(TASK_DETAIL_MAIN_RESULT_CODE, intent);
        }

        super.finish();
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


}
