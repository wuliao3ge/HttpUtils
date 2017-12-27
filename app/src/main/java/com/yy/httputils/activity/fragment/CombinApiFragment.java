package com.yy.httputils.activity.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yy.httputils.R;
import com.yy.httputils.databinding.FragmentCombinapiBinding;
import com.yy.httputils.entity.api.SubjectPostApi;
import com.yy.httputils.entity.resulte.BaseResultEntity;
import com.yy.httputils.entity.resulte.SubjectResulte;
import com.yy.httputils.model.GetDataModel;
import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.http.FragHttpManager;
import com.yy.yhttputils.listener.HttpOnNextListener;

import java.util.ArrayList;

/**
 * <pre>
 *     author : ly
 *     time   : 2017/08/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CombinApiFragment extends RxFragment implements HttpOnNextListener{

    private GetDataModel getDataModel;
    private FragHttpManager fragHttpManager;
    //    post请求接口信息
    private SubjectPostApi postEntity;


    private FragmentCombinapiBinding binding;

    public CombinApiFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_combinapi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = DataBindingUtil.bind(view);
        postEntity = new SubjectPostApi();
        postEntity.setAll(true);
        fragHttpManager = new FragHttpManager(this,this);
        fragHttpManager.doHttpDeal(postEntity);
//        getDataModel = new GetDataModel(getActivity(),this);
//        getDataModel.onClick();
    }

    @Override
    public void onNext(String resulte, String method) {
        Log.i("CombinApiFragment",resulte);
        /*post返回处理*/
        if (method.equals(postEntity.getMethod())) {
            BaseResultEntity<ArrayList<SubjectResulte>> subjectResulte = JSONObject.parseObject(resulte, new
                    TypeReference<BaseResultEntity<ArrayList<SubjectResulte>>>() {
                    });
            binding.text3.setText("post返回：\n" + subjectResulte.getData().toString());
        }
    }

    @Override
    public void onError(ApiException e, String method) {

    }
}
