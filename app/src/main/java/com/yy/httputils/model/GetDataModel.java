package com.yy.httputils.model;

import android.content.Context;
import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.yy.httputils.AppApplication;
import com.yy.httputils.entity.api.SubjectPostApi;
import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.listener.HttpOnNextListener;

/**
 * Created by ly on 2017/12/7.
 */

public class GetDataModel {
    SubjectPostApi postEntity = new SubjectPostApi();
    private Context context;
    private LifecycleProvider lifecycleProvider;

    public GetDataModel(Context context,LifecycleProvider lifecycleProvider){
        postEntity.setAll(true);
        this.context = context;
        this.lifecycleProvider = lifecycleProvider;
    }



    public  void onClick(){
        AppApplication.getInstance().getHttpManager()
                .setLifecycle(lifecycleProvider)
                .setContext(context)
                .setOnNextListener(new HttpOnNextListener() {
                    @Override
                    public void onNext(String resulte, String method) {
                        Log.i("自定义回调","onNext");
                    }

                    @Override
                    public void onError(ApiException e, String method) {
                        Log.i("自定义回调","onError");
                    }
                })
                .doHttpDeal(postEntity);
    }

}
