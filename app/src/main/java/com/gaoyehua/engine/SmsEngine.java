package com.gaoyehua.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gaoyehua on 2016/7/29.
 * A.需求：
 * 实现手机备份的功能
 * B.作用：
 * 获取手机短信的操作,备份短信
 * C.思路：1.获得内容解析者 2，获得内容解析者提供的uri 3.获取查询路径 4.查询操作 5.解析Cursor
 * 备份短信：XML序列器，逐层解析
 *
 */
public class SmsEngine {

    //1.创建刷子
    public interface ShowProgress {
        //最大进度
        public void setMax(int max);
        //当前进度
        public void setProgress(int progress);
    }

    //2.给刷子
    public static void getAllSms(Context context,ShowProgress showProgress){
        //1.获得内容解析者
        ContentResolver contentResolver =context.getContentResolver();
        //2，获得内容解析者提供的uri
        //3.获取查询路径
        Uri uri =Uri.parse("content://sms");
        //4.查询操作
        Cursor cursor =contentResolver.query(uri,new String[]
        {"address","date","type","body"},null,null,null);

        //设置最大进度
        int count =cursor.getCount();
        showProgress.setMax(count);

        //设置当前进度条
        int progress =0;

        //备份短信
        //2.1获得xml序列器
        XmlSerializer xmlSerializer = Xml.newSerializer();
        try {
            //2.2设置保存的文件的位置
            xmlSerializer.setOutput(new FileOutputStream
                    (new File("/mnt/sdcard/backupsms.xml")),"utf-8");
            //2.3设置头文件
            xmlSerializer.startDocument("utf-8",true);
            //2.4设置根标签
            xmlSerializer.startTag(null,"smss");
            //5.解析Cursor
            while (cursor.moveToNext()){
                //每条信息的启示标签
                xmlSerializer.startTag(null,"sms");
                //设置每个文件的标签
                xmlSerializer.startTag(null,"address");
                String address=cursor.getString(0);
                xmlSerializer.text(address);
                xmlSerializer.endTag(null,"address");

                xmlSerializer.startTag(null,"date");
                String date=cursor.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag(null,"date");

                xmlSerializer.startTag(null,"type");
                String type=cursor.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag(null,"type");

                xmlSerializer.startTag(null,"body");
                String body=cursor.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null,"body");

                xmlSerializer.endTag(null,"sms");
                Log.i("data","address:"+address+"   date:"+date+"  type:"+type+"  body:"+body);


                //设置当前进度条
                progress++;
                showProgress.setProgress(progress);
            }
            xmlSerializer.endTag(null,"smss");
            xmlSerializer.endDocument();
            //刷新
            xmlSerializer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
