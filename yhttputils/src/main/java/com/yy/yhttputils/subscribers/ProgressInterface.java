package com.yy.yhttputils.subscribers;

import android.content.DialogInterface;

/**
 * Created by ly on 2017/7/28.
 */

public interface ProgressInterface {
    void dismissProgress();
    void setProgressTitle(String s);
    void setProgressTitle(int sid);
    void setProgressMessage(String s);
    void setProgressCancelable(boolean flag);
    void setProgressOnCancelListener(DialogInterface.OnCancelListener onCancelListener);
    boolean isProgressShowing();
    void Progressshow();
    void onCance();
}
