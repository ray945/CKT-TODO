package com.ckt.ckttodo.network;

import com.ckt.ckttodo.ui.BaseView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ckt on 5/16/17.
 */

public class HTTPService {

    private static Object sLock = new Object();

    private static HTTPService sInstance;


    public static HTTPService getHTTPService() {
        if (sInstance == null) {
            synchronized (sLock) {

                if (sInstance == null) {

                    sInstance = new HTTPService();
                }
            }


        }

        return sInstance;
    }

    public void doHTTPRequest(final Request request, final BaseView notify) {

        rx.Observable
                .create(new rx.Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        OkHttpClient client = HTTPUtil.getClient();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                subscriber.onNext(response.body().string());
                            }
                        });

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        notify.replyNetworkErr();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        notify.replyRequestResult(s);
                    }
                });


    }


}
