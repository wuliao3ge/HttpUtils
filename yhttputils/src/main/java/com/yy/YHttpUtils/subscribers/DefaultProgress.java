package com.yy.YHttpUtils.subscribers;

import android.app.ProgressDialog;
import android.content.Context;

import com.yy.YHttpUtils.base.BaseProgress;

/**
 * Created by ly on 2017/7/28.
 */

public class DefaultProgress extends BaseProgress {

    private ProgressDialog pd;

    public DefaultProgress(Context context) {
        super(context);
        pd = new ProgressDialog(context);
    }

    @Override
    public void dismissProgress() {
        pd.dismiss();
    }
    @Override
    public void setProgressTitle(String s) {
        pd.setTitle(s);
    }
    @Override
    public void setProgressTitle(int sid) {
        pd.setTitle(sid);
    }
    @Override
    public void setProgressMessage(String s) {
        pd.setMessage(s);
    }
    @Override
    public void setProgressCancelable(boolean flag) {
        pd.setCancelable(flag);
    }
    @Override
    public void setProgressOnCancelListener(OnCancelListener onCancelListener) {
        pd.setOnCancelListener(onCancelListener);
    }
    @Override
    public boolean isProgressShowing() {
        return pd.isShowing();
    }
    @Override
    public void progressShow() {
        pd.show();
    }
    @Override
    public void onCance() {
    }
}
