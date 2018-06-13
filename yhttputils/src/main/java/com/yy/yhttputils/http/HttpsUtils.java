package com.yy.yhttputils.http;

import android.util.Log;

import com.yy.yhttputils.RxRetrofitApp;
import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.base.BaseProgress;
import com.yy.yhttputils.exception.RetryWhenNetworkException;
import com.yy.yhttputils.framework.HttpInterface;
import com.yy.yhttputils.http.func.ExceptionFunc;
import com.yy.yhttputils.http.func.ResulteFunc;
import com.yy.yhttputils.listener.HttpOnNextListener;
import com.yy.yhttputils.subscribers.ProgressSubscriber;
import com.yy.yhttputils.utils.MCach;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ly on 2018/6/13.
 */

public class HttpsUtils implements HttpInterface {




    private static BaseProgress baseProgress = null;
    private InputStream[] inputStreams;

    private List<HttpOnNextListener> httpOnNextListenerList;
    public HttpsUtils() {
        httpOnNextListenerList = new ArrayList<>();
    }

    /**
     * 返回数据监听
     * @param onNextListener
     * @return
     */
    public void setOnNextListener(HttpOnNextListener onNextListener){
        if(httpOnNextListenerList!=null)
        {
            httpOnNextListenerList.clear();
            httpOnNextListenerList.add(onNextListener);
        }else{
            httpOnNextListenerList = new ArrayList<>();
            httpOnNextListenerList.add(onNextListener);
        }
    }

    /**
     * 设置证书
     * @param certificates 证书流
     * @return
     */
    public void setCertificate(InputStream... certificates) {
        this.inputStreams = certificates;
    }

    public void setBaseProgress(BaseProgress baseProgress) {
        this.baseProgress = baseProgress;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(final BaseApi basePar) {
        Retrofit retrofit = getReTrofit(basePar.getConnectionTime(), basePar.getBaseUrl());
        if(httpOnNextListenerList!=null&&httpOnNextListenerList.size()>0)
        {
            httpDeal(basePar.getObservable(retrofit), basePar);
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

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(inputStreams!=null)
        {
            setCertificates(builder,inputStreams);
        }else{
            SSLSocketFactory ssfFactory = null;
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

                ssfFactory = sc.getSocketFactory();
            } catch (Exception e) {
            }
            builder.sslSocketFactory(ssfFactory)
                    .hostnameVerifier(new TrustAllHostnameVerifier())
                    .cookieJar(new MCookie());
        }

        //手动创建一个OkHttpClient并设置超时时间缓存等设置
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
        if(RxRetrofitApp.isDebug())
        {
            Log.i("httpsManager","httpDeal");
        }
        observable = observable.retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*异常处理*/
                .onErrorResumeNext(new ExceptionFunc())
                /*生命周期管理*/
//                    .compose(lifecycleProvider.get().bindToLifecycle())
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

    public class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    public class MCookie implements CookieJar {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            MCach.saveCookies(cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return MCach.getCookies();
        }
    }

    @Override
    public void release() {
        if(httpOnNextListenerList!=null)
        {
            httpOnNextListenerList.clear();
            httpOnNextListenerList = null;
        }
    }
}
