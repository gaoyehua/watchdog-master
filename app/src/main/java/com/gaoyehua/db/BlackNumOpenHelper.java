package com.gaoyehua.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gaoyehua on 2016/7/23.
 */

//未进行单元测试
public class BlackNumOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME ="Info" ;

    public BlackNumOpenHelper(Context context) {
        /**
         * context : 上下文
         * name :　数据库名称
         * factory：游标工厂
         * version : 数据库的版本号
         * @param context
         */
        super(context, "blacknum.db", null, 1);
    }
//////第一次创建数据库的调用,创建表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        //
        db.execSQL("create table "+DB_NAME+"" +
                "(_id integer primary key autoincrement,blacknum varchar(20),mode varchar(2))");

    }
    //当数据库版本发生变化的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
