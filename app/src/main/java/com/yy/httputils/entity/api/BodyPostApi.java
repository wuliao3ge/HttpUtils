package com.yy.httputils.entity.api;

import com.yy.httputils.HttpPostService;
import com.yy.httputils.model.ApiModel;
import com.yy.yhttputils.api.BaseApi;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by ly on 2018/1/7.
 */

public class BodyPostApi extends BaseApi {

    private ApiModel apiModel;

    public ApiModel getApiModel() {
        return apiModel;
    }

    public void setApiModel(ApiModel apiModel) {
        this.apiModel = apiModel;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        return httpPostService.getAllVedio(apiModel);
    }
}
