package com.ckt.ckttodo.widgt;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;

/**
 * Created by ckt on 2/7/17.
 */

public class TimeWatchView extends Chronometer {


    private long mTime;
    private long mNextTime;
    private OnTimeCompleteListener mListener;
    //    private SimpleDateFormat mTimeFormat;
    private static final long maxSends = 24 * 40 * 60;

    public TimeWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO 自动生成的构造函数存根
//        mTimeFormat = new SimpleDateFormat("HH:mm:ss");
        this.setOnChronometerTickListener(listener);
    }

    public TimeWatchView(Context context) {
        super(context);

    }

    /**
     * 重新启动计时
     */
    public void reStart(long _time_s) {
        if (_time_s == -1) {
            mNextTime = mTime;
        } else {
            mTime = mNextTime = _time_s;
        }
        this.start();
    }

    public void reStart() {
        reStart(-1);
    }

    /**
     * 继续计时
     */
    public void onResume() {
        this.start();
    }

    /**
     * 暂停计时
     */
    public void onPause() {
        this.stop();
    }

    /**
     * 设置时间格式
     *
     * @param pattern 计时格式
     */
//    public void setTimeFormat(String pattern) {
//        mTimeFormat = new SimpleDateFormat(pattern);
//    }

    public void setOnTimeCompleteListener(OnTimeCompleteListener l) {
        mListener = l;
    }

    OnChronometerTickListener listener = new OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            if (mNextTime <= 0) {
                if (mNextTime == 0) {
                    TimeWatchView.this.stop();
                    if (null != mListener)
                        mListener.onTimeComplete();
                }
                mNextTime = 0;
                updateTimeText();
                return;
            }

            mNextTime--;

            updateTimeText();
        }
    };

    /**
     * 初始化时间
     *
     * @param _time_s
     */
    public void initTime(long _time_s) {
        if (_time_s > maxSends) {
            return;
        }
        mTime = mNextTime = _time_s;
        updateTimeText();
    }

    public long getSpendTime() {
        return mTime - mNextTime;
    }

    private void updateTimeText() {
//        String time = mTimeFormat.format(new Date());
        String time = formatTime(mNextTime);
        Log.d("TTT", "updateTimeText: " + time + " mNextTime = " + mNextTime);

        this.setText(time);
    }

    private String formatTime(long thisTime) {
        long min, hour, sec;
        StringBuilder build = new StringBuilder();
        hour = thisTime / (60 * 60);
        min = (thisTime - hour * 60 * 60) / 60;
        sec = (thisTime - hour * 60 * 60 - min * 60);
        build = formatText(build, hour);
        build = formatText(build, min);
        build = formatText(build, sec);
        build.deleteCharAt(build.length() - 1);
        return build.toString();
    }

    private StringBuilder formatText(StringBuilder build, long t) {
        if (t < 10) {
            build.append("0").append(t).append(":");
        } else {
            build.append(t).append(":");
        }

        return build;
    }

    interface OnTimeCompleteListener {
        void onTimeComplete();
    }
}
