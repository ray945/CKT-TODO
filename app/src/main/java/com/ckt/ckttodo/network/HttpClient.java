package com.ckt.ckttodo.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ckt on 5/16/17.
 */

public class HttpClient {

    private volatile static OkHttpClient sClient;
    private volatile static Retrofit sRetrofit;

    public static OkHttpClient getClient() {

        if (sClient == null) {
            sClient = new OkHttpClient();
        }

        return sClient;

    }

    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .client(HttpClient.getClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(HttpConstants.SERVER_HOST)
                    .build();
        }
        return sRetrofit;
    }

    public static <T> T getHttpService(Class<T> service) {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .client(HttpClient.getClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(HttpConstants.SERVER_HOST)
                    .build();
        }
        return sRetrofit.create(service);
    }

}
