package com.ckt.ckttodo.retrofit;

import com.ckt.ckttodo.database.PostTask;
import com.ckt.ckttodo.database.Result;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mozre on 2017/6/8.
 */

public interface TaskService {

    @GET("project/plan/task")
    Observable<Result> getTasks(@Query("email") String email, @Query("token") String token, @Query("planid") String planId);

    @GET("project/task")
    Observable<Result<PostTask>> getTasksById(@Query("email") String email, @Query("token") String token);

    @POST("project/task")
    @FormUrlEncoded
    Observable<Result<PostTask>> postNewTask(@FieldMap Map<String, String> map);

}
