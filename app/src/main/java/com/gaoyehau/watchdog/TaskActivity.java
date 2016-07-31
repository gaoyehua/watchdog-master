package com.gaoyehau.watchdog;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoyehua.bean.AppInfo;
import com.gaoyehua.bean.TaskInfo;
import com.gaoyehua.engine.AppEngine;
import com.gaoyehua.engine.TaskEngine;
import com.gaoyehua.utils.MyAsycnTask;
import com.gaoyehua.utils.TaskUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyehua on 2016/7/27.
 */
public class TaskActivity extends Activity {
    private ListView lv_task_processes;
    private ProgressBar loading;
    private List<TaskInfo> list;
    //用户程序集合
    private List<TaskInfo> userappinfo;
    //系统程序的集合
    private List<TaskInfo> systemappinfo;
    private MyAdapter myadapter;
    TaskInfo taskInfo ;

    private TextView tv_task_processnum;
    private TextView tv_task_memory;
    private int processCount;
    private boolean isshow =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        lv_task_processes =(ListView) findViewById(R.id.lv_task_processes);
        loading =(ProgressBar) findViewById(R.id.loading);

        tv_task_processnum=(TextView) findViewById(R.id.tv_task_processnum);
        tv_task_memory =(TextView) findViewById(R.id.tv_task_memory);
        //进程的个数
       processCount =TaskUtils.getProcessCount(getApplicationContext());
        tv_task_processnum.setText("运行中的进程:\n"+processCount+"个");
        //可用的内存
        long availableRom =TaskUtils.availableRom(getApplicationContext());
        //数据转换
        String availaRom =Formatter.formatFileSize(getApplicationContext(),availableRom);
        //获取总的内存
        //根据SDK来判断使用那个方法
        int SDK = Build.VERSION.SDK_INT;
        long totalRom;
        if(SDK>=16){
            totalRom =TaskUtils.totalRom(getApplicationContext());
        }else {
            totalRom =TaskUtils.totalRom();
        }
        //数据转换
        String totalRam =Formatter.formatFileSize(getApplicationContext(),totalRom);
        tv_task_memory.setText("剩余/总内存:\n" + availaRom + "/"
                + totalRam);

