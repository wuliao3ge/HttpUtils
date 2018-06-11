package com.yy.yhttputils.http;

import com.yy.yhttputils.exception.ApiException;

/**
 * Created by ly on 2018/3/10.
 */

public interface DefaultHttpListener {
    /**
     * 开始加载
     */
    void onStart();
    /**
     * 加载结束
     */
    void onComplete();
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
     * @param e
     * @param method
     */
    void onError(ApiException e, String method);
}
