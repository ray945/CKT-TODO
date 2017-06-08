package com.ckt.ckttodo.retrofit;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mozre on 2017/6/8.
 */

public interface TaskService {

    @GET("project/plan/task")
    Observable<ResponseBody> getTasks(@Query("email") String email, @Query("token") String token, @Query("planid") String planId);

    @GET("project/task")
    Observable<ResponseBody> getTasksById(@Query("email") String email, @Query("token") String token);

    @POST("project/task")
    @FormUrlEncoded
    Observable<RequestBody> postNewTask(@FieldMap Map<String, String> map);

}
