package com.yy.httputils.model;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.yy.httputils.AppApplication;
import com.yy.httputils.entity.api.SubjectPostApi;
import com.yy.httputils.entity.resulte.BaseResultEntity;
import com.yy.httputils.entity.resulte.SubjectResulte;
import com.yy.httputils.framework.BaseLoadListener;
import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.listener.HttpOnNextListener;

import java.util.ArrayList;

/**
 * Created by ly on 2017/12/7.
 */

public class GetDataModel {
    SubjectPostApi postEntity = new SubjectPostApi();
    private Context context;
    private LifecycleProvider lifecycleProvider;
//    private DataViewModel dataViewModel;

    private BaseLoadListener<String> baseLoadListener;


    public GetDataModel(Context context,LifecycleProvider lifecycleProvider){
        postEntity.setAll(true);
        this.context = context;
        this.lifecycleProvider = lifecycleProvider;
    }

    public BaseLoadListener<String> getBaseLoadListener() {
        return baseLoadListener;
    }

    public void setBaseLoadListener(BaseLoadListener<String> baseLoadListener) {
        this.baseLoadListener = baseLoadListener;
    }

    public  void getData(){
        AppApplication.getInstance().getHttpManager()
                .setLifecycle(lifecycleProvider)
                .setContext(context)
                .setOnNextListener(new HttpOnNextListener() {
                    @Override
                    public void onNext(String resulte, String method) {
                        Log.i("自定义回调","onNext");
                        BaseResultEntity<ArrayList<SubjectResulte>> subjectResulte = JSONObject.parseObject(resulte, new
                                TypeReference<BaseResultEntity<ArrayList<SubjectResulte>>>() {
                                });
                        baseLoadListener.loadSuccess(subjectResulte.getData().toString());
                    }

                    @Override
                    public void onError(ApiException e, String method) {
                        Log.i("自定义回调","onError");
                    }
                })
                .doHttpDeal(postEntity);
    }

}
