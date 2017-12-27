package com.yy.httputils.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.httputils.R;
import com.yy.httputils.databinding.ActivityDataBindBinding;
import com.yy.httputils.model.DataViewModel;
import com.yy.httputils.model.GetDataModel;

public class DataBindActivity extends RxAppCompatActivity {

    private ActivityDataBindBinding binding;

    private DataViewModel dataViewModel;

    private GetDataModel getDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_data_bind);

        getDataModel = new GetDataModel(this,this);
        dataViewModel = new DataViewModel(getDataModel);
        binding.setModel(dataViewModel);
    }
}
