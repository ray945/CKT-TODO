package com.ckt.ckttodo.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.IPomodoAidlInterface;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.service.PomodoCubeService;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_clockanimation);
        setupWindowAnimations();
        mTimer = (CircleAlarmTimerView) findViewById(R.id.ctv);
        mTimer.setCircleTimerListener(this);
        tvStart = (TextView) findViewById(R.id.start_tv);
        mTimer.setCurrentTime(0);
        mPomodo = new PomodoCubeNotificationUtil(this);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer.getCurrentTime() == 0) {
                    Toast.makeText(ClockAnimationActivity.this, getResources().getString(R.string.please_set_time), Toast.LENGTH_SHORT).show();
                } else {
                    mTimer.startTimer();
                    mPomodo.startPomodoCubeNotification(mTimer.getCurrentTime());
                    startCount = true;
                    tvStart.setText(getResources().getString(R.string.keep_focused));
                }
            }
        });

        mTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startCount) {
                    new AlertDialog.Builder(ClockAnimationActivity.this).setMessage(getResources().getString(R.string.Abandon_Pomo)).setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“确认”后的操作
                            Log.d("MOZRE", "onClick: Post");
                            EventBus.getDefault().post(false);
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

//          TODO
//        PomodoCubeService.MyBinder binder = (PomodoCubeService.MyBinder) getIntent().getSerializableExtra(PomodoCubeService.PASS_BINDER);
//        if (binder != null) {
//            Toast.makeText(this, "BIDDDD++++++++++++++++++++++++" + binder.getTime(), Toast.LENGTH_SHORT).show();
//
//        }


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
    public void onServiceFrom(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
