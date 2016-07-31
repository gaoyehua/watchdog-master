package com.gaoyehua.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by gaoyehua on 2016/7/28.
 */
public class TaskUtils {
    public static int getProcessCount(Context context){
        //获取进程个数
        ActivityManager am =(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos =am.getRunningAppProcesses();
        return runningAppProcessInfos.size();

    }

    //获取可用的内存
    public static long availableRom(Context context){
        //获取可用的内存
        ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo =new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;

    }
    //获取总的内存
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static long totalRom(Context context) {
        //获取可用的内存
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.totalMem;//16版本之上才有，之前的是没有
    }
    //兼容版本

    public static long totalRom(){
        File file =new File("/proc/meminfo");
        StringBuilder sb =new StringBuilder();
        //获取文件
        try {
            BufferedReader br =new BufferedReader(new FileReader(file));
            String readline = br.readLine();
            //获取数据
            char[] charArray =  readline.toCharArray();
            for(char c :charArray){
                if(c>='0'&&c<='9'){
                    sb.append(c);
                }
                String string =sb.toString();
                //转化成long
                long parseLong =Long.parseLong(string);
                return parseLong*1024;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
