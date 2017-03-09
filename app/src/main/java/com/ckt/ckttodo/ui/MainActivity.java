package com.ckt.ckttodo.ui;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.databinding.ActivityMainBinding;
import com.ckt.ckttodo.util.CircularAnimUtil;
import com.ckt.ckttodo.util.Constants;
import com.ckt.ckttodo.util.NotificationBroadcastReceiver;
import com.ckt.ckttodo.util.PermissionUtil;
import com.ckt.ckttodo.util.excelutil.EventTaskExcelBean;
import com.ckt.ckttodo.util.excelutil.ExcelManager;
import com.ckt.ckttodo.widgt.VoiceInputDialog;

import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;

import io.realm.RealmResults;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , TaskFragment.ShowMainMenuItem, ActivityCompat.OnRequestPermissionsResultCallback
        , VoiceInputDialog.VoiceInputFinishedListener {
    private static final String TAG = "main";
    public static final String PLAN_ID = "planId";
    public static final String SHARE_PREFERENCES_NAME = "com.ckt.ckttodo";
    private static final String IS_FIRST_CHECK_PERMISSION = "permission_status";
    private static final int REQUEST_PERMISSIONS = 1;
    public final static int MAIN_TO_NEW_TASK_CODE = 100;
    public final static int MAIN_TO_TASK_DETAIL_CODE = 200;
    private ActivityMainBinding mActivityMainBinding;
    private MenuItem mMenuItemSure;
    private MenuItem mMenuItemFalse;
    private TaskFragment mTaskFragment;
    private List<Fragment> mFragmentList;
    private ScreenOffBroadcast mScreenOffBroadcast;
    private static String[] PERMISSION_LIST = new String[]{Constants.RECORD_AUDIO, Constants.READ_PHONE_STATE, Constants.READ_EXTERNAL_STORAGE, Constants.WRITE_EXTERNAL_STORAGE};
    private VoiceInputDialog mDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NewTaskActivity.NEW_TASK_SUCCESS_RESULT_CODE) {
            mTaskFragment.notifyData();
        } else if (resultCode == TaskDetailActivity.TASK_DETAIL_MAIN_RESULT_CODE) {
            if (data != null) {
                boolean shouldUpdateData = data.getBooleanExtra(TaskDetailActivity.IS_TASK_DETAIL_MODIFY, false);
                if (shouldUpdateData) {
                    mTaskFragment.notifyData();
                }
            }
        }
        if (requestCode == Constant.REQUEST_CODE_PICK_FILE){
            if (resultCode ==RESULT_OK){
                ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                withResultInsertDatabase(list.get(0).getPath());
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean flag = PermissionUtil.verifyPermission(grantResults);
            if (flag) {
                Toast.makeText(this, getResources().getString(R.string.get_permission_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.get_permission_fail), Toast.LENGTH_LONG).show();
            }

        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerScreenOffBroadcast();
        mFragmentList = new ArrayList<>();
        initUI();
        setupWindowAnimations();
        initNotification(this);
        initPermission();
    }

    private void initPermission() {
        SharedPreferences preferences = MainActivity.this.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(IS_FIRST_CHECK_PERMISSION, true);
        if (isFirstTime) {
            getTheVoiceInput();
            preferences.edit().putBoolean(IS_FIRST_CHECK_PERMISSION, false).commit();
        }

    }

    public static void initNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent("com.ckt.ckttodo.alarm");
        intent.putExtra(NotificationBroadcastReceiver.NOTIFICATION_TITLE, context.getResources().getString(R.string.remind_title));
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 100000, pendingIntent);
    }

    private void initUI() {
        mDialog = new VoiceInputDialog(this, R.style.VoiceInputDialog, this);
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        Toolbar toolbar = mActivityMainBinding.appBarMain.toolbar;
        toolbar.setTitle(R.string.app_name);
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

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mActivityMainBinding.appBarMain.fab.setVisibility(View.INVISIBLE);
                        mActivityMainBinding.appBarMain.fam.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                    case 2:
                        mActivityMainBinding.appBarMain.fam.collapse();
                        mActivityMainBinding.appBarMain.fab.setVisibility(View.VISIBLE);
                        mActivityMainBinding.appBarMain.fam.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = mActivityMainBinding.appBarMain.contentMain.tabLayout;
        tabLayout.setupWithViewPager(viewPager);

        mActivityMainBinding.appBarMain.addVoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences preferences = MainActivity.this.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE);
//                boolean isFirstTime = preferences.getBoolean(IS_FIRST_CHECK_PERMISSION, true);
//                if (isFirstTime) {
//                    getTheVoiceInput();
//                    preferences.edit().putBoolean(IS_FIRST_CHECK_PERMISSION, false).commit();
//                } else {
                mDialog.show();
//                }
            }
        });
        mActivityMainBinding.appBarMain.addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, NewTaskActivity.class), MAIN_TO_NEW_TASK_CODE);
            }
        });

        mActivityMainBinding.appBarMain.addAttach.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NormalFilePickActivity.class);
                intent.putExtra(Constant.MAX_NUMBER,1);
                intent.putExtra(NormalFilePickActivity.SUFFIX,new String[]{"xlsx","xls"});
                startActivityForResult(intent,Constant.REQUEST_CODE_PICK_FILE);
            }
        });


        mActivityMainBinding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
