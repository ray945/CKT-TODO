package com.ckt.ckttodo.util;

import com.ckt.ckttodo.database.EventTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ckt on 2/23/17.
 */

public class TranserverUtil {

    public static final String DATE_FORMAT = "yyyy年MM月dd日 HH时mm分";

    public static String hourTime(Long time) {
        return new SimpleDateFormat("HH:mm").format(new Date(time)).trim();
    }

    public static String millsToDate(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return (new SimpleDateFormat(DATE_FORMAT)).format(calendar.getTime());
    }


}
