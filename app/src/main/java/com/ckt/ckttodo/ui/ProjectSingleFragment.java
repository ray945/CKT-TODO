package com.ckt.ckttodo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.util.PlanListAdapter;
import io.realm.RealmList;

public class ProjectSingleFragment extends Fragment {
    private String mProjectId;
    private int mPlanStatus;


    public static Fragment getInstance(String projectId, int planStatus) {
        ProjectSingleFragment fragment = new ProjectSingleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("projectId", projectId);
        bundle.putInt("planStatus", planStatus);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mPlanStatus = bundle.getInt("planStatus");
        mProjectId = bundle.getString("projectId");
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_project, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        Project project = DatabaseHelper.getInstance(getContext())
            .find(Project.class)
            .equalTo(Project.PROJECT_ID, mProjectId)
            .findFirst();
        TextView projectName = (TextView) view.findViewById(R.id.tv_project_name);
        view.findViewById(R.id.tv_project_progress).setVisibility(View.GONE);
        view.findViewById(R.id.tv_project_progress_text).setVisibility(View.GONE);
        initNewPlanButton((ImageButton) view.findViewById(R.id.btn_addPlan), mProjectId);
        projectName.setText(project.getProjectTitle());
        RecyclerView mPlansRv = (RecyclerView) view.findViewById(R.id.rv_plans);
        mPlansRv.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RealmList<Plan> planList = project.getPlans();
        // RealmList<Plan> planCategoryList = new RealmList<>();
        // for (Plan plan : planList) {
        //     if (plan.getStatus() == mPlanStatus) {
        //         planCategoryList.add(plan);
        //     }
        // }
        // PlanListAdapter adapter = new PlanListAdapter(getContext(), planCategoryList);
        PlanListAdapter adapter = new PlanListAdapter(getContext(), planList);
        adapter.setOnItemClickListener(new PlanListAdapter.OnItemClickListener() {
            @Override public void onItemClick(int position, View view) {
                // Intent intent = new Intent(getActivity(),DetailPlanActivity.class);
                // intent.putExtra("planId", planList.get(position).getPlanId());
                // startActivity(intent);
            }
        });
        mPlansRv.setAdapter(adapter);
    }


    private void initNewPlanButton(ImageButton button, final String projectId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewPlanActivity.class);
                intent.putExtra(NewPlanActivity.PROJECT_ID, projectId);
                intent.putExtra(NewPlanActivity.TAG, "1");
                getContext().startActivity(intent);
            }
        });

    }
}
