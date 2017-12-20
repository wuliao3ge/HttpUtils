package com.yy.httputils;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yy.yhttputils.RxRetrofitApp;
import com.yy.yhttputils.http.HttpManager;

/**
 * Created by ly on 17-7-25.
 */

public class AppApplication extends Application {

    private static AppApplication appApplication;

    private HttpManager httpManager;

    public static AppApplication getInstance(){
        if(appApplication ==null)
        {
            appApplication = new AppApplication();
        }
        return appApplication;
    }

    public AppApplication() {
        httpManager = HttpManager.getInstance();
    }



    private RefWatcher mRefWatcher;



    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
//        RxRetrofitApp.init(this,"https://www.izaodao.com/Api/");


        Utils.init(this);

        LeakCanary.install(this);
        RxRetrofitApp.Create(this)
                .setBaseUrl("https://www.izaodao.com/Api/")
                .setDebug(true)
                .setProgressMassge("请求数据中,请稍等……");
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }


    public static RefWatcher getRefWatcher() {
        return getInstance().mRefWatcher;
    }

}