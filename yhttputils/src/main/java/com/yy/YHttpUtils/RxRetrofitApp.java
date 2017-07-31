package com.yy.YHttpUtils;

import android.app.Application;

/**
 * 全局设定
 * Created by ly on 17-7-25.
 */

public class RxRetrofitApp {
    private static Application application;
    private static boolean debug;
    private static String BaseUrl;
    private static String ProgressMassge;
    private static RxRetrofitApp rxRetrofitApp;

    public RxRetrofitApp(Application application) {
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


//    /**
//     * http库初始化
//     * @param app Application
//     */
//    public static void init(Application app){
//        if(rxRetrofitApp)
//        init(app,true,"");
//    }
//
//    /**
//     * http库初始化
//     * @param app Application
//     * @param debug 是否打印log
//     */
//    public static RxRetrofitApp init(Application app,boolean debug){
//        init(app,debug,"");
//        return RxRetrofitApp.this;
//    }
//
//    /**
//     * http库初始化
//     * @param app Application
//     * @param baseUrl  基础url
//     */
//    public static RxRetrofitApp init(Application app,String baseUrl){
//        init(app,true,baseUrl);
//    }
//
//    /**
//     * http库初始化
//     * @param app Application
//     * @param debug 是否打印log
//     * @param baseUrl 基础url
//     */
//    public static void init(Application app,boolean debug,String baseUrl){
//        setApplication(app);
//        setDebug(debug);
//        setBaseUrl(baseUrl);
//    }


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

    public  static String getProgressMassge() {
        return ProgressMassge;
    }

    public  RxRetrofitApp setProgressMassge(String progressMassge) {
        ProgressMassge = progressMassge;
        return rxRetrofitApp;
    }
}
