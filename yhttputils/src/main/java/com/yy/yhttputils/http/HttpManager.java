package com.yy.yhttputils.http;

import com.yy.yhttputils.RxRetrofitApp;
import com.yy.yhttputils.api.BaseApi;
import com.yy.yhttputils.base.BaseProgress;
import com.yy.yhttputils.enums.NetType;
import com.yy.yhttputils.framework.HttpInterface;
import com.yy.yhttputils.listener.HttpOnNextListener;

import java.io.InputStream;

/**
 * Created by ly on 2018/6/13.
 */

public class HttpManager {
    private static HttpManager httpManager;
    private HttpInterface httpTools;

    public static HttpManager getInstance(){
        if(httpManager ==null)
        {
            httpManager = new HttpManager();
        }
        return httpManager;
    }
    public HttpManager(){
        if(RxRetrofitApp.getRxRetrofitApp().getNetType()== NetType.NETTYPE_HTTP)
        {
            httpTools = new HttpUtils();
        }else{
            httpTools = new HttpsUtils();
        }
    }


    /**
     * 设置回调
     * @param onNextListener
     * @return
     */
    public HttpManager setOnNextListener(HttpOnNextListener onNextListener) {
        httpTools.setOnNextListener(onNextListener);
        return httpManager;
    }

    /**
     * 执行api
     * @param basePar
     * @return
     */
    public void doHttpDeal(BaseApi basePar) {
        httpTools.doHttpDeal(basePar);
    }

    /**
     * 设置对话框
     * @param baseProgress
     * @return
     */
    public HttpManager setBaseProgress(BaseProgress baseProgress) {
        httpTools.setBaseProgress(baseProgress);
        return httpManager;
    }


    /**
     * 设置证书流
     * @param certificates
     */
    public void setCertificate(InputStream... certificates) {
        httpTools.setCertificate(certificates);
    }

    public void release(){
        httpTools.release();
    }





}
