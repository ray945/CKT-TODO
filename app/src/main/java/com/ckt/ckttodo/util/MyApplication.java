package com.ckt.ckttodo.util;

import android.app.Application;
import android.content.SharedPreferences;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by zhiwei.li
 */

public class MyApplication extends Application {

    public static final String INIT_DATA = "initData";
    public static final String IS_FIRST = "isFirst";

    @Override
    public void onCreate() {
        super.onCreate();
        if (getSharedPreferences(MyApplication.INIT_DATA, MODE_PRIVATE).getBoolean(MyApplication.IS_FIRST, true)) {
            InitDataBase.initData(getApplicationContext());
            SharedPreferences.Editor editor = getSharedPreferences(MyApplication.INIT_DATA, MODE_PRIVATE).edit();
            editor.putBoolean(MyApplication.IS_FIRST, false);
            editor.apply();

        }
    }

}
