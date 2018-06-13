package com.yy.yhttputils;

import android.app.Application;
import android.util.Log;

import com.yy.yhttputils.base.BaseProgress;
import com.yy.yhttputils.enums.NetType;
import com.yy.yhttputils.http.HttpManager;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 全局设定
 * Created by ly on 17-7-25.
 */

public class RxRetrofitApp {
    private static Application application;
    /** 是否 */
    private static boolean debug;
    private static String BaseUrl;
    private static String ProgressMassge;
    private static RxRetrofitApp rxRetrofitApp;
    private static int downConnectonTime=6;
    private NetType netType = NetType.NETTYPE_HTTP;

    private RxRetrofitApp(Application application) {
        this.application = application;
    }

    public static RxRetrofitApp Create(Application application)
    {
        if(rxRetrofitApp==null)
        {
            rxRetrofitApp = new RxRetrofitApp(application);
        }
        return rxRetrofitApp;
    }

    public static RxRetrofitApp getRxRetrofitApp() {
        return rxRetrofitApp;
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        RxRetrofitApp.application = application;
    }

    public static boolean isDebug() {
        return debug;
    }

    public  RxRetrofitApp setDebug(boolean debug) {
        RxRetrofitApp.debug = debug;
        return rxRetrofitApp;
    }

    public static String getBaseUrl() {
        return BaseUrl;
    }

    public  RxRetrofitApp setBaseUrl(String baseUrl) {
        BaseUrl = baseUrl;
        return rxRetrofitApp;
    }



    public RxRetrofitApp setDownConnectonTime(int downConnectonTime) {
        this.downConnectonTime = downConnectonTime;
        return rxRetrofitApp;
    }

    public static int getDownConnectonTime() {
        return downConnectonTime;
    }

    public  static String getProgressMassge() {
        return ProgressMassge;
    }

    public  RxRetrofitApp setProgressMassge(String progressMassge) {
        ProgressMassge = progressMassge;
        return rxRetrofitApp;
    }

    public NetType getNetType() {
        return netType;
    }

    public RxRetrofitApp setNetType(NetType netType) {
        this.netType = netType;
        return rxRetrofitApp;
    }


    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     */
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
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


    /**
     * 释放资源
     */
    public void release(){
        HttpManager.getInstance().release();
    }
}
