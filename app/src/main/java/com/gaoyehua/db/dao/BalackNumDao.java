package com.gaoyehua.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gaoyehua.bean.BlackNumInfo;
import com.gaoyehua.db.BlackNumOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyehua on 2016/7/23.
 */
public class BalackNumDao  {
    public static final int CALL=0;
    public static final int SMS=1;
    public static final int ALL=2;

    private BlackNumOpenHelper blackNumOpenHelper;
    //在构造函数中获取BlackNumOpenHelper
    public BalackNumDao(Context context){

        blackNumOpenHelper =new BlackNumOpenHelper(context);

    }
    //增删改查


    //增加
    public void addBlackNum(String blackunm,int mode){
        //1.获取数据库
        SQLiteDatabase database =blackNumOpenHelper.getWritableDatabase();
        //2.进行增加操作
        //
        ContentValues values =new ContentValues();
        values.put("blacknum",blackunm);
        values.put("mode",mode);
        database.insert(BlackNumOpenHelper.DB_NAME,null,values);
        //关闭数据库
        database.close();
    }
    //更新
    public void updateBlackNum(String blacknum,int mode){
        //1.获取数据库
        SQLiteDatabase database =blackNumOpenHelper.getWritableDatabase();
        //2.更新操作
        ContentValues values=new ContentValues();
        values.put("mode",mode);
        database.update(blackNumOpenHelper.DB_NAME,values,"blacknum=?",new String[] {blacknum});
        //3.关闭数据库
        database.close();
    }
    //查询
    public int queryBlackNum(String blacknum){
        int mode =-1;
        //1.获取数据库
        SQLiteDatabase database =blackNumOpenHelper.getWritableDatabase();
        //2.查询操作
        Cursor cursor =database.query(blackNumOpenHelper.DB_NAME,new String[]{"mode"},
                "blacknum=?",new String[] {blacknum},null,null,null);
        //3.解析cursor
        if(cursor.moveToNext()){
            mode =cursor.getInt(0);
        }
        //4.关闭数据库
        database.close();
        cursor.close();
        return mode;
    }
    //删除
    public void deleteBlackNum(String blacknum){
        //1.获取数据库
        SQLiteDatabase database=blackNumOpenHelper.getWritableDatabase();
        //2.删除的操作
        database.delete(blackNumOpenHelper.DB_NAME,"blacknum=?",new String[] {blacknum});

        //3.关闭数据路
        database.close();
    }
    public List<BlackNumInfo> queryAllBlackNum() {
        List<BlackNumInfo> list =new ArrayList<BlackNumInfo>();
        //1.获取数据库
        SQLiteDatabase database =blackNumOpenHelper.getWritableDatabase();
        //2.查询操作
        Cursor cursor=database.query(blackNumOpenHelper.DB_NAME,new String[]{"blacknum","mode"},
                null,null,null,null,"_id desc");//desc倒序查询,asc:正序查询,默认正序查询
        //3.解析cursor
        if(cursor.moveToNext()){
            //获取查询出来的数据
            String blacknum =cursor.getString(0);
            int  mode =cursor.getInt(1);
            BlackNumInfo blackNumInfo=new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        //4.关闭数据库
        database.close();
        cursor.close();
        return list;

    }

    /**
     * 查询部分数据
     * 查询20条数据
     * MaxNum : 查询的总个数
     * startindex : 查询的起始位置
     * @return
     */

    public List<BlackNumInfo> getPartBlackNum(int MaxNum,int startindex){

        List<BlackNumInfo> list=new ArrayList<BlackNumInfo>();
        //1.获取数据库
        SQLiteDatabase database =blackNumOpenHelper.getWritableDatabase();
        //2.查询的操作
        Cursor cursor =database.rawQuery("select blacknum,mode from info order by _id desc limit ? offset ?",
                new String[]{MaxNum+"",startindex+""});
        //3.解析Cursor
        while (cursor.moveToNext()){
            //
            String blacknum=cursor.getString(0);
            int mode=cursor.getInt(1);
            BlackNumInfo blackNumInfo =new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }

        //4.关闭数据库
        database.close();
        cursor.close();
        return list;


    }

}