//                        getTheVoiceInput();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle(R.string.new_project).setView(editTextView).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String projectName = editText.getText().toString().trim();
                                for (Project project : DatebaseHelper.getInstance(MainActivity.this).findAll(Project.class)) {
                                    if (projectName.equals(project.getProjectTitle())) {
                                        showToast(getResources().getString(R.string.project_exist));
                                        return;
                                    }
                                }
                                if (!projectName.equals("")) {
                                    Project project = new Project();
                                    project.setProjectId(UUID.randomUUID().toString());
                                    project.setProjectTitle(projectName);
                                    Date date = new Date();
                                    project.setCreateTime(date.getTime());
                                    project.setEndTime(date.getTime());
                                    project.setLastUpdateTime(date.getTime());
                                    DatebaseHelper.getInstance(MainActivity.this).insert(project);
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
                        CircularAnimUtil.startActivity(MainActivity.this, intent, mActivityMainBinding.appBarMain.fab,
                                R.color.colorPrimary);
                        break;
                }
            }
        });
    }

    private void getTheVoiceInput() {
        if (ActivityCompat.checkSelfPermission(this, Constants.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Constants.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Constants.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Constants.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestContactsPermission();

        }
       /* mVoiceInput.startListening();
        Log.e(TAG, "task click " + mVoiceInput.isListening());*/
    }

    private void requestContactsPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Constants.RECORD_AUDIO) || !ActivityCompat.shouldShowRequestPermissionRationale(this, Constants.READ_PHONE_STATE) || !ActivityCompat.shouldShowRequestPermissionRationale(this, Constants.READ_EXTERNAL_STORAGE) || !ActivityCompat.shouldShowRequestPermissionRationale(this, Constants.WRITE_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(this, PERMISSION_LIST, REQUEST_PERMISSIONS);
        }
    }

    @SuppressWarnings("unchecked")
    void transitionTo(Intent i) {
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


    private void registerScreenOffBroadcast() {
        if (mScreenOffBroadcast == null) {
            mScreenOffBroadcast = new ScreenOffBroadcast(this);

        }
        mScreenOffBroadcast.registerBroadcast();
    }

    private void unRegisterScreenOffBroadcast() {
        new Intent(MainActivity.this, NewTaskActivity.class);
        if (mScreenOffBroadcast != null) {
            mScreenOffBroadcast.unregisterBroadcast();
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
        mMenuItemFalse = menu.findItem(R.id.menu_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_sure:
                //将选中事件置为完成状态

                mMenuItemFalse.setVisible(false);
                mMenuItemSure.setVisible(false);
                mTaskFragment.finishTaskAction();
                break;
            case R.id.menu_delete:
                //删除选中项结束事件
                mMenuItemFalse.setVisible(false);
                mMenuItemSure.setVisible(false);
                mTaskFragment.finishDeleteAction(true);
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
        switch (id){
            case R.id.nav_task:

                break;
            case R.id.nav_file:
                transitionTo(new Intent(this, FinishedTaskActivity.class));
                break;
            case R.id.nav_count:
                transitionTo(new Intent(this, ChartActivity.class));
                break;
            case R.id.nav_team:

                break;
            case R.id.nav_settings:

                break;
            case R.id.nav_about:
                transitionTo(new Intent(this, AboutActivity.class));
                break;
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (mMenuItemSure.isVisible()) {
                mTaskFragment.finishDeleteAction(false);
                mMenuItemSure.setVisible(false);
                mMenuItemFalse.setVisible(false);
                return true;
            }else if(mDialog.isShowing()){
                mDialog.onRecordFinish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onVoiceInputFinished(String result) {
        Intent intent = new Intent(this, NewTaskActivity.class);
        intent.putExtra(NewTaskActivity.VOICE_INPUT, result);
        startActivityForResult(intent, MAIN_TO_NEW_TASK_CODE);
    }


    //解析来自Excel的数据
    private void withResultInsertDatabase(String docPath){
        try {
            File file = new File(docPath);
            InputStream inputStream = new FileInputStream(file);
            ExcelManager excelManager = new ExcelManager();
            List<EventTaskExcelBean> tasks = excelManager.fromExcel(inputStream, EventTaskExcelBean.class);
            for (EventTaskExcelBean task : tasks){
                EventTask eventTask = new EventTask();
                eventTask.setTaskId(UUID.randomUUID().toString());
                eventTask.setTaskTitle(task.getTaskTitle());
                eventTask.setTaskContent(task.getTaskContent());
                if (!task.getPlanName().equals("")){
                    RealmResults<Plan> plans = DatebaseHelper.getInstance(getApplicationContext()).findAll(Plan.class);
                    for (Plan plan : plans){
                        if (task.getPlanName().equals(plan.getPlanName())){
                            eventTask.setPlan(plan);
                        }
                    }
                    //当数据库中没有该计划时。
                    // if (eventTask.getPlan()==null){
                    //     final Plan plan = new Plan();
                    //     plan.setPlanId(UUID.randomUUID().toString());
                    //     plan.setPlanName(task.getPlanName());
                    //     eventTask.setPlan(plan);
                    // }
                }
                eventTask.setTaskType(Integer.parseInt(task.getTaskType()));
                eventTask.setTaskPriority(Integer.parseInt(task.getTaskPriority()));
                eventTask.setTaskPredictTime(Float.parseFloat(task.getTaskPredictTime()));
                eventTask.setTaskRemindTime(Long.parseLong(task.getTaskRemindTime())*60);

                DatebaseHelper.getInstance(getApplicationContext()).insert(eventTask);
            }
            mTaskFragment.notifyData();
            showToast(getResources().getString(R.string.import_success));
        } catch (Exception e) {
            e.printStackTrace();
            showToast(getResources().getString(R.string.import_failed));
        }
    }
}
