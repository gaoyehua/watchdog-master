package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gaoyehua.engine.ContactEngine;

import java.util.AbstractCollection;

/**
 * Created by 高业华 on 2016/7/18.
 */
public class SetUp3Activity extends SetUpBaseActivity {

    private EditText et_setup3_safenum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        //初始化控件
         et_setup3_safenum =(EditText) findViewById(R.id.et_setup3_safenum);
        //回显
        et_setup3_safenum.setText(sp.getString("safenum",""));

    }

    @Override
    public void pre_activity() {
        Intent intent =new Intent(this,SetUp2Activity.class);
        startActivity(intent);
        finish();
        //执行平移界面效果
        overridePendingTransition(R.anim.setup_enter_pre,R.anim.setup_exit_pre);
    }

    @Override
    public void next_actvity() {
        //跳转到下一个界面
        //获取号码
        String safenum =et_setup3_safenum.getText().toString().trim();
        //判断是否为空
        if(TextUtils.isEmpty(safenum)) {
            Toast.makeText(getApplicationContext(),
                    "请输入安全号码！",Toast.LENGTH_SHORT).show();
            return;
        }
        //保存数据
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("safenum",safenum);
        editor.commit();
        Intent intent =new Intent(this,SetUp4Activity.class);
        startActivity(intent);
        finish();
        //执行平移界面效果
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);

    }

    /*
    选择联系人按钮
     */
    public void selectcontact(View view) {
        Intent intent =new Intent(this, ContactActivity.class);
        //startActivity(intent);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if(data!=null){
            String num =data.getStringExtra("num");
            //将获取的号码设置给安全框
            et_setup3_safenum.setText(num);
        }
    }

}

