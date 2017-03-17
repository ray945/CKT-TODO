package com.ckt.ckttodo.util;

import android.content.Context;
import android.content.Intent;

import com.ckt.ckttodo.service.PomodoCubeService;

/**
 * Created by ckt on 3/12/17.
 */

public class PomodoCubeNotificationUtil {

    private Context mContext;


    public PomodoCubeNotificationUtil(Context context) {
        this.mContext = context;
    }


    public void startPomodoCubeNotification(int seconds) {


        Intent intentService = new Intent(mContext, PomodoCubeService.class);
        intentService.putExtra(PomodoCubeService.PASS_SECONDS, seconds);
        mContext.startService(intentService);

    }


}
