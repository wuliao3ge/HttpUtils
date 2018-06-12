package com.yy.httputils.model.datamodel;

import android.content.Context;
import android.util.Log;

//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.yy.httputils.AppApplication;
import com.yy.httputils.entity.api.SubjectPostApi;
import com.yy.httputils.entity.resulte.BaseResultEntity;
import com.yy.httputils.entity.resulte.SubjectResulte;
import com.yy.httputils.framework.BaseLoadListener;
import com.yy.yhttputils.base.HttpBaseModel;
import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.http.HttpsManager;
import com.yy.yhttputils.listener.HttpOnNextListener;

import java.util.ArrayList;

/**
 * Created by ly on 2017/12/7.
 */

public class GetDataModel extends HttpBaseModel{
    SubjectPostApi postEntity = new SubjectPostApi();
//    private DataViewModel dataViewModel;

    private BaseLoadListener<String> baseLoadListener;


    public GetDataModel(){
        postEntity.setAll(true);
    }

    public BaseLoadListener<String> getBaseLoadListener() {
        return baseLoadListener;
    }

    public void setBaseLoadListener(BaseLoadListener<String> baseLoadListener) {
        this.baseLoadListener = baseLoadListener;
    }

    public  void getData(){
        HttpsManager.getInstance().setOnNextListener(new HttpOnNextListener() {
            @Override
            public void onNext(String resulte, String method) {
                Gson gson = new Gson();
                BaseResultEntity<ArrayList<SubjectResulte>> subjectResulte = gson.fromJson(resulte,BaseResultEntity.class);
                baseLoadListener.loadSuccess(subjectResulte.getData().toString());
            }

            @Override
            public void onError(ApiException e, String method) {

            }
        })
                .doHttpDeal(postEntity);
//        httpManager.doHttpDeal(postEntity);
    }

    @Override
    public void onNext(String resulte, String method) {
//        BaseResultEntity<ArrayList<SubjectResulte>> subjectResulte = JSONObject.parseObject(resulte, new
//                                TypeReference<BaseResultEntity<ArrayList<SubjectResulte>>>() {
//                                });
        Gson gson = new Gson();
        BaseResultEntity<ArrayList<SubjectResulte>> subjectResulte = gson.fromJson(resulte,BaseResultEntity.class);
        baseLoadListener.loadSuccess(subjectResulte.getData().toString());
    }

    @Override
    public void onError(ApiException e, String method) {

    }
}
