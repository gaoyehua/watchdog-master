package com.gaoyehua.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gaoyehau.watchdog.R;

/**
 * Created by 高业华 on 2016/7/17.
 */
public class SettingView extends RelativeLayout {

    private TextView tv_setting_title;
    private TextView tv_setting_des;
    private CheckBox cb_setting_update;
    private String des_on;
    private String des_off;
    //在代码中使用设置

    public SettingView(Context context) {
        super(context);
        init();
    }
    //在布局中使用设置
    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //自定义控件设置相应的值
        String title1 =attrs.
                getAttributeValue("http://schemas.android.com/apk/res/com.gaoyehau.watchdog","title1");
        String des_on = attrs.
           getAttributeValue("http://schemas.android.com/apk/res/com.gaoyehau.watchdog","des_on");
        String des_ff =attrs.
           getAttributeValue("http://schemas.android.com/apk/res/com.gaoyehau.watchdog","des_off");
        //初始化控件的值
        tv_setting_title.setText(title1);
        if(isChecked()){
            tv_setting_des.setText(des_on);
        }else{
            tv_setting_des.setText(des_ff);
        }

    }
    //也是布局中，多了一个参数
    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();

    }

    /*
    添加控件，自定义控件
     */
    public void init() {
        //添加布局文件
        //第一种方式：爹有了找孩子，
        //View view =View.inflate(getContext(),R.layout.settingview,null);
        //this.addView(view);

        //第二种方式：孩子有了找爹，喜当爹
        View view =View.inflate(getContext(), R.layout.settingview,this);
        //初始化标题、描述信息、ck控件
        tv_setting_title= (TextView) view.findViewById(R.id.tv_setting_title);
        tv_setting_des =(TextView) view.findViewById(R.id.tv_setting_des);
        cb_setting_update =(CheckBox) view.findViewById(R.id.cb_setting_update);
    }
    //需要添加一些方法，是程序员能够方便的改变方法的值

    //添加标题
    public void setTitle(String title) {
        tv_setting_title.setText(title);
    }
    //添加描述信息
    public void setDes(String des) {
        tv_setting_des.setText(des);
    }
    //添加勾选框状态
    public void setChecked(boolean isChecked) {
        //勾选框的状态
        cb_setting_update.setChecked(isChecked);

     //   if (isChecked()) {
      //      tv_setting_des.setText(des_on);
      //  }else{
       //     tv_setting_des.setText(des_off);
       // }
    }
    //获取勾选框的状态
    public boolean isChecked() {
        return cb_setting_update.isChecked();
    }



}
