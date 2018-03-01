package com.yy.yhttputils.base;

import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.http.HttpManager;
import com.yy.yhttputils.listener.HttpOnNextListener;

/**
 * Created by ly on 18-2-24.
 */

public abstract class HttpBaseModel implements HttpOnNextListener {
    protected HttpManager httpManager;
    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }
    @Override
    public abstract void onNext(String resulte, String method);

    @Override
    public abstract void onError(ApiException e, String method);
}
