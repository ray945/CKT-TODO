package com.ckt.ckttodo.util;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ItemProjectBinding;
import com.ckt.ckttodo.ui.NewTaskActivity;
import com.ckt.ckttodo.ui.ProjectFragment;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;

import java.text.NumberFormat;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private RealmResults<Plan> planList;
    private Context context;
    private TaskListAdapter adapter;
    private static final String TAG = "ZHIWEI";
    private RealmList<EventTask> tasks;


    public ProjectListAdapter(Context context, RealmResults<Plan> planList) {
        this.planList = planList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_project, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProjectListAdapter.ViewHolder holder, int position) {
        Plan plan = planList.get(position);
        tasks = plan.getEventTasks();
        RealmList<EventTask> threeTasks;
        final int planId = plan.getPlanId();

        calculateProgress(tasks, planId);
        holder.binding.btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, NewTaskActivity.class);
                intent.putExtra(NewTaskActivity.GET_PLAN_ID_FROM_PROJECT, planId);
                context.startActivity(intent);
            }
        });


        holder.binding.btnAddTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||motionEvent.getAction() == MotionEvent.ACTION_DOWN ){
                    view.setBackgroundResource(R.color.colorPrimaryDark);
                }else if (motionEvent.getAction()== MotionEvent.ACTION_BUTTON_RELEASE||motionEvent.getAction() == MotionEvent.ACTION_UP){
                    view.setBackgroundResource(R.color.color_d3d3d3);
                }
                return false;
            }
        });

        RecyclerView rvTasks = holder.binding.rvTasks;
        Log.e(TAG,String.valueOf(tasks.size()));
        adapter = new TaskListAdapter(context,tasks);
        rvTasks.setAdapter(adapter);
        holder.bind(plan);

    }


    @Override
    public int getItemCount() {
        return (planList == null) ? 0 : planList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemProjectBinding binding;

        ViewHolder(ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            initRecyclerView(binding.rvTasks, context);
        }

        void bind(Plan plan) {
            binding.setPlan(plan);
            binding.executePendingBindings();
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ProjectTaskListDecoration(context));
    }

    private void calculateProgress(RealmList<EventTask> tasks, final int planId) {
        float totalTaskTime = 0;
        float doneTaskTime = 0;
        for (EventTask task : tasks) {
            totalTaskTime += task.getTaskPredictTime();
            if (task.getTaskStatus() == EventTask.DONE) {
                doneTaskTime += task.getTaskPredictTime();
            }

        }
        float accomplishProgressTemp = doneTaskTime / totalTaskTime;
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        final String accomplishProgress = numberFormat.format(accomplishProgressTemp);
        DatebaseHelper.getInstance(context).getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Plan tempPlan = DatebaseHelper.getInstance(context).getRealm().where(Plan.class).equalTo(ProjectFragment.PLAN_ID, planId).findFirst();
                tempPlan.setAccomplishProgress(accomplishProgress);
            }
        });
    }

}
