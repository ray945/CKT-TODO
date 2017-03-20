package com.ckt.ckttodo.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.ActTime;
import com.ckt.ckttodo.service.PomodoCubeService;
import com.ckt.ckttodo.util.Constants;
import com.ckt.ckttodo.util.PomodoCubeNotificationUtil;
import com.ckt.ckttodo.widgt.CircleAlarmTimerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ckt on 2/20/17.
 */

public class ClockAnimationActivity extends Activity implements CircleAlarmTimerView.CircleTimerListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CircleAlarmTimerView mTimer;
    private TextView tvStart;
    private boolean startCount;
    private PomodoCubeNotificationUtil mPomodo;
    private SharedPreferences mSharedPreferences;
    public static final String TOTAL_TIME = "TOTAL_TIME";
    private static final String GET_POMODO_RUN_TIME_ACTION = "com.ckt.ckttodo.get_pomodo_run_time_action";
    public static final String SERVICE_IS_RUNNING = "service_is_running";
    private boolean isServiceRunning;
    private int serviceTimes;
    private PomodoCubeService.PomodoBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_clockanimation);
        if (Build.VERSION.SDK_INT >= 21){
            setupWindowAnimations();
        }
        initUi();
        mPomodo = new PomodoCubeNotificationUtil(this);
        binder = (PomodoCubeService.PomodoBinder) getIntent().getSerializableExtra(PomodoCubeService.PASS_BINDER);
        ClockAnimationActivity.this.sendBroadcast(new Intent().setAction(GET_POMODO_RUN_TIME_ACTION));
        mSharedPreferences = getBaseContext().getSharedPreferences(Constants.SHARE_NAME_CKT, MODE_PRIVATE);
        isServiceRunning = mSharedPreferences.getBoolean(SERVICE_IS_RUNNING, false);
        mTimer.setCircleTimerListener(this);
        mTimer.setCurrentTime(0);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer.getCurrentTime() == 0) {
                    Toast.makeText(ClockAnimationActivity.this, getResources().getString(R.string.please_set_time), Toast.LENGTH_SHORT).show();
                } else {
                    mTimer.startTimer();
                    mSharedPreferences.edit().putInt(TOTAL_TIME, mTimer.getCurrentTime()).commit();
                    mPomodo.startPomodoCubeNotification(mTimer.getCurrentTime(),mTimer.getmCurrentRadian());
                    startCount = true;
                    tvStart.setText(getResources().getString(R.string.keep_focused));
                }
            }
        });

        //时钟点击事件
        mTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startCount) {
                    new AlertDialog.Builder(ClockAnimationActivity.this).setMessage(getResources().getString(R.string.Abandon_Pomo)).setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“确认”后的操作,放弃番茄，关闭service
                            EventBus.getDefault().post(false);
                            stopService(new Intent(ClockAnimationActivity.this,PomodoCubeService.class));
                            mTimer.setState(false);
                            tvStart.setText(getResources().getString(R.string.start));
                            finish();
                        }
                    }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                        }
                    }).show();
                }
            }
        });

    }

    private void initUi(){
        tvStart = (TextView) findViewById(R.id.start_tv);
        mTimer = (CircleAlarmTimerView) findViewById(R.id.ctv);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onTimerStop() {

    }

    @Override
    public void onTimerStart(int time) {
    }

    @Override
    public void onTimerPause(int time) {
        Toast.makeText(this, "onTimerPause", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerTimingValueChanged(int time) {
    }

    @Override
    public void onTimerSetValueChanged(int time) {
        Log.d(TAG, "onTimerSetValueChanged");
    }

    @Override
    public void onTimerOver() {
        if (!ClockAnimationActivity.this.isFinishing()) {
            new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.Pomo_Finish)).setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 点击“确认”后的操作
                    mTimer.setCurrentTime(0);
                    tvStart.setText(getResources().getString(R.string.start));
                    finish();
                }
            }).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTime(ActTime actTime) {
        this.serviceTimes = actTime.getSeconds();
        if (isServiceRunning){
            //点击通知栏进来，且时间小于0
            if (serviceTimes <= 0) {
                mTimer.setCurrentTime(0);
                mTimer.setmCurrentRadian((float) (2 * Math.PI));
                tvStart.setText(getResources().getString(R.string.start));
            }else if (binder != null) {
                //点击通知栏进来，且时间大于0
                Log.d(TAG, "getTime: time is:" + binder.getTime());
                mTimer.setCurrentTime(binder.getTime());
                mTimer.setmCurrentRadian(binder.getRadian());
                mTimer.setRecordTime(mSharedPreferences.getInt(TOTAL_TIME,0));
                mTimer.startTimer();
                tvStart.setText(getResources().getString(R.string.keep_focused));
            } else if (serviceTimes > 0) {
                //点击侧边栏番茄时钟直接进来
                mTimer.setCurrentTime(serviceTimes);
                mTimer.setmCurrentRadian(actTime.getRadians());
                mTimer.setRecordTime(mSharedPreferences.getInt(TOTAL_TIME,0));
                mTimer.startTimer();
                tvStart.setText(getResources().getString(R.string.keep_focused));
            }

        }
    }
}
