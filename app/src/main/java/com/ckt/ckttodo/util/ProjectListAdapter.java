package com.ckt.ckttodo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.databinding.ItemProjectBinding;
import com.ckt.ckttodo.ui.NewPlanActivity;
import com.ckt.ckttodo.ui.PlanDetailActivity;
import com.ckt.ckttodo.ui.ProjectFragment;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    private SmartRecyclerAdapter mSmartRecyclerAdapter;
    private RealmList<Plan> mThreePlans;


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
        holder.itemView.setTag(position);
        final Project project = projectList.get(position);

        final RealmList<Plan> plans = project.getPlans();
        final String projectId = project.getProjectId();

        calculateProgress(plans, projectId);
        initNewPlanButton(holder.binding.btnAddPlan, projectId);
        holder.binding.tvProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View editTextView = layoutInflater.inflate(R.layout.dialog_edittext, null);
                final EditText editText = (EditText) editTextView.findViewById(R.id.new_task_name);
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                editText.setText(project.getProjectTitle());
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(editText, 0);
                    }
                }, 200);
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.new_project).setView(editTextView).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String projectName = editText.getText().toString().trim();
                        if (!projectName.equals(project.getProjectTitle())) {
                            for (Project project : DatebaseHelper.getInstance(context).findAll(Project.class)) {
                                if (projectName.equals(project.getProjectTitle())) {
                                    showToast(context.getResources().getString(R.string.project_exist));
                                    return;
                                }
                            }
                        } else {
                            return;
                        }
                        if (!projectName.equals("")) {
                            DatebaseHelper.getInstance(context).getRealm().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    project.setProjectTitle(projectName);
                                    Date date = new Date();
                                    project.setLastUpdateTime(date.getTime());
                                    realm.copyToRealmOrUpdate(project);
                                    showToast("修改成功");
                                    ProjectListAdapter.this.notifyDataSetChanged();
                                }
                            });
                        } else {
                            showToast(context.getResources().getString(R.string.plan_not_null));
                        }
                    }
                }).setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });
        final RecyclerView rvPlans = holder.binding.rvPlans;
        if (plans.size() <= 3) {
            PlanListAdapter adapter = new PlanListAdapter(context, plans);
            adapter.setOnItemClickListener(new PlanListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    Intent intent = new Intent(context, PlanDetailActivity.class);
                    intent.putExtra("planId", plans.get(position).getPlanId());
                    context.startActivity(intent);
                }
            });
            rvPlans.setAdapter(adapter);
        } else if (plans.size() >= 4) {
            mThreePlans = new RealmList<>();
            for (int i = 0; i < 3; i++) {
                mThreePlans.add(plans.get(i));
            }
            final PlanListAdapter threeAdapter = new PlanListAdapter(context, mThreePlans);
            mSmartRecyclerAdapter = new SmartRecyclerAdapter(threeAdapter);
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
            mSmartRecyclerAdapter.setFooterView(footerButton);
            threeAdapter.setOnItemClickListener(new PlanListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(int position, View view) {
                    Intent intent = new Intent(context, PlanDetailActivity.class);
                    intent.putExtra("planId", plans.get(position).getPlanId());
                    context.startActivity(intent);
                }
            });
            rvPlans.setAdapter(mSmartRecyclerAdapter);
        }
        holder.bind(project);

    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void flash() {
        if (mSmartRecyclerAdapter != null) {
            mSmartRecyclerAdapter.notifyDataSetChanged();
        }
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

    private void initNewPlanButton(ImageButton button, final String projectId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewPlanActivity.class);
                intent.putExtra(NewPlanActivity.PROJECT_ID, projectId);
                intent.putExtra(NewPlanActivity.TAG, "1");
                context.startActivity(intent);
            }
        });

    }
}
