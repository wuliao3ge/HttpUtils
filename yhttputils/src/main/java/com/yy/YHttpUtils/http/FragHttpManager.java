package com.yy.YHttpUtils.http;

import android.util.Log;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yy.YHttpUtils.Api.BaseApi;
import com.yy.YHttpUtils.RxRetrofitApp;
import com.yy.YHttpUtils.exception.RetryWhenNetworkException;
import com.yy.YHttpUtils.http.func.ExceptionFunc;
import com.yy.YHttpUtils.http.func.ResulteFunc;
import com.yy.YHttpUtils.listener.HttpOnNextListener;
import com.yy.YHttpUtils.listener.HttpOnNextSubListener;
import com.yy.YHttpUtils.subscribers.FragProgressSubscriber;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * <pre>
 *     author : ly
 *     time   : 2017/08/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class FragHttpManager {
    /*软引用對象*/
    private SoftReference<HttpOnNextListener> onNextListener;
    private SoftReference<HttpOnNextSubListener> onNextSubListener;
    private SoftReference<RxFragment> rxfraggment;

    public FragHttpManager(HttpOnNextListener onNextListener, RxFragment rxFragment) {
        this.onNextListener = new SoftReference(onNextListener);
        this.rxfraggment = new SoftReference(rxFragment);
    }

    public FragHttpManager(HttpOnNextSubListener onNextSubListener, RxFragment rxFragment) {
        this.onNextSubListener = new SoftReference(onNextSubListener);
        this.rxfraggment = new SoftReference(rxFragment);
    }


    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(final BaseApi basePar) {
        Retrofit retrofit = getReTrofit(basePar.getConnectionTime(), basePar.getBaseUrl());
        httpDeal(basePar.getObservable(retrofit), basePar);
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
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        /*创建retrofit对象*/
        final Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
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
          /*失败后的retry配置*/
        observable = observable.retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*异常处理*/
                .onErrorResumeNext(new ExceptionFunc())
                /*生命周期管理*/
                .compose(rxfraggment.get().bindToLifecycle())
                //Note:手动设置在activity onDestroy的时候取消订阅
//                .compose(appCompatActivity.get().bindUntilEvent(ActivityEvent.DESTROY))
                /*返回数据统一判断*/
                .map(new ResulteFunc())
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread());

        /*ober回调，链接式返回*/
        if (onNextSubListener != null && null != onNextSubListener.get()) {
            onNextSubListener.get().onNext(observable, basePar.getMethod());
        }

        /*数据String回调*/
        if (onNextListener != null && null != onNextListener.get()) {
            FragProgressSubscriber subscriber = new FragProgressSubscriber(basePar, onNextListener, rxfraggment);
            observable.subscribe(subscriber);
        }
    }

    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (RxRetrofitApp.isDebug()) {
                    Log.d("RxRetrofit", "Retrofit====Message:" + message);
                }
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
