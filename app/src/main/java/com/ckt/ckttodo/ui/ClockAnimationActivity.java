package com.ckt.ckttodo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.widgt.CircleAlarmTimerView;

/**
 * Created by ckt on 2/20/17.
 */

public class ClockAnimationActivity extends Activity  implements CircleAlarmTimerView.CircleTimerListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private CircleAlarmTimerView mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockanimation);
        setupWindowAnimations();
        mTimer = (CircleAlarmTimerView) findViewById(R.id.ctv);
        mTimer.setCurrentTime(1500);
        mTimer.setCircleTimerListener(this);
        mTimer.startTimer();
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

    public void start(View v)
    {
        mTimer.startTimer();
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
        Log.d(TAG, "onTimerTimingValueChanged");
    }

    @Override
    public void onTimerSetValueChanged(int time) {
        Log.d(TAG, "onTimerSetValueChanged");
    }

    @Override
    public void onTimerSetValueChange(int time) {
        Log.d(TAG, "onTimerSetValueChange");
    }

}