        //加载数据
        fillData();
        listViewItemClick();

    }
    //条目的点击事件

    private void listViewItemClick() {
        lv_task_processes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //1.屏蔽用户程序和系统的程序
                if(position ==0 && position ==userappinfo.size()+1){
                    return;
                }
                //2.获取条目所对应的应用程序的信息
                //数据就要从userappinfo和systemappinfo中获取
                if (position <= userappinfo.size()) {
                    //用户程序
                    taskInfo = userappinfo.get(position-1);
                }else{
                    //系统程序
                    taskInfo = systemappinfo.get(position - userappinfo.size() - 2);
                }

                //checkbox的状态的变化值
                if(taskInfo.ischecked()){
                    taskInfo.setIschecked(false);
                }else {
                    //当前应用不能进行选择
                    if(!taskInfo.getPackageName().equals(getPackageName())){
                        taskInfo.setIschecked(true);
                    }

                }
                //更新界面
                //myadapter.notifyDataSetChanged();
                ViewHolder viewHolder =(ViewHolder) view.getTag();
                viewHolder.cb_itemtask_check.setChecked(taskInfo.ischecked());

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
                list = TaskEngine.getTaskAllInfo(getApplicationContext());
                //拆分用户程序和系统程序
                userappinfo = new ArrayList<TaskInfo>();
                systemappinfo = new ArrayList<TaskInfo>();
                for(TaskInfo taskInfo :list){
                    if(taskInfo.isUser()){
                        userappinfo.add(taskInfo);

                    }else {
                        systemappinfo.add(taskInfo);
                    }
                }

            }

            @Override
            public void posttask() {
                if (myadapter == null) {
                    myadapter = new MyAdapter();
                    lv_task_processes.setAdapter(myadapter);
                }else{
                    myadapter.notifyDataSetChanged();
                }
                loading.setVisibility(View.INVISIBLE);

            }
        }.execute();

    }
    private class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return isshow ==true ? userappinfo.size()+1+systemappinfo.size()+1 :userappinfo.size()+1;
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
                textview.setText("用户进程("+userappinfo.size()+")");

                return textview;
            }else if(position ==userappinfo.size()+1){
                TextView textview =new TextView(getApplicationContext());
                textview.setBackgroundColor(Color.GRAY);
                textview.setTextColor(Color.WHITE);
                textview.setTextSize(20);
                textview.setText("系统进程("+systemappinfo.size()+")");

                return textview;

            }


            View view;
            ViewHolder viewHolder;
            //instanceof检查是否为类的对象
            if(convertView !=null && convertView instanceof RelativeLayout){

                view =convertView;
                viewHolder =(ViewHolder) view.getTag();

            }else {
                view=View.inflate(getApplication(),R.layout.item_task,null);
                viewHolder =new ViewHolder();
                viewHolder.iv_itemtask_icon=(ImageView) view.findViewById(R.id.iv_itemtask_icon);
                viewHolder.tv_itemtask_name =(TextView) view.findViewById(R.id.tv_itemtask_name);
                viewHolder.tv_itemtask_memory =(TextView) view.findViewById(R.id.tv_itemtask_memory);
                viewHolder.cb_itemtask_check =(CheckBox) view.findViewById(R.id.cb_itemtask_check);
                //将viewHoder与view对象绑定
                view.setTag(viewHolder);
            }
            //获取应用信息
            //数据要从user和System中获取
            if(position <=userappinfo.size()){
                taskInfo =userappinfo.get(position-1);

            }else {
                taskInfo =systemappinfo.get(position-userappinfo.size()-2);
            }

            //显示信息
            //设置显示数据,null.方法    参数为null
            if (taskInfo.getIcon() == null) {
                viewHolder.iv_itemtask_icon.setImageResource(R.drawable.ic_default);
           }else {
            viewHolder.iv_itemtask_icon.setImageDrawable(taskInfo.getIcon());

            }
            //名称为空的可以设置为包名
            if (TextUtils.isEmpty(taskInfo.getName())) {
                viewHolder.tv_itemtask_name.setText(taskInfo.getPackageName());
            }else{
                viewHolder.tv_itemtask_name.setText(taskInfo.getName());
            }

            long ramsize =taskInfo.getRamSize();
            //数据转换
            String formatFileSize = Formatter.formatFileSize(getApplicationContext(),ramsize);
            viewHolder.tv_itemtask_memory.setText("占用内存:"+formatFileSize);

            //根据CheckBox的状态来确定是否选中
            if(taskInfo.ischecked()){
                viewHolder.cb_itemtask_check.setChecked(true);

            }else {
                viewHolder.cb_itemtask_check.setChecked(false);
            }

            //隐藏对话框，在geview中有if必须出现else，否则会出现问题
            if(taskInfo.getPackageName().equals(getPackageName())){
                viewHolder.cb_itemtask_check.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.cb_itemtask_check.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }

    static class ViewHolder{
        ImageView iv_itemtask_icon;
        TextView tv_itemtask_name,tv_itemtask_memory;
        CheckBox cb_itemtask_check;
    }

    //
    public void all(View v){
        //用户程序
        for(int i=0;i< userappinfo.size();i++){
            if (!userappinfo.get(i).getPackageName().equals(getPackageName())) {
                userappinfo.get(i).setIschecked(true);
            }

        }
        //系统程序
        for(int i=0;i< systemappinfo.size();i++){
            systemappinfo.get(i).setIschecked(true);
        }
        //更新界面
            myadapter.notifyDataSetChanged();

    }
    //
    public void cancel(View v){
        //用户程序
        for(int i=0;i< userappinfo.size();i++){
            userappinfo.get(i).setIschecked(false);
        }
        //系统程序
        for(int i=0;i< systemappinfo.size();i++){
            systemappinfo.get(i).setIschecked(false);
        }
        //更新界面
        myadapter.notifyDataSetChanged();

    }
    //
    public void clean(View v){
        //获取进程管理者
        ActivityManager activitymanager =(ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //杀掉进程
        List<TaskInfo> deleteInfo =new ArrayList<TaskInfo>();

        //用户进程
        for(int i=0; i< userappinfo.size();i++){
            if(userappinfo.get(i).ischecked()){

                activitymanager.killBackgroundProcesses(userappinfo.get(i).getPackageName());
                deleteInfo.add(userappinfo.get(i));

            }
        }
        //系统进程
        for(int i=0; i< systemappinfo.size();i++){
            if(systemappinfo.get(i).ischecked()){

                activitymanager.killBackgroundProcesses(systemappinfo.get(i).getPackageName());
                deleteInfo.add(systemappinfo.get(i));

            }
        }
        long memory =0;
        //遍历杀死的进程
        for(TaskInfo taskInfo: deleteInfo){
            if(taskInfo.isUser()){
                userappinfo.remove(taskInfo);
            }else {
                systemappinfo.remove(taskInfo);
            }
            memory+=taskInfo.getRamSize();
        }
        //这里只是字节，还要进项数据转换
         String formatter=Formatter.formatFileSize(getApplicationContext(),memory);
        Toast.makeText(getApplicationContext(),"共清理了:"+deleteInfo.size()+"个进程,共释放:"
                +formatter+"内存空间",Toast.LENGTH_SHORT).show();

        //更改清理后进程的个数和剩余的总内存
        processCount = processCount - deleteInfo.size();
        tv_task_processnum.setText("运行中进程:\n" + processCount + "个");

        // 更改剩余总内存,重新获取剩余总内存
        // 获取剩余,总内存'
        long availableRam = TaskUtils.availableRom(getApplicationContext());
        // 数据转化
        String availaRam = Formatter.formatFileSize(getApplicationContext(),
                availableRam);
        // 获取总内存
        // 根据不同的sdk版去调用不同的方法
        // 1.获取当前的sdk版本
        int sdk = android.os.Build.VERSION.SDK_INT;
        long totalRam;
        if (sdk >= 16) {
            totalRam = TaskUtils.totalRom(getApplicationContext());
        } else {
            totalRam = TaskUtils.totalRom();
        }
        // 数据转化
        String totRam = Formatter.formatFileSize(getApplicationContext(),
                totalRam);
        tv_task_memory.setText("剩余/总内存:\n" + availaRam + "/"
                + totRam);


        //为下次清理做准备
        deleteInfo.clear();
        deleteInfo=null;
        //更新界面
        myadapter.notifyDataSetChanged();

    }
    //
    public void setting(View v){
        //设置是否显示系统应用
        isshow =!isshow;
        //更新界面
        myadapter.notifyDataSetChanged();

    }

}


