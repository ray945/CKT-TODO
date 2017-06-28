package com.ckt.ckttodo.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.PostPlan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.Result;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HTTPService;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.retrofit.PlanService;
import com.ckt.ckttodo.retrofit.ProjectService;
import com.ckt.ckttodo.util.Constants;
import com.ckt.ckttodo.util.TranserverUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by hcy on 17-5-25.
 */

public class DetailProActivity extends AppCompatActivity {

    private static final String TAG = "DetailProActivity";
    private ViewPager mViewPager;
    private PendingFragment mPendingFragment;
    private OngoingFragment mOngoingFragment;
    private CompletedFragment mCompletedFragment;
    private TabLayout mTabLayout;
    private DatabaseHelper mHelper;
    protected Project project;
    private Project mModifyProject;
    private Spinner mSpinner;
    private LinkedList<String> mItems;
    public int lastPosition = 0;

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(DetailProActivity.this).inflate(R.layout.simple_spinner_dropdown_item, parent, false);
            }
            if (position == mItems.size() - 1) {
                convertView.findViewById(R.id.spinner_sprint_content).setVisibility(View.GONE);
                convertView.findViewById(R.id.spinner_sprint_add).setVisibility(View.VISIBLE);
            } else {
                TextView textView = ((TextView) convertView.findViewById(R.id.spinner_sprint_content));
                textView.setText(mItems.get(position));
                textView.setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.spinner_sprint_add).setVisibility(View.GONE);
            }

            return convertView;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpro);
        initUI();
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHelper = DatabaseHelper.getInstance(this);
        getSupportActionBar().setTitle(getResources().getString(R.string.detail_project));
        final String projectId = getIntent().getStringExtra(Project.PROJECT_ID);
        project = mHelper.getRealm().where(Project.class).contains(Project.PROJECT_ID, projectId).findFirst();
        mTabLayout = (TabLayout) findViewById(R.id.detail_tab);
        mViewPager = (ViewPager) findViewById(R.id.detail_view_pager);
        mSpinner = (Spinner) findViewById(R.id.nice_spinner);
        mItems = new LinkedList<>();
        for (int i = 0; i < project.getSprintCount(); ++i) {
            mItems.add(getString(R.string.sprint) + " " + (i + 1));
        }
        mItems.add("");
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == (mItems.size() - 1)) {
                    mItems.add(mItems.size() - 1, getString(R.string.sprint) + " " + mItems.size());
//                    mItems.add(project.getSprintCount(), getString(R.string.sprint) + " " + (project.getSprintCount() + 1));
                    mSpinner.setSelection(lastPosition);
                    postNewSprint(mItems.size() - 1);
                } else {
                    if (lastPosition != position) {
                        getCurrentSprintData(position + 1, Plan.PLAN_START);
                        getCurrentSprintData(position + 1, Plan.PLAN_PENDING);
                        getCurrentSprintData(position + 1, Plan.DONE);

                    }
                    lastPosition = position;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            };
        });
        FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new Fragment();
                switch (position) {
                    case 0:
                         // mPendingFragment = new PendingFragment();
                         // fragment = mPendingFragment;
                        fragment = ProjectSingleFragment.getInstance(project.getProjectId(), Plan.PLAN_PENDING);
                        break;
                    case 1:
                         // mOngoingFragment = new OngoingFragment();
                         // fragment = mOngoingFragment;
                        fragment = ProjectSingleFragment.getInstance(project.getProjectId(), Plan.PLAN_START);
                        break;
                    case 2:
                         // mCompletedFragment = new CompletedFragment();
                         // fragment = mCompletedFragment;
                        fragment = ProjectSingleFragment.getInstance(project.getProjectId(), Plan.DONE);
                        break;
                    default:
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String[] mTitles = {"待处理", "进行中", "已完成"};
                return mTitles[position];
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void postNewSprint(int sprint) {
        User user = new User(this);
        HttpClient.getHttpService(ProjectService.class).postNewSprint(user.getEmail(), user.getToken(), project.getProjectId(), sprint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result value) {
                        switch (value.getResultcode()) {
                            case BeanConstant.SUCCESS_RESULT_CODE:
                                mModifyProject = new Project(project);
                                mModifyProject.setSprintCount(project.getSprintCount() + 1);
                                mHelper.update(mModifyProject);
                                Toast.makeText(DetailProActivity.this, getString(R.string.sprint_add_success), Toast.LENGTH_SHORT).show();
                                mSpinner.setSelection(mItems.size() - 2);
                                break;
                            case BeanConstant.USER_STATUS_INVALID_ERRO_RESULT_CODE:
                                mItems.remove(mItems.size() - 2);
                                mSpinner.setSelection(lastPosition);
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(DetailProActivity.this, getString(R.string.login_status_timeout), Toast.LENGTH_SHORT).show();
                                break;
                            case BeanConstant.PASS_DATA_INVALID_RESULT_CODE:
                                mItems.remove(mItems.size() - 2);
                                mAdapter.notifyDataSetChanged();
                                mSpinner.setSelection(lastPosition);
                                Toast.makeText(DetailProActivity.this, getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mItems.remove(mItems.size() - 2);
                        mAdapter.notifyDataSetChanged();
                        mSpinner.setSelection(lastPosition);
                        Toast.makeText(DetailProActivity.this, getString(R.string.request_service_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getCurrentSprintData(final int sprint, final int status) {

        final User user = new User(this);
        HttpClient.getHttpService(PlanService.class).getPlans(user.getEmail(), user.getToken(), sprint, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<PostPlan>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<PostPlan> value) {
                        switch (value.getResultcode()) {
                            case BeanConstant.SUCCESS_RESULT_CODE:
                                try {
                                    saveData(value.getData(), user.getEmail());
                                } catch (Exception e) {
                                    onError(e);
                                }
                                SharedPreferences sharedPreferences = DetailProActivity.this
                                        .getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE);
                                sharedPreferences.edit().putInt(Constants.CURRENT_SPRINT, sprint).apply();
                                if (status == Plan.DONE) {
                                    if (mCompletedFragment != null) {
                                        mCompletedFragment.notifySprintChanged();
                                    }
                                } else if (status == Plan.PLAN_START) {
                                    if (mOngoingFragment != null) {
                                        mOngoingFragment.notifySprintChanged();
                                    }
                                } else if (status == Plan.PLAN_PENDING) {
                                    if (mPendingFragment != null) {
                                        mPendingFragment.notifySprintChanged();
                                    }
                                }
                                break;
                            case BeanConstant.USER_STATUS_INVALID_ERRO_RESULT_CODE:
                                Toast.makeText(DetailProActivity.this, getString(R.string.login_status_timeout), Toast.LENGTH_SHORT).show();
                                break;
                            case BeanConstant.PASS_DATA_INVALID_RESULT_CODE:
                                Toast.makeText(DetailProActivity.this, getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(DetailProActivity.this, getString(R.string.request_service_fail), Toast.LENGTH_SHORT).show();
                        // completeRefresh();
                    }


                    @Override
                    public void onComplete() {
                        // completeRefresh();
                    }
                });

    }

    // private void completeRefresh() {
    //     switch (mViewPager.getCurrentItem()) {
    //         case 0:
    //             mPendingFragment.onRefreshComplete();
    //             break;
    //         case 1:
    //             mOngoingFragment.onRefreshComplete();
    //             break;
    //         case 2:
    //             mCompletedFragment.onRefreshComplete();
    //             break;
    //     }
    // }

    private void saveData(List<PostPlan> data, String email) throws Exception {

        if (data == null || data.size() == 0) {
            return;
        }
        List<Plan> insertPlan = new ArrayList<>();
        List<Plan> updatePlan = new ArrayList<>();
        Plan plan;
        Plan exPlan = null;

        for (PostPlan postPlan : data) {
            plan = TranserverUtil.convertPlan(postPlan, mHelper, email);
            try {
                exPlan = mHelper.getRealm().where(Plan.class).contains(Plan.PLAN_ID, plan.getPlanId()).findFirst();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (exPlan == null) {
                insertPlan.add(plan);
            } else {
                updatePlan.add(plan);
            }
        }

        if (insertPlan.size() > 0) {
            mHelper.insert(insertPlan);
        }
        if (updatePlan.size() > 0) {
            mHelper.update(updatePlan);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void postNewPlan(final Plan plan) {

        User user = new User(this);
        Map<String, String> postMap = new HashMap<>();
        postMap.put(BeanConstant.EMAIL, user.getEmail());
        postMap.put(BeanConstant.TOKEN, user.getToken());
        JSONObject jPlan = new JSONObject();
        jPlan.put("plan_id", plan.getPlanId());
        jPlan.put("project_id", plan.getProjectId());
        jPlan.put("mem_id", user.getId());
        jPlan.put("plan_name", plan.getPlanName());
        jPlan.put("sprint", plan.getSprint());
        jPlan.put("plan_state", plan.getStatus());
        postMap.put("plan", jPlan.toJSONString());

        HttpClient.getHttpService(PlanService.class).postNewPlan(postMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result value) {
                        switch (value.getResultcode()) {
                            case BeanConstant.SUCCESS_RESULT_CODE:
                                // if (mViewPager.getCurrentItem() == 0) {
                                //     mPendingFragment.postPlanSuccessful(plan.getPlanName(), plan.getPlanId());
                                // } else if (mViewPager.getCurrentItem() == 1) {
                                //     mOngoingFragment.postPlanSuccessful(plan.getPlanName(), plan.getPlanId());
                                // } else if (mViewPager.getCurrentItem() == 2) {
                                //     mCompletedFragment.postPlanSuccessful(plan.getPlanName(), plan.getPlanId());
                                // }
                                break;
                            case BeanConstant.USER_STATUS_INVALID_ERRO_RESULT_CODE:
                                Toast.makeText(DetailProActivity.this, getString(R.string.login_status_timeout), Toast.LENGTH_SHORT).show();
                                break;
                            case BeanConstant.PASS_DATA_INVALID_RESULT_CODE:
                                Toast.makeText(DetailProActivity.this, getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(DetailProActivity.this, getString(R.string.request_service_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        DatabaseHelper.getInstance(DetailProActivity.this).insert(plan);

                    }
                });

    }
}
