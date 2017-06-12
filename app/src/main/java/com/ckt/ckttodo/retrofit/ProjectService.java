package com.ckt.ckttodo.retrofit;

import com.ckt.ckttodo.database.PostProject;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.Result;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
    Observable<Result<PostProject>> getProjects(@Query("email")String email, @Query("token")String token, @Query("target_email")String targetEmail);
}
