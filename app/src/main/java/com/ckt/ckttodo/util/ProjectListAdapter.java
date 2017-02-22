package com.ckt.ckttodo.util;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.databinding.ItemProjectBinding;
import com.ckt.ckttodo.ui.ProjectFragment;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;

import java.text.NumberFormat;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by zhiwei.li
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private RealmResults<Project> projectList;
    private Context context;
    private static final String TAG = "ZHIWEI";


    public ProjectListAdapter(Context context, RealmResults<Project> projectList) {
        this.projectList = projectList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_project, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project project = projectList.get(position);

        final RealmList<Plan> plans = project.getPlans();
        final String projectId = project.getProjectId();

        calculateProgress(plans, projectId);
        initNewTaskButton(holder.binding.btnAddPlan, projectId);

        final RecyclerView rvPlans = holder.binding.rvPlans;
        if (plans.size() <= 3) {
            PlanListAdapter adapter = new PlanListAdapter(context, plans);
            adapter.setOnItemClickListener(new PlanListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {

                }
            });
            rvPlans.setAdapter(adapter);
        } else if (plans.size() >= 4) {
            RealmList<Plan> threePlans = new RealmList<>();
            for (int i = 0; i < 3; i++) {
                threePlans.add(plans.get(i));
            }
            final PlanListAdapter threeAdapter = new PlanListAdapter(context, threePlans);
            SmartRecyclerAdapter smartRecyclerAdapter = new SmartRecyclerAdapter(threeAdapter);
            ImageButton footerButton = (ImageButton) LayoutInflater.from(context).inflate(R.layout.item_project_plan_footer, rvPlans, false);
            footerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    threeAdapter.clear();
                    threeAdapter.setPlans(plans);
                    SmartRecyclerAdapter adapter1 = new SmartRecyclerAdapter(threeAdapter);
                    rvPlans.setAdapter(adapter1);
                }
            });
            smartRecyclerAdapter.setFooterView(footerButton);
            rvPlans.setAdapter(smartRecyclerAdapter);
        }

        holder.bind(project);

    }

    private void calculateProgress(RealmList<Plan> plans, final String projectId) {
        float finishPlan = 0;
        for (Plan plan : plans) {
            if (plan.getStatus() == Plan.DONE) {
                finishPlan++;
            }
        }
        float accomplishProgressTemp = finishPlan / plans.size();
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        final String accomplishProgress = numberFormat.format(accomplishProgressTemp);
        DatebaseHelper.getInstance(context).getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Project tempProject = DatebaseHelper.getInstance(context).getRealm().where(Project.class).equalTo(ProjectFragment.PROJECT_ID, projectId).findFirst();
                tempProject.setAccomplishProgress(accomplishProgress);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (projectList == null) ? 0 : projectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemProjectBinding binding;

        ViewHolder(ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            initRecyclerView(binding.rvPlans, context);
        }

        void bind(Project project) {
            binding.setProject(project);
            binding.executePendingBindings();
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ProjectTaskListDecoration(context));
    }

   /* private void calculateProgress(RealmList<EventTask> tasks, final String planId) {
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
    }*/

    private void initNewTaskButton(ImageButton button, final String planId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(context, NewTaskActivity.class);
                intent.putExtra(NewTaskActivity.GET_PLAN_ID_FROM_PROJECT, planId);
                context.startActivity(intent);*/
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
