package com.yy.HttpUtils;

import android.app.Application;

import com.yy.YHttpUtils.RxRetrofitApp;

/**
 * Created by ly on 17-7-25.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxRetrofitApp.init(this,"https://www.izaodao.com/Api/");
    }
}
