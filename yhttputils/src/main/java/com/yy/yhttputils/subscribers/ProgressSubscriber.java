package com.yy.yhttputils.subscribers;


import android.content.Context;
import android.content.DialogInterface;


import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.base.BaseProgress;
import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.exception.CodeException;
import com.yy.yhttputils.exception.HttpTimeException;
//import com.yy.YHttpUtils.http.cookie.CookieResulte;
import com.yy.yhttputils.listener.HttpOnNextListener;


import java.lang.ref.SoftReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by ly on 17-7-26.
 */
public class ProgressSubscriber<T> implements Observer<T> {
    /*是否弹框*/
    private boolean showPorgress = true;
    //    回调接口
    private HttpOnNextListener mSubscriberOnNextListener;

    //    加载框可自己定义
    private BaseProgress pd;
    /*请求数据*/
    private BaseApi api;

    private Disposable mDisposable;

    /**
     * 构造
     *
     * @param api
     */
    public ProgressSubscriber(BaseApi api,HttpOnNextListener  listenerSoftReference) {
        this.api = api;
        this.pd = api.getProgress();
        this.mSubscriberOnNextListener = listenerSoftReference;
        setShowPorgress(api.isShowProgress());
        if (api.isShowProgress()) {
            initProgressDialog(api.isCancel());
        }
    }


    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel) {

//        if (pd == null && context != null) {
//            pd = new DefaultProgress(context);
//        }
//        pd.setProgressMessage(api.getProgressMassge());
//        pd.setCancelable(cancel);
//        if (cancel) {
//            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialogInterface) {
//                    onCancelProgress();
//                    pd.onCance();
//                }
//            });
//        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
//        if (!isShowPorgress()) return;
//        Context context = mActivity.get();
//        if (pd == null || context == null) return;
//        if (!pd.isProgressShowing()) {
//            pd.progressShow();
//        }
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
//        if (!isShowPorgress()) return;
//        if (pd != null && pd.isProgressShowing()) {
//            pd.dismissProgress();
//        }
    }


    public boolean isShowPorgress() {
        return showPorgress;
    }

    /**
     * 是否需要弹框设置
     *
     * @param showPorgress
     */
    public void setShowPorgress(boolean showPorgress) {
        this.showPorgress = showPorgress;
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }


    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        dismissProgressDialog();
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
        dismissProgressDialog();
    }


    /**
     * 获取cache数据
     */
    private void getCache() {

    }


    /**
     * 错误统一处理
     *
     * @param e
     */
    private void errorDo(Throwable e) {
        HttpOnNextListener httpOnNextListener = mSubscriberOnNextListener;
        if (httpOnNextListener == null) return;
        if (e instanceof ApiException) {
            httpOnNextListener.onError((ApiException) e,api.getMethod());
        } else if (e instanceof HttpTimeException) {
            HttpTimeException exception = (HttpTimeException) e;
            httpOnNextListener.onError(new ApiException(exception, CodeException.RUNTIME_ERROR, exception.getMessage()),api.getMethod());
        } else {
            httpOnNextListener.onError(new ApiException(e, CodeException.UNKNOWN_ERROR, e.getMessage()),api.getMethod());
        }
    }



    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.mDisposable = d;
        showProgressDialog();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext((String) t, api.getMethod());
        }
    }

}