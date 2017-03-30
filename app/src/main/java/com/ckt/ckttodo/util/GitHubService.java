package com.ckt.ckttodo.util;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * retrofit 测试
 * Created by zhiwei.li on 2017/3/30.
 */

public interface GitHubService {

    @GET("users/{user}/repos")
    Call<String> listRepos(@Path("user") String user);

}

