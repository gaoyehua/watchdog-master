package com.gaoyehua.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by gaoyehua on 2016/7/27.
 */
public class AppUtils {

    //获得可用内存

    public static long getAvailbleSD(){
        //获得SD卡的路径
        File path = Environment.getExternalStorageDirectory();
        //
        //硬盘的API操作
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();//获取每块的大小
        long totalBlocks = stat.getBlockCount();//获取总块数
        long availableBlocks = stat.getAvailableBlocks();//获取可用的块数
        return availableBlocks*blockSize;

    }
    public static long getAvailableRom(){
        //
        File path =Environment.getDataDirectory();
        //硬盘的API操作
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();//获取每块的大小
        long totalBlocks = stat.getBlockCount();//获取总块数
        long availableBlocks = stat.getAvailableBlocks();//获取可用的块数
        return availableBlocks*blockSize;

    }

}
