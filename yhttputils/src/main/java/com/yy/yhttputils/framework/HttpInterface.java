package com.yy.yhttputils.framework;

import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.base.BaseProgress;
import com.yy.yhttputils.listener.HttpOnNextListener;

import java.io.InputStream;

/**
 * Created by ly on 2018/6/13.
 */

public interface HttpInterface {
    void setOnNextListener(HttpOnNextListener onNextListener);
    void setCertificate(InputStream... certificates);
    void doHttpDeal(BaseApi basePar);
    void setBaseProgress(BaseProgress baseProgress);
    void release();
}
