package com.ckt.ckttodo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ckt.ckttodo.ui.ClockAnimationActivity;
import com.ckt.ckttodo.util.Constants;
import com.ckt.ckttodo.util.TranserverUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ckt on 3/12/17.
 */

public class PomodoCubeService extends Service {

    private static final String POMODO_NOTIFICATION_TIP = "番茄时钟计时中";
    private static final String POMODO_TITLE = "番茄时钟计时中";
    private static final String POMODO_BELONG = "将时间指定到任务";
    private static final String POMODO_FINISH = "完成一个番茄时钟";

    private static final int NOTIFICATION_ID = 1220;
    private static final int POMODO_CUBE_TO_ACTIVITY_REQUEST_CODE = 30;
    public static final String PASS_SECONDS = "Pass_seconds";
    public static final String PASS_RADIAN = "Pass_radian";
    public static final String PASS_BINDER = "pass_binder";
    public static final String TOTAL_TIME = "TOTAL_TIME";
    private NotificationManager mNotificationManager;
    private Timer mTimer;
    private NotificationCompat.Builder builder;
    private TimerTask mTimerTask;
    private Notification mNotification;
    private SharedPreferences mSharedPreferences;
    private int seconds;
    private float radians;
    private MyBinder mMyBinder;



    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        initTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            seconds = intent.getIntExtra(PASS_SECONDS, 0);
            radians = intent.getFloatExtra(PASS_RADIAN, 0);
        } else {
            seconds = mSharedPreferences.getInt(PASS_SECONDS, 0);
            radians = mSharedPreferences.getFloat(PASS_RADIAN, 0);
        }
        startPomodoCubeNotification(seconds,radians);
//        EventBus.getDefault().post("AAAAAAAAAAAAAAAAAAAAAAAAA");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mMyBinder;
    }

    private void initTask() {
        mMyBinder = new MyBinder();
        mTimer = new Timer();
        mSharedPreferences = getBaseContext().getSharedPreferences(Constants.SHARE_NAME_CKT, MODE_PRIVATE);
        mNotificationManager = (NotificationManager) getBaseContext().getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getBaseContext(), ClockAnimationActivity.class);
        intent.putExtra(PASS_BINDER,mMyBinder);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), POMODO_CUBE_TO_ACTIVITY_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(getBaseContext());
        builder.setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setTicker(POMODO_NOTIFICATION_TIP)
                .setContentTitle(POMODO_TITLE)
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(true);
        mNotification = builder.build();
//        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
    }

    public void startPomodoCubeNotification(final Integer second,final Float radian) {

        if (second <= 0) {
            return;
        }

        mTimerTask = new TimerTask() {

            private int countSeconds = second;
            private float countRadian = radian;

            @Override
            public void run() {

                builder.setContentText(TranserverUtil.formatTime(countSeconds));
                mNotification = builder.build();
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                if (countSeconds == 0) {
                    builder.setContentText(POMODO_BELONG)
                            .setContentTitle(POMODO_FINISH);
                    mNotification = builder.build();
                    mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                    cancel();
                }
                --countSeconds;
                countRadian -= (2 * Math.PI) / mSharedPreferences.getInt(TOTAL_TIME,0);
                seconds = countSeconds;
                radians = countRadian;
                mSharedPreferences.edit().putInt(PASS_SECONDS, countSeconds).commit();
                mSharedPreferences.edit().putFloat(PASS_RADIAN, countRadian).commit();

            }

        };
        mTimer.schedule(mTimerTask, 0, 1000);

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void setStop(boolean flag) {
        Log.d("MOZRE", "setStop: ");
        if (!flag)
            mTimer.cancel();
    }


    public class MyBinder extends Binder implements Serializable{


        public int getTime(){
            return seconds;
        }

        public float getRadian(){
            return radians;
        }

        public MyBinder() {
            super();
        }
    }

}
