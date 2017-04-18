package com.ckt.ckttodo.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by zhiwei.li on 2017/3/30.
 */

public interface NetworkService {

    @GET("CKTTODO/login.do")
    Call<String> login(@Query("mem_name") String user, @Query("mem_pwd") String pwd);

    @GET("CKTTODO/register.do")
    Call<String> register(
        @Query("name") String name,
        @Query("pwd") String pwd,
        @Query("email") String email, @Query("level") String level, @Query("token") String token);

}