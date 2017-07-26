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

    /**
     * http库初始化
     * @param app Application
     */
    public static void init(Application app){
        init(app,true,"");
    }

    /**
     * http库初始化
     * @param app Application
     * @param debug 是否打印log
     */
    public static void init(Application app,boolean debug){
        init(app,debug,"");
    }

    /**
     * http库初始化
     * @param app Application
     * @param baseUrl  基础url
     */
    public static void init(Application app,String baseUrl){
        init(app,true,baseUrl);
    }

    /**
     * http库初始化
     * @param app Application
     * @param debug 是否打印log
     * @param baseUrl 基础url
     */
    public static void init(Application app,boolean debug,String baseUrl){
        setApplication(app);
        setDebug(debug);
        setBaseUrl(baseUrl);
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

    public static void setDebug(boolean debug) {
        RxRetrofitApp.debug = debug;
    }

    public static String getBaseUrl() {
        return BaseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        BaseUrl = baseUrl;
    }
}
