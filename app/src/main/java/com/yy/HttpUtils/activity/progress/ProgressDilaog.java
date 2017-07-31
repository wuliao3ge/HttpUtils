//package com.yy.HttpUtils.activity.progress;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.Gravity;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import com.yy.HttpUtils.R;
//import com.yy.YHttpUtils.subscribers.ProgressAbs;
//
///**
// * Created by ly on 2017/7/28.
// */
//
//public class ProgressDilaog extends ProgressAbs {
//
//    private static CustomProgressDialog dialog;
//
//    public ProgressDilaog(Context context) {
//        super(context);
//        dialog = new CustomProgressDialog(context);
//    }
//
//    @Override
//    protected void dismiss() {
//        if(dialog != null && dialog.isShowing()){
//            dialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void setTitle(String s) {
//
//    }
//
//    @Override
//    protected void setTitle(int sid) {
//
//    }
//
//    @Override
//    protected void setMessage(String s) {
//        dialog.setMessage(s);
//    }
//
//    @Override
//    protected void setCancelable(boolean flag) {
//        dialog.setCancelable(flag);
//    }
//
//    @Override
//    protected void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
//        // 监听返回键处理
//        dialog.setOnCancelListener(onCancelListener);
//    }
//
//    @Override
//    protected boolean isShowing() {
//        return false;
//    }
//
//    @Override
//    protected void show() {
//        // 设置居中
//        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        // 设置背景层透明度
//        lp.dimAmount = 0.1f;
//        dialog.getWindow().setAttributes(lp);
//        dialog.show();
//    }
//
//    @Override
//    protected void onCance() {
//
//    }
//}
