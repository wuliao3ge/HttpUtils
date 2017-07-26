package com.yy.YHttpUtils.listener.upload;

/**
 * 上传进度回调类
 * Created by ly on 17-7-26.
 */

public interface UploadProgressListener {
    /**
     * 上传进度
     * 手动回调到主线程中
     * @param currentBytesCount
     * @param totalBytesCount
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}