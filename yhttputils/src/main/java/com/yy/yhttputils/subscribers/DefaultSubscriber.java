package com.yy.yhttputils.subscribers;



import android.util.Log;

import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.exception.CodeException;
import com.yy.yhttputils.exception.HttpTimeException;
import com.yy.yhttputils.http.DefaultHttpListener;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 调用者自己对请求数据进行处理
 */

public class DefaultSubscriber<T> implements Observer<T> {
    //    回调接口
    private DefaultHttpListener defaultHttpListener;
    /*请求数据*/
    private BaseApi api;

    private Disposable mDisposable;

    /**
     * 构造
     *
     * @param api
     */
    public DefaultSubscriber(BaseApi api, DefaultHttpListener  defaultHttpListener) {
        this.api = api;
        this.defaultHttpListener = defaultHttpListener;
        if(defaultHttpListener!=null)
        {
            Log.d("init", "DefaultSubscriber: 成功 ");
        }else{
            Log.d("init","DefaultSubscriber: 失败 ");
        }
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        errorDo(e);
    }

    /**
     * 加载网络数据结束
     */
    @Override
    public void onComplete() {
        DefaultHttpListener httpListener = defaultHttpListener;
        if (httpListener == null) return;
        httpListener.onComplete();
    }

    /**
     * 错误统一处理
     *
     * @param e
     */
    private void errorDo(Throwable e) {
        DefaultHttpListener httpListener = defaultHttpListener;
        if (httpListener == null) return;
        if (e instanceof ApiException) {
            httpListener.onError((ApiException) e,api.getMethod());
        } else if (e instanceof HttpTimeException) {
            HttpTimeException exception = (HttpTimeException) e;
            httpListener.onError(new ApiException(exception, CodeException.RUNTIME_ERROR, exception.getMessage()),api.getMethod());
        } else {
            httpListener.onError(new ApiException(e, CodeException.UNKNOWN_ERROR, e.getMessage()),api.getMethod());
        }
    }



    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.mDisposable = d;
        DefaultHttpListener httpListener = defaultHttpListener;
        if (httpListener == null) return;
        httpListener.onStart();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        DefaultHttpListener httpListener = defaultHttpListener;
        if(httpListener!=null)
        {
            defaultHttpListener.onNext((String) t, api.getMethod());
            Log.i("onNext","成功");
        }else {
            Log.i("onNext","失败");
        }
    }
}
