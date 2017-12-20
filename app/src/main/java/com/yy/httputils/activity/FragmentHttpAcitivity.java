package com.yy.httputils.activity;



import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yy.httputils.R;
import com.yy.httputils.activity.fragment.CombinApiFragment;

public class FragmentHttpAcitivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_acitivity);

        //步骤一：添加一个FragmentTransaction的实例
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //步骤二：用add()方法加上Fragment的对象rightFragment
        CombinApiFragment rightFragment = new CombinApiFragment();
        transaction.add(R.id.right, rightFragment,"aaaa");

        //步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
        transaction.commit();

    }
}
