package com.yy.httputils.activity;

import android.os.Bundle;

import com.yy.httputils.R;
import com.yy.httputils.model.datamodel.DataModel1;
import com.yy.yhttputils.base.HttpBaseActivity;

public class Main2Activity extends HttpBaseActivity<DataModel1> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
