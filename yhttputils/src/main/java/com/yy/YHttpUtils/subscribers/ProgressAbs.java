package com.yy.YHttpUtils.subscribers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

/**
 * Created by ly on 2017/7/28.
 */

public abstract class ProgressAbs extends Dialog {


    public ProgressAbs(@NonNull Context context) {
        super(context);
    }

    public ProgressAbs(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected abstract void dismissProgress();
    protected abstract void setProgressTitle(String s);
    protected abstract void setProgressTitle(int sid);
    protected abstract void setProgressMessage(String s);
    protected abstract void setProgressCancelable(boolean flag);
    protected abstract void setProgressOnCancelListener(DialogInterface.OnCancelListener onCancelListener);
    protected abstract boolean isProgressShowing();
    protected abstract void Progressshow();
    protected abstract void onCance();

}
