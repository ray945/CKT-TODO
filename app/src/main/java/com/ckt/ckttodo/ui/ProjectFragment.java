package com.ckt.ckttodo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.databinding.FragmentProjectBinding;
import com.ckt.ckttodo.util.ProjectListAdapter;

import io.realm.RealmResults;

/**
 * Created by zhiwei.li
 */

public class ProjectFragment extends Fragment {

    public static final String PLAN_ID = "planId";
    public static final String PROJECT_ID = "projectId";
    public static final String TASK_START_TIME = "taskStartTime";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentProjectBinding binding = FragmentProjectBinding.inflate(inflater);
        RecyclerView rvProjects = binding.rvProject;

        RealmResults<Project> projectList = DatebaseHelper.getInstance(getContext()).findAll(Project.class);
        ProjectListAdapter adapter = new ProjectListAdapter(getContext(), projectList);
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
