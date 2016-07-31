package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoyehua.bean.AppInfo;
import com.gaoyehua.db.dao.WatchDogDao;
import com.gaoyehua.engine.AppEngine;
import com.gaoyehua.utils.AppUtils;
import com.gaoyehua.utils.DensityUtils;
import com.gaoyehua.utils.MyAsycnTask;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Created by gaoyehua on 2016/7/26.
 */
public class SoftManagerActivity extends Activity implements View.OnClickListener {

    private ListView lv_softmanager_soft;
    private TextView tv_softmanager_scroll;
    private ProgressBar loading;
    private List<AppInfo> list;
    private List<AppInfo> userappinfo;
    private List<AppInfo> systemappinfo;
    private PopupWindow popuwindow;
    private WatchDogDao watchDogDao;
    AppInfo appInfo;
    //系统程序的集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);

        //得到看门狗数据操作
        watchDogDao=new WatchDogDao(getApplication());

        //初始化控件
        lv_softmanager_soft =(ListView) findViewById(R.id.lv_softmanager_soft);
        loading =(ProgressBar) findViewById(R.id.loading);
        tv_softmanager_scroll =(TextView) findViewById(R.id.tv_itemsoftmanager_scroll);
        TextView tv_softmanager_rom =(TextView) findViewById(R.id.tv_softmanager_rom);
        TextView tv_softmanager_sd =(TextView) findViewById(R.id.tv_softmanager_sd);

        //1.获得可用内存
        long availableSD = AppUtils.getAvailbleSD();
        long availableROM=AppUtils.getAvailableRom();
        //2.数据转换
        String sizeSD = android.text.format.Formatter.formatFileSize(getApplicationContext(),availableSD);
        String sizeROM = android.text.format.Formatter.formatFileSize(getApplicationContext(),availableROM);

        //3.显示数据
        tv_softmanager_sd.setText("SD卡可用:"+sizeSD);
        tv_softmanager_rom.setText("手机可用:"+sizeROM);

        //获取数据
        fillData();
        listViewOnScroll();
        listViewItemClick();
        listViewItemLongClick();

    }

    /*

    条目的长按点击事件
     */
    private void listViewItemLongClick() {
        lv_softmanager_soft.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("data","长按点击事件");
                //加锁解锁的操作
                //屏蔽用户应用和系统系统的条目
                if(position ==0 && position ==userappinfo.size()+1){
                    return true;
                }
                //获取数据
                if (position <= userappinfo.size()) {
                    //用户程序
                    appInfo = userappinfo.get(position-1);
                }else{
                    //系统程序
                    appInfo = systemappinfo.get(position - userappinfo.size() - 2);
                }
                //加锁解锁
                ViewHolder viewHolder =(ViewHolder) view.getTag();
                //判断应用是否加锁
                if(watchDogDao.queryLockApp(appInfo.getPackageName())){
                    //解锁
                    watchDogDao.deleteLockApp(appInfo.getPackageName());
                    viewHolder.iv_softmanager_islock.setImageResource(R.drawable.unlock);
                }else {
                    //加锁
                    if(!appInfo.getPackageName().equals(getPackageName())){
                        watchDogDao.addLockApp(appInfo.getPackageName());
                        viewHolder.iv_softmanager_islock.setImageResource(R.drawable.lock);
                    }else {
                        Toast.makeText(getApplicationContext(),"当前的应用程序不能加锁！",Toast.LENGTH_SHORT).show();
                    }


                }
                return true;
            }
        });



    }

    /*
    条目监听事件
     */
    private void listViewItemClick() {
        lv_softmanager_soft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {
            //弹出气泡
            //1.屏蔽用户程序和系统的程序
            if(position ==0 && position ==userappinfo.size()+1){
                return;
            }
            //2.获取条目所对应的应用程序的信息
            //数据就要从userappinfo和systemappinfo中获取
            if (position <= userappinfo.size()) {
                //用户程序
                appInfo = userappinfo.get(position-1);
            }else{
                //系统程序
                appInfo = systemappinfo.get(position - userappinfo.size() - 2);
            }
            //5.隐藏气泡
            hidepopupwindow();
            //3.弹出气泡
            View contentview =View.inflate(getApplicationContext(),
                    R.layout.popupwindow,null);

            //初始化控件
            LinearLayout ll_popuwindow_uninstall = (LinearLayout) contentview.findViewById(R.id.ll_softmanager_uninstall);
            LinearLayout ll_popuwindow_start = (LinearLayout) contentview.findViewById(R.id.ll_softmanager_start);
            LinearLayout ll_popuwindow_share = (LinearLayout) contentview.findViewById(R.id.ll_softmanager_share);
            LinearLayout ll_popuwindow_detail = (LinearLayout) contentview.findViewById(R.id.ll_softmanager_detail);

            //实现点击事件，在这里使用接口
            ll_popuwindow_uninstall.setOnClickListener(SoftManagerActivity.this);
            ll_popuwindow_start.setOnClickListener(SoftManagerActivity.this);
            ll_popuwindow_share.setOnClickListener(SoftManagerActivity.this);
            ll_popuwindow_detail.setOnClickListener(SoftManagerActivity.this);



            popuwindow =new PopupWindow(contentview,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popuwindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //4.获取条目的位置，让气泡显示在相应的位置
            int[] location =new int[2];
            view.getLocationInWindow(location);

            int x=location[0];
            int y=location[1];
            popuwindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP ,
                    x+ DensityUtils.dip2qx(getApplicationContext(),140),y);
            //6.设置动画

            //缩放动画
            //前四个 :　控制控件由没有变到有   动画 0:没有    1:整个控件
            //后四个:控制控件是按照自身还是父控件进行变化
            //RELATIVE_TO_SELF : 以自身变化
            //RELATIVE_TO_PARENT : 以父控件变化
            ScaleAnimation scaleanimaion =new ScaleAnimation(0,1,0,1,
                    Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.5f);
            scaleanimaion.setDuration(500);

            //渐变动画
            AlphaAnimation alphaAnimation =new AlphaAnimation(0.4f,1.0f);
            alphaAnimation.setDuration(500);

            //组合动画
            //共用相同的动画插补器
            AnimationSet animationSet =new AnimationSet(true);

            //添加动画
            animationSet.addAnimation(scaleanimaion);
            animationSet.addAnimation(alphaAnimation);
            //执行动画
            contentview.startAnimation(animationSet);

        }
    });

}
    //隐藏气泡
    private void hidepopupwindow(){
        if(popuwindow !=null){
            popuwindow.dismiss();
            popuwindow =null;
        }

    }

    /*
    创建滑动显示的对话框
    listView的滑动监听
     */
    private void listViewOnScroll() {
        lv_softmanager_soft.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滑动状态改变的时候使用
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            //滑动的时候使用
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                //
                hidepopupwindow();
                if(userappinfo !=null && systemappinfo !=null){
                    if(firstVisibleItem >=userappinfo.size()+1){
                        tv_softmanager_scroll.setText("系统程序("+systemappinfo.size()+")");

                    }else {
                        tv_softmanager_scroll.setText("用户程序("+userappinfo.size()+")");
                    }
                }
            }
        });


    }

    private void fillData() {
        new MyAsycnTask(){

            @Override
            public void pretask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doingtask() {
                list = AppEngine.getAppInfo(getApplicationContext());
                //拆分用户程序和系统程序
                userappinfo =new ArrayList<AppInfo>();
                systemappinfo =new ArrayList<AppInfo>();
                for(AppInfo appInfo :list){
                    if(appInfo.isUser()){
                        userappinfo.add(appInfo);

                    }else {
                        systemappinfo.add(appInfo);
                    }
                }

            }

            @Override
            public void posttask() {
                lv_softmanager_soft.setAdapter(new MyAdapter());
                loading.setVisibility(View.INVISIBLE);

            }
        }.execute();

    }

    //实现点击事件

    @Override
    public void onClick(View v) {

        //判断是点击的那个按钮
        switch (v.getId()){
            case R.id.ll_softmanager_uninstall:
                System.out.println("卸载");

                uninstall();
                break;
            case R.id.ll_softmanager_start:
                System.out.println("启动");
                start();
                break;
            case R.id.ll_softmanager_share:
                System.out.println("分享");
                share();
                break;
            case R.id.ll_softmanager_detail:
                System.out.println("详情");
                detatil();
                break;
        }



    }

    private void share() {
        /**
         *  Intent
         {
         act=android.intent.action.SEND
         typ=text/plain
         flg=0x3000000
         cmp=com.android.mms/.ui.ComposeMessageActivity (has extras)   intent中包含信息
         } from pid 228
         */
        Intent intent=new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"发现一款很好用的手机杀毒软件："+appInfo.getName()+",下载地址尽在百度搜素");
        startActivity(intent);
    }

    private void detatil() {
        /**
         *  Intent
         {
         act=android.settings.APPLICATION_DETAILS_SETTINGS    action
         dat=package:com.example.android.apis   data
         cmp=com.android.settings/.applications.InstalledAppDetails
         } from pid 228
         */
        Intent intent =new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:"+appInfo.getPackageName()));
        startActivity(intent);

    }

    private void start() {
        PackageManager pm = getPackageManager();
        Intent intent =pm.getLaunchIntentForPackage(appInfo.getPackageName());
        if(intent !=null){

            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(),"系统核心程序，无法启动！",Toast.LENGTH_SHORT).show();
        }

    }

    private void uninstall() {
        /**
         * <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <action android:name="android.intent.action.DELETE" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="package" />
         </intent-filter>
         */
        if(appInfo.isUser()){

            if(!appInfo.getPackageName().equals(getPackageName())){

                Intent inent =new Intent();
                inent.setAction("android.intent.action.DELETE");
                inent.addCategory("android.intent.category.DEFAULT");
                inent.setData(Uri.parse("package:"+appInfo.getPackageName()));
                startActivityForResult(inent,0);
            }else {
                Toast.makeText(getApplicationContext(),"主人，千万别想不开啊！",Toast.LENGTH_SHORT).show();

            }

        }else {
            Toast.makeText(getApplicationContext(),"系统应用，请先获得root权限",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();
    }

    private class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return userappinfo.size()+systemappinfo.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //增加条目
            if(position ==0){
                TextView textview =new TextView(getApplicationContext());
                textview.setBackgroundColor(Color.GRAY);
                textview.setTextColor(Color.WHITE);
                textview.setTextSize(20);
                textview.setText("用户程序("+userappinfo.size()+")");

                return textview;
            }else if(position ==userappinfo.size()+1){
                TextView textview =new TextView(getApplicationContext());
                textview.setBackgroundColor(Color.GRAY);
                textview.setTextColor(Color.WHITE);
                textview.setTextSize(20);
                textview.setText("系统程序("+systemappinfo.size()+")");

                return textview;

            }


            View view;
            ViewHolder viewHolder;
            //instanceof检查是否为类的对象
            if(convertView !=null && convertView instanceof RelativeLayout){

                view =convertView;
                viewHolder =(ViewHolder) view.getTag();

            }else {
                view=View.inflate(getApplication(),R.layout.item_softmanager,null);
                viewHolder =new ViewHolder();
                viewHolder.iv_itemsoftmanage_icon=(ImageView) view.findViewById(R.id.iv_itemsoftmanager_icon);
                viewHolder.tv_itemsoftmanager_name =(TextView) view.findViewById(R.id.tv_itemsoftmanager_name);
                viewHolder.tv_itemsoftmanager_version =(TextView) view.findViewById(R.id.tv_itemsoftmanager_versionname);
                viewHolder.tv_itemsoftmanager_issd =(TextView) view.findViewById(R.id.tv_itemsoftmanager_issd);
                viewHolder.iv_softmanager_islock =(ImageView) view. findViewById(R.id.iv_softmanager_islock);
                //将viewHoder与view对象绑定
                view.setTag(viewHolder);
            }
            //获取应用信息

            //数据要从user和System中获取
            if(position <=userappinfo.size()){
                appInfo =userappinfo.get(position-1);

            }else {
                appInfo =systemappinfo.get(position-userappinfo.size()-2);
            }

            //显示信息
            viewHolder.iv_itemsoftmanage_icon.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_itemsoftmanager_name.setText(appInfo.getName());
            viewHolder.tv_itemsoftmanager_version.setText(appInfo.getVersionName());
            boolean sd =appInfo.isSD();
            if(sd){
                viewHolder.tv_itemsoftmanager_issd.setText("SD卡");
            }else {
                viewHolder.tv_itemsoftmanager_issd.setText("手机内存");
            }

            //判断应用是否解锁和解锁
            if(watchDogDao.queryLockApp(appInfo.getPackageName())){
                //加锁
                viewHolder.iv_softmanager_islock.setImageResource(R.drawable.lock);
            }else  {
                //解锁
                viewHolder.iv_softmanager_islock.setImageResource(R.drawable.unlock);
            }


            return view;
        }
    }

    static class ViewHolder{
        ImageView iv_itemsoftmanage_icon,iv_softmanager_islock;
        TextView tv_itemsoftmanager_name,tv_itemsoftmanager_issd,tv_itemsoftmanager_version;
    }
}
