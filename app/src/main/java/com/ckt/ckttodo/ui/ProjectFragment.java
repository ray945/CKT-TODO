package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.PostProject;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.databinding.FragmentProjectBinding;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.retrofit.ProjectService;
import com.ckt.ckttodo.util.ProjectListAdapter;
import com.ckt.ckttodo.util.TranserverUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhiwei.li
 */

public class ProjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ProjectFragment";
    public static final String PLAN_ID = "planId";
    public static final String PROJECT_ID = "projectId";
    public static final String TASK_START_TIME = "taskStartTime";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RealmResults<Project> mProjectList;
    private ProjectListAdapter mAdapter;
    private Plan currentPlan;
    private List<EventTask> mTasks;
    private NotifyTask mNotifyTask;
    private CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentProjectBinding binding = FragmentProjectBinding.inflate(inflater);
        RecyclerView rvProjects = binding.rvProject;
        mProjectList = DatebaseHelper.getInstance(getContext()).findAll(Project.class);

        mSwipeRefreshLayout = binding.swipeProject;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();

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
                        mTasks = new ArrayList<EventTask>();
                        for (Plan plan : mProjectList.get(position).getPlans()) {
                            plans.add(plan);
                        }
                        if (plans != null) {
                            for (int i = 0; i < plans.size(); i++) {
                                currentPlan = plans.get(i);
                                mTasks.clear();
                                if (plans.get(i).getEventTasks() != null) {
                                    for (int j = 0; j < plans.get(i).getEventTasks().size(); j++) {
                                        mTasks.add(plans.get(i).getEventTasks().get(j));
                                    }
                                    DatebaseHelper.getInstance(getContext()).getRealm().executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (EventTask task : mTasks) {
                                                task.setPlanId("");
                                                realm.copyToRealmOrUpdate(task);
//                                              DatebaseHelper.getInstance(getContext()).delete(task);
                                            }
                                            currentPlan.getEventTasks().clear();
                                            realm.copyToRealmOrUpdate(currentPlan);
                                        }
                                    });
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

    public void notifyDataChange() {
        mProjectList = DatebaseHelper.getInstance(getContext()).findAll(Project.class);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.flash();
    }

    @Override
    public void onRefresh() {

        Retrofit retrofit = HttpClient.getRetrofit();
        ProjectService projectService = retrofit.create(ProjectService.class);
        User user = new User(getContext());
        mSubscription.add(projectService.getProjects(user.getmEmail(), user.getmToken(), user.getmEmail())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        try {
                            String replyStr = response.string();
                            JSONObject replyJson = JSON.parseObject(replyStr);
                            switch (replyJson.getInteger(BeanConstant.RESULT_CODE)) {
                                case BeanConstant.SUCCESS_RESULT_CODE:
                                    String resultData = replyJson.getString("data");
                                    saveAndNotifyDataChange(resultData);
                                    break;
                                case BeanConstant.USER_STATUS_INVALID_ERRO_RESULT_CODE:
                                    Toast.makeText(getContext(), getString(R.string.login_status_timeout), Toast.LENGTH_SHORT).show();
                                    break;
                                case BeanConstant.PASS_DATA_INVALID_RESULT_CODE:
                                    Toast.makeText(getContext(), getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
        );

    }

    private void saveAndNotifyDataChange(String resultData) {
        DatebaseHelper helper = DatebaseHelper.getInstance(getContext());
        List<PostProject> postProjects = JSONObject.parseArray(resultData, PostProject.class);
        if (postProjects.size() == 0) {
            return;
        }
        List<Project> insertList = new ArrayList<>();
        List<Project> updateList = new ArrayList<>();
        for (PostProject postProject : postProjects) {
            Project project = helper.getRealm().where(Project.class).contains("projectId", postProject.getProjectId()).findFirst();
            if (project == null) {
                insertList.add(TranserverUtil.transProject(helper, postProject));
            } else {
                updateList.add(TranserverUtil.transProject(helper, postProject));
            }
        }
        helper.update(updateList);
        helper.insert(insertList);
        notifyDataChange();
    }


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
