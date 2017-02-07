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
        binding.fabProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View editTextView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edittext, null);
                final EditText editText = (EditText) editTextView.findViewById(R.id.new_task_name);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.new_plan)
                        .setView(editTextView)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Realm realm = DatebaseHelper.getInstance(getContext()).getRealm();
                                final String taskName = editText.getText().toString().trim();
                                for (Plan plan : DatebaseHelper.getInstance(getContext()).findAll(Plan.class)) {
                                    if (taskName.equals(plan.getPlanName())) {
                                        showToast(getResources().getString(R.string.plan_exist));
                                        return;
                                    }
                                }
                                if (!taskName.equals("")) {
                                    Plan plan = new Plan();
                                    int id;
                                    if (realm.where(Plan.class).count() == 0) {
                                        id = 0;
                                    } else {
                                        RealmResults<Plan> plans = realm.where(Plan.class).findAllSorted(PLAN_ID, false);
                                        id = plans.first().getPlanId();
                                        id += 1;
                                    }
                                    plan.setPlanId(id);
                                    plan.setPlanName(taskName);
                                    Date date = new Date();
                                    plan.setCreateTime(date.getTime());
                                    DatebaseHelper.getInstance(getContext()).insert(plan);
                                } else {
                                    showToast(getResources().getString(R.string.plan_not_null));
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });

         RealmResults<Plan> planList = DatebaseHelper.getInstance(getContext()).findAll(Plan.class);
        ProjectListAdapter adapter = new ProjectListAdapter(getContext(), planList);
        initRecyclerView(rvProjects, adapter, getContext());
        return binding.getRoot();
    }

    void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
