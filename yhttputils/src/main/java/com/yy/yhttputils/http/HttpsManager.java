package com.yy.yhttputils.http;

import android.content.Context;
import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.yhttputils.RxRetrofitApp;
import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.exception.RetryWhenNetworkException;
import com.yy.yhttputils.http.func.ExceptionFunc;
import com.yy.yhttputils.http.func.ResulteFunc;
import com.yy.yhttputils.listener.HttpOnNextListener;
import com.yy.yhttputils.subscribers.ProgressSubscriber;
import com.yy.yhttputils.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

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
 * http交互处理类
 * Created by ly on 17-7-25.
 */
public class HttpsManager {
    /*软引用對象*/
    private SoftReference<HttpOnNextListener> onNextListener;
//    private HttpOnNextListener onNextListener;
    private SoftReference<Context> context;
    private SoftReference<LifecycleProvider> lifecycleProvider;
    private static HttpsManager httpManager;
    private InputStream certificates;
    private OkHttpClient.Builder builder;
    public static HttpsManager getInstance(){
        if(httpManager ==null)
        {
            httpManager = new HttpsManager();
        }
        return httpManager;
    }

    @Deprecated
    public HttpsManager(HttpOnNextListener onNextListener, RxAppCompatActivity appCompatActivity) {
        this.onNextListener = new SoftReference(onNextListener);
        this.context = new SoftReference(appCompatActivity);
        this.lifecycleProvider = new SoftReference<LifecycleProvider>(appCompatActivity);
        builder = new OkHttpClient.Builder();
    }

    public HttpsManager(){
        builder = new OkHttpClient.Builder();
    }


    /**
     * 生命周期管理句柄
     * @param lifecycleProvider
     * @return
     */
    public HttpsManager setLifecycle(LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider =  new SoftReference<>(lifecycleProvider);
        return this;
    }

    /**
     * 用于对话框显示的句柄
     * @param context
     * @return
     */
    public HttpsManager setContext(Context context) {
        this.context = new SoftReference(context);
        return this;
    }

    /**
     * 返回数据监听
     * @param onNextListener
     * @return
     */
    public HttpsManager setOnNextListener(HttpOnNextListener onNextListener){
        this.onNextListener = new SoftReference<>(onNextListener);
        return this;
    }


    /**
     * 设置证书
     * @param context 全局变量
     * @param cerName 证书名称
     * @return
     */
    public HttpsManager setCer(Context context,String cerName){
        if(StringUtil.isNoEmpty(cerName))
        {
            try {
                setCertificates(builder,context.getAssets().open(cerName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            SSLSocketFactory ssfFactory = null;
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

                ssfFactory = sc.getSocketFactory();
            } catch (Exception e) {
            }
            builder.sslSocketFactory(ssfFactory);
        }
        return httpManager;
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
            if (onNextListener != null&&onNextListener.get()!=null) {
                ProgressSubscriber subscriber = new ProgressSubscriber(basePar, onNextListener, context);
                observable.subscribe(subscriber);
            }else{

            }
    }

/**
 * 通过okhttpClient来设置证书
 * @param clientBuilder OKhttpClient.builder
 * @param certificates 读取证书的InputStream
 */

public void setCertificates(OkHttpClient.Builder clientBuilder, InputStream... certificates) {
    try {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        int index = 0;
        for (InputStream certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificateFactory
                    .generateCertificate(certificate));
            try {
                if (certificate != null)
                    certificate.close();
            } catch (IOException e) {
            }
        }
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    public class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
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
