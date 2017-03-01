package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ActivityPlanDetailBinding;
import com.ckt.ckttodo.util.TaskListAdapter;
import com.ckt.ckttodo.widgt.TaskDateDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class PlanDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PLAN_ID = "planId";
    private ActivityPlanDetailBinding mActivityPlanDetailBinding;
    private String planId;
    private int flag = 1;
    private TaskDateDialog mTaskDateDialog;
    private Calendar mCalendar;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM月dd日");
    private long planStartTime;
    private long planEndTime;
    private Plan plan;
    private TaskListAdapter mTasklistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.planDetail));
        mActivityPlanDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_plan_detail);
        init();
    }

    private void init() {
        getData();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) PlanDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mActivityPlanDetailBinding.etPlanTitle, 0);
            }
        }, 200);
        mActivityPlanDetailBinding.tvDetailStartTime.setOnClickListener(this);
        mActivityPlanDetailBinding.tvDetailEndTime.setOnClickListener(this);
        mTaskDateDialog = new TaskDateDialog(this, new TaskDateDialog.ClickedSureListener() {
            @Override
            public void onClickedSureListener(Calendar cal) {
                mCalendar = cal;
                if (flag == 1) {
                    mActivityPlanDetailBinding.tvDetailStartTime.setText(mDateFormat.format(cal.getTime()));
                    planStartTime = cal.getTime().getTime();
                } else {
                    if (flag == 2) {
                        mActivityPlanDetailBinding.tvDetailEndTime.setText(mDateFormat.format(cal.getTime()));
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


    private void getData() {
        Intent intent = getIntent();
        planId = intent.getStringExtra(PLAN_ID);
        DatebaseHelper.getInstance(PlanDetailActivity.this).getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                plan = DatebaseHelper.getInstance(PlanDetailActivity.this).getRealm().where(Plan.class).equalTo(PLAN_ID, planId).findFirst();
                mActivityPlanDetailBinding.setPlan(plan);
            }
        });
        planStartTime = plan.getStartTime();
        planEndTime = plan.getEndTime();
        mTasklistAdapter = new TaskListAdapter(this, plan.getEventTasks());
        mTasklistAdapter.setOnItemClickListener(new NoteFragment.NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(PlanDetailActivity.this, TaskDetailActivity.class);
                intent.putExtra(TaskDetailActivity.EVENT_TASK_ID, plan.getEventTasks().get(position).getTaskId());
                startActivityForResult(intent, MainActivity.MAIN_TO_TASK_DETAIL_CODE);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mActivityPlanDetailBinding.rvTask.setLayoutManager(layoutManager);
        mActivityPlanDetailBinding.rvTask.setAdapter(mTasklistAdapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void show() {
        if (plan.getStartTime() == planStartTime && plan.getEndTime() == planEndTime && plan.getPlanName().equals(mActivityPlanDetailBinding.etPlanTitle.getText().toString().trim()) && plan.getPlanContent().equals(mActivityPlanDetailBinding.etPlanDescription.getText().toString().trim())) {
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

    private void savePlan() {
        if (mActivityPlanDetailBinding.etPlanTitle.getText().toString().trim().equals("") || mActivityPlanDetailBinding.etPlanDescription.getText().toString().trim().equals("")) {
            Toast.makeText(PlanDetailActivity.this, "不能为空哦!", Toast.LENGTH_SHORT).show();
        } else {
            if (planStartTime > planEndTime) {
                toast();
            } else {
                DatebaseHelper.getInstance(this).getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        plan.setStartTime(planStartTime);
                        plan.setEndTime(planEndTime);
                        plan.setPlanName(mActivityPlanDetailBinding.etPlanTitle.getText().toString().trim());
                        plan.setPlanContent(mActivityPlanDetailBinding.etPlanDescription.getText().toString().trim());
                        realm.copyToRealmOrUpdate(plan);
                    }
                });
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTasklistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_detailStartTime:
                flag = 1;
                mTaskDateDialog.show(planStartTime);
                break;
            case R.id.tv_detailEndTime:
                flag = 2;
                mTaskDateDialog.show(planEndTime);
                break;
        }
    }
}
