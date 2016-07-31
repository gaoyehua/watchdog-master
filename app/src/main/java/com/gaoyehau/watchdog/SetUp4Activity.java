package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by 高业华 on 2016/7/18.
 */
public class SetUp4Activity extends SetUpBaseActivity {

    private CheckBox cb_setup4_safe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        //初始化控件
        cb_setup4_safe = (CheckBox) findViewById(R.id.cb_setup4_safe);

        if (sp.getBoolean("safe", true)) {
            cb_setup4_safe.setText("您已经开启防盗保护");
            cb_setup4_safe.setChecked(true);
        } else {
            cb_setup4_safe.setText("您没有开启防盗保护");
            cb_setup4_safe.setChecked(false);
        }

        cb_setup4_safe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor edit = sp.edit();
                //根据勾选框改变状态
                if (isChecked) {
                    cb_setup4_safe.setText("您已经开启防盗保护");
                    cb_setup4_safe.setChecked(true);
                    //保存状态
                    edit.putBoolean("safe", true);
                } else {
                    cb_setup4_safe.setText("您没有开启防盗保护");
                    cb_setup4_safe.setChecked(false);
                    //保存状态
                    edit.putBoolean("safe", false);
                }
                edit.commit();
            }
        });
    }
    @Override
    public void pre_activity() {
        Intent intent =new Intent(this,SetUp3Activity.class);
        startActivity(intent);
        finish();
        //执行平移界面效果
        overridePendingTransition(R.anim.setup_enter_pre,R.anim.setup_exit_pre);
    }

    @Override
    public void next_actvity() {
        //跳转到手机防盗界面
        SharedPreferences.Editor editor =sp.edit();
        editor.putBoolean("first",false);
        editor.commit();
        //跳转到手机防盗界面
        Intent intent =new Intent(this,LostFindActivity.class);
        startActivity(intent);
        finish();

    }
}
