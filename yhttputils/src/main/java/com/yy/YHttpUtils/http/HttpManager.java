package com.yy.YHttpUtils.http;

import android.content.Context;
import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.yy.YHttpUtils.api.BaseApi;
import com.yy.YHttpUtils.RxRetrofitApp;
import com.yy.YHttpUtils.exception.RetryWhenNetworkException;
import com.yy.YHttpUtils.http.func.ExceptionFunc;
import com.yy.YHttpUtils.http.func.ResulteFunc;
import com.yy.YHttpUtils.listener.HttpOnNextListener;
import com.yy.YHttpUtils.subscribers.ProgressSubscriber;

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
 * http交互处理类
 * Created by ly on 17-7-25.
 */
public class HttpManager {
    /*软引用對象*/
    private SoftReference<HttpOnNextListener> onNextListener;
//    private HttpOnNextListener onNextListener;
    private SoftReference<Context> context;
    private SoftReference<LifecycleProvider> lifecycleProvider;
    private static HttpManager httpManager;
    public static HttpManager getInstance(){
        if(httpManager ==null)
        {
            httpManager = new HttpManager();
        }
        return httpManager;
    }

    /**
     * 生命周期管理句柄
     * @param lifecycleProvider
     * @return
     */
    public HttpManager setLifecycle(LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider =  new SoftReference<>(lifecycleProvider);
        return this;
    }

    /**
     * 用于对话框显示的句柄
     * @param context
     * @return
     */
    public HttpManager setContext(Context context) {
        this.context = new SoftReference(context);
        return this;
    }

    /**
     * 返回数据监听
     * @param onNextListener
     * @return
     */
    public HttpManager setOnNextListener(HttpOnNextListener onNextListener){
        this.onNextListener = new SoftReference<>(onNextListener);
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

        /*数据String回调*/
            if (onNextListener != null) {
                ProgressSubscriber subscriber = new ProgressSubscriber(basePar, onNextListener, context);
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
