package com.ckt.ckttodo.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.Visibility;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.util.ScreenUtils;
import com.ckt.ckttodo.util.VoiceInputUtil;
import com.ckt.ckttodo.widgt.VoiceView;



/**
 * Created by ckt on 2/23/17.
 */

public class VoiceActivity extends AppCompatActivity implements VoiceView.OnRecordListener, VoiceInputUtil.VoiceChangeListener{
    private VoiceView mVoiceView;
    private boolean mIsRecording = false;
    private VoiceInputUtil mVoiceInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        mVoiceView = (VoiceView) findViewById(R.id.voiceview);
        mVoiceView.setOnRecordListener(this);
        mVoiceInput = new VoiceInputUtil(this);
        mVoiceInput.setOnVoiceChangeListener(this);
        mVoiceView.startRecording();
        if (Build.VERSION.SDK_INT >= 21){
            setupWindowAnimations();
        }
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
    public void onRecordStart() {
        mIsRecording = true;
        mVoiceInput.startListening();
    }

    @Override
    public void onRecordFinish() {
        mIsRecording = false;
        mVoiceInput.stopListening();
    }

    @Override
    protected void onDestroy() {
        if(mIsRecording){
            mVoiceInput.stopListening();
            mIsRecording = false;
        }
        super.onDestroy();
    }

    @Override
    public void onVoiceChanged(int volume) {
        int act = ScreenUtils.dp2px(this, 20) / 5;
        int min = ScreenUtils.dp2px(this, 68) / 2;
        mVoiceView.animateRadius(Math.max(min, min + volume * act));
    }

    @Override
    public void onBackResult(String result) {

    }

    @Override
    public void onNoInput() {

    }
}
