package com.yy.HttpUtils.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;


import com.jude.easyrecyclerview.EasyRecyclerView;
import com.yy.HttpUtils.R;
import com.yy.HttpUtils.activity.adapter.DownAdapter;
import com.yy.YHttpUtils.downlaod.DownInfo;
import com.yy.YHttpUtils.downlaod.DownState;
import com.yy.YHttpUtils.utils.DbDwonUtil;

import java.io.File;
import java.util.List;

/**
 * 多任務下載
 */
public class DownLaodActivity extends AppCompatActivity {
    List<DownInfo> listData;
    DbDwonUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_laod);
        dbUtil = DbDwonUtil.getInstance();
        dbUtil.deleteDowninfoAll();
        initResource();
        initWidget();


    }

    /*数据*/
    private void initResource() {

        listData = dbUtil.queryDownAll();
        /*第一次模拟服务器返回数据掺入到数据库中*/
        if (listData.isEmpty()) {
            String[] downUrl = new String[]{"http://111.63.135.60/files/1210000036C86DC5/s1.xmcdn.com/apk/MainApp_v6.3.9.3_c133_release_proguard_170703_and-a1.apk",
                    "http://111.63.135.60/files/1210000036C86DC5/s1.xmcdn.com/apk/MainApp_v6.3.9.3_c133_release_proguard_170703_and-a1.apk",
                    "http://111.63.135.60/files/1210000036C86DC5/s1.xmcdn.com/apk/MainApp_v6.3.9.3_c133_release_proguard_170703_and-a1.apk",
                    "http://111.63.135.60/files/1210000036C86DC5/s1.xmcdn.com/apk/MainApp_v6.3.9.3_c133_release_proguard_170703_and-a1.apk"};
            for (int i = 0; i < downUrl.length; i++) {
                File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "test" + i + (i==0?".pdf":".apk"));
                DownInfo apkApi = new DownInfo(downUrl[i]);
                apkApi.setId(i);
                apkApi.setState(DownState.START);
                apkApi.setSavePath(outputFile.getAbsolutePath());
                dbUtil.save(apkApi);
            }
            listData = dbUtil.queryDownAll();
        }
    }

    /*加载控件*/
    private void initWidget() {
        EasyRecyclerView recyclerView = (EasyRecyclerView) findViewById(R.id.rv);
        DownAdapter adapter = new DownAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.addAll(listData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*记录退出时下载任务的状态-复原用*/
        for (DownInfo downInfo : listData) {
            dbUtil.update(downInfo);
        }
    }
}
