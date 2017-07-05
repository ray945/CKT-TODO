package com.ckt.ckttodo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.PostTask;
import com.ckt.ckttodo.database.Result;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.model.Goal;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.retrofit.TaskService;
import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.callback.BatCallback;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hcy on 17-6-25.
 */

public class DetailPlanActivity extends AppCompatActivity
    implements BatListener, OnItemClickListener, OnOutsideClickedListener {

    private BatRecyclerView mRecyclerView;
    private BatAdapter mAdapter;
    private List<BatModel> mGoals;
    private BatItemAnimator mAnimator;
    private LinearLayout ll;

    private static final String TAG = "DetailPlanActivity";
    private Plan mPlan;
    private RealmList<EventTask> tasks;
    private String mPlanId;
    private DatabaseHelper mDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plan);
        initData();
        initUI();
    }


    private void initData() {
        mDBHelper = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();
        mPlanId = intent.getStringExtra("planId");
        mPlan = mDBHelper.find(Plan.class).equalTo("planId", mPlanId).findFirst();
        tasks = mPlan.getEventTasks();
        mGoals = new ArrayList<>();
        for (final EventTask task : tasks) {
            mGoals.add(new BatModel() {
                @Override public boolean isChecked() {
                    return task.getTaskStatus() == EventTask.DONE;
                }


                @Override public String getText() {
                    return task.getTaskTitle();
                }


                @Override public void setChecked(boolean b) {
                    mDBHelper.getRealm().beginTransaction();
                    if (b) {
                        task.setTaskStatus(EventTask.DONE);
                    } else {
                        task.setTaskStatus(EventTask.START);
                    }
                    mDBHelper.getRealm().commitTransaction();
                    mDBHelper.update(task);
                }
            });
        }
    }


    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.detail_plan));
        mRecyclerView = (BatRecyclerView) findViewById(R.id.bat_recycler_view);
        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(this));
        mAnimator = new BatItemAnimator();
        mAdapter = new BatAdapter(mGoals, this, mAnimator);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnOutsideClickListener(this);
        mRecyclerView.getView().setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BatCallback(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView.getView());
        mRecyclerView.getView().setItemAnimator(mAnimator);
        mRecyclerView.setAddItemListener(this);
        ll = (LinearLayout) findViewById(R.id.ll_detail_plan);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.revertAnimation();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void add(final String string) {
        final EventTask task = new EventTask();
        task.setTaskId(UUID.randomUUID().toString());
        task.setTaskTitle(string);
        task.setTaskType(EventTask.WORK);
        task.setTaskStartTime(new Date().getTime());
        task.setTaskPriority(EventTask.ENHANCEMENT);
        task.setPlanId(mPlanId);
        mGoals.add(0, new BatModel() {
            @Override public boolean isChecked() {
                return false;
            }


            @Override public String getText() {
                return string;
            }


            @Override public void setChecked(boolean b) {
                mDBHelper.getRealm().beginTransaction();
                if (b) {
                    task.setTaskStatus(EventTask.DONE);
                } else {
                    task.setTaskStatus(EventTask.START);
                }
                mDBHelper.getRealm().commitTransaction();
                mDBHelper.update(task);
            }
        });
        mDBHelper.getRealm().beginTransaction();
        task.setPlan(mPlan);
        mPlan.getEventTasks().add(task);
        mDBHelper.getRealm().commitTransaction();
        postNewTask(task);
        mAdapter.notify(AnimationType.ADD, 0);
    }


    @Override
    public void delete(int position) {
        BatModel model = mGoals.get(position);
        for (EventTask task : tasks) {
            if (model.getText().equals(task.getTaskTitle())) {
                mDBHelper.getRealm().beginTransaction();
                mPlan.getEventTasks().remove(task);
                mDBHelper.getRealm().commitTransaction();
            }
        }
        mGoals.remove(position);
        mAdapter.notify(AnimationType.REMOVE, position);
    }


    @Override
    public void move(int from, int to) {
        if (from >= 0 && to >= 0) {
            mAnimator.setPosition(to);
            BatModel model = mGoals.get(from);
            mGoals.remove(model);
            mGoals.add(to, model);
            mAdapter.notify(AnimationType.MOVE, from, to);

            if (from == 0 || to == 0) {
                mRecyclerView.getView().scrollToPosition(Math.min(from, to));
            }
        }
    }


    @Override
    public void onClick(BatModel item, int position) {
        Toast.makeText(this, item.getText(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onOutsideClicked() {
        mRecyclerView.revertAnimation();
    }


    public void postNewTask(EventTask task) {
        User user = new User(this);
        Map<String, String> postMap = new HashMap<>();
        postMap.put(BeanConstant.EMAIL, user.getEmail());
        postMap.put(BeanConstant.TOKEN, user.getToken());
        JSONObject object = new JSONObject();
        object.put("plan_id", task.getPlanId());
        object.put("task_id", task.getTaskId());
        object.put("men_id", user.getId());
        object.put("task_title", task.getTaskTitle());
        object.put("task_type", task.getTaskType());
        object.put("task_prioirty", task.getTaskPriority());
        object.put("task_status", task.getTaskStatus());
        object.put("task_start_time", String.valueOf(task.getTaskStartTime()));
        postMap.put("task", object.toJSONString());
        HttpClient.getHttpService(TaskService.class).postNewTask(postMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<PostTask>>() {
                    @Override public void onSubscribe(Disposable d) {

                    }


                    @Override public void onNext(Result<PostTask> value) {
                        switch (value.getResultcode()) {
                            case BeanConstant.SUCCESS_RESULT_CODE:
                                Toast.makeText(DetailPlanActivity.this,"post task success.",Toast.LENGTH_SHORT).show();
                                break;
                            case BeanConstant.USER_STATUS_INVALID_ERRO_RESULT_CODE:
                                Toast.makeText(DetailPlanActivity.this, getString(R.string.login_status_timeout), Toast.LENGTH_SHORT).show();
                                break;
                            case BeanConstant.PASS_DATA_INVALID_RESULT_CODE:
                                Toast.makeText(DetailPlanActivity.this, getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }


                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(DetailPlanActivity.this, getString(R.string.request_service_fail), Toast.LENGTH_SHORT).show();
                    }


                    @Override public void onComplete() {

                    }
                });

    }


    public void postDeleteTask(EventTask task) {

    }


    public void postUpdateTask(EventTask task) {

    }
}
