package com.ckt.ckttodo.widgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.util.Constants;

import java.util.List;

/**
 * Created by ckt on 2/7/17.
 */

public class TimeWatchDialog extends Dialog {

    private Button mButtomStopCount;
    private TimeWatchView mTimeWatchView;
    private CancelClickedListener mListener;

    public TimeWatchDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TimeWatchDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public TimeWatchDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_timewatch);
        init();

    }

    private void init() {
        mTimeWatchView = ((TimeWatchView) findViewById(R.id.dialog_time_watch));
        mTimeWatchView.initTime(Constants.HALF_HOUR_TO_SEC);
        mButtomStopCount = (Button) findViewById(R.id.dialog_cancel);
        mButtomStopCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClickedListener();
            }
        });
        setCanceledOnTouchOutside(false);
    }

    public void start() {
        mTimeWatchView.start();
    }

    public long stop() {
        mTimeWatchView.stop();
        dismiss();
        return mTimeWatchView.getSpendTime();
    }

    @Override
    public void show() {
        super.show();
    }

    public void setOnCancelClickedListener(CancelClickedListener cancelClickedListener) {
        this.mListener = cancelClickedListener;
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }

    public interface CancelClickedListener {

        void onCancelClickedListener();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mListener.onCancelClickedListener();
        }

        return true;
    }
}
