package com.yy.httputils.activity.progress;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yy.httputils.R;
import com.yy.yhttputils.base.BaseProgress;

/**
 * Created by ly on 2017/7/28.
 */

public class CustomProgressDialog extends BaseProgress {
    private static CustomProgressDialog dialog;
    private static String message;
    private static Context mContext;


    public CustomProgressDialog() {
        super();
    }

    public CustomProgressDialog(int themeResId) {
        super(themeResId);
    }

    public CustomProgressDialog(Context context) {
        super(context);
        this.mContext = context;

    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }




    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(R.style.Custom_Progress);
        }
        dialog.setContentView(R.layout.layout_progress_dialog);

        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.tv_message).setVisibility(View.GONE);
        } else {
            TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
            tv_message.setText(message);
        }
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.1f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        Log.i("dialog","show");
    }

    @Override
    public void onCance() {

    }


    public void setProgressMessage(String s) {
        message = s;
    }

    @Override
    public void setProgressCancelable(boolean flag) {

    }

    @Override
    public void setProgressOnCancelListener(OnCancelListener onCancelListener) {

    }


    public void dismissProgress() {
        dialog.dismiss();
        Log.i("dialog","dismiss");
    }

    @Override
    public void setProgressTitle(String s) {

    }

    @Override
    public void setProgressTitle(int sid) {

    }


    public boolean isProgressShowing() {
        if(dialog!=null)
        {
            return dialog.isShowing();
        }
        return false;
    }
}
