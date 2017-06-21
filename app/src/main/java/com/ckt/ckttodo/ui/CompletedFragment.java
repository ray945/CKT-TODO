package com.ckt.ckttodo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.database.UserInfo;
import com.ckt.ckttodo.model.Goal;
import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.callback.BatCallback;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hcy on 17-5-25.
 */

public class CompletedFragment extends Fragment implements BatListener, OnItemClickListener, OnOutsideClickedListener {
    private BatRecyclerView mRecyclerView;
    private BatAdapter mAdapter;
    private LinkedList<BatModel> mGoals;
    private BatItemAnimator mAnimator;
    private DetailProActivity mDetailProActivity;
    private DatebaseHelper mHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDetailProActivity = (DetailProActivity) context;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_personal_project, container, false);
        mRecyclerView = (BatRecyclerView) mView.findViewById(R.id.rv_personal_project);
        mGoals = new LinkedList<>();
        mAnimator = new BatItemAnimator();
        mAdapter = new BatAdapter(mGoals, this, mAnimator);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnOutsideClickListener(this);
        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.getView().setAdapter(mAdapter);
        mRecyclerView.getView().setItemAnimator(mAnimator);
        mRecyclerView.setAddItemListener(this);
//        sceenData();
        mHelper = DatebaseHelper.getInstance(getContext());
        mView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.revertAnimation();
            }
        });
        return mView;
    }

//    private void sceenData() {
//        mHelper = DatebaseHelper.getInstance(getContext());
//        List<Plan> planList = mHelper.getRealm().where(Plan.class).contains(Project.PROJECT_ID, mDetailProActivity.mProjectId).findAll();
//        for (Plan plan : planList) {
//            mGoals.add(new Goal(plan.getProjectId()));
//        }
//        mAdapter.notifyDataSetChanged();
//    }

    @Override
    public void add(String string) {
        Plan plan = new Plan();
        plan.setPlanId(UUID.randomUUID().toString());
        UserInfo userInfo = mHelper.getRealm().where(UserInfo.class).contains(UserInfo.MEM_EMAIL, String.valueOf(new User(getContext()).getmEmail())).findFirst();
        if (mDetailProActivity.mProjectId == null) {
            Toast.makeText(getContext(), getString(R.string.project_id_not_null), Toast.LENGTH_SHORT).show();
        }
        plan.setProjectId(mDetailProActivity.mProjectId);
        plan.setUserInfo(userInfo);
        plan.setPlanName(string);
        mDetailProActivity.postNewPlan(plan);
    }

    public void postPlanSuccessful(String string) {
        mGoals.addFirst(new Goal(string));
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

}
