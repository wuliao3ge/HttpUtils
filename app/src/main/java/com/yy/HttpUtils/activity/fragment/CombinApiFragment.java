package com.yy.HttpUtils.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yy.HttpUtils.R;
import com.yy.HttpUtils.entity.api.SubjectPostApi;
import com.yy.YHttpUtils.exception.ApiException;
import com.yy.YHttpUtils.http.FragHttpManager;
import com.yy.YHttpUtils.listener.HttpOnNextListener;

/**
 * <pre>
 *     author : ly
 *     time   : 2017/08/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CombinApiFragment extends RxFragment implements HttpOnNextListener{

    //    post请求接口信息
    private SubjectPostApi postEntity;
    private FragHttpManager fragHttpManager;
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
        fragHttpManager = new FragHttpManager(this,CombinApiFragment.this);
        postEntity = new SubjectPostApi();
        postEntity.setAll(true);
        fragHttpManager.doHttpDeal(postEntity);
    }

    @Override
    public void onNext(String resulte, String method) {
        Log.e("CombinApiFragment",resulte);
    }

    @Override
    public void onError(ApiException e, String method) {

    }
}
