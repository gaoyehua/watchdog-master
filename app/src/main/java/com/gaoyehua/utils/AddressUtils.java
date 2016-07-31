package com.gaoyehua.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by gaoyehua on 2016/7/22.
 */
public class AddressUtils {
    /*

    动态获取服务是否开启

     */
    public static boolean isRunningService(String className, Context context){

        //获取活动管理者
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取当前的所有的活动
        List<ActivityManager.RunningServiceInfo> runningServices =
                activityManager.getRunningServices(1000);
        //遍历集合
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices){
            //获取控件的标识
            ComponentName service= runningServiceInfo.service;
            //获得全类名
            String className2=service.getClassName();
            if(className.equals(className2)){
                return true;
            }
        }
        return false;
    }


}
