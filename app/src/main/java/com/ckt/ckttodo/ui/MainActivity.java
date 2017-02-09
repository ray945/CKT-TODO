package com.ckt.ckttodo.ui;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskFragment.ShowMainMenuItem {

    private ActivityMainBinding mActivityMainBinding;
    private MenuItem mMenuItemSure;
    private MenuItem mMenuItemFalse;
    private TaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //TODO here
        mTaskFragment = new TaskFragment();

        ViewPager viewPager = mActivityMainBinding.appBarMain.contentMain.viewPager;
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = mTaskFragment;
                        break;
                    case 1:
                        fragment = new ProjectFragment();
                        break;
                    case 2:
                        fragment = new NoteFragment();
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
                String[] mTitles = {getString(R.string.task), getString(R.string.project), getString(R.string.note)};
                return mTitles[position];
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = mActivityMainBinding.appBarMain.contentMain.tabLayout;
        tabLayout.setupWithViewPager(viewPager);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mMenuItemSure = menu.findItem(R.id.menu_sure);
        mMenuItemFalse = menu.findItem(R.id.menu_no);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_research:
                startActivity(new Intent(this, NewTaskActivity.class));
                break;
            case R.id.menu_sure:
                //删除选中项并结束事件
                mMenuItemFalse.setVisible(false);
                mMenuItemSure.setVisible(false);
                mTaskFragment.finishDeleteAction(true);
                break;
            case R.id.menu_no:
                //不删除选中项结束事件
                mMenuItemFalse.setVisible(false);
                mMenuItemSure.setVisible(false);
                mTaskFragment.finishDeleteAction(false);
                break;
        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

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


    @Override
    public void setShowMenuItem(boolean isShow) {
        mMenuItemFalse.setVisible(isShow);
        mMenuItemSure.setVisible(isShow);
    }


}
