package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.gaoyehua.ui.SettingView;

/**
 * Created by 高业华 on 2016/7/18.
 */
public class SetUp2Activity extends SetUpBaseActivity {

    private SettingView sv_setting_sim;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        Log.w("SetUp2Activity", "启动第2个界面");
        sp = getSharedPreferences("config", MODE_PRIVATE);

        sv_setting_sim = (SettingView) findViewById(R.id.sv_setting_sim);


        //初始化自定义控件
        sv_setting_sim.setTitle("点击绑定SIM卡");
        if (sp.getBoolean("sim", true)) {
            sv_setting_sim.setDes("sim卡已绑定");
            sv_setting_sim.setChecked(true);
        } else {
            sv_setting_sim.setDes("sim卡没有绑定");
            sv_setting_sim.setChecked(false);
        }

        //设置自定义控件的点击事件

        sv_setting_sim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                //更改状态
                //根据checkbox之前的状态来改变checkbox的状态
                if (sv_setting_sim.isChecked()) {
                    //
                    sv_setting_sim.setDes("sim卡没有绑定");
                    sv_setting_sim.setChecked(false);
                    //保存状态
                    edit.putBoolean("sim", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                } else {
                    //绑定sim卡
                    //获取sim卡号
                    //电话管理者
                    TelephonyManager tel =(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    //tel.getLine1Number();//获取SIM卡绑定的电话号码
                    //   line1:双卡双待.在中国不太适用,运营商一般不会将SIM卡和手机号码绑定
                    String sim =tel.getSimSerialNumber();
                    //获取序列号，sim卡唯一的标识
                    //保存sim卡号
                    edit.putString("sim",sim);
                    sv_setting_sim.setDes("sim卡已绑定");
                    sv_setting_sim.setChecked(true);
                    //保存状态
                    edit.putBoolean("sim", true);

                }
                edit.commit();
            }
        });

    }
    //
    //


    @Override
    public void pre_activity() {

        Intent intent =new Intent(this,SetUp1Activity.class);
        startActivity(intent);
        finish();

        //执行平移界面效果
        overridePendingTransition(R.anim.setup_enter_pre,R.anim.setup_exit_pre);
    }
    @Override
    public void next_actvity() {
        //跳转到下一个界面
        Intent intent =new Intent(this,SetUp3Activity.class);
        startActivity(intent);
        finish();
        //执行平移界面效果
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
    }

}

