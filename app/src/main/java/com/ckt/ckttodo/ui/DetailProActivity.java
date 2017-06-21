package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.Result;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.network.HttpConstants;
import com.ckt.ckttodo.retrofit.PlanService;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


import java.util.Arrays;
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
    protected String mProjectId;
    private Spinner mSpinner;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpro);
        initUI();
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.personal_project));
        mTabLayout = (TabLayout) findViewById(R.id.detail_tab);
        mViewPager = (ViewPager) findViewById(R.id.detail_view_pager);
        mSpinner = (Spinner) findViewById(R.id.nice_spinner);
        List<String> items = new LinkedList<>(Arrays.asList("Sprint1", "Sprint2", "Sprint3", "Sprint4", "Sprint5"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, android.R.id.text1, items);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);

        FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new Fragment();
                switch (position) {
                    case 0:
                        mPendingFragment = new PendingFragment();
                        fragment = mPendingFragment;
                        break;
                    case 1:
                        mOngoingFragment = new OngoingFragment();
                        fragment = mOngoingFragment;
                        break;
                    case 2:
                        mCompletedFragment = new CompletedFragment();
                        fragment = mCompletedFragment;
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
        mProjectId = getIntent().getStringExtra(Project.PROJECT_ID);
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
        postMap.put(BeanConstant.EMAIL, user.getmEmail());
        postMap.put(BeanConstant.TOKEN, user.getmToken());
        JSONObject jPlan = new JSONObject();
        jPlan.put("plan_id", plan.getPlanId());
        jPlan.put("project_id", plan.getProjectId());
        jPlan.put("mem_id", user.getmID());
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
                                if (mViewPager.getCurrentItem() == 0) {
                                    mPendingFragment.postPlanSuccessful(plan.getPlanName());
                                } else if (mViewPager.getCurrentItem() == 1) {
                                    mOngoingFragment.postPlanSuccessful(plan.getPlanName());
                                } else if (mViewPager.getCurrentItem() == 2) {
                                    mCompletedFragment.postPlanSuccessful(plan.getPlanName());
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
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
