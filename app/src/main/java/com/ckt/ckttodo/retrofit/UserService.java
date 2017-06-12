package com.ckt.ckttodo.retrofit;

import com.ckt.ckttodo.database.Result;
import com.ckt.ckttodo.database.ResultUser;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ckt on 6/9/17.
 */

public interface UserService {

    @GET("user/login")
    Observable<ResultUser> doLogin(@Query("email") String email, @Query("password") String password);

    @POST("user")
    @FormUrlEncoded
    Observable<Result> doSignup(@FieldMap Map<String, String> map);
}
