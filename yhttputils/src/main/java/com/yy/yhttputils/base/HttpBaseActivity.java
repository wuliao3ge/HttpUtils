package com.yy.yhttputils.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.yhttputils.http.HttpManager;
/**
 * Created by ly on 18-2-24.
 */

public class HttpBaseActivity<T extends HttpBaseModel> extends RxAppCompatActivity{
    protected Context mContext;
    protected HttpManager httpManager;
    protected T dataModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        httpManager = new HttpManager()
                .setContext(this)
                .setLifecycle(this);
        dataModel.setHttpManager(httpManager);
    }

//    public <T extends HttpBaseModel> void initDataModel(T... arg){
//            for(int i =0;i<arg.length;i++)
//            {
//                arg[i].setHttpManager(httpManager);
//            }
//    }
}
