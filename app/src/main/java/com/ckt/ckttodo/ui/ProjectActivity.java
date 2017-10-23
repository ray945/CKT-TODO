package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.adapter.ProjectAdapter;
import com.ckt.ckttodo.database.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ProjectActivity extends AppCompatActivity {
    private ProjectAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUI();
    }

    private void initUI() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_project);
        List<Project> project = new ArrayList<>();
        mAdapter = new ProjectAdapter(R.layout.project_recycler_list_item, project);
        for (int i = 0; i < 20; ++i) {
            project.add(new Project());
        }
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LayoutInflater inflater = LayoutInflater.from(this);
        View noticeView = inflater.inflate(R.layout.project_recycler_list_notice_header, null);
        mAdapter.addHeaderView(noticeView);
        View organizationView = inflater.inflate(R.layout.project_recycler_list_organization_header, null);
        mAdapter.addHeaderView(organizationView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
    }
}
