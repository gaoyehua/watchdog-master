package com.gaoyehua.service;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.gaoyehau.watchdog.R;
import com.gaoyehua.bean.TaskInfo;
import com.gaoyehua.receiver.MyWidget;
import com.gaoyehua.utils.TaskUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gaoyehua on 2016/7/28.
 */
public class WidgetService extends Service {
    private AppWidgetManager appWidgetManager;
    private widgetReciver widgetReciver;
    private Timer timer;
    private ScreenOffReciver screenOffReciver;
    private ScreenOnReciver screenOnReciver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /*
    清理进程的广播接受者

     */
    private class widgetReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //  清理进程
            killprocess();
        }

    }
    /*
    锁屏的进程广播接受者

     */
    public class ScreenOffReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("data","锁屏了----");
            //清理进程
            Log.i("data","清理进程----");
            killprocess();
            //停止更新
            Log.i("data","停止更新----");
            stopUpdate();
        }
    }
    /*

    注册解锁的广播接受者
     */
    public class ScreenOnReciver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            //开始更新界面
            Log.i("data","开始更新界面----");
            updateWidgets();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //更新桌面小控件
        //注册清理进程的广播接受者
        //1,。广播接受者
        widgetReciver =new widgetReciver();
        //2.设置接受的广播事件
        IntentFilter intentfilter =new IntentFilter();
        intentfilter.addAction("aa.bb.cc");
        //3.注册广播接受者
        registerReceiver(widgetReciver,intentfilter);

        /*
        注册锁屏广播接受者

         */
        //1.广播接受者
        screenOffReciver =new ScreenOffReciver();
        //2.设置广播接收事件
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReciver,intentFilter);

        /*
        注册解锁的广播接收者

         */
        //1.广播接收者
        screenOnReciver =new ScreenOnReciver();
        //2.设置广播接收事件
        IntentFilter intentFilter1 =new IntentFilter();
        intentFilter1.addAction(Intent.ACTION_SCREEN_ON);
        //3.注册广播接收者
        registerReceiver(screenOnReciver,intentFilter1);


        //widget的管理者
        appWidgetManager= AppWidgetManager.getInstance(this);
        //更新操作
        updateWidgets();
    }

    public void killprocess(){
        //获取进程管理者
        ActivityManager am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppprocesses =am.getRunningAppProcesses();
        //遍历集合
        for(ActivityManager.RunningAppProcessInfo runningAppProcessesInfo :runningAppprocesses){
            //判断是否是我们自己的程序
            if(!runningAppProcessesInfo.processName.equals(getPackageName())){
                am.killBackgroundProcesses(runningAppProcessesInfo.processName);
            }

        }


    }
    //注销广播接受者
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdate();
        //关闭锁屏广播接收者
        if(screenOffReciver!=null){
            unregisterReceiver(screenOffReciver);
            screenOffReciver=null;

        }

        if (screenOnReciver != null) {
            unregisterReceiver(screenOnReciver);
            screenOnReciver = null;
        }

        if(widgetReciver!=null){
            unregisterReceiver(widgetReciver);
            widgetReciver =null;
        }
    }

    /*
   停止更新
    */
    private void stopUpdate() {

        //关闭广播
        if(timer!=null){
            timer.cancel();
            timer=null;
        }

    }

    /*
    更新widget
     */
    private void updateWidgets() {
        //在这里也可以创建一个子线程，使其更新时间，但工作中很少用
        //计数器
        timer =new Timer();
        //
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //更新操作
                ComponentName provider =new ComponentName(WidgetService.this, MyWidget.class);
                RemoteViews views =new RemoteViews(getPackageName(), R.layout.process_widget);
                //远程布局不能通过findviewbyid互殴的初始化控件
                //更新布局的值

                views.setTextViewText(R.id.process_count,"正在运行软件:"+ TaskUtils.getProcessCount(WidgetService.this));
                views.setTextViewText(R.id.process_memory,"可用内存:"+ Formatter.formatFileSize(WidgetService.this,TaskUtils.availableRom(WidgetService.this)));
                //按钮点击事件
                Intent intent =new Intent();
                intent.setAction("aa.bb.cc");
                //sendBroadcast(intent);
                //viewId：点击控件的id
                //pendingIntent:延迟意图
                PendingIntent pendingIntent =PendingIntent.getBroadcast(WidgetService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);

                //更新操作
                appWidgetManager.updateAppWidget(provider,views);

            }
        },2000,2000);

    }
}
