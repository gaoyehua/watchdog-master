package com.gaoyehau.watchdog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoyehua.db.dao.addressDao;

/**
 * Created by gaoyehua on 2016/7/21.
 */
public class AddressActivity extends Activity {

    private  EditText et_address_queryphone;
    private  TextView et_address_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
         et_address_queryphone =(EditText) findViewById(R.id.et_address_queryphone);
         et_address_location =(TextView) findViewById(R.id.tv_address_location);

        //监听文本框变化的操作
        et_address_queryphone.addTextChangedListener(new TextWatcher() {
            //文本完成之前调用的方法
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //文本调用完成时调用的方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //1.获取号码
                String phone =s.toString();
                //2.数据库中查询号码
               String querryAddress=addressDao.querryAddress(phone,getApplicationContext());

                //3.显示号码
                if(!TextUtils.isEmpty(querryAddress)) {
                     et_address_location.setText(querryAddress);

                }

            }
            //文本调用之后调用的方法
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    /*
    查询号码归属地
     */
    public void query(View view) {
        //1.获取输入的号码
        String phone = et_address_queryphone.getText().toString().trim();
        //2.判断
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(getApplicationContext(),
                    "请输入要查询的号码！",Toast.LENGTH_SHORT).show();
            //实现抖动的效果

            Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
            et_address_queryphone.startAnimation(shake);


            //实现振动的效果
            Vibrator vibrator =(Vibrator) getSystemService(VIBRATOR_SERVICE);

            vibrator.vibrate(100);

        }else {
            //查询号码归属地
            String queryAddress = addressDao.querryAddress(phone,getApplicationContext());
            //判断查询到的号码是否为空
            if(!TextUtils.isEmpty(queryAddress)) {
                et_address_location.setText(queryAddress);
            }
        }

    }
}
