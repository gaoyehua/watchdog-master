package com.gaoyehua.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 高业华 on 2016/7/19.
 */
public class ContactEngine  {
    ///获取联系人
    public static List<HashMap<String,String>> getAllContactInfo(
            Context context) {

        ArrayList<HashMap<String,String>> list =new
                ArrayList<HashMap<String, String>>();
        //获取内容解析者
        ContentResolver resolver =context.getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts www.baidu.com/jdk
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        Uri raw_uri =Uri.parse("content://com.android.contacts/raw_contacts");
        Uri data_uri =Uri.parse("content://com.android.contacts/data");
        //查询操作，先查询raw_contacts,再查询contact_id
        Cursor cursor =resolver.query(raw_uri,new String[] {"contact_id"},null,null,null);
        //解析Cursor
        while (cursor.moveToNext()){
            //6.获取查询的数据
            String contact_id =cursor.getString(0);
            if(!TextUtils.isEmpty(contact_id)) {
                // 7.根据contact_id查询view_data表中的数据
                // selection : 查询条件
                // selectionArgs :查询条件的参数
                // sortOrder : 排序
                // 空指针: 1.null.方法 2.参数为null
                Cursor c =resolver.query(data_uri,new String[] {"data1","mimetype"},
                        "raw_contact_id=?",new String[] {contact_id},null);
                HashMap<String,String> map =new HashMap<String, String>();
                //8.解析c
                while (c.moveToNext()) {
                    //9.获取数据
                    String data1 =c.getString(0);
                    String mimetype =c.getString(1);
                    //10.根据类型去判断获取的data1的数据并保存
                    if(mimetype.equals("vnd.android.cursor.item/phone_v2")){
                        //电话
                        map.put("phone",data1);
                    }else if(mimetype.equals("vnd.android.cursor.item/name")) {
                        //姓名
                        map.put("name",data1);
                    }
                }
                //添加到集合中数据
                list.add(map);
                //12、关闭cursor
                c.close();

            }
        }
        //12.关闭cursor
        cursor.close();
        return list;

    }

}
