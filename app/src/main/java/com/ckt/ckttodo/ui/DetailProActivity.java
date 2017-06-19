package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ckt.ckttodo.R;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hcy on 17-5-25.
 */

public class DetailProActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Fragment mFragment;
    private TabLayout mTabLayout;
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, android.R.id.text1,  items);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);

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
        mTabLayout.setupWithViewPager(mViewPager);
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
}
