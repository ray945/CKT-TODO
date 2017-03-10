package com.ckt.ckttodo.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.widgt.CircleAlarmTimerView;

/**
 * Created by ckt on 2/20/17.
 */

public class ClockAnimationActivity extends Activity  implements CircleAlarmTimerView.CircleTimerListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private CircleAlarmTimerView mTimer;
    private TextView tvStart;
    private boolean startCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockanimation);
        setupWindowAnimations();
        mTimer = (CircleAlarmTimerView) findViewById(R.id.ctv);
        mTimer.setCircleTimerListener(this);
        tvStart = (TextView) findViewById(R.id.start_tv);
        mTimer.setCurrentTime(0);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer.getCurrentTime() == 0){
                    Toast.makeText(ClockAnimationActivity.this,"please set time",Toast.LENGTH_SHORT).show();
                }else {
                    mTimer.startTimer();
                    startCount = true;
                    tvStart.setText("lay down your phone");
                }
            }
        });

        mTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startCount){
                    new AlertDialog.Builder(ClockAnimationActivity.this).setMessage("是否选择后台计时？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“确认”后的操作
                            finish();
                            mTimer.setState(false);
                            tvStart.setText("start");
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                        }
                    }).show();
                }
            }
        });
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
        new AlertDialog.Builder(this).setMessage("任务完成").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击“确认”后的操作
                mTimer.setCurrentTime(0);
                tvStart.setText("start");
                finish();
            }
        }).show();
        tvStart.setText("start");
    }
}
