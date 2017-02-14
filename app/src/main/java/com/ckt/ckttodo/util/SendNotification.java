package com.ckt.ckttodo.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.ckt.ckttodo.ui.MainActivity;

/**
 * @author
 * @version 1.0
 * @date 2017/2/13
 */
public class SendNotification {
    private static final int NOTIFICATION_ID_1 = 11111111;

    public static void send(Context context, String title, String content, String subText, String ticker, int smallIcon, int largeIcon, int defaultSound, int defaultVibrate, boolean autoCancel, int visibility) {
        Bitmap largeBitmap = BitmapFactory.decodeResource(context.getResources(), largeIcon);
        NotificationManager myManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        myManager.cancel(NOTIFICATION_ID_1);
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder myBuilder = new Notification.Builder(context);
        myBuilder.setSmallIcon(smallIcon)//设置小图标
                .setContentTitle(title)//设置内容标题
                .setContentText(content)//设置内容
                .setSubText(subText)//设置内容子内容
                .setTicker(ticker).setLargeIcon(largeBitmap).setDefaults(defaultSound | defaultVibrate).setAutoCancel(autoCancel)//点击后取消  
                .setWhen(System.currentTimeMillis())//设置通知时间  
                .setPriority(Notification.PRIORITY_HIGH)//高优先级  
                .setContentIntent(pi);  //3.关联PendingIntent  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myBuilder.setVisibility(visibility);
        }
        myManager.notify(NOTIFICATION_ID_1, myBuilder.build());
    }

}
