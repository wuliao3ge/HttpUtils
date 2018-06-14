package com.yy.httputils;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yy.httputils.activity.progress.CustomProgressDialog;
import com.yy.yhttputils.RxRetrofitApp;
import com.yy.yhttputils.enums.NetType;
import com.yy.yhttputils.http.HttpManager;

/**
 * Created by ly on 17-7-25.
 */

public class AppApplication extends Application {

    private static AppApplication appApplication;


    public static AppApplication getInstance(){
        if(appApplication ==null)
        {
            appApplication = new AppApplication();
        }
        return appApplication;
    }



    private RefWatcher mRefWatcher;



    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
//        RxRetrofitApp.init(this,"https://www.izaodao.com/Api/");




        LeakCanary.install(this);
        RxRetrofitApp.Create(this)
                .setBaseUrl("https://www.izaodao.com/Api/")
                .setDebug(true)
                .setBaseProgress(new CustomProgressDialog())
                .setNetType(NetType.NETTYPE_HTTPS)
                .setProgressMassge("请求数据中,请稍等……");
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        RxRetrofitApp.getRxRetrofitApp().release();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        RxRetrofitApp.getRxRetrofitApp().release();
    }
}
