package com.yy.httputils.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.yy.httputils.BR;
import com.yy.httputils.framework.BaseLoadListener;
import com.yy.httputils.model.datamodel.GetDataModel;
import com.yy.yhttputils.base.HttpBaseModel;

import java.util.List;

/**
 * Created by ly on 2017/12/26.
 */

public class DataViewModel extends BaseObservable implements BaseLoadListener<String>{
    private String data;
//    private T getDataModel;
    private GetDataModel getDataModel;

    public DataViewModel(GetDataModel getDataModel) {
        this.getDataModel = getDataModel;
        getDataModel.setBaseLoadListener(this);
//        ((GetDataModel)getDataModel).setBaseLoadListener(this);
//        getDataModel.setDataViewModel(this);
    }

    public void onClick(){
        getDataModel.getData();
    }

    @Bindable
    public String getData() {
        return data;
    }

    public void setData(String data) {
       this.data =data;
        notifyPropertyChanged(BR.data);
    }

    @Override
    public void loadSuccess(String s) {
            setData(s);
    }

    @Override
    public void loadSuccessForList(List<String> list) {

    }

    @Override
    public void loadFailure(String message) {

    }

    @Override
    public void loadStart() {

    }

    @Override
    public void loadComplete() {

    }
}
