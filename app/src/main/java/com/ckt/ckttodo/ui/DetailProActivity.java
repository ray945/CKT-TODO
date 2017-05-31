package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.widgt.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by hcy on 17-5-25.
 */

public class DetailProActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Fragment mFragment;
    private CircleIndicator mCircleIndicator;
    private NiceSpinner mNiceSpinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpro);
        initUI();
    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.detail_view_pager);
        mCircleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mNiceSpinner = (NiceSpinner) findViewById(R.id.spinner_sprint);
        List<String> dataset = new LinkedList<>(Arrays.asList("Sprint1", "Sprint2", "Sprint3", "Sprint4", "Sprint5"));
        mNiceSpinner.attachDataSource(dataset);
        FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        mFragment = new PendingFragment();
                        break;
                    case 1:
                        mFragment = new OngoingFragment();
                        break;
                    case 2:
                        mFragment = new CompletedFragment();
                        break;
                    default:
                        break;
                }
                return mFragment;
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
        mCircleIndicator.setViewPager(mViewPager);
    }
}
