package com.ckt.ckttodo.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.ui.MainActivity;
import io.realm.RealmResults;
import java.util.Date;

/**
 * Created by zhiwei.li
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_TITLE = "title";


    @Override public void onReceive(Context context, Intent intent) {

        RealmResults<EventTask> tasks = DatebaseHelper.getInstance(context)
            .findAll(EventTask.class);
        for (int i = 0; i < tasks.size(); i++) {
            EventTask task = tasks.get(i);
            long time = task.getTaskStartTime() - (new Date()).getTime();
            if (time < (task.getTaskRemindTime() + 59) * 1000 &&
                time > (task.getTaskRemindTime() - 30) * 1000) {
                NotificationCompat.Builder builder = new NotificationCompat
                    .Builder(context)
                    .setContentTitle(intent.getStringExtra(NOTIFICATION_TITLE))
                    .setContentText(task.getTaskTitle())
                    .setSmallIcon(R.mipmap.ic_launcher);
                builder.build().flags = Notification.FLAG_AUTO_CANCEL;

                Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);

                NotificationManager manager = (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
                manager.notify(i, builder.build());
            }
        }
    }
}
