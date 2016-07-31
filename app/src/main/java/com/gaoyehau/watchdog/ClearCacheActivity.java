package com.gaoyehau.watchdog;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.gaoyehua.Fragment.cacheFragment;
import com.gaoyehua.Fragment.sdFragment;

/**
 * Created by gaoyehua on 2016/7/30.
 *
 *功能：手机清理缓存
 */

public class ClearCacheActivity extends FragmentActivity {

    private cacheFragment cachefragment;
    private sdFragment sdfragment;
    private FragmentManager fragmentManager;
    //如果要对fragment进行管理操作,activity必须继承fragmentActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clearcache);

        //挂载Fragment
        //1.获取Fragment
        cachefragment =new cacheFragment();
        sdfragment =new sdFragment();
        //2.加载Fragment
        //2.1开启Fragment管理者
        fragmentManager =getFragmentManager();
        //2.2添加事物
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        //2.3添加fragment操作
        //数据不会重新刷新
        //参数1:fragment要替换的控件的id
        //参数2:要替换fragment

        transaction.add(R.id.fram_clearcache_fragment,cachefragment);
        //界面展示以最后一个添加的为准
        transaction.add(R.id.fram_clearcache_fragment,sdfragment);
        //隐藏fragment操作
        transaction.hide(sdfragment);
        //也可以实现替换操作,数据会重新刷新
       // transaction.replace(R.id.fram_clearcache_fragment,cachefragment);
        transaction.commit();
    }
    //缓存清理
    public void cache(View v){
        //隐藏sdfragment,显示cacheFragment
        //1.获取事务
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        //2.隐藏显示操作
        beginTransaction.hide(sdfragment);//隐藏fragmnt
        beginTransaction.show(cachefragment);//显示fragment操作
        //3.提交
        beginTransaction.commit();
    }
    //SD清理
    public void sd(View v){
        //隐藏sdfragment,显示cacheFragment
        //1.获取事务
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        //2.隐藏显示操作
        beginTransaction.hide(cachefragment);//隐藏fragmnt
        beginTransaction.show(sdfragment);//显示fragment操作
        //3.提交
        beginTransaction.commit();
    }
}
