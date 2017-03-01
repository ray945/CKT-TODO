package com.ckt.ckttodo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ckt.ckttodo.ui.MainActivity;

/**
 *
 * Created by zhiwei.li on 2017/3/1.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            MainActivity.initNotification(context);
        }
    }
}
