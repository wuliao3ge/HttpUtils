package com.yy.yhttputils;

import android.app.Application;

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
}
