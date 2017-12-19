package com.yy.YHttpUtils.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

/**
 * Created by ly on 2017/7/28.
 */

public abstract class BaseProgress extends Dialog {


    public BaseProgress(@NonNull Context context) {
        super(context);
    }

    public BaseProgress(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public abstract void dismissProgress();
    public abstract void setProgressTitle(String s);
    public abstract void setProgressTitle(int sid);
    public abstract void setProgressMessage(String s);
    public abstract void setProgressCancelable(boolean flag);
    public abstract void setProgressOnCancelListener(OnCancelListener onCancelListener);
    public abstract boolean isProgressShowing();
    public abstract void progressShow();
    public abstract void onCance();

}
