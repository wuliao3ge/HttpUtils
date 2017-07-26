package com.yy.YHttpUtils.listener;


import io.reactivex.Observable;

/**
 * 回调ober对象
 * Created by ly on 17-7-26.
 */

public interface HttpOnNextSubListener {

    /**
     * ober成功回调
     * @param observable
     * @param method
     */
    void onNext(Observable observable, String method);
}
