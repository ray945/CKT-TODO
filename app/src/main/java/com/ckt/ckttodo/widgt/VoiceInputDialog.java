package com.ckt.ckttodo.widgt;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.util.ScreenUtils;
import com.ckt.ckttodo.util.VoiceInputUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ckt on 2/26/17.
 */

public class VoiceInputDialog extends Dialog implements VoiceView.OnRecordListener,
        VoiceInputUtil.VoiceChangeListener {

    private VoiceInputUtil mVoiceInput;
    private VoiceView mVoiceView;
    private Context mContext;
    private VoiceInputFinishedListener mListener;
    private LinearLayout mLinearLayout;
    private StringBuilder mBuild = new StringBuilder();
    private String voiceContent = "";
    private TextView tvInput,tvNo,tvYes,tvNote;
    boolean finishInput;
    public VoiceInputDialog(Context context, VoiceInputFinishedListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
    }

    protected VoiceInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener, VoiceInputFinishedListener listener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        this.mListener = listener;
    }

    public VoiceInputDialog(Context context, int themeResId, VoiceInputFinishedListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_voice);
        mVoiceView = (VoiceView) findViewById(R.id.voiceview);
        tvInput = (TextView) findViewById(R.id.voice_input);
        tvNote = (TextView) findViewById(R.id.voice_note);
        tvNo = (TextView) findViewById(R.id.voice_no);
        tvYes = (TextView) findViewById(R.id.voice_yes);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetState();
                finishInput = true;
                dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!voiceContent.isEmpty()){
                    mListener.onVoiceInputFinished(voiceContent);
                    finishInput = true;
                    resetState();
                    dismiss();
                }
            }
        });
        mLinearLayout = (LinearLayout) findViewById(R.id.voice_ll);
        mLinearLayout.setVisibility(View.INVISIBLE);
        tvInput.setVisibility(View.INVISIBLE);
        mVoiceView.setOnRecordListener(this);
        mVoiceInput = new VoiceInputUtil(mContext);
        mVoiceInput.setOnVoiceChangeListener(this);
        mVoiceView.startRecording();
        mVoiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceInput.startListening();
            }
        });
    }

    private void resetState(){
        tvInput.setText("");
        tvInput.setVisibility(View.INVISIBLE);
        tvNote.setText("请说出要新建的任务");
        voiceContent = "";
        mLinearLayout.setVisibility(View.INVISIBLE);
        mBuild.setLength(0);
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
        /*if(mVoiceInput.isListening()){
            mVoiceInput.stopListening();
            dismiss();
        }*/
    }


    @Override
    public void onVoiceChanged(int volume) {
        int act = ScreenUtils.dp2px(getContext(), 20) / 5;
        int min = ScreenUtils.dp2px(getContext(), 68) / 2;
        mVoiceView.animateRadius(Math.max(min, min + volume * act));
    }

    @Override
    public void onBackResult(String result) {
        voiceContent = mBuild.append(result).toString();
        if (!voiceContent.isEmpty()) {
            tvInput.setText(voiceContent);
            tvInput.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
            tvNote.setText("点击继续语音输入");
        }
    }

    public interface VoiceInputFinishedListener {
        void onVoiceInputFinished(String result);
    }
}
