package com.yy.HttpUtils.entity.api;


import com.yy.HttpUtils.HttpUploadService;
import com.yy.YHttpUtils.Api.BaseApi;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * 上传请求api
 * Created by WZG on 2016/10/20.
 */

public class UploadApi extends BaseApi {
    /*需要上传的文件*/
    private MultipartBody.Part part;

    public UploadApi() {
        setShowProgress(true);
        setMethod("AppYuFaKu/uploadHeadImg");
        setCache(false);
    }

    public MultipartBody.Part getPart() {
        return part;
    }

    public void setPart(MultipartBody.Part part) {
        this.part = part;
    }


    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpUploadService httpService = retrofit.create(HttpUploadService.class);
        RequestBody uid= RequestBody.create(MediaType.parse("text/plain"), "4811420");
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), "2bd467f727cdf2138c1067127e057950");
        return httpService.uploadImage(uid,key,getPart());
    }

}
