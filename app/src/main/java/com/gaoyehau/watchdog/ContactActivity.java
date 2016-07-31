package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaoyehua.engine.ContactEngine;
import com.gaoyehua.utils.MyAsycnTask;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 高业华 on 2016/7/19.
 */
public class ContactActivity extends Activity {

    private ListView lv_contact_contacts;
    private List<HashMap<String, String>> list;
    private ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //异步加载框架，工具类MyAsycnTask.java

        new MyAsycnTask(){
            @Override
            public void pretask() {
                if(loading!=null){
                    loading.setVisibility(loading.VISIBLE);
                }
            }
            @Override
            public void doingtask() {
                list = ContactEngine.
                        getAllContactInfo(getApplicationContext());
            }
            @Override
            public void posttask() {
                //进行操作
                lv_contact_contacts.setAdapter(new MyAdapter());
                //数据显示完成,隐藏进度条
                loading.setVisibility(loading.INVISIBLE);
            }
        }.execute();

        //初始化控件
        loading =(ProgressBar) findViewById(R.id.loading);
        lv_contact_contacts =(ListView) findViewById(R.id.lv_contact_contacts);
        //lv_contact_contacts.setAdapter(new MyAdapter());
        lv_contact_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                //将点击联系人的号码传递给设置安全号码的界面
                Intent intent =new Intent();
                intent.putExtra("num",list.get(position).get("phone"));
                //将数据传递给设置安全号码的界面
                //设置结果的方法，会将结果返回到调用当前活动的活动
                setResult(RESULT_OK,intent);
                //移除界面
                finish();
            }
        });

    }

    private class MyAdapter extends BaseAdapter {

        //条目的个数
        @Override
        public int getCount() {
            if(list!=null){
                return list.size();
            }
            return 0;
        }

        //条目的样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view =View.inflate(getApplicationContext(),R.layout.item_contact,null);
            //初始化控件
            //view.findViewById 是从item_contact找控件,findViewById是从activity_contacts找控件
            TextView tv_itemcontact_name = (TextView) view.findViewById(R.id.tv_itemcontact_name);
            TextView tv_itemcontact_phone = (TextView) view.findViewById(R.id.tv_itemcontact_phone);

            //设置条目中的值
            tv_itemcontact_name.setText(list.get(position).get("name"));
            tv_itemcontact_phone.setText(list.get(position).get("phone"));
            return view;
        }
        //条目的内容
        @Override
        public Object getItem(int position) {
            return null;
        }
        //条目的id

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }



}
