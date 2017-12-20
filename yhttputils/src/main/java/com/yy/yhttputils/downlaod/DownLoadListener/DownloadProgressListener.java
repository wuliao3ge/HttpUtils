package com.yy.yhttputils.downlaod.DownLoadListener;


/**
 * 成功回调处理
 * Created by ly on 17-7-26.
 */
public interface DownloadProgressListener {
    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}
