package com.yy.httputils.framework;

import java.util.List;

/**
 * Created by ly on 2017/12/27.
 */

public interface BaseLoadListener<T>{


    void loadSuccess(T t);

    /**
     * 加载数据成功
     *
     * @param list
     */
    void loadSuccessForList(List<T> list);

    /**
     * 加载失败
     *
     * @param message
     */
    void loadFailure(String message);

    /**
     * 开始加载
     */
    void loadStart();

    /**
     * 加载结束
     */
    void loadComplete();
}
