package com.ckt.ckttodo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mozre on 2/7/17.
 */

public class Constants {

    public static final long ONE_HOUR_TO_SEC = 1 * 60 * 60;

    public static final long HALF_HOUR_TO_SEC = ONE_HOUR_TO_SEC / 2;

    public static final long TEN_MIN_TO_SEC = 10 * 60;

    public static final long TWENTY_MIN_TO_SEC = 20 * 60;

    //最大可转换时间为一年
    public static final long MAX_SEC = ONE_HOUR_TO_SEC * 24 * 365;

    public static final long MAX_HOUR = MAX_SEC / ONE_HOUR_TO_SEC;

    public static final String XUNFEI_APPID = "58a6b790";
    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";



}
