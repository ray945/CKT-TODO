package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.databinding.ActivityNewPlanBinding;
import com.ckt.ckttodo.widgt.TaskDateDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.realm.Realm;

public class NewPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityNewPlanBinding mActivityNewPlanBinding;
    private ViewDataBinding mViewDataBinding;
    private Calendar mCalendar;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM月dd日");
    private TaskDateDialog mTaskDateDialog;
    private String mProjectId;
    private long planStartTime;
    private long planEndTime;
    private int flag = 1;
    public static final String PROJECT_ID = "projectId";
    public static final String TAG = "tag";
    public static final String PLAN_ID = "planId";
    private String tag;
    private String planId;
    private Plan plan;
    private boolean isEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_plan));
        mActivityNewPlanBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_plan);
        init();
    }

    private void init() {
        getData();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) NewPlanActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mActivityNewPlanBinding.etPlanTitle, 0);
            }
        }, 200);
        mActivityNewPlanBinding.tvPlanStartTime.setOnClickListener(this);
        mActivityNewPlanBinding.tvPlanEndTime.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sure:
                savePlan();
                break;
            case android.R.id.home:
                show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void show() {
        if ("2".equals(tag)) {
            if (mActivityNewPlanBinding.etPlanTitle.getText().toString().trim().equals(plan.getPlanName()) &&
                    isChangeContent() && planStartTime == plan.getStartTime() && planEndTime == plan.getEndTime()) {
                onBackPressed();
            } else {
                new AlertDialog.Builder(this).setTitle("是否保存？").setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePlan();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).show();
            }
        } else if (mActivityNewPlanBinding.etPlanTitle.getText().toString().trim().equals("") && mActivityNewPlanBinding.etPlanDescription.getText().toString().trim().equals("")) {
            onBackPressed();
        } else {
            new AlertDialog.Builder(this).setTitle("是否保存？").setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("是", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    savePlan();
                }
            }).setNegativeButton("否", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            }).show();
        }

    }

    private boolean isChangeContent() {
        if (isEmpty) {
            if (mActivityNewPlanBinding.etPlanDescription.getText().toString().trim().equals("")) {
                return true;
            } else {
                return false;
            }
        } else if (mActivityNewPlanBinding.etPlanDescription.getText().toString().trim().equals(plan.getPlanContent())) {
            return true;
        } else {
            return false;
        }
    }

    private void savePlan() {
        if (mActivityNewPlanBinding.etPlanTitle.getText().toString().trim().equals("")) {
            Toast.makeText(NewPlanActivity.this, "不能为空哦!", Toast.LENGTH_SHORT).show();
        } else {
            if (planStartTime > planEndTime) {
                toast();
            } else {
                DatabaseHelper.getInstance(NewPlanActivity.this).getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Project sProject = DatabaseHelper.getInstance(NewPlanActivity.this).getRealm().where(Project.class).equalTo(PROJECT_ID, mProjectId).findFirst();
                        if ("2".equals(tag)) {
                            plan.setPlanContent(mActivityNewPlanBinding.etPlanDescription.getText().toString().trim());
                            plan.setPlanName(mActivityNewPlanBinding.etPlanTitle.getText().toString().trim());
                            plan.setStartTime(planStartTime);
                            plan.setEndTime(planEndTime);
                            Date date = new Date();
                            sProject.setLastUpdateTime(date.getTime());
                            realm.copyToRealmOrUpdate(sProject);
                        } else {
                            Plan plan = new Plan();
                            if (!mActivityNewPlanBinding.etPlanDescription.getText().toString().trim().equals("")) {
                                plan.setPlanContent(mActivityNewPlanBinding.etPlanDescription.getText().toString().trim());
                            }
                            plan.setPlanName(mActivityNewPlanBinding.etPlanTitle.getText().toString().trim());
                            plan.setPlanId(UUID.randomUUID().toString());
                            plan.setCreateTime(System.currentTimeMillis());
                            plan.setStartTime(planStartTime);
                            plan.setEndTime(planEndTime);
                            plan.setProjectId(mProjectId);
                            sProject.getPlans().add(plan);
                        }

                    }
                });
                if ("2".equals(tag)) {
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "新建成功", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_planStartTime:
                flag = 1;
                mTaskDateDialog.show(planStartTime);
                break;
            case R.id.tv_planEndTime:
                flag = 2;
                mTaskDateDialog.show(planEndTime);
                break;
        }
    }

    public void getData() {
        Intent intent = getIntent();
        mProjectId = intent.getStringExtra(PROJECT_ID);
        tag = intent.getStringExtra(TAG);
        if ("2".equals(tag)) {
            getSupportActionBar().setTitle("修改计划");
            planId = intent.getStringExtra(PLAN_ID);
            DatabaseHelper.getInstance(NewPlanActivity.this).getRealm().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    plan = DatabaseHelper.getInstance(NewPlanActivity.this).getRealm().where(Plan.class).equalTo(PLAN_ID, planId).findFirst();
                }
            });
            mActivityNewPlanBinding.etPlanTitle.setText(plan.getPlanName());
            if (plan.getPlanContent() != null || !plan.getPlanContent().equals("")) {
                isEmpty = false;
                mActivityNewPlanBinding.etPlanDescription.setText(plan.getPlanContent());
            } else {
                isEmpty = true;
            }
            planStartTime = plan.getStartTime();
            planEndTime = plan.getEndTime();
        } else {
            planStartTime = System.currentTimeMillis();
            planEndTime = planStartTime;
        }
        mActivityNewPlanBinding.tvPlanEndTime.setText(mDateFormat.format(planEndTime));
        mActivityNewPlanBinding.tvPlanStartTime.setText(mDateFormat.format(planStartTime));
        mTaskDateDialog = new TaskDateDialog(this, new TaskDateDialog.ClickedSureListener() {
            @Override
            public void onClickedSureListener(Calendar cal) {
                mCalendar = cal;
                if (flag == 1) {
                    mActivityNewPlanBinding.tvPlanStartTime.setText(mDateFormat.format(cal.getTime()));
                    planStartTime = cal.getTime().getTime();
                } else {
                    if (flag == 2) {
                        mActivityNewPlanBinding.tvPlanEndTime.setText(mDateFormat.format(cal.getTime()));
                        planEndTime = cal.getTime().getTime();
                    }
                }
                if (planStartTime > planEndTime) {
                    toast();
                }
                mTaskDateDialog.dismiss();
            }
        });
    }

    private void toast() {
        Toast.makeText(this, "亲结束时间要比开始时间要晚的哦！", Toast.LENGTH_SHORT).show();
    }
}
