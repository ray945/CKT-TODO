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
    public static String hourTime(Long time) {
        return new SimpleDateFormat("HH:mm").format(new Date(time)).trim();
    }

}
