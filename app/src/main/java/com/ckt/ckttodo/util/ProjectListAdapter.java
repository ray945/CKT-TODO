package com.ckt.ckttodo.util;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ItemProjectBinding;
import com.ckt.ckttodo.ui.NewTaskActivity;
import com.ckt.ckttodo.ui.ProjectFragment;
import com.tumblr.bookends.Bookends;

import java.text.NumberFormat;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private RealmResults<Plan> planList;
    private Context context;
    private boolean moreTask = true;

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
        final Plan plan = planList.get(position);
        final RealmList<EventTask> tasks = plan.getEventTasks();
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
                Plan tempPlan = DatebaseHelper.getInstance(context).getRealm().where(Plan.class).equalTo(ProjectFragment.PLAN_ID, plan.getPlanId()).findFirst();
                tempPlan.setAccomplishProgress(accomplishProgress);
            }
        });
        holder.binding.btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String planName = plan.getPlanName();
                Intent intent = new Intent(context, NewTaskActivity.class);
                intent.putExtra("PLAN_NAME", planName);
                context.startActivity(intent);
            }
        });
        holder.bind(plan);

        final RecyclerView rvTasks = holder.binding.rvTasks;
        rvTasks.addItemDecoration(new ProjectTaskListDecoration(context));
        if (tasks.size() <= 3) {
            taskListAdapter adapter = new taskListAdapter(context, tasks);
            ProjectFragment.initRecyclerView(rvTasks, adapter, context);
        } else {
            final taskListAdapter adapter = new taskListAdapter(context);
            final RealmList<EventTask> taskThree = new RealmList<>();
            for (int i = 0; i < 3; i++) {
                taskThree.add(tasks.get(i));
            }
            adapter.setTasks(taskThree);
            final Bookends<taskListAdapter> adapterBookends = new Bookends<>(adapter);
            final ImageButton footerButton = (ImageButton) LayoutInflater.from(context).inflate(R.layout.item_project_tasks_footer, rvTasks, false);
            footerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (moreTask) {
                        showMoreTask(adapter, tasks, footerButton, R.drawable.ic_vertical_align_top_black_48dp, rvTasks, adapterBookends);
                    } else {
                        showMoreTask(adapter, taskThree, footerButton, R.drawable.ic_more_horiz_black_48dp, rvTasks, adapterBookends);
                    }
                }
            });
            adapterBookends.addFooter(footerButton);
            rvTasks.setAdapter(adapterBookends);
        }
    }

    private void showMoreTask(taskListAdapter adapter, RealmList<EventTask> tasks,
                              ImageButton footerButton, int iconRes, RecyclerView rvTasks,
                              Bookends<taskListAdapter> adapterBookends) {
        moreTask = !moreTask;
        adapter.clear();
        adapter.setTasks(tasks);
        footerButton.setImageResource(iconRes);
        rvTasks.setAdapter(adapterBookends);
    }

    @Override
    public int getItemCount() {
        return (planList == null) ? 0 : planList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemProjectBinding binding;

        ViewHolder(ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Plan plan) {
            binding.setPlan(plan);
            binding.executePendingBindings();
        }
    }

}
