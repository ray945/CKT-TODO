package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ckt.ckttodo.R;

/**
 * Created by hcy on 17-5-25.
 */

public class DetailProActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Fragment mFragment;
    private TabLayout mTabLayout;
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
