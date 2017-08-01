package com.yy.YHttpUtils.Base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.YHttpUtils.R;
import com.yy.YHttpUtils.exception.ApiException;
import com.yy.YHttpUtils.listener.HttpOnNextListener;

public class YRxBaseActivity extends RxAppCompatActivity implements HttpOnNextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNext(String resulte, String method) {

    }

    @Override
    public void onError(ApiException e, String method) {

    }
}
