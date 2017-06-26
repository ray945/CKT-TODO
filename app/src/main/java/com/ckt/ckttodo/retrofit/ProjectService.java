package com.ckt.ckttodo.retrofit;

import com.ckt.ckttodo.database.PostProject;
import com.ckt.ckttodo.database.Result;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mozre on 2017/6/6.
 */

public interface ProjectService {

    @FormUrlEncoded
    @POST("project")
    Observable<Result> postNewProject(@FieldMap Map<String, String> map);

    @GET("project")
    Observable<Result<PostProject>> getProjects(@Query("email") String email, @Query("token") String token, @Query("target_email") String targetEmail);

    @GET("project/delete")
    Observable<Result> deleteProject(@Query("email") String email, @Query("token") String token, @Query("project_id") String projectId);

    @GET("project/sprint")
    Observable<Result> postNewSprint(@Query("email") String email, @Query("token") String token, @Query("projectId") String projectId, @Query("sprint") int sprint);
}
