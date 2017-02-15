package com.ckt.ckttodo.util;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ItemProjectBinding;
import com.ckt.ckttodo.ui.NewTaskActivity;
import com.ckt.ckttodo.ui.ProjectFragment;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;

import org.mozilla.universalchardet.prober.statemachine.SMModel;

import java.text.NumberFormat;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by zhiwei.li
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private RealmResults<Plan> planList;
    private Context context;
    private static final String TAG = "ZHIWEI";


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

        final RealmList<EventTask> tasks = plan.getEventTasks();
        final String planId = plan.getPlanId();

        calculateProgress(tasks, planId);
        initNewTaskButton(holder.binding.btnAddTask,planId);

        final RecyclerView rvTasks = holder.binding.rvTasks;
        final TaskListAdapter adapter = new TaskListAdapter(context);
        final TaskListAdapter threeAdapter = new TaskListAdapter(context);
        if (tasks.size() <= 3){
            adapter.setTasks(tasks);
            rvTasks.setAdapter(adapter);
        }else if (tasks.size() >= 4){
            RealmList<EventTask> threeTasks = new RealmList<>();
            for (int i = 0; i < 3; i++) {
                threeTasks.add(tasks.get(i));
            }
            threeAdapter.setTasks(threeTasks);
            SmartRecyclerAdapter smartRecyclerAdapter = new SmartRecyclerAdapter(threeAdapter);
            ImageButton footerButton = (ImageButton) LayoutInflater.from(context).inflate(R.layout.item_project_tasks_footer,rvTasks,false);
            footerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    threeAdapter.clear();
                    threeAdapter.setTasks(tasks);
                    SmartRecyclerAdapter adapter1 = new SmartRecyclerAdapter(threeAdapter);
                    rvTasks.setAdapter(adapter1);
                }
            });
            smartRecyclerAdapter.setFooterView(footerButton);
            rvTasks.setAdapter(smartRecyclerAdapter);
        }

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

    private void calculateProgress(RealmList<EventTask> tasks, final String planId) {
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

    private void initNewTaskButton(ImageButton button, final String planId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewTaskActivity.class);
                intent.putExtra(NewTaskActivity.GET_PLAN_ID_FROM_PROJECT, planId);
                context.startActivity(intent);
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundResource(R.color.colorPrimaryDark);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_MOVE ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    view.setBackgroundResource(R.color.background_grey);
                }
                return false;
            }
        });
    }
}
