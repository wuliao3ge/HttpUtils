package com.yy.httputils;

import com.yy.httputils.model.ApiModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 测试接口service-post相关
 * Created by WZG on 2016/12/19.
 */

public interface HttpPostService {


    @GET("AppFiftyToneGraph/videoLink/{once_no}")
    Observable<String> getAllVedioBy(@Query("once_no") boolean once_no);

    @POST("AppFiftyToneGraph/videoLink/")
    Observable<String> getAllVedio(@Body ApiModel apiModel);

    @GET
    Observable<String> getHttps(@Url String url);
}
