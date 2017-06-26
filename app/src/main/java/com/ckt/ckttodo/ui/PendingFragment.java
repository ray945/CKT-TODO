package com.ckt.ckttodo.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.database.UserInfo;
import com.ckt.ckttodo.model.Goal;
import com.ckt.ckttodo.util.Constants;
import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hcy on 17-5-25.
 */

public class PendingFragment extends Fragment implements BatListener, OnItemClickListener, OnOutsideClickedListener, SwipeRefreshLayout.OnRefreshListener {
    private BatRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BatAdapter mAdapter;
    private List<BatModel> mGoals;
    private BatItemAnimator mAnimator;
    private DetailProActivity mDetailProActivity;
    private DatabaseHelper mHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDetailProActivity = (DetailProActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_personal_project, container, false);
        mRecyclerView = (BatRecyclerView) mView.findViewById(R.id.rv_personal_project);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_plan);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mGoals = new ArrayList<BatModel>();
        mAnimator = new BatItemAnimator();
        mAdapter = new BatAdapter(mGoals, this, mAnimator);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnOutsideClickListener(this);
        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.getView().setAdapter(mAdapter);
        mRecyclerView.getView().setItemAnimator(mAnimator);
        mRecyclerView.setAddItemListener(this);
        sceenData();
        mView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.revertAnimation();
            }
        });
        return mView;
    }

    private void sceenData() {
        mGoals.clear();
        int sprint = getContext().getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE).getInt(Constants.CURRENT_SPRINT, 1);
        mHelper = DatabaseHelper.getInstance(getContext());
        List<Plan> planList = mHelper.getRealm().where(Plan.class).contains(Project.PROJECT_ID, mDetailProActivity.project.getProjectId()).findAll();
        for (Plan plan : planList) {
            if (plan.getStatus() == Plan.PLAN_PENDING && sprint == plan.getSprint()) {
                mGoals.add(new Goal(plan.getPlanName(), plan.getPlanId()));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void add(String string) {
        DatabaseHelper helper = DatabaseHelper.getInstance(getContext());
        Plan plan = new Plan();
        plan.setSprint(mDetailProActivity.lastPosition + 1);
        plan.setPlanId(UUID.randomUUID().toString());
        plan.setStatus(Plan.PLAN_PENDING);
        UserInfo userInfo = helper.getRealm().where(UserInfo.class).contains(UserInfo.MEM_EMAIL, String.valueOf(new User(getContext()).getEmail())).findFirst();
        if (mDetailProActivity.project.getProjectId()== null) {
            Toast.makeText(getContext(), getString(R.string.project_id_not_null), Toast.LENGTH_SHORT).show();
        }
        plan.setProjectId(mDetailProActivity.project.getProjectId());
        plan.setUserInfo(userInfo);
        plan.setPlanName(string);
        mDetailProActivity.postNewPlan(plan);
    }

    public void postPlanSuccessful(String string, String planId) {
        mGoals.add(0, new Goal(string, planId));
        mAdapter.notify(AnimationType.ADD, 0);
    }


    @Override
    public void delete(int position) {
        /* conflict with viewpager,don't realize */
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
    }

    @Override
    public void onOutsideClicked() {
        mRecyclerView.revertAnimation();
    }


    @Override
    public void onRefresh() {

        int sprint = getContext().getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE).getInt(Constants.CURRENT_SPRINT, 1);
        mDetailProActivity.getCurrentSprintData(sprint, Plan.PLAN_PENDING);
    }

    public void notifySprintChanged() {
        sceenData();
    }

    public void onRefreshComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
