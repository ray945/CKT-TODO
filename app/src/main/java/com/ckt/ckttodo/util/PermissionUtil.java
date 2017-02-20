package com.ckt.ckttodo.util;

import android.content.pm.PackageManager;

/**
 * Created by ckt on 2/19/17.
 */

public class PermissionUtil {

    public static boolean verifyPermission(int[] grantResults) {

        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_DENIED) {
                return false;
            }

        }

        return true;
    }


}
