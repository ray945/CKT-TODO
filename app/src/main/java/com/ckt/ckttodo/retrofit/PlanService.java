package com.ckt.ckttodo.retrofit;

import com.ckt.ckttodo.database.PostPlan;
import com.ckt.ckttodo.database.Result;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mozre on 6/14/17.
 */

public interface PlanService {

    @POST("project/plan")
    @FormUrlEncoded
    Observable<Result> postNewPlan(@FieldMap Map<String, String> map);

    @GET("project/plan")
    Observable<Result<PostPlan>> getPlans(@Query("email") String email, @Query("token") String token,  @Query("status") int status);
}
