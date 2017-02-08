package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.FragmentProjectBinding;
import com.ckt.ckttodo.util.ProjectListAdapter;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProjectFragment extends Fragment {

    public static final String PLAN_ID = "planId";
    public static final String TASK_START_TIME = "taskStartTime";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentProjectBinding binding = FragmentProjectBinding.inflate(inflater);
        RecyclerView rvProjects = binding.rvProject;

        RealmResults<Plan> planList = DatebaseHelper.getInstance(getContext()).findAll(Plan.class);
        ProjectListAdapter adapter = new ProjectListAdapter(getContext(), planList);
        initRecyclerView(rvProjects, adapter, getContext());
        return binding.getRoot();
    }

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
