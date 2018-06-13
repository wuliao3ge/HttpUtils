package com.yy.httputils.entity.api;

import com.yy.httputils.HttpPostService;
import com.yy.yhttputils.api.BaseApi;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by ly on 2018/6/13.
 */

public class HttpsApi extends BaseApi{

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        return httpPostService.getHttps(url);

    }
}
