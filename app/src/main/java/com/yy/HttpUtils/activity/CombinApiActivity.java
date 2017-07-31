package com.yy.HttpUtils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.HttpUtils.R;
import com.yy.HttpUtils.activity.progress.CustomProgressDialog;
import com.yy.HttpUtils.entity.api.CombinApi;
import com.yy.HttpUtils.entity.resulte.BaseResultEntity;
import com.yy.HttpUtils.entity.resulte.SubjectResulte;
import com.yy.YHttpUtils.exception.ApiException;
import com.yy.YHttpUtils.listener.HttpOnNextListener;


import java.util.ArrayList;


/**
 * 统一api类处理方案界面
 *
 * @author wzg
 */
public class CombinApiActivity extends RxAppCompatActivity implements HttpOnNextListener {
    private TextView tvMsg;
    CombinApi api;
    private CustomProgressDialog progressDilaog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combin_api);
        api = new CombinApi(this, this);
//        progressDilaog = new CustomProgressDialog(this);
//        api.setProgress(progressDilaog);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        findViewById(R.id.btn_rx_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.postApi(true);
            }
        });
        findViewById(R.id.btn_rx_all_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.postApiOther(true);
            }
        });
    }

    @Override
    public void onNext(String resulte, String method) {
        BaseResultEntity<ArrayList<SubjectResulte>> subjectResulte = JSONObject.parseObject(resulte, new
                TypeReference<BaseResultEntity<ArrayList<SubjectResulte>>>() {
                });
        tvMsg.setText("统一post返回：\n" + subjectResulte.getData().toString());
    }

    @Override
    public void onError(ApiException e, String method) {
        tvMsg.setText("失败："+method+"\ncode=" + e.getCode() + "\nmsg:" + e.getDisplayMessage());
    }
}
