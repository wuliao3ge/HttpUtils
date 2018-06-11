package com.yy.yhttputils.http;

import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.yy.yhttputils.RxRetrofitApp;
import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.exception.RetryWhenNetworkException;
import com.yy.yhttputils.http.func.ExceptionFunc;
import com.yy.yhttputils.http.func.ResulteFunc;
import com.yy.yhttputils.subscribers.DefaultSubscriber;


import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ly on 2018/3/10.
 */

public class NoPgHttpManager {
        /*软引用對象*/
        private SoftReference<DefaultHttpListener> defaultHttpListener;
        private SoftReference<LifecycleProvider> lifecycleProvider;
        private static NoPgHttpManager httpManager;

        public static NoPgHttpManager getInstance(){
            if(httpManager ==null)
            {
                httpManager = new NoPgHttpManager();
            }
            return httpManager;
        }



        public NoPgHttpManager(){

        }


        /**
         * 生命周期管理句柄
         * @param lifecycleProvider
         * @return
         */
        public NoPgHttpManager setLifecycle(LifecycleProvider lifecycleProvider) {
            this.lifecycleProvider =  new SoftReference<>(lifecycleProvider);
            return this;
        }



        /**
         * 返回数据监听
         * @param defaultHttpListener
         * @return
         */
        public NoPgHttpManager setHttpListener(DefaultHttpListener defaultHttpListener){
            this.defaultHttpListener = new SoftReference<>(defaultHttpListener);
            return this;
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
            if(lifecycleProvider!=null)
            {
                observable = observable.retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                        basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*异常处理*/
                        .onErrorResumeNext(new ExceptionFunc())
                /*生命周期管理*/
                        .compose(lifecycleProvider.get().bindToLifecycle())
                        //Note:手动设置在activity onDestroy的时候取消订阅
//                .compose(appCompatActivity.get().bindUntilEvent(ActivityEvent.DESTROY))
                /*返回数据统一判断*/
                        .map(new ResulteFunc())
                /*http请求线程*/
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                        .observeOn(AndroidSchedulers.mainThread());
            }else{
                observable = observable.retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                        basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*异常处理*/
                        .onErrorResumeNext(new ExceptionFunc())
                        //Note:手动设置在activity onDestroy的时候取消订阅
//                .compose(appCompatActivity.get().bindUntilEvent(ActivityEvent.DESTROY))
                /*返回数据统一判断*/
                        .map(new ResulteFunc())
                /*http请求线程*/
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                        .observeOn(AndroidSchedulers.mainThread());
            }



        /*数据String回调*/
            if (defaultHttpListener != null&&defaultHttpListener.get()!=null) {
                DefaultSubscriber subscriber = new DefaultSubscriber(basePar, defaultHttpListener.get());
                observable.subscribe(subscriber);
            }else{

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

