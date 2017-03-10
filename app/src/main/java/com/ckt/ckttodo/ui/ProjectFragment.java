package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.databinding.FragmentProjectBinding;
import com.ckt.ckttodo.util.ProjectListAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by zhiwei.li
 */

public class ProjectFragment extends Fragment {

    public static final String PLAN_ID = "planId";
    public static final String PROJECT_ID = "projectId";
    public static final String TASK_START_TIME = "taskStartTime";
    private RealmResults<Project> mProjectList;
    private ProjectListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentProjectBinding binding = FragmentProjectBinding.inflate(inflater);
        RecyclerView rvProjects = binding.rvProject;
        mProjectList = DatebaseHelper.getInstance(getContext()).findAll(Project.class);
        rvProjects.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvProjects, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(getContext()).setMessage("确认删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作 
                        List<Plan> plans = new ArrayList<Plan>();
                        List<EventTask> tasks = new ArrayList<EventTask>();
                        for (Plan plan : mProjectList.get(position).getPlans()) {
                            plans.add(plan);
                        }
                        if (plans != null) {
                            for (int i = 0; i < plans.size(); i++) {
                                tasks.clear();
                                if (plans.get(i).getEventTasks() != null) {
                                    for (int j = 0; j < plans.get(i).getEventTasks().size(); j++) {
                                        tasks.add(plans.get(i).getEventTasks().get(j));
                                    }
                                    for (EventTask task : tasks) {
                                        DatebaseHelper.getInstance(getContext()).delete(task);
                                    }
                                }
                                DatebaseHelper.getInstance(getContext()).delete(plans.get(i));
                            }
                            mNotifyTask = (NotifyTask) getActivity();
                            mNotifyTask.notifyTask();
                            mAdapter.flash();
                        }
                        DatebaseHelper.getInstance(getContext()).delete(mProjectList.get(position));
                        mAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作 
                    }
                }).show();
            }
        }));
        mAdapter = new ProjectListAdapter(getContext(), mProjectList);
        initRecyclerView(rvProjects, mAdapter, getContext());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.flash();
    }

    NotifyTask mNotifyTask;

    public interface NotifyTask {
        void notifyTask();
    }

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mListener;

        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (childView != null && mListener != null) {
                        mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());

            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
