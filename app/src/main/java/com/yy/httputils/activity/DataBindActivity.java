package com.yy.httputils.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.httputils.R;
import com.yy.httputils.databinding.ActivityDataBindBinding;
import com.yy.httputils.model.DataViewModel;
import com.yy.httputils.model.datamodel.GetDataModel;
import com.yy.yhttputils.base.HttpBaseActivity;

public class DataBindActivity extends HttpBaseActivity<GetDataModel> {

    private ActivityDataBindBinding binding;

    private DataViewModel dataViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_data_bind);
        dataViewModel = new DataViewModel();
        dataViewModel.setGetDataModel(dataModel);
        binding.setModel(dataViewModel);
    }
}
