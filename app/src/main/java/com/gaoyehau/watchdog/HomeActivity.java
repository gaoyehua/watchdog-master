package com.gaoyehau.watchdog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by 高业华 on 2016/7/16.
 */
public class HomeActivity extends Activity {

    private AlertDialog dialog;
    private SharedPreferences sp;
    private GridView gv_home_gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("comfig",MODE_PRIVATE);
         gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
        gv_home_gridview.setAdapter(new Myadapter());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //更具位置设置
                switch (position) {
                    case 0://手机防盗
                        //判断是否是初次设置
                        if(TextUtils.isEmpty(sp.getString("password",""))) {
                            //设置密码
                            showSetPassWordDialog();
                        }else {
                            //输入密码
                            showEnterPasswordDialog();
                        }

                        break;
                    case 1://通信卫士
                        Intent intent2 =new Intent(HomeActivity.this,CallSmsSafe.class);
                        startActivity(intent2);
                        break;
                    case 2://软件管理
                        Intent intent3 =new Intent(HomeActivity.this,SoftManagerActivity.class);
                        startActivity(intent3);
                        break;
                    case 3://进程管理
                        Intent intent4 =new Intent(HomeActivity.this,TaskActivity.class);
                        startActivity(intent4);
                        break;
                    case 4://流量统计
                        Intent intent9 = new Intent(HomeActivity.this,TrafficActivity.class);
                        startActivity(intent9);
                        break;
                    case 5://手机杀毒
                        Intent intent5 = new Intent(HomeActivity.this,AntivirusActivity.class);
                        startActivity(intent5);
                        break;
                    case 6://缓存清理
                        Intent intent6 = new Intent(HomeActivity.this,ClearCacheActivity.class);
                        startActivity(intent6);
                        break;

                    case 7://高级工具
                        Intent intent1 =new Intent(HomeActivity.this,AtoolsActivity.class);
                        startActivity(intent1);
                        break;

                    case 8: //设置中心
                        Intent intent =new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /*
    输入密码对话框

     */
    protected void showSetPassWordDialog() {
        //第一步：复制布局

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        //设置对话框不能消失
        builder.setCancelable(false);
        //将布局文件转化为view对象
        View view =View.inflate(getApplicationContext(),R.layout.dialog_setpassword,null);
        //初始化界面控件
        final EditText et_setpassword_password =(EditText) view.findViewById(R.id.et_setpassword_password);
        final EditText et_setpassword_comfrim =(EditText) view.findViewById(R.id.et_setpassword_comfrim);
        Button btn_ok =(Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle =(Button) view.findViewById(R.id.btn_cancle);

        //设置按钮的点击事件
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //设置密码
                //获取输入框中的密码是否为空
                String password=et_setpassword_password.getText().toString().trim();
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(),
                            "请输入密码",Toast.LENGTH_SHORT).show();
                     return;
                }
                //获取确认密码的密码
                String comfrim_password =et_setpassword_comfrim.getText().toString().trim();

               if(password.equals(comfrim_password)) {
                   //保存密码
                   SharedPreferences.Editor edit =sp.edit();
                   edit.putString("password",password);
                   edit.commit();
                   //隐藏对话框
                   dialog.dismiss();
                   //提醒用户
                   Toast.makeText(getApplicationContext(),
                           "密码设置成功", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(getApplicationContext(),
                           "两次密码输入不一致", Toast.LENGTH_SHORT).show();
               }

            }

        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //builder.setView(view);
        dialog = builder.create();
        dialog.setView(view,0,0,0,0);
        dialog.show();
    }
    int count =0;
    /*
    输入密码对话框
     */
    protected void showEnterPasswordDialog() {
        //复制布局
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        //设置对话框不能消失
        builder.setCancelable(false);
        //将布局文件转化为View文件
        View view =View.inflate(getApplicationContext(),R.layout.dialog_enterpassword,null);
        //初始化控件文件
        final EditText et_enterpassword_password =(EditText)
                view.findViewById(R.id.et_enterpassword_password);
        Button btn_ok =(Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle =(Button) view.findViewById(R.id.btn_cancle);
        ImageView iv_enterpassword_hide =(ImageView) view.findViewById(R.id.iv_enterpassword_hide);

        iv_enterpassword_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count%2==0) {
                    //显示密码
                    et_enterpassword_password.setInputType(0);
                }else {
                    //隐藏密码
                    et_enterpassword_password.setInputType(129);
                }
                count++;
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入是否为空
                String password =et_enterpassword_password.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),
                            "请输入密码！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取保存密码
                String sp_password =sp.getString("password","");
                if(password.equals(sp_password)) {
                    //跳转到手机防盗界面
                    Intent intent1 =new Intent(HomeActivity.this,LostFindActivity.class);
                    startActivity(intent1);
                    dialog.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),
                            "密码输入错误！",Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog=builder.create();
        dialog.show();

    }






    private  class Myadapter extends BaseAdapter {
        int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
        String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
                "高级工具", "设置中心" };
        //设置条目个数
        @Override
        public int getCount() {
            return 9;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //TextView textView =new TextView(getApplicationContext());
            //textView.setText("第"+position+"个条目");
            //将布局文件转化成View对象
            View view =View.inflate(getApplicationContext(),R.layout.item_home,null);

            //每个条目的样式都不一样，需要初始化样式

            ImageView iv_itemhome_icon =   (ImageView) view.findViewById(R.id.iv_itemhome_icon);
            TextView  tv_item_text =  (TextView)  view.findViewById(R.id.tv_itemhome_text);
            //逐个设置图片文本

            iv_itemhome_icon.setImageResource(imageId[position]);
            tv_item_text.setText(names[position]);

            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
