package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by 高业华 on 2016/7/18.
 */
public abstract class SetUpBaseActivity extends Activity {

    private GestureDetector gestureDetector;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp =getSharedPreferences("comfig",MODE_PRIVATE);
        //获取一个手势识别器
        //要想让手势识别器生效，必须将其注册到界面的触摸时间中
        gestureDetector =new GestureDetector(this,new MyOnGestureListener());

    }
    //界面的触摸事件
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //手势识别器的使用
    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        //down ：按下
        //up：松开
        //velocityX：在X轴上运动的速度
        @Override
        public boolean onFling(MotionEvent down,MotionEvent up,
                               float velocityX,float velocityY) {
            float startX =down.getRawX();
            float endX =up.getRawX();
            float startY =down.getRawY();
            float endY =up.getRawY();
            //判断是否执滑动操作
            if(Math.abs(startY-endY)>100) {
                Toast.makeText(getApplicationContext(),
                        "主人你又在乱滑了，别闹了...",Toast.LENGTH_SHORT).show();
                return false;
            }
            if ((startX-endX)>100) {
                next_actvity();

            }
            if((endX-startX)>100) {
                pre_activity();
            }

            return true;
        }

    }


    //按钮点击事件的操作
    //将每个界面中的上一步,下一步按钮操作,抽取到父类中
    public void pre(View view) {

        pre_activity();
    }
    public void next(View view) {

        next_actvity();

    }

    public abstract void pre_activity() ;
    public abstract void next_actvity() ;
    //在父类中直接对子类中的返回键进行统一的处理
	/*@Override
	public void onBackPressed() {
		pre_activity();
		super.onBackPressed();
	}*/
    //监听手机物理按钮的点击事件
    //keyCode :　物理按钮的标示
    //event : 按键的处理事件
   @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        //判断keycode是否是返回键的标识
        if(KeyCode == KeyEvent.KEYCODE_BACK) {
            //true:是可以屏蔽按键的事件
            //return true;
            pre_activity();
        }
        return super.onKeyDown(KeyCode,event);

    }



}

