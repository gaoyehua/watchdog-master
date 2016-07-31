package com.gaoyehua.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gaoyehau.watchdog.R;
import com.gaoyehua.service.GpsService;

/**
 * Created by 高业华 on 2016/7/20.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static MediaPlayer mediaPlayer;

    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    //static 解决两首报警音同时播放的问题
    //广播接受者在每接受一个广播事件，就会new一个广播接受者
    @Override
    public void onReceive(Context context, Intent intent) {
        //接受解析短信
        //70汉字一条短信,71汉字两条短信
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            //解析成SmsMessage
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();//获取短信的内容
            String sender;//获取发件人
            sender = smsMessage.getOriginatingAddress();
             Log.i("SmsReceiver", "发件人:" + sender + "  短信内容:" + body);
            //判断短信是哪个指令
            if ("#*location*#".equals(body)) {
                //GPS追踪
                Log.i("SmsReceiver","GPS追踪");
                //开启服务
                Intent intent1=new Intent(context, GpsService.class);
                context.startService(intent1);
                //拦截短信
                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*alarm*#".equals(body)){
                //播放报警音乐
                Log.i("SmsReceiver","播放报警音乐");
                //在播放报警音乐的时候现将音量调到最大
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                //设置系统音量的大小
                //streamType : 声音的类型
                //index : 声音的大小   0最小    15最大
                //flags : 指定信息的标签
                //getStreamMaxVolume : 获取系统最大音量,streamType:声音的类型
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);

                //判断是否播放报警音乐
                if(mediaPlayer!=null){
                    mediaPlayer.release();
                }
                mediaPlayer=MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setVolume(1.0f,1.0f);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*wipedata*#".equals(body)){
                //远程删除数据
                Log.i("SmsReceiver","远程删除数据");
                devicePolicyManager.wipeData(0);
                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }else if("#*lockscreen*#".equals(body)){
                //远程锁屏
                Log.i("SmsReceiver","远程锁屏");
                if(devicePolicyManager.isAdminActive(componentName)) {
                    devicePolicyManager.lockNow();
                }
                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }
        }
    }
}