package com.ckt.ckttodo.ui;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import android.transition.Explode;
import android.transition.Transition;


import com.ckt.ckttodo.R;
import com.ckt.ckttodo.databinding.ActivityChartBinding;

/**
 * Created by zhiwei.li
 */

public class ChartActivity extends AppCompatActivity {

    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        titles = getResources().getStringArray(R.array.chart_title);
        replaceFragment(ChartFragment.getInstance(titles[0]));
        setupWindowAnimations();
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
        ActivityChartBinding activityChartBinding = DataBindingUtil.setContentView(ChartActivity.this, R.layout.activity_chart);
        Toolbar toolbar = activityChartBinding.toolbar;
        toolbar.setTitle(R.string.dataCount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.week_btn:
                if (checked){
                    replaceFragment(ChartFragment.getInstance(titles[0]));
                }
                break;
            case R.id.month_btn:
                if (checked){
                    replaceFragment(ChartFragment.getInstance(titles[1]));
                }
                break;
            case R.id.quarter_btn:
                if (checked){
                    replaceFragment(ChartFragment.getInstance(titles[2]));
                }
                break;
            case R.id.year_btn:
                if (checked){
                    replaceFragment(ChartFragment.getInstance(titles[3]));
                }
                break;
        }
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.chart_content,fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_count_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.click_me:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
