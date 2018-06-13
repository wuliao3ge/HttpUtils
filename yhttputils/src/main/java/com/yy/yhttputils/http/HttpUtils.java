package com.yy.yhttputils.http;

import com.yy.yhttputils.RxRetrofitApp;
import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.base.BaseProgress;
import com.yy.yhttputils.exception.RetryWhenNetworkException;
import com.yy.yhttputils.framework.HttpInterface;
import com.yy.yhttputils.http.func.ExceptionFunc;
import com.yy.yhttputils.http.func.ResulteFunc;
import com.yy.yhttputils.listener.HttpOnNextListener;
import com.yy.yhttputils.subscribers.ProgressSubscriber;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ly on 2018/6/13.
 */

public class HttpUtils implements HttpInterface{

    private  BaseProgress baseProgress = null;
    private List<HttpOnNextListener> httpOnNextListenerList;


    public HttpUtils(){
        httpOnNextListenerList = new ArrayList<>();
    }

    @Override
    public void setOnNextListener(HttpOnNextListener onNextListener) {
        if(httpOnNextListenerList!=null)
        {
            httpOnNextListenerList.clear();
            httpOnNextListenerList.add(onNextListener);
        }else{
            httpOnNextListenerList = new ArrayList<>();
            httpOnNextListenerList.add(onNextListener);
        }
    }

    @Override
    public void setCertificate(InputStream... certificates) {

    }

    @Override
    public void doHttpDeal(BaseApi basePar) {
        if (baseProgress!=null)
        {
            basePar.setProgress(baseProgress);
        }
        Retrofit retrofit = getReTrofit(basePar.getConnectionTime(), basePar.getBaseUrl());
        httpDeal(basePar.getObservable(retrofit), basePar);
    }


    @Override
    public void setBaseProgress(BaseProgress baseProgress) {
        this.baseProgress = baseProgress;
    }

    @Override
    public void release() {
        if(httpOnNextListenerList!=null)
        {
            httpOnNextListenerList.clear();
            httpOnNextListenerList = null;
        }
    }


    /**
     * 获取Retrofit对象
     *
     * @param connectTime
     * @param baseUrl
     * @return
     */
    public Retrofit getReTrofit(int connectTime, String baseUrl) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(connectTime, TimeUnit.SECONDS);
        if (RxRetrofitApp.isDebug()) {
            builder.addInterceptor(RxRetrofitApp.getRxRetrofitApp().getHttpLoggingInterceptor());
        }

        /*创建retrofit对象*/
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build();
        return retrofit;
    }

    /**
     * RxRetrofit处理
     *
     * @param observable
     * @param basePar
     */
    public void httpDeal(Observable observable, BaseApi basePar) {
        observable = observable.retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*异常处理*/
                .onErrorResumeNext(new ExceptionFunc())
                /*生命周期管理*/
//                .compose(lifecycleProvider.get().bindToLifecycle())
                //Note:手动设置在activity onDestroy的时候取消订阅
//                .compose(appCompatActivity.get().bindUntilEvent(ActivityEvent.DESTROY))
                /*返回数据统一判断*/
                .map(new ResulteFunc())
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread());

        /*数据String回调*/
        if (httpOnNextListenerList != null&&httpOnNextListenerList.size()>0) {
            ProgressSubscriber subscriber = new ProgressSubscriber(basePar, httpOnNextListenerList.get(0));
            observable.subscribe(subscriber);
        }
    }



}
