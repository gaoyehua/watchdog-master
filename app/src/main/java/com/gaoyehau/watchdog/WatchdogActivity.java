package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoyehua.service.WatchDogService;

/**
 * Created by gaoyehua on 2016/7/29.
 */
public class WatchdogActivity extends Activity {

    private ImageView iv_watchdog_icon;
    private TextView tv_watchdog_name;
    private String packagename;
    private EditText ed_watchdog_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchdog);
        //加载控件
        iv_watchdog_icon = (ImageView) findViewById(R.id.iv_watchdog_icon);
        tv_watchdog_name = (TextView) findViewById(R.id.tv_watchdog_name);
        ed_watchdog_password = (EditText) findViewById(R.id.ed_watchdog_password);

        //接收获取数据
        Intent intent5=getIntent();
        packagename =intent5.getStringExtra("packageName");
        //设置显示加锁程序的图标和名称
        PackageManager pm =getPackageManager();
        try {
            ApplicationInfo applicationInfo =pm.getApplicationInfo(packagename,0);
            Drawable icon =applicationInfo.loadIcon(pm);
            String name =applicationInfo.loadLabel(pm).toString();
            iv_watchdog_icon.setImageDrawable(icon);
            tv_watchdog_name.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    /*
    解决,点击home最小,打开其他加锁程序,显示上一个加锁程序的图片和名称的bug

     */
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            /**
             * Starting: Intent {
             act=android.intent.action.MAIN
             cat=[android.intent.category.HOME
             ] cmp=com.android.launcher/com.android.launcher2.Launcher } from pid 208
             */
            //跳转到主界面
            Intent intent =new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    解锁密码
     */
    public void lock(View view){
        //解锁
        //1.获取输入的密码
        String password =ed_watchdog_password.getText().toString().trim();
        //2.判断密码是否一致
        if("123".equals(password)){
            //解锁
            //一般通过广播的方式来发送信息
            Intent intent7 =new Intent();
            intent7.setAction("com.gaoyehau.watchdog.unlock");
            //自定义发送广播
            intent7.putExtra("packagename",packagename);
            sendBroadcast(intent7);
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"密码错误！",Toast.LENGTH_SHORT).show();
        }

    }

}
