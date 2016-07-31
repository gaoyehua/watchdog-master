package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.gaoyehua.service.AddressService;
import com.gaoyehua.service.WatchDogService;
import com.gaoyehua.ui.SettingClinckView;
import com.gaoyehua.ui.SettingView;
import com.gaoyehua.utils.AddressUtils;

import java.util.AbstractCollection;

/**
 * Created by 高业华 on 2016/7/17.
 */
public class SettingActivity extends Activity {

    private SettingView  sv_setting_update;
    private SettingView  sv_setting_address;
    private SettingClinckView  scv_setting_changedbk;
    private SettingClinckView  scv_setting_location;
    private SharedPreferences sp;
    private SettingView sv_setting_lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //保存文件设置

        sp =getSharedPreferences("config",MODE_PRIVATE);
        sv_setting_update =(SettingView) findViewById(R.id.sv_setting_update);
        sv_setting_address =(SettingView) findViewById(R.id.sv_setting_address);
        scv_setting_changedbk = (SettingClinckView) findViewById(R.id.scv_setting_changebdk);
        scv_setting_location =(SettingClinckView) findViewById(R.id.scv_setting_location);
        sv_setting_lock =(SettingView) findViewById(R.id.sv_setting_lock);
        //
        update();
        //
        changdbk();
        //
        location();

    }

    private void location() {
        scv_setting_location.setTitle("归属地提示框位置");
        scv_setting_location.setDes("设置归属地提示框的显示位置");
        scv_setting_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到设置位置的界面
                Intent intent = new Intent(SettingActivity.this,DragViewActivity.class);
                startActivity(intent);
            }
        });
    }
    //设置提示框的风格
    private void changdbk() {
        final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
        //设置标题和描述信息
        scv_setting_changedbk.setTitle("归属地提示框风格");
        //根据保存的选中的选项的索引值设置自定义组合控件描述信息回显操作
        scv_setting_changedbk.setDes(items[sp.getInt("which", 0)]);
        //
        scv_setting_changedbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                AlertDialog.Builder builder =new AlertDialog.Builder(SettingActivity.this);
                //设置图标
                builder.setIcon(R.mipmap.ic_launcher);
                //设置标题
                builder.setTitle("归属地提示风格");
                //设置成单选框
                builder.setSingleChoiceItems(items, sp.getInt("which", 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("which", which);
                        edit.commit();
                        //1.设置自定义组合控件描述信息文本
                        scv_setting_changedbk.setDes(items[which]);//根据选中选项索引值从items数组中获取出相应文本,设置给描述信息控件
                        //2.隐藏对话框
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        address();
        lock();
    }

    /*
    软件锁

     */
    private void lock() {

        //初始化自定义控件
        sv_setting_lock.setTitle("软件锁");

        //动态的获取服务是否开启
        if(AddressUtils.isRunningService("com.gaoyehua.service.WatchDogService",
                getApplicationContext())){

            //开启服务
            sv_setting_lock.setDes("打开隐私保护");
            sv_setting_lock.setChecked(true);
        }else {
            //关闭服务
            sv_setting_lock.setDes("关闭隐私保护");
            sv_setting_lock.setChecked(false);
        }


        //设置自定义控件的点击事件

        sv_setting_lock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                //更改状态
                //根据checkbox之前的状态来改变checkbox的状态
                Intent inntent =new Intent(SettingActivity.this,WatchDogService.class);
                if (sv_setting_lock.isChecked()) {
                    //关闭服务
                    stopService(inntent);
                    sv_setting_lock.setDes("关闭隐私保护");
                    sv_setting_lock.setChecked(false);
                    //保存状态
                    edit.putBoolean("update", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                }else{
                    //打开提示更新
                    startService(inntent);
                    sv_setting_lock.setDes("打开隐私保护");
                    sv_setting_lock.setChecked(true);
                    //保存状态
                    edit.putBoolean("update", true);
                }
                edit.commit();
            }
        });


    }

    /*
    提示更新的操作
     */
     private void update(){

         //初始化自定义控件
         sv_setting_update.setTitle("提示更新");

         if(sp.getBoolean("update",true)) {
             sv_setting_update.setDes("打开提示信息");
             sv_setting_update.setChecked(true);
         }else {
             sv_setting_update.setDes("关闭提示信息");
             sv_setting_update.setChecked(false);
         }
         //设置自定义控件的点击事件

         sv_setting_update.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 SharedPreferences.Editor edit = sp.edit();
                 //更改状态
                 //根据checkbox之前的状态来改变checkbox的状态
                 if (sv_setting_update.isChecked()) {
                     //关闭提示更新
                     sv_setting_update.setDes("关闭提示更新");
                     sv_setting_update.setChecked(false);
                     //保存状态
                     edit.putBoolean("update", false);
                     //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                 }else{
                     //打开提示更新
                     sv_setting_update.setDes("打开提示更新");
                     sv_setting_update.setChecked(true);
                     //保存状态
                     edit.putBoolean("update", true);
                 }
                 edit.commit();
             }
         });

     }
    private void address() {
        //初始化自定义控件
        sv_setting_address.setTitle("显示号码归属地");

        if(sp.getBoolean("address",true)) {
            sv_setting_address.setDes("打开显示号码归属地");
            sv_setting_address.setChecked(true);
        }else {
            sv_setting_address.setDes("关闭显示号码归属地");
            sv_setting_address.setChecked(false);
        }
        //
        if(AddressUtils.isRunningService("com.gaoyehua.service.AddressService",
                getApplicationContext())){
            sv_setting_address.setDes("打开显示号码归属地");
            sv_setting_address.setChecked(true);
        }else{
            sv_setting_address.setDes("关闭显示号码归属地");
            sv_setting_address.setChecked(false);
        }


        //设置自定义控件的点击事件

        sv_setting_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                Intent intent=new Intent(SettingActivity.this, AddressService.class);
                //更改状态
                //根据checkbox之前的状态来改变checkbox的状态
                if (sv_setting_address.isChecked()) {
                    //关闭提示更新
                    stopService(intent);
                    //
                    sv_setting_address.setDes("关闭显示号码归属地");
                    sv_setting_address.setChecked(false);
                    //保存状态
                    edit.putBoolean("address", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                }else{
                    //打开提示更新
                    startService(intent);
                    //
                    sv_setting_address.setDes("打开显示号码归属地");
                    sv_setting_address.setChecked(true);
                    //保存状态
                    edit.putBoolean("address", true);
                }
                edit.commit();
            }
        });



    }
}
