package com.yy.httputils.entity.api;


import com.yy.httputils.HttpPostService;
import com.yy.yhttputils.api.BaseApi;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * 测试数据
 * Created by WZG on 2016/7/16.
 */
public class SubjectPostApi extends BaseApi {
    //    接口需要传入的参数 可自定义不同类型
    private boolean all;
    /*任何你先要传递的参数*/
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;


    /**
     * 默认初始化需要给定初始设置
     * 可以额外设置请求设置加载框显示，回调等（可扩展）
     * 设置可查看BaseApi
     */
    public SubjectPostApi() {
        setMethod("AppFiftyToneGraph/videoLink");
    }


    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService httpService = retrofit.create(HttpPostService.class);
        return httpService.getAllVedioBy(isAll());
    }

}
