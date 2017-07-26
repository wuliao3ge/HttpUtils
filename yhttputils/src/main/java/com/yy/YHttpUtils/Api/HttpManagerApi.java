package com.yy.YHttpUtils.Api;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.YHttpUtils.http.HttpManager;
import com.yy.YHttpUtils.listener.HttpOnNextListener;
import com.yy.YHttpUtils.listener.HttpOnNextSubListener;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by ly on 17-7-25.
 */

public class HttpManagerApi extends BaseApi {
    private HttpManager manager;

    public HttpManagerApi(HttpOnNextListener onNextListener, RxAppCompatActivity appCompatActivity) {
        manager = new HttpManager(onNextListener, appCompatActivity);
    }

    public HttpManagerApi(HttpOnNextSubListener onNextSubListener, RxAppCompatActivity appCompatActivity) {
        manager = new HttpManager(onNextSubListener, appCompatActivity);
    }

    protected Retrofit getRetrofit() {
        return  manager.getReTrofit(getConnectionTime(), getBaseUrl());
    }


    protected void doHttpDeal(Observable observable) {
        manager.httpDeal(observable, this);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return null;
    }
}
