package com.ckt.ckttodo.ui;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.database.UserInfo;
import com.ckt.ckttodo.util.PlanListAdapter;
import io.realm.RealmList;
import java.util.UUID;

public class ProjectSingleFragment extends Fragment {
    private String mProjectId;
    private int mPlanStatus;
    private DetailProActivity mDetailProActivity;

    public static final int NEW_PLAN_REQUEST_CODE = 1;
    private static final String TAG = "ProjectSingleFragment";


    public static Fragment getInstance(String projectId, int planStatus) {
        ProjectSingleFragment fragment = new ProjectSingleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("projectId", projectId);
        bundle.putInt("planStatus", planStatus);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        mDetailProActivity = (DetailProActivity) context;
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
        final RealmList<Plan> planList = project.getPlans();

        // Classify plan
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
                Intent intent = new Intent(getActivity(), DetailPlanActivity.class);
                intent.putExtra("planId", planList.get(position).getPlanId());
                startActivity(intent);
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
                startActivityForResult(intent, NEW_PLAN_REQUEST_CODE);
            }
        });

    }


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == NEW_PLAN_REQUEST_CODE) {
                Log.e(TAG, data.getExtras().getString("planName"));
                DatabaseHelper helper = DatabaseHelper.getInstance(getContext());
                Plan plan = new Plan();
                plan.setSprint(mDetailProActivity.lastPosition + 1);
                plan.setPlanId(UUID.randomUUID().toString());
                plan.setStatus(mPlanStatus);
                UserInfo userInfo = helper.getRealm()
                    .where(UserInfo.class)
                    .contains(UserInfo.MEM_EMAIL, String.valueOf(new User(getContext()).getEmail()))
                    .findFirst();
                if (mDetailProActivity.project.getProjectId() == null) {
                    Toast.makeText(getContext(), getString(R.string.project_id_not_null),
                        Toast.LENGTH_SHORT).show();
                }
                plan.setProjectId(mDetailProActivity.project.getProjectId());
                plan.setUserInfo(userInfo);
                plan.setPlanName(data.getExtras().getString("planName"));
                mDetailProActivity.postNewPlan(plan);
            }
        }
    }
}
