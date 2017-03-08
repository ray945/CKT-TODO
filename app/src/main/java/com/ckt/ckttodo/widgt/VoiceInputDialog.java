package com.ckt.ckttodo.widgt;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.util.ScreenUtils;
import com.ckt.ckttodo.util.VoiceInputUtil;

import java.util.List;

/**
 * Created by ckt on 2/26/17.
 */

public class VoiceInputDialog extends Dialog implements VoiceView.OnRecordListener,
        VoiceInputUtil.VoiceChangeListener{

    private boolean mIsRecording = false;
    private VoiceInputUtil mVoiceInput;
    private VoiceView mVoiceView;
    private Context mContext;
    private VoiceInputFinishedListener mListener;

    public VoiceInputDialog(Context context,VoiceInputFinishedListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
    }

    protected VoiceInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener,VoiceInputFinishedListener listener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        this.mListener = listener;
    }

    public VoiceInputDialog(Context context, int themeResId,VoiceInputFinishedListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        mVoiceView = (VoiceView) findViewById(R.id.voiceview);
        mVoiceView.setOnRecordListener(this);
        mVoiceInput = new VoiceInputUtil(mContext);
        mVoiceInput.setOnVoiceChangeListener(this);
        mVoiceView.startRecording();
    }

    @Override
    public void onNoInput() {
        dismiss();
    }

    @Override
    public void show() {
        super.show();
        mVoiceInput.startListening();
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }
    @Override
    public void onRecordStart() {
    }

    @Override
    public void onRecordFinish() {
        if(mVoiceInput.isListening()){
            mVoiceInput.stopListening();
            dismiss();
        }
    }




    @Override
    public void onVoiceChanged(int volume) {
        int act = ScreenUtils.dp2px(getContext(), 20) / 5;
        int min = ScreenUtils.dp2px(getContext(), 68) / 2;
        mVoiceView.animateRadius(Math.max(min, min + volume * act));
    }

    @Override
    public void onBackResult(String result) {
        this.mListener.onVoiceInputFinished(result);
        dismiss();
    }
    public interface VoiceInputFinishedListener {
        void onVoiceInputFinished(String result);
    }
}
