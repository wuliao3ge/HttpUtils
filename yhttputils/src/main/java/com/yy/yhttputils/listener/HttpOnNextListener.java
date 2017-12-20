package com.yy.yhttputils.listener;


import com.yy.yhttputils.exception.ApiException;

/**
 * 成功回调处理
 * Created by ly on 17-7-26.
 */
public interface HttpOnNextListener {
    /**
     * 成功后回调方法
     *
     * @param resulte
     * @param method
     */
    void onNext(String resulte, String method);

    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     *
     * @param e
     * @param method
     */
    void onError(ApiException e, String method);
}
