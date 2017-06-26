package com.ckt.ckttodo.ui;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.databinding.ActivityMainBinding;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpConstants;
import com.ckt.ckttodo.network.HTTPHelper;
import com.ckt.ckttodo.network.HTTPService;
import com.ckt.ckttodo.util.CircularAnimUtil;
import com.ckt.ckttodo.util.Constants;
import com.ckt.ckttodo.util.NotificationBroadcastReceiver;
import com.ckt.ckttodo.util.PermissionUtil;
import com.ckt.ckttodo.util.ScreenUtils;
import com.ckt.ckttodo.util.VoiceInputUtil;
import com.ckt.ckttodo.util.excelutil.EventTaskExcelBean;
import com.ckt.ckttodo.util.excelutil.ExcelManager;
import com.ckt.ckttodo.widgt.VoiceInputDialog;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;

import io.realm.RealmResults;
import okhttp3.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TaskFragment.ShowMainMenuItem, ActivityCompat.OnRequestPermissionsResultCallback,
        VoiceInputDialog.VoiceInputFinishedListener, ProjectFragment.NotifyTask, BaseView {
    private static final String TAG = "main";
    public static final String PLAN_ID = "planId";
    private static final String IS_FIRST_CHECK_PERMISSION = "permission_status";
    private static final int REQUEST_PERMISSIONS = 1;
    public static final int MAIN_TO_NEW_TASK_CODE = 100;
    public static final int MAIN_TO_TASK_DETAIL_CODE = 200;
    public static final int MAIN_TO_NEW_PROJECT_CODE = 300;
    private static final int ANIM_DURATION_TOOLBAR = 200;
    private static final int ANIM_DURATION_FAB = 300;
    private ActivityMainBinding mActivityMainBinding;
    private MenuItem mMenuItemSure;
    private MenuItem mMenuItemFalse;
    private TaskFragment mTaskFragment;
    private List<Fragment> mFragmentList;
    private ScreenOffBroadcast mScreenOffBroadcast;
    private static String[] PERMISSION_LIST = new String[]{Constants.RECORD_AUDIO, Constants.READ_PHONE_STATE, Constants.READ_EXTERNAL_STORAGE, Constants.WRITE_EXTERNAL_STORAGE};
    private VoiceInputDialog mDialog;
    private ConnectivityManager mConnectivityManager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private FloatingActionsMenu floatingActionsMenu;

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
        } else if (requestCode == Constant.REQUEST_CODE_PICK_FILE) {
            if (resultCode == RESULT_OK) {
                ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                if (list.size() == 0) {
                    return;
                }
                withResultInsertDatabase(list.get(0).getPath());
            }
        } else if (requestCode == MAIN_TO_NEW_PROJECT_CODE && resultCode == NewProjectActivity.NEW_PROJECT_SUCCESS_RESULT_CODE) {
            if (mFragmentList.get(1) != null) {
                ((ProjectFragment)mFragmentList.get(1)).notifyDataChange();
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
        mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        initUI();
        if (Build.VERSION.SDK_INT >= 21) {
            setupWindowAnimations();
        }
        initNotification(this);
        initPermission();
    }

    private void initPermission() {
        SharedPreferences preferences = MainActivity.this.getSharedPreferences(Constants.SHARE_NAME_CKT, Context.MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(IS_FIRST_CHECK_PERMISSION, true);
        if (isFirstTime) {
            getTheVoiceInput();
            preferences.edit().putBoolean(IS_FIRST_CHECK_PERMISSION, false).apply();
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
        mDialog = new VoiceInputDialog(this, this);
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mToolbar = mActivityMainBinding.appBarMain.toolbar;
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = mActivityMainBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.addDrawerListener(toggle);


        NavigationView navigationView = mActivityMainBinding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        disableNavigationViewScrollbars(navigationView);

        User user = new User(this);
        //侧滑栏 用户名相关展示
        TextView textViewUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_userName);
        textViewUsername.setText(user.getName());
        TextView textViewEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_userEmail);
        textViewEmail.setText(user.getEmail());
        //TODO UserICON


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


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        mTabLayout = mActivityMainBinding.appBarMain.contentMain.tabLayout;
        mTabLayout.setupWithViewPager(viewPager);

        floatingActionsMenu = mActivityMainBinding.appBarMain.fam;

        mActivityMainBinding.appBarMain.addVoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityMainBinding.appBarMain.fam.collapse();
                if (VoiceInputUtil.isNetAvaliable(mConnectivityManager)) {

                    mDialog.show();
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.net_not_connected), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mActivityMainBinding.appBarMain.addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityMainBinding.appBarMain.fam.collapse();
                startActivityForResult(new Intent(MainActivity.this, NewTaskActivity.class), MAIN_TO_NEW_TASK_CODE);
            }
        });

        mActivityMainBinding.appBarMain.addAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityMainBinding.appBarMain.fam.collapse();
                Intent intent = new Intent(MainActivity.this, NormalFilePickActivity.class);
                intent.putExtra(Constant.MAX_NUMBER, 1);
                intent.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"xlsx", "xls"});
                startActivityForResult(intent, Constant.REQUEST_CODE_PICK_FILE);
            }
        });


        mActivityMainBinding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        break;
                    case 1:
                        startActivityForResult(new Intent(MainActivity.this, NewProjectActivity.class), MAIN_TO_NEW_PROJECT_CODE);
                        break;
                    case 2:
                        Log.e(TAG, "note click");
                        Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                        intent.putExtra("noteTag", "2");
                        CircularAnimUtil.startActivity(MainActivity.this, intent, mActivityMainBinding.appBarMain.fab, R.color.colorPrimary);
                        break;
                }
            }
        });
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    private void setAnimate() {
        int actionbarSize = ScreenUtils.dp2px(this,56);
        mToolbar.setTranslationY(-actionbarSize);
        mToolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);

        mTabLayout.setTranslationY(-actionbarSize);
        mTabLayout.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_FAB)
                .setStartDelay(400);

        floatingActionsMenu.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        floatingActionsMenu.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_FAB)
                .setStartDelay(400);

    }

    private void getTheVoiceInput() {
        if (ActivityCompat.checkSelfPermission(this, Constants.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Constants.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Constants.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Constants.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestContactsPermission();

        }
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
        setAnimate();
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
        switch (id) {
            case R.id.nav_task:

                break;
            case R.id.nav_file:
                transitionTo(new Intent(this, FinishedTaskActivity.class));
                break;
            case R.id.nav_timer:
                transitionTo(new Intent(this, ClockAnimationActivity.class));
                break;
            case R.id.nav_count:
                transitionTo(new Intent(this, ChartActivity.class));
                break;
            case R.id.nav_team:
                transitionTo(new Intent(this, LoginActivity.class));
                break;
            case R.id.nav_project:
                transitionTo(new Intent(this, ProjectActivity.class));
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                transitionTo(new Intent(this, AboutActivity.class));
                break;
            case R.id.nav_logout:
                doLogout();
                break;
        }
        DrawerLayout drawer = mActivityMainBinding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doLogout() {
        User user = new User(this);
        user.setIsLogin(false);
        startActivity(new Intent(this, LoginActivity.class));
        Map<String, String> map = new HashMap<>();
        map.put(BeanConstant.EMAIL, user.getEmail());
        map.put(BeanConstant.TOKEN, user.getToken());
        Request request = HTTPHelper.getGetRequest(map, HttpConstants.PATH_LOGINOUT);
        HTTPService.getHTTPService().doHTTPRequest(request, this);
        finish();
    }

    @Override
    public void replyRequestResult(String strJson) {
        //登出没有回包
    }

    @Override
    public void replyNetworkErr() {
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMenuItemSure.isVisible()) {
                mTaskFragment.finishDeleteAction(false);
                mMenuItemSure.setVisible(false);
                mMenuItemFalse.setVisible(false);
                return true;
            } else if (mDialog.isShowing()) {
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
    private void withResultInsertDatabase(String docPath) {
        try {
            File file = new File(docPath);
            InputStream inputStream = new FileInputStream(file);
            ExcelManager excelManager = new ExcelManager();
            List<EventTaskExcelBean> tasks = excelManager.fromExcel(inputStream, EventTaskExcelBean.class);
            for (EventTaskExcelBean task : tasks) {
                EventTask eventTask = new EventTask();
                eventTask.setTaskId(UUID.randomUUID().toString());
                eventTask.setTaskTitle(task.getTaskTitle());
                eventTask.setTaskContent(task.getTaskContent());
                if (!task.getPlanName().equals("")) {
                    RealmResults<Plan> plans = DatabaseHelper.getInstance(getApplicationContext()).findAll(Plan.class);
                    for (Plan plan : plans) {
                        if (task.getPlanName().equals(plan.getPlanName())) {
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
                eventTask.setTaskRemindTime(Long.parseLong(task.getTaskRemindTime()) * 60);

                DatabaseHelper.getInstance(getApplicationContext()).insert(eventTask);
            }
            mTaskFragment.notifyData();
            showToast(getResources().getString(R.string.import_success));
        } catch (Exception e) {
            e.printStackTrace();
            showToast(getResources().getString(R.string.import_failed));
        }
    }

    @Override
    public void notifyTask() {
        mTaskFragment.notifyData();
    }
}
