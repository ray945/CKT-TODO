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

import android.widget.Toast;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.databinding.FragmentProjectBinding;
import com.ckt.ckttodo.util.ProjectListAdapter;

import io.realm.Sort;
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
    private Plan currentPlan;
    private List<EventTask> mTasks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentProjectBinding binding = FragmentProjectBinding.inflate(inflater);
        RecyclerView rvProjects = binding.rvProject;
        mProjectList = DatebaseHelper.getInstance(getContext())
                .getRealm()
                .where(Project.class)
                .findAllSorted("createTime", Sort.ASCENDING);
        rvProjects.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvProjects,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO: add jump project detail logic.
                        Toast.makeText(getActivity(), mProjectList.get(position).getProjectTitle(),
                                Toast.LENGTH_SHORT).show();
                    }


                    @Override public void onItemLongClick(View view, final int position) {
                        new AlertDialog.Builder(getContext())
                                .setMessage(R.string.want_delete)
                                .setPositiveButton(R.string.sure,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface,
                                                                int i) {
                                                DatebaseHelper.getInstance(getActivity())
                                                        .delete(mProjectList.get(position));
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton(R.string.cancel, null)
                                .show();
                    }
                }));
        mAdapter = new ProjectListAdapter(getContext(), mProjectList);
        initRecyclerView(rvProjects, mAdapter, getContext());
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public interface NotifyTask {
        void notifyTask();
    }


    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
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

            mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }


                        @Override
                        public void onLongPress(MotionEvent e) {
                            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                            if (childView != null && mListener != null) {
                                mListener.onItemLongClick(childView,
                                        recyclerView.getChildAdapterPosition(childView));
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
