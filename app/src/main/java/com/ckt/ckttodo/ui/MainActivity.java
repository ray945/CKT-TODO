package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "main";
    public static final String PLAN_ID = "planId";

    private ActivityMainBinding mActivityMainBinding;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentList = new ArrayList<>();
        initUI();
    }

    private void initUI() {
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        Toolbar toolbar = mActivityMainBinding.appBarMain.toolbar;
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        toolbar.setTitle(formatter.format(curDate));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = mActivityMainBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        NavigationView navigationView = mActivityMainBinding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        final ViewPager viewPager = mActivityMainBinding.appBarMain.contentMain.viewPager;
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new TaskFragment();
                        break;
                    case 1:
                        fragment = new ProjectFragment();
                        break;
                    case 2:
                        fragment = new NoteFragment();
                        break;
                }
                mFragmentList.add(fragment);
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String[] mTitles = {getString(R.string.task), getString(R.string.project), getString(R.string.note)};
                return mTitles[position];
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = mActivityMainBinding.appBarMain.contentMain.tabLayout;
        tabLayout.setupWithViewPager(viewPager);

        mActivityMainBinding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        Log.e(TAG, "task click");
                        //TODO Task floating button click
                        break;
                    case 1:
                        Log.e(TAG, "project click");
                        View editTextView = getLayoutInflater().inflate(R.layout.dialog_edittext, null);
                        final EditText editText = (EditText) editTextView.findViewById(R.id.new_task_name);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.new_plan)
                                .setView(editTextView)
                                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Realm realm = DatebaseHelper.getInstance(MainActivity.this).getRealm();
                                        final String taskName = editText.getText().toString().trim();
                                        for (Plan plan : DatebaseHelper.getInstance(MainActivity.this).findAll(Plan.class)) {
                                            if (taskName.equals(plan.getPlanName())) {
                                                showToast(getResources().getString(R.string.plan_exist));
                                                return;
                                            }
                                        }
                                        if (!taskName.equals("")) {
                                            Plan plan = new Plan();
                                            int id;
                                            if (realm.where(Plan.class).count() == 0) {
                                                id = 0;
                                            } else {
                                                RealmResults<Plan> plans = realm.where(Plan.class).findAllSorted(PLAN_ID, false);
                                                id = plans.first().getPlanId();
                                                id += 1;
                                            }
                                            plan.setPlanId(id);
                                            plan.setPlanName(taskName);
                                            Date date = new Date();
                                            plan.setCreateTime(date.getTime());
                                            DatebaseHelper.getInstance(MainActivity.this).insert(plan);
                                        } else {
                                            showToast(getResources().getString(R.string.plan_not_null));
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null);
                        builder.create().show();
                        break;
                    case 2:
                        Log.e(TAG, "note click");
                        //TODO Note floating button click
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = mActivityMainBinding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_task) {
            // Handle the camera action
        } else if (id == R.id.nav_count) {

        } else if (id == R.id.nav_team) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = mActivityMainBinding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
