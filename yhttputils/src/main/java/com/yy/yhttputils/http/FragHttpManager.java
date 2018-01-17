package com.yy.yhttputils.http;

import android.content.Context;
import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yy.yhttputils.RxRetrofitApp;
import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.exception.RetryWhenNetworkException;
import com.yy.yhttputils.http.func.ExceptionFunc;
import com.yy.yhttputils.http.func.ResulteFunc;
import com.yy.yhttputils.listener.HttpOnNextListener;
import com.yy.yhttputils.subscribers.FragProgressSubscriber;

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
//import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ly on 2017/12/26.
 */
@Deprecated
public class FragHttpManager {
    private SoftReference<HttpOnNextListener> onNextListener;
    private SoftReference<RxFragment> rxfraggment;


    public FragHttpManager(HttpOnNextListener onNextListener, RxFragment rxFragment) {
        this.onNextListener = new SoftReference(onNextListener);
        this.rxfraggment = new SoftReference(rxFragment);
    }

    @Deprecated
    public void doHttpDeal(BaseApi basePar) {
        Retrofit retrofit = this.getReTrofit(basePar.getConnectionTime(), basePar.getBaseUrl());
        this.httpDeal(basePar.getObservable(retrofit), basePar);
    }

    private Retrofit getReTrofit(int connectTime, String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout((long)connectTime, TimeUnit.SECONDS);
        if(RxRetrofitApp.isDebug()) {
            builder.addInterceptor(this.getHttpLoggingInterceptor());
        }

        Retrofit retrofit = (new retrofit2.Retrofit.Builder()).client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl).build();
        return retrofit;
    }
    public void httpDeal(Observable observable, BaseApi basePar) {
        observable = observable.retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(), basePar.getRetryDelay(), basePar.getRetryIncreaseDelay())).onErrorResumeNext(new ExceptionFunc()).compose(((RxFragment)this.rxfraggment.get()).bindToLifecycle()).map(new ResulteFunc()).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        if(this.onNextListener != null && null != this.onNextListener.get()) {
            FragProgressSubscriber subscriber = new FragProgressSubscriber(basePar, this.onNextListener, this.rxfraggment);
            observable.subscribe(subscriber);
        }

    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            public void log(String message) {
                if(RxRetrofitApp.isDebug()) {
                    Log.d("RxRetrofit", "Retrofit====Message:" + message);
                }

            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
