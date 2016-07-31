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
public class SettingClinckView extends RelativeLayout {

    private TextView tv_setting_title;
    private TextView tv_setting_des;

    public SettingClinckView(Context context) {
        super(context);
        init();
    }

    //在布局中使用设置
    public SettingClinckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    //也是布局中，多了一个参数
    public SettingClinckView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        View view = View.inflate(getContext(), R.layout.settingclinckview, this);
        //初始化标题、描述信息、ck控件
        tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);
        tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);

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

}

