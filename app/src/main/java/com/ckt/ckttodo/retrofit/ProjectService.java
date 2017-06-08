package com.ckt.ckttodo.retrofit;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by mozre on 2017/6/6.
 */

public interface ProjectService {

    @FormUrlEncoded
    @POST("project")
    Observable<ResponseBody> postNewProject(@FieldMap Map<String, String> map);

}
