package com.yy.httputils.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.squareup.haha.perflib.Main;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.httputils.R;
import com.yy.httputils.entity.api.BodyPostApi;
import com.yy.httputils.entity.api.SubjectPostApi;
import com.yy.httputils.entity.api.UploadApi;
import com.yy.httputils.entity.resulte.BaseResultEntity;
import com.yy.httputils.entity.resulte.SubjectResulte;
import com.yy.httputils.entity.resulte.UploadResulte;
import com.yy.yhttputils.downlaod.DownInfo;
import com.yy.yhttputils.downlaod.DownState;
import com.yy.yhttputils.downlaod.HttpDownManager;
import com.yy.yhttputils.exception.ApiException;
import com.yy.yhttputils.http.HttpManager;
import com.yy.yhttputils.http.HttpsManager;
import com.yy.yhttputils.listener.HttpDownOnNextListener;
import com.yy.yhttputils.listener.HttpOnNextListener;

import java.io.File;
import java.util.ArrayList;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yy.yhttputils.listener.UploadProgressListener;
import com.yy.yhttputils.upload.ProgressRequestBody;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class MainActivity extends RxAppCompatActivity implements View.OnClickListener, HttpOnNextListener {
    private TextView tvMsg;
    private NumberProgressBar progressBar;
    private ImageView img;
    //    公用一个HttpManager
//    private HttpsManager manager;

    private HttpDownManager downloadManager;
    //    post请求接口信息
    private SubjectPostApi postEntity;
    //    上传接口信息
    private UploadApi uplaodApi;

    private DownInfo apkApi;


    private BodyPostApi bodyPostApi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
//        manager = new HttpsManager();


//         /*初始化数据*/
//        manager = HttpManager.getInstance()
//                .setContext(this)
//                .setLifecycle(this)
//                .setOnNextListener(this);

        downloadManager = HttpDownManager.getInstance();
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        findViewById(R.id.btn_rx).setOnClickListener(this);
        findViewById(R.id.btn_rx_databinding).setOnClickListener(this);
        findViewById(R.id.btn_rx_mu_down).setOnClickListener(this);
        findViewById(R.id.btn_rx_uploade).setOnClickListener(this);
        findViewById(R.id.btn_rx_mu_fr).setOnClickListener(this);
        findViewById(R.id.btn_rx_mu_down_pause).setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
        progressBar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        postEntity = new SubjectPostApi();
        postEntity.setAll(true);

        /*上传接口内部接口有token验证，所以需要换成自己的接口测试，检查file文件是否手机存在*/
        uplaodApi = new UploadApi();
        File file = new File("/storage/emulated/0/Download/11.jpg");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file_name", file.getName(), new ProgressRequestBody
                (requestBody, new UploadProgressListener() {
                    @Override
                    public void onProgress(final long currentBytesCount, final long totalBytesCount) {
                        /*回到主线程中，可通过timer等延迟或者循环避免快速刷新数据*/
                        Observable.just(currentBytesCount)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {

                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        tvMsg.setText("提示:上传中");
                                        progressBar.setMax((int) totalBytesCount);
                                        progressBar.setProgress((int) currentBytesCount);
                                    }
                                });
                    }
                }));
        uplaodApi.setPart(part);


        bodyPostApi = new BodyPostApi();

    }



    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(MainActivity.this);
        rxPermission
                .requestEach(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
//                            LogUtils.d(permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                            LogUtils.d(permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
//                            LogUtils.d(permission.name + " is denied.");
                        }
                    }
                });
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rx_databinding:
                Intent intent = new Intent(this, DataBindActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_rx:
//                manager.setOnNextListener(MainActivity.this);
//                manager.doHttpDeal(postEntity);
                HttpsManager.getInstance().setOnNextListener(MainActivity.this)
                        .doHttpDeal(postEntity);
                break;
            case R.id.btn_rx_uploade:
                /** 上传数据 */
//                manager.doHttpDeal(uplaodApi);
                break;
            case R.id.btn_rx_mu_down:
                if(apkApi!=null)
                {
                    if(apkApi.getState()==DownState.PAUSE)
                    {
                        downloadManager.startDown(apkApi);
                    }
                }else{
                    /**下载文件 */
                    File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "test" + (".apk"));
                    apkApi = new DownInfo("http://111.63.135.60/files/1210000036C86DC5/s1.xmcdn.com/apk/MainApp_v6.3.9.3_c133_release_proguard_170703_and-a1.apk");
                    apkApi.setState(DownState.START);
                    apkApi.setSavePath(outputFile.getAbsolutePath());
                    apkApi.setListener(httpProgressOnNextListener);
                    downloadManager.startDown(apkApi);
                }
                break;
            case R.id.btn_rx_mu_down_pause:
                    if(apkApi!=null)
                    {
                        downloadManager.pause(apkApi);
                    }
                break;
            case R.id.btn_rx_mu_fr:
                /** fragment 中使用httpmanager */
                Intent intent1 = new Intent(this, FragmentHttpAcitivity.class);
                startActivity(intent1);
                break;
        }
    }


    /*下载回调*/
    HttpDownOnNextListener<DownInfo> httpProgressOnNextListener=new HttpDownOnNextListener<DownInfo>() {
        @Override
        public void onNext(DownInfo baseDownEntity) {
            tvMsg.setText("提示：下载完成");
            Toast.makeText(MainActivity.this,baseDownEntity.getSavePath(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart() {
            tvMsg.setText("提示:开始下载");
        }

        @Override
        public void onComplete(DownInfo info) {
            tvMsg.setText("提示：下载结束");
            progressBar.setMax((int) info.getCountLength());
            progressBar.setProgress((int) info.getCountLength());
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            tvMsg.setText("失败:"+e.toString());
        }


        @Override
        public void onPuase() {
            super.onPuase();
            tvMsg.setText("提示:暂停");
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            LogUtils.i("提示:下载中  readLength:"+readLength+"--countLength:"+countLength);
            tvMsg.setText("提示:下载中  readLength:"+readLength+"--countLength:"+countLength);
            progressBar.setMax((int) countLength);
            progressBar.setProgress((int) readLength);
        }
    };






    @Override
    public void onNext(String resulte, String mothead) {

        /*post返回处理*/
        if (mothead.equals(postEntity.getMethod())) {
//            BaseResultEntity<ArrayList<SubjectResulte>> subjectResulte = JSONObject.parseObject(resulte, new
//                    TypeReference<BaseResultEntity<ArrayList<SubjectResulte>>>() {
//                    });
            Gson gson = new Gson();
            BaseResultEntity<ArrayList<SubjectResulte>> subjectResulte = gson.fromJson(resulte,BaseResultEntity.class);

            tvMsg.setText("post返回：\n" + subjectResulte.getData().toString());
        }

        /*上传返回处理*/
        if (mothead.equals(uplaodApi.getMethod())) {
//            BaseResultEntity<UploadResulte> subjectResulte = JSONObject.parseObject(resulte, new
//                    TypeReference<BaseResultEntity<UploadResulte>>() {
//                    });
            Gson gson = new Gson();
            BaseResultEntity<UploadResulte> subjectResulte = gson.fromJson(resulte,BaseResultEntity.class);

            UploadResulte uploadResulte = subjectResulte.getData();
            tvMsg.setText("上传成功返回：\n" + uploadResulte.getHeadImgUrl());
            Glide.with(MainActivity.this).load(uploadResulte.getHeadImgUrl()).skipMemoryCache(true).into(img);
        }
    }

    @Override
    public void onError(ApiException e, String method) {
        tvMsg.setText("失败："+method+"\ncode=" + e.getCode() + "\nmsg:" + e.getDisplayMessage());
    }

}
