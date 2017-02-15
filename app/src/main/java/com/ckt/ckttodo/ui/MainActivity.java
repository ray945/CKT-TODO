package com.ckt.ckttodo.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Visibility;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskFragment.ShowMainMenuItem {
    private static final String TAG = "main";
    public static final String PLAN_ID = "planId";
    public final static int MAIN_TO_NEW_TASK_CODE = 100;
    private ActivityMainBinding mActivityMainBinding;
    private MenuItem mMenuItemSure;
    private MenuItem mMenuItemFalse;
    private TaskFragment mTaskFragment;
    private List<Fragment> mFragmentList;
    private ScreenOffBroadcast mScreenOffBroadcast;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MAIN_TO_NEW_TASK_CODE){
            mTaskFragment.notifyData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerScreenOffBroadcast();
        mFragmentList = new ArrayList<>();
        initUI();
        setupWindowAnimations();
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
                        mTaskFragment = new TaskFragment();
                        fragment = mTaskFragment;
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
                        editText.setFocusable(true);
                        editText.setFocusableInTouchMode(true);
                        editText.requestFocus();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.showSoftInput(editText, 0);
                            }
                        }, 200);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle(R.string.new_plan).setView(editTextView).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String taskName = editText.getText().toString().trim();
                                for (Plan plan : DatebaseHelper.getInstance(MainActivity.this).findAll(Plan.class)) {
                                    if (taskName.equals(plan.getPlanName())) {
                                        showToast(getResources().getString(R.string.plan_exist));
                                        return;
                                    }
                                }
                                if (!taskName.equals("")) {
                                    Plan plan = new Plan();
                                    plan.setPlanId(UUID.randomUUID().toString());
                                    plan.setPlanName(taskName);
                                    Date date = new Date();
                                    plan.setCreateTime(date.getTime());
                                    DatebaseHelper.getInstance(MainActivity.this).insert(plan);
                                } else {
                                    showToast(getResources().getString(R.string.plan_not_null));
                                }
                            }
                        }).setNegativeButton(R.string.cancel, null);
                        builder.create().show();
                        break;
                    case 2:
                        Log.e(TAG, "note click");
                        Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                        intent.putExtra("noteTag", "2");
                        transitionTo(intent);
                        break;
                }
            }
        });
    }

    @SuppressWarnings("unchecked") void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }

    private void setupWindowAnimations() {
        Visibility enterTransition = buildEnterTransition();
        getWindow().setEnterTransition(enterTransition);
    }

    private Visibility buildEnterTransition() {
        Fade enterTransition = new Fade();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        // This view will not be affected by enter transition animation
        return enterTransition;
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
    private void registerScreenOffBroadcast( ) {
        if( mScreenOffBroadcast == null ) {
            mScreenOffBroadcast = new ScreenOffBroadcast( this );
        }
        mScreenOffBroadcast.registerBroadcast( );
    }

    private void unRegisterScreenOffBroadcast( ) {
        if( mScreenOffBroadcast != null ) {
            mScreenOffBroadcast.unregisterBroadcast( );
        }
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
                startActivityForResult(new Intent(this, NewTaskActivity.class),MAIN_TO_NEW_TASK_CODE);;
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
            startActivity(new Intent(this,FinishedTaskActivity.class));

        } else if (id == R.id.nav_count) {
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            transitionTo(intent);
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

    @Override
    public void setShowMenuItem(boolean isShow) {
        mMenuItemFalse.setVisible(isShow);
        mMenuItemSure.setVisible(isShow);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterScreenOffBroadcast();
    }
}
