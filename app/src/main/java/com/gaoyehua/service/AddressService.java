package com.gaoyehua.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gaoyehau.watchdog.R;
import com.gaoyehua.db.dao.addressDao;

/**
 * Created by gaoyehua on 2016/7/22.
 */
public class AddressService extends Service {

    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private WindowManager windowManager;
    //private TextView textView;
    private View view;
    private MyOutGoingCallRecevier myOutGoingCallRecevier;
    private SharedPreferences sp;
    private int width;
    private int height;
    private WindowManager.LayoutParams params;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    服务中建立广播接收者
     */
    private class MyOutGoingCallRecevier extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //查询外拨电话的号码归属地
            //1.获取外拨电话
            String phone = getResultData();
            //2.查询号码归属地
            String queryAddress = addressDao.querryAddress(phone, getApplicationContext());
            //3.判断号码归属地是否为空
            if (!TextUtils.isEmpty(queryAddress)) {
                //显示toast
                showToast(queryAddress);
            }


        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sp =getSharedPreferences("comfig",MODE_PRIVATE);

        //1.设置一个广播接受者
        myOutGoingCallRecevier =new MyOutGoingCallRecevier();
        //2.设置一个广播接受事件
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");//设置接受的广播事件
        //3.注册广播接受者
        registerReceiver(myOutGoingCallRecevier, intentFilter);

        //1.获取手机管理者
         telephonyManager=(TelephonyManager)
                getSystemService(TELEPHONY_SERVICE);
        //2.监听电话状态
        myPhoneStateListener=new MyPhoneStateListener();
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

    }

    private class MyPhoneStateListener extends PhoneStateListener{
        //电话的状态
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲状态,挂断电话
                    hideToast();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //通话状态
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃状态
                    //查询归属地的号码并显示
                    String queryAddress=addressDao.querryAddress(incomingNumber,getApplicationContext());
                    if(!TextUtils.isEmpty(queryAddress)){
                        //Toast.makeText(getApplicationContext(),queryAddress,Toast.LENGTH_SHORT).show();

                        showToast(queryAddress);
                    }

                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
    /*

    显示Toast
     */
    public void showToast(String queryAddress) {

        int[] bgcolor = new int[] {
                R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_gray, R.drawable.call_locate_green };
        //1.获取windowManager
         windowManager =(WindowManager) getSystemService(WINDOW_SERVICE);

        //
        view=View.inflate(getApplicationContext(), R.layout.toast_custom,null);
        TextView tv_toastcustom_address = (TextView) view.findViewById(R.id.tv_toastcustom_address);
        tv_toastcustom_address.setText(queryAddress);

        //根据归属地风格显示
        view.setBackgroundResource(bgcolor[sp.getInt("which", 0)]);


        // textView =new TextView(getApplicationContext());
       // textView.setText(queryAddress);
       // textView.setTextColor(Color.RED);
        //textView.setTextSize(10);
        Log.i("AddressService","开启服务");
        //2.shezhiToastshuxing
        //layoutparams是toast的属性,控件要添加到那个父控件中,父控件就要使用那个父控件的属性,表示控件的属性规则符合父控件的属性规则
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; //宽度包裹内容
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //没有焦点

      //          | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  // 不可触摸

                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持当前屏幕
        params.format = PixelFormat.TRANSLUCENT; // 透明
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // 不能执行toast的类型

        //优先于电话
        //设置Toast的位置
        params.gravity= Gravity.LEFT | Gravity.TOP;
        params.x =sp.getInt("x",120);//表示距离边框的距离
        params.y =sp.getInt("y",100);

        setTouch();

        //3.获取view对象
        windowManager.addView(view,params);

    }

    //Toast移动
    private void setTouch() {

        view.setOnTouchListener(new View.OnTouchListener() {
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
                        if(params !=null){
                            params.x+=dX;
                            params.y+=dY;
                        }

                        //控制控件的坐标不能移出外拨电话界面
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y=0;
                        }
                        if (params.x > width-view.getWidth()) {
                            params.x = width-view.getWidth();
                        }
                        if (params.y > height - view.getHeight() - 25) {
                            params.y = height - view.getHeight() - 25;
                        }

                        windowManager.updateViewLayout(view, params);//更新windowmanager中的控件
                        //5.更新开始的坐标
                        startX=newX;
                        startY=newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起的事件
                        System.out.println("抬起了....");
                        //保存控件的坐标,保存的是控件的坐标不是手指坐标
                        //获取控件的坐标
                        int x = params.x;
                        int y = params.y;

                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("x", x);
                        edit.putInt("y", y);
                        edit.commit();
                        break;
                }
                //True if the listener has consumed the event, false otherwise.
                //true:事件消费了,执行了,false:表示事件被拦截了
                return false;
            }
        });

    }
    /*
    隐藏Toast
     */

     public void hideToast() {
         if(windowManager!=null&& view!= null) {
             windowManager.removeView(view);
             windowManager=null;
             view =null;

         }


     }
    public void onDestroy(){
        //当关闭服务的时候，取消监听
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        super.onDestroy();

    }
}
