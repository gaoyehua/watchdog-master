package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by 高业华 on 2016/7/18.
 */
public class SetUp1Activity extends SetUpBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        Log.i("SetUp1Activity","启动第一个界面");

    }


    @Override
    public void pre_activity() {

    }

    @Override
    public void next_actvity() {
        Intent intent =new Intent(this,SetUp2Activity.class);
        startActivity(intent);
        finish();
        //执行平移界面效果
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
    }

}
