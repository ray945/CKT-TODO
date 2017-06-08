package com.ckt.ckttodo.network;

import okhttp3.OkHttpClient;

/**
 * Created by ckt on 5/16/17.
 */

public class HttpClient {

    private volatile static OkHttpClient mClient;


    public static OkHttpClient getClient() {

        if (mClient == null) {
            mClient = new OkHttpClient();
        }

        return mClient;

    }

}
