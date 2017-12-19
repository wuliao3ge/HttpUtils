package com.yy.YHttpUtils.subscribers;


import android.content.Context;
import android.content.DialogInterface;


import com.yy.YHttpUtils.api.BaseApi;
import com.yy.YHttpUtils.RxRetrofitApp;
import com.yy.YHttpUtils.base.BaseProgress;
import com.yy.YHttpUtils.exception.ApiException;
import com.yy.YHttpUtils.exception.CodeException;
import com.yy.YHttpUtils.exception.HttpTimeException;
//import com.yy.YHttpUtils.http.cookie.CookieResulte;
import com.yy.YHttpUtils.listener.HttpOnNextListener;


import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


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
    private SoftReference<HttpOnNextListener> mSubscriberOnNextListener;
    //    软引用反正内存泄露
    private SoftReference<Context> mActivity;
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
    public ProgressSubscriber(BaseApi api,SoftReference<HttpOnNextListener>  listenerSoftReference, SoftReference<Context>
            mActivity) {
        this.api = api;
        this.pd = api.getProgress();
        this.mSubscriberOnNextListener = listenerSoftReference;
        this.mActivity = mActivity;
        setShowPorgress(api.isShowProgress());
        if (api.isShowProgress()) {
            initProgressDialog(api.isCancel());
        }
    }


    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel) {
        Context context = mActivity.get();
        if (pd == null && context != null) {
            pd = new DefaultProgress(context);
        }
        pd.setProgressMessage(api.getProgressMassge());
        pd.setCancelable(cancel);
        if (cancel) {
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    onCancelProgress();
                    pd.onCance();
                }
            });
        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!isShowPorgress()) return;
        Context context = mActivity.get();
        if (pd == null || context == null) return;
        if (!pd.isProgressShowing()) {
            pd.progressShow();
        }
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!isShowPorgress()) return;
        if (pd != null && pd.isProgressShowing()) {
            pd.dismissProgress();
        }
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
        /*需要緩存并且本地有缓存才返回*/
//        if (api.isCache()) {
//            getCache();
//        } else {
            errorDo(e);
//        }
        dismissProgressDialog();
    }


    /**
     * 获取cache数据
     */
    private void getCache() {
//        Observable.just(api.getUrl()).subscribe(new Consumer<String>() {
//
//
//            @Override
//            public void accept(String s) throws Exception {
//                           /*获取缓存数据*/
//                CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(s);
//                if (cookieResulte == null) {
//                    throw new HttpTimeException(HttpTimeException.NO_CHACHE_ERROR);
//                }
//                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
//                if (time < api.getCookieNoNetWorkTime()) {
//                    if (mSubscriberOnNextListener.get() != null) {
//                        mSubscriberOnNextListener.get().onNext(cookieResulte.getResulte(), api.getMethod());
//                    }
//                } else {
//                    CookieDbUtil.getInstance().deleteCookie(cookieResulte);
//                    throw new HttpTimeException(HttpTimeException.CHACHE_TIMEOUT_ERROR);
//                }
//            }
//        });
    }


    /**
     * 错误统一处理
     *
     * @param e
     */
    private void errorDo(Throwable e) {
        Context context = mActivity.get();
        if (context == null) return;
        HttpOnNextListener httpOnNextListener = mSubscriberOnNextListener.get();
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

//        /*缓存并且有网*/
//        if (api.isCache() && AppUtil.isNetworkAvailable(RxRetrofitApp.getApplication())) {
//             /*获取缓存数据*/
//            CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrl());
//            if (cookieResulte != null) {
//                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
//                if (time < api.getCookieNetWorkTime()) {
//                    if (mSubscriberOnNextListener.get() != null) {
//                        mSubscriberOnNextListener.get().onNext(cookieResulte.getResulte(), api.getMethod());
//                    }
//                    onComplete();
//                    mDisposable.dispose();
//                }
//            }
//        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
//         /*缓存处理*/
//        if (api.isCache()) {
//            CookieResulte resulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrl());
//            long time = System.currentTimeMillis();
//            /*保存和更新本地数据*/
//            if (resulte == null) {
//                resulte = new CookieResulte(api.getUrl(), t.toString(), time);
//                CookieDbUtil.getInstance().saveCookie(resulte);
//            } else {
//                resulte.setResulte(t.toString());
//                resulte.setTime(time);
//                CookieDbUtil.getInstance().updateCookie(resulte);
//            }
//        }
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.get().onNext((String) t, api.getMethod());
        }
    }

}