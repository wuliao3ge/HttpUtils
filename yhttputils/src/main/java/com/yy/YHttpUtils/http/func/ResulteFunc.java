package com.yy.YHttpUtils.http.func;


import com.yy.YHttpUtils.exception.HttpTimeException;

import io.reactivex.functions.Function;

/**
 * 服务器返回数据判断
 * Created by WZG on 2017/3/23.
 */

public class ResulteFunc implements Function<Object,Object> {
    @Override
    public Object apply(Object o) {
        if (o == null || "".equals(o.toString())) {
            throw new HttpTimeException("数据错误");
        }
        return o;
    }
}
