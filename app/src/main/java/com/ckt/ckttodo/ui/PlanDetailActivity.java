package com.ckt.ckttodo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ActivityPlanDetailBinding;
import com.ckt.ckttodo.util.TaskListAdapter;

import io.realm.Realm;

public class PlanDetailActivity extends AppCompatActivity {

    private static final String PLAN_ID = "planId";
    private ActivityPlanDetailBinding mActivityPlanDetailBinding;
    private String planId;
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
            case R.id.menu_edit:
                Intent intent = new Intent(PlanDetailActivity.this, NewPlanActivity.class);
                intent.putExtra(NewPlanActivity.PROJECT_ID, plan.getProjectId());
                intent.putExtra(NewPlanActivity.TAG, "2");
                intent.putExtra(NewPlanActivity.PLAN_ID, planId);
                startActivity(intent);
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
