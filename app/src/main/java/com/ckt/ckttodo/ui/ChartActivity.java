package com.ckt.ckttodo.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.transition.Explode;
import android.transition.Transition;

import android.view.MenuItem;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.databinding.ActivityChartBinding;
import java.util.ArrayList;

/**
 * Created by zhiwei.li
 */

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        if (Build.VERSION.SDK_INT >= 21){
            setupWindowAnimations();
        }
    }


    private void setupWindowAnimations() {
        Transition transition;
        transition = buildEnterTransition();
        getWindow().setEnterTransition(transition);
    }


    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }


    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActivityChartBinding binding = DataBindingUtil.setContentView(
            ChartActivity.this, R.layout.activity_chart);
        final String[] titles = getResources().getStringArray(R.array.chart_title);
        final ArrayList<Fragment> fragments = new ArrayList<>();
        for (String title : titles) {
            fragments.add(ChartFragment.getInstance(title));
        }
        ViewPager viewPager = binding.viewPager;
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override public Fragment getItem(int position) {
                return fragments.get(position);
            }


            @Override public int getCount() {
                return fragments.size();
            }


            @Override public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        binding.tabLayout.setupWithViewPager(viewPager);
    }


    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    //     getMenuInflater().inflate(R.menu.data_count_menu, menu);
    //     return super.onCreateOptionsMenu(menu);
    // }
    //
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.click_me:

                return true;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
