package com.yy.yhttputils.subscribers;


import android.util.Log;

import com.yy.yhttputils.downlaod.DownInfo;
import com.yy.yhttputils.downlaod.DownLoadListener.DownloadProgressListener;
import com.yy.yhttputils.downlaod.DownState;
import com.yy.yhttputils.downlaod.HttpDownManager;
import com.yy.yhttputils.listener.HttpDownOnNextListener;

import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 断点下载处理类Subscriber
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by ly on 17-7-26.
 */
public class ProgressDownSubscriber<T> implements Observer<T> , DownloadProgressListener {
    //弱引用结果回调
    private SoftReference<HttpDownOnNextListener> mSubscriberOnNextListener;
    /*下载数据*/
    private DownInfo downInfo;

    private Disposable mDisposable;


    public ProgressDownSubscriber(DownInfo downInfo) {
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo=downInfo;
    }


    public void setDownInfo(DownInfo downInfo) {
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo=downInfo;
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.mDisposable = d;
        if(mSubscriberOnNextListener.get()!=null){
            mSubscriberOnNextListener.get().onStart();
        }
        downInfo.setState(DownState.START);
    }


    public void unsubscribe(){
        if(!this.mDisposable.isDisposed())
        {
            this.mDisposable.dispose();
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
        if(mSubscriberOnNextListener.get()!=null){
            mSubscriberOnNextListener.get().onError(e);
        }
        HttpDownManager.getInstance().remove(downInfo);
        downInfo.setState(DownState.ERROR);
    }


    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        downInfo.setState(DownState.FINISH);
        if(mSubscriberOnNextListener.get()!=null){
            mSubscriberOnNextListener.get().onComplete(downInfo);
        }
        HttpDownManager.getInstance().remove(downInfo);
//        DbDwonUtil.getInstance().update(downInfo);
    }



    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }

    @Override
    public void update(long read, final long count, boolean done) {
//        Log.i("apdate1 "," readLength:"+read+"--countLength:"+count);
        if(downInfo.getCountLength()>count){
            read=downInfo.getCountLength()-count+read;
        }else{
            downInfo.setCountLength(count);
        }
        downInfo.setReadLength(read);
        if (mSubscriberOnNextListener.get() != null) {
            /*接受进度消息，造成UI阻塞，如果不需要显示进度可去掉实现逻辑，减少压力*/
            Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                             /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
//                            Log.i("apdate2 "," readLength:"+aLong+"--countLength:"+count);
                             if(downInfo.getState()== DownState.PAUSE||downInfo.getState()== DownState.STOP)
                                 return;
                            downInfo.setState(DownState.DOWN);
//                            Log.i("apdate3 "," readLength:"+aLong+"--countLength:"+count);
                            mSubscriberOnNextListener.get().updateProgress(aLong,count);
                        }
                    });
        }
    }

}