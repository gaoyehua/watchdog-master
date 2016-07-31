package com.gaoyehua.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.gaoyehua.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyehua on 2016/7/27.
 */
public class TaskEngine {
    //获取任务信息
    public static List<TaskInfo> getTaskAllInfo(Context context){
        List<TaskInfo> list=new ArrayList<TaskInfo>();
        //1.获取进程管理者
        ActivityManager activityManager =(ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm =context.getPackageManager();
        //2.获取所有正在运行的程序
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos
                =activityManager.getRunningAppProcesses();
        //3.遍历集合
        for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo :runningAppProcessInfos){
            TaskInfo taskInfo =new TaskInfo();
            //1.获取进程信息
            //获取包名，名称
            String packageName = runningAppProcessInfo.processName;
            taskInfo.setPackageName(packageName);
            //获取进程所占用的内存
            Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
            int totalpss =memoryInfo[0].getTotalPss();
            long ramsize =totalpss*1024;
            taskInfo.setRamSize(ramsize);


            try {
                //获取应用信息
                ApplicationInfo applicationInfo=pm.getApplicationInfo(packageName,0);
                //图标
                Drawable icon =applicationInfo.loadIcon(pm);
                taskInfo.setIcon(icon);
                //名称
                String name =applicationInfo.loadLabel(pm).toString();
                taskInfo.setName(name);
                //判断是否是系统程序
                int flag =applicationInfo.flags;
                boolean isuser;
                if((applicationInfo.FLAG_SYSTEM & flag) ==applicationInfo.FLAG_SYSTEM){

                    isuser=false;

                }else {
                    isuser =true;
                }
                //保存信息
                taskInfo.setUser(isuser);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //添加到集合中
            list.add(taskInfo);

        }

        return list;
    }


}
