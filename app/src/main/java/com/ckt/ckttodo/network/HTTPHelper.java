package com.ckt.ckttodo.network;

import android.util.Log;

import java.util.Map;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ckt on 5/16/17.
 */

public class HTTPHelper {

    private static final String TAG = "HTTPHelper";

    public static Request getGetRequest(Map<String, String> data, String path) {

        StringBuilder builder = new StringBuilder(HTTPConstants.SERVER_HOST).append(path).append("?");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");

        }
        builder.deleteCharAt(builder.length() - 1);

        Log.d(TAG, "getGetRequest: " + builder.toString().replace(" ",""));
        Request request = new Request.Builder().url(builder.toString().replace(" ","")).build();

        return request;

    }

    public static Request getPostRequest(Map<String, String> data, String path) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(HTTPConstants.SERVER_HOST + path).post(requestBody).build();

    }


}
