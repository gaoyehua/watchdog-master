package com.gaoyehua.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by 高业华 on 2016/7/19.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BootCompleteReceiver","手机重启了...");
        SharedPreferences sp =context.getSharedPreferences("comfig",context.MODE_PRIVATE);
        if(sp.getBoolean("safe",false)) {

            //获取保存的sim卡号
            String sp_sim =sp.getString("sim","");
            //再次获取本地的sim卡号
            TelephonyManager tel =(TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String sim =tel.getSimSerialNumber();
            //判断两个sim卡号是否为空
            if(!TextUtils.isEmpty(sp_sim)&&!TextUtils.isEmpty(sim)) {

                if(!sp_sim.equals(sim)) {
                    //发送报警短信
                    //；两次的sim卡号不一致
                    SmsManager smsManager =SmsManager.getDefault();
                    smsManager.sendTextMessage(sp.getString("safenum","5556"),
                            null,"大哥，我被人打劫了，Help me！！！",null,null);
                }

            }

        }


    }
}
