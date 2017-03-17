package com.ckt.ckttodo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.repacked.google.common.eventbus.EventBus;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.ui.ClockAnimationActivity;
import com.ckt.ckttodo.util.Constants;
import com.ckt.ckttodo.util.PomodoCubeNotificationUtil;
import com.ckt.ckttodo.util.TranserverUtil;

import org.greenrobot.eventbus.Subscribe;

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
    private NotificationManager mNotificationManager;
    private Timer mTimer;
    private NotificationCompat.Builder builder;
    private TimerTask mTimerTask;
    private Notification mNotification;
    private SharedPreferences mSharedPreferences;
    private int seconds;
    private boolean mStop = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MOZRE", "onCreate: ");
        initTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            Log.d("MOZRE", "onStartCommand: " + intent);
            seconds = intent.getIntExtra(PASS_SECONDS, 0);
        } else {
            Log.d("MOZRE", "onStartCommand: Share");
            seconds = mSharedPreferences.getInt(PASS_SECONDS, 0);
        }
        Log.d("MOZRE", "onStartCommand: " + seconds);
        startPomodoCubeNotification(seconds);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("MOZRE", "onDestroy: " + seconds);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initTask() {
        mTimer = new Timer();
        mSharedPreferences = getBaseContext().getSharedPreferences(Constants.SHARE_NAME_CKT,MODE_PRIVATE);
        mNotificationManager = (NotificationManager) getBaseContext().getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getBaseContext(), ClockAnimationActivity.class);
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

    public void startPomodoCubeNotification(final Integer second) {

        if (second <= 0) {
            return;
        }

        mTimerTask = new TimerTask() {

            private int countSeconds = second;

            @Override
            public void run() {

//                Log.d("MOZRE", "run: ");
                if (!mStop) {
                    Log.d("MOZRE", "run: start");
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
                    mSharedPreferences.edit().putInt(PASS_SECONDS, countSeconds).commit();

                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        mTimer.schedule(mTimerTask, 0, 1000);

    }

//    @Subscribe
//    private void setStop(boolean stop) {
//        mStop = stop;
//    }

}
