package com.yy.HttpUtils.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yy.HttpUtils.AppApplication;
import com.yy.HttpUtils.entity.api.SubjectPostApi;
import com.yy.YHttpUtils.exception.ApiException;
import com.yy.YHttpUtils.http.HttpManager;
import com.yy.YHttpUtils.listener.HttpOnNextListener;

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
