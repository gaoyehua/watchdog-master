package com.gaoyehua.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by gaoyehua on 2016/7/30.
 * 功能：数据库病毒查询操作
 *
 */
public class antivirusDao {
    //查询程序在否在病毒应用库中
    public static boolean queryAntiVirus(Context context,String md5){
        boolean ishave =false;
        //1.获取要查询的数据库的路径
        File file =new File(context.getFilesDir(),"antivirus.db");
        //2.打开数据库
        SQLiteDatabase database =SQLiteDatabase.openDatabase(file.getAbsolutePath(),null,SQLiteDatabase.OPEN_READONLY);
        //3.获取数据
        Cursor cursor = database.query("datable",null,"md5=?",new String[]{md5},null,null,null);
        //4.解析数据
        if(cursor.moveToNext()){
            ishave =true;
        }
        database.close();
        cursor.close();
        return ishave;

    }

}
