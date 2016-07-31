package com.gaoyehua.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.gaoyehua.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyehua on 2016/7/26.
 */
public class AppEngine {

    public static List<AppInfo> getAppInfo(Context context){
        List<AppInfo> list =new ArrayList<AppInfo>();
        //获取应用程序的信息
        //获取包的管理者
        PackageManager pm =context.getPackageManager();
        //获取系统中安装的所有应用程序信息
        List<PackageInfo> installedPackage =pm.getInstalledPackages(0);
        for(PackageInfo packageInfo :installedPackage){
            //获取包名
            String packageName =packageInfo.packageName;
            //获取版本号
            String versionName =packageInfo.versionName;
            //获取应用信息
            ApplicationInfo applicationInfo =packageInfo.applicationInfo;
            //获取应用名
            String name=applicationInfo.loadLabel(pm).toString();
            //获取图标
            Drawable icon =applicationInfo.loadIcon(pm);
            //判断是否是系统应用
            boolean isUser;
            int flags =applicationInfo.flags;
            if((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM){
                isUser=false;
            }else {
                isUser=true;
            }
            //判断是否安装在SD卡
            boolean isSD;
            if((applicationInfo.FLAG_EXTERNAL_STORAGE & flags)
                    == applicationInfo.FLAG_EXTERNAL_STORAGE){
                isSD =true;
            }else {
                isSD =false;
            }
            //添加到bean中
            AppInfo appInfo =new AppInfo(name,icon,packageName,
                    versionName,isSD,isUser);
            //将bean添加到List集合中
            list.add(appInfo);

        }
        return list;

    }

}
