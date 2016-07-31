package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by gaoyehua on 2016/7/23.
 */
public class DragViewActivity extends Activity {

    private LinearLayout ll_dragview_toast;
    private SharedPreferences sp;
    private TextView tv_draview_buttom;
    private TextView tv_draview_top;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        sp= getSharedPreferences("comfig",MODE_PRIVATE);
        ll_dragview_toast = (LinearLayout) findViewById(R.id.ll_dragview_toast);
         tv_draview_buttom =(TextView) findViewById(R.id.tv_dragview_buttom);
         tv_draview_top =(TextView) findViewById(R.id.tv_dragview_top);
        //设置回显操作
        //获取保存的坐标
        int x=sp.getInt("x",0);
        int y=sp.getInt("y",0);
        Log.i("DragViewActivity","X:"+x+"Y:"+y);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_dragview_toast.getLayoutParams();

        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_dragview_toast.getLayoutParams();
        //2.2设置相应的属性
        //leftMargin : 距离父控件左边的距离,根据布局文件中控件中layout_marginLeft属性效果相似
        params.leftMargin = x;
        params.topMargin = y;
        //2.3给控件设置属性
        ll_dragview_toast.setLayoutParams(params);


        //获取屏幕的宽度
        WindowManager windowManager=(WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics =new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        width=outMetrics.widthPixels;
        height=outMetrics.heightPixels;

        if(y>=height/2){
            tv_draview_buttom.setVisibility(View.INVISIBLE);
            tv_draview_top.setVisibility(View.VISIBLE);

        }else {
            tv_draview_buttom.setVisibility(View.VISIBLE);
            tv_draview_top.setVisibility(View.INVISIBLE);

        }



        setTouch();
        setDoubleClinck();
    }

    long[] mHits= new long[2];

    private void setDoubleClinck() {
        ll_dragview_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1]= SystemClock.uptimeMillis();
                if(mHits[0]>= (SystemClock.uptimeMillis()-500)){
                    //双击居中
                    int l= (width - ll_dragview_toast.getWidth())/2;
                    int t = (height -25- ll_dragview_toast.getHeight())/2;
                    ll_dragview_toast.layout(l, t, l+ll_dragview_toast.getWidth(), t+ll_dragview_toast.getHeight());
                    //保存控件的坐标
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("x", l);
                    edit.putInt("y", t);
                    edit.commit();
                }

            }
        });




    }


    /**
     * 设置触摸监听
     */
    private void setTouch() {
        ll_dragview_toast.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            //v : 当前的控件
            //event : 控件执行的事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //event.getAction() : 获取控制的执行的事件
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下的事件
                        System.out.println("按下了....");
                        //1.按下控件,记录开始的x和y的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动的事件
                        System.out.println("移动了....");
                        //2.移动到新的位置记录新的位置的x和y的坐标
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        //3.计算移动的偏移量
                        int dX = newX-startX;
                        int dY = newY-startY;
                        //4.移动相应的偏移量,重新绘制控件
                        //获取的时候原控件距离父控件左边和顶部的距离
                        int l = ll_dragview_toast.getLeft();
                        int t = ll_dragview_toast.getTop();
                        //获取新的控件的距离父控件左边和顶部的距离
                        l+=dX;
                        t+=dY;
                        int r = l+ll_dragview_toast.getWidth();
                        int b = t+ll_dragview_toast.getHeight();
                        ////在绘制控件之前,判断ltrb的值是否超出屏幕的大小,如果是就不在进行绘制控件的操作
                        if(l < 0 || r > width || t < 0 || b > height - 25) {
                            break;
                        }

                        ll_dragview_toast.layout(l, t, r, b);//重新绘制控件

                        //隐藏显示提示栏
                        int top =ll_dragview_toast.getTop();
                        if(top>=height/2){
                            tv_draview_buttom.setVisibility(View.INVISIBLE);
                            tv_draview_top.setVisibility(View.VISIBLE);

                        }else {
                            tv_draview_buttom.setVisibility(View.VISIBLE);
                            tv_draview_top.setVisibility(View.INVISIBLE);

                        }
                        //5.更新开始的坐标
                        startX=newX;
                        startY=newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起的事件
                        System.out.println("抬起了....");
                        //保存到新移动的坐标
                        int  x=ll_dragview_toast.getLeft();
                        int  y=ll_dragview_toast.getTop();

                        SharedPreferences.Editor edit=sp.edit();
                        edit.putInt("x",x);
                        edit.putInt("y",y);
                        edit.commit();
                        break;
                }
                //True if the listener has consumed the event, false otherwise.
                //true:事件消费了,执行了,false:表示事件被拦截了
                return false;
            }
        });
    }

}
