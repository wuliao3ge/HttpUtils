package com.yy.HttpUtils.entity.api;


import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.HttpUtils.HttpPostService;
import com.yy.YHttpUtils.Api.HttpManagerApi;
import com.yy.YHttpUtils.listener.HttpOnNextListener;
import com.yy.YHttpUtils.listener.HttpOnNextSubListener;

/**
 * 多api共存方案
 * Created by WZG on 2017/4/13.
 */

public class CombinApi extends HttpManagerApi {

    public CombinApi(HttpOnNextListener onNextListener, RxAppCompatActivity appCompatActivity) {
        super(onNextListener, appCompatActivity);
        /*统一设置*/
        setCache(true);
    }

    public CombinApi(HttpOnNextSubListener onNextSubListener, RxAppCompatActivity appCompatActivity) {
        super(onNextSubListener, appCompatActivity);
        /*统一设置*/
        setCache(true);
    }


    /**
     * post请求演示
     * api-1
     *
     * @param all 参数
     */
    public void postApi(final boolean all) {
        /*也可单独设置请求，会覆盖统一设置*/
        setCache(false);
        setMethod("AppFiftyToneGraph/videoLink");
        HttpPostService httpService = getRetrofit().create(HttpPostService.class);
        doHttpDeal(httpService.getAllVedioBy(all));
    }

    /**
     * post请求演示
     * api-2
     *
     * @param all 参数
     */
    public void postApiOther(boolean all) {
        setCache(true);
        setMethod("AppFiftyToneGraph/videoLink");
        HttpPostService httpService = getRetrofit().create(HttpPostService.class);
        doHttpDeal(httpService.getAllVedioBy(all));
    }

}
