package com.ckt.ckttodo.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.service.PomodoCubeService;
import com.ckt.ckttodo.ui.ClockAnimationActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ckt on 3/12/17.
 */

public class PomodoCubeNotificationUtil {

    private static final int NOTIFICATION_ID = 1220;
    private static final int POMODO_CUBE_TO_ACTIVITY_REQUEST_CODE = 30;
    private NotificationManager mNotificationManager;
    private Timer mTimer;
    private Context mContext;
    private NotificationCompat.Builder builder;
    private TimerTask mTimerTask;
    private Notification mNotification;
    private int seconds;


    public PomodoCubeNotificationUtil(Context context) {
        this.mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        initTask();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mTimer.schedule(mTimerTask, 0, 1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void initTask() {
        mTimer = new Timer();
        Intent intent = new Intent(mContext, ClockAnimationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, POMODO_CUBE_TO_ACTIVITY_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(mContext);
        builder.setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setTicker(mContext.getResources().getText(R.string.pomodo_notification_tip))
                .setContentTitle(mContext.getResources().getString(R.string.pomodo_title))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(true);
        mNotification = builder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        Intent intentService = new Intent(mContext, PomodoCubeService.class);
        mContext.bindService(intentService, connection, Context.BIND_AUTO_CREATE);
        mContext.startService(intentService);

    }


    public void startPomodoCubeNotification(int seconds) {

        if (seconds <= 0) {
            return;
        }
        this.seconds = seconds;

        mTimerTask = new TimerTask() {

            private int countSeconds = PomodoCubeNotificationUtil.this.seconds;

            @Override
            public void run() {

                builder.setContentText(formatTime(countSeconds));
                mNotification = builder.build();
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                if (countSeconds == 0) {
                    builder.setContentText(mContext.getResources().getString(R.string.pomodo_belong))
                            .setContentTitle(mContext.getResources().getString(R.string.pomodo_finish));
                    mNotification = builder.build();
                    mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                    cancel();
                }
                --countSeconds;
            }
        };

    }

    private String formatTime(int thisTime) {
        int min, hour, sec;
        StringBuilder build = new StringBuilder();
        hour = thisTime / (60 * 60);
        min = (thisTime - hour * 60 * 60) / 60;
        sec = (thisTime - hour * 60 * 60 - min * 60);
        if (hour > 0) {
            build = formatText(build, hour);
        }
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

}
