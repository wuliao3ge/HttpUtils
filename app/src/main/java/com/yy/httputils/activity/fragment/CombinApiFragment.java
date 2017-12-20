package com.yy.httputils.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yy.httputils.R;
import com.yy.httputils.model.GetDataModel;
import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.listener.HttpOnNextListener;

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

        getDataModel = new GetDataModel(getActivity(),this);

        getDataModel.onClick();
    }

    @Override
    public void onNext(String resulte, String method) {
        Log.e("CombinApiFragment",resulte);
    }

    @Override
    public void onError(ApiException e, String method) {

    }
}
