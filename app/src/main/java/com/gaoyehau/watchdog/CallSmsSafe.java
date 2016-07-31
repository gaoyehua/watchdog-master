package com.gaoyehau.watchdog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoyehua.bean.BlackNumInfo;
import com.gaoyehua.db.dao.BalackNumDao;
import com.gaoyehua.utils.MyAsycnTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyehua on 2016/7/24.
 */
public class CallSmsSafe extends Activity {
    private ListView lv_callsmssafe_blacknum;
    private ProgressBar callsmssafe_loading;
    private List<BlackNumInfo> list;
    private BalackNumDao blackNumDao;
    private TextView tv_callsmssafe_blacknum;
    private MyAdapter myAdapter;
    private  TextView tv_callsmssafe_mode;
    private ImageView iv_callsmssafe_delete;
    private AlertDialog dialog;
    //查询的总个数
    private final int MAXNUM=20;
    //起始位置
    private int startIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsmssafe);

        list =new ArrayList<BlackNumInfo>();
        list.clear();
        blackNumDao = new BalackNumDao(getApplicationContext());
        lv_callsmssafe_blacknum =(ListView) findViewById(R.id.lv_callsmssafe_blacknum);
        callsmssafe_loading =(ProgressBar) findViewById(R.id.callsmssafe_loading);

        fillData();

        //listview滑动监听事件
        lv_callsmssafe_blacknum.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当滑动状态改变的时候调用的方法
            //view : listview
            //scrollState : 滑动状态
            //SCROLL_STATE_IDLE : 空闲的状态
            //SCROLL_STATE_TOUCH_SCROLL : 缓慢滑动的状态
            //SCROLL_STATE_FLING : 快速滑动
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当listview静止的时候判断界面显示的最后一个条目是否是查询数据的最后一个条目,是加载下一波数据,不是用户进行其他操作
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //获取界面显示最后一个条目
                    int position = lv_callsmssafe_blacknum.getLastVisiblePosition();//获取界面显示最后一个条目,返回的时候条目的位置
                    //判断是否是查询数据的最后一个数据  20   0-19
                    if (position == list.size()-1) {
                        //加载下一波数据
                        //更新查询的其实位置   0-19    20-39
                        startIndex+=MAXNUM;
                        fillData();
                    }
                }
            }
            //当滑动的时候调用的方法
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void fillData() {
        new MyAsycnTask() {
            //查询数据之前
            @Override
            public void pretask() {
                callsmssafe_loading.setVisibility(View.VISIBLE);
            }
           //查询数据
            @Override
            public void doingtask() {

                if(list ==null) {
                    //1.2.3    4.5.6
                    list = blackNumDao.getPartBlackNum(MAXNUM,startIndex);
                }else{
                    //addAll : 将一个集合整合到另一个集合
                    //A [1.2.3] B[4.5.6]
                    //A.addAll(B)  A [1.2.3.4.5.6]
                    list.addAll(blackNumDao.getPartBlackNum(MAXNUM,startIndex));
                }
                 //   list = blackNumDao.queryAllBlackNum();
            }
            //子线程中显示数据
            @Override
            public void posttask() {
                //lv_callsmssafe_blacknum.setAdapter(new MyAdapter());
                // callsmssafe_loading.setVisibility(View.VISIBLE);
                if (myAdapter == null) {
                    myAdapter = new MyAdapter();
                    lv_callsmssafe_blacknum.setAdapter(myAdapter);
                }else{
                    myAdapter.notifyDataSetChanged();
                }
                callsmssafe_loading.setVisibility(View.INVISIBLE);
            }
        }.execute();

    }
    public class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {

                return list.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final BlackNumInfo blackNumInfo = list.get(position);
            View view;
            ViewHolder viewHolder;
            if(convertView == null){
            view =View.inflate(getApplicationContext(),R.layout.item_callsmssafe,null);
                //创建新的新的容器
                viewHolder =new ViewHolder();
                viewHolder.tv_callsmssafe_blacknum =(TextView) view.findViewById(R.id.tv_callsmssafe_blacknum);
                viewHolder.tv_callsmssafe_mode =(TextView) view.findViewById(R.id.tv_callsmssafe_mode);
                viewHolder.iv_callsmssafe_delete =(ImageView) view.findViewById(R.id.iv_callsmssafe_delete);
                //绑定容器
                view.setTag(viewHolder);

            }else{
                view=convertView;
                //
                viewHolder =(ViewHolder) view.getTag();
            }
            //设置显示数据
            viewHolder.tv_callsmssafe_blacknum.setText(blackNumInfo.getBlacknum());
            //根据获取的位置获得bean对象

            //tv_callsmssafe_blacknum .setText(blackNumInfo.getBlacknum());
            int mode =blackNumInfo.getMode();
            switch (mode){
                case BalackNumDao.CALL:
                    viewHolder.tv_callsmssafe_mode.setText("电话拦截");
                    break;
                case BalackNumDao.SMS:
                    viewHolder.tv_callsmssafe_mode.setText("短信拦截");
                    break;
                case BalackNumDao.ALL:
                    viewHolder.tv_callsmssafe_mode.setText("全部拦截");
                    break;
            }
            //删除黑名单
            viewHolder.iv_callsmssafe_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除黑名单的操作
                    AlertDialog.Builder builder =new AlertDialog.Builder(CallSmsSafe.this);
                    builder.setMessage("您确定要删除黑名单号码："+blackNumInfo.getBlacknum()+"?");
                    //设置确定和取消按钮
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除黑名单数据库中的号码
                            blackNumDao.deleteBlackNum(blackNumInfo.getBlacknum());
                            //删除界面的显示的黑名单的号码
                            list.remove(position);
                            //更新界面
                            myAdapter.notifyDataSetChanged();
                            //隐藏对话框
                            dialog.dismiss();

                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();

                }
            });

            return view;
        }
    }

    //
    class ViewHolder {

        TextView tv_callsmssafe_blacknum,tv_callsmssafe_mode;
        ImageView iv_callsmssafe_delete;
    }
    public void addBlackNum(View view){
        //弹出对话框,让用户去添加黑名单
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        view = View.inflate(getApplicationContext(), R.layout.dialog_addblacknum, null);
        //初始化控件,执行添加操作
        final EditText et_addblacknum_blacknum = (EditText) view.findViewById(R.id.et_addblacknum_blacknum);
        final RadioGroup rg_addblacknum_modes = (RadioGroup) view.findViewById(R.id.rg_addblacknum_modes);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);

        //设置按钮的点击事件
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //添加黑名单号码操作
                //1.获取输入的黑名单号码
                String blacknum = et_addblacknum_blacknum.getText().toString().trim();
                //2.判断获取的内容是否为空
                if (TextUtils.isEmpty(blacknum)) {
                    Toast.makeText(getApplicationContext(), "请输入黑名单号码", 0).show();
                    return;
                }
                //3.获取拦截模式
                int mode = -1;
                int radioButtonId = rg_addblacknum_modes.getCheckedRadioButtonId();//获取选中的RadioButton的id
                switch (radioButtonId) {
                    case R.id.rb_addblacknum_tel:
                        //电话拦截
                        mode = BalackNumDao.CALL;
                        break;
                    case R.id.rb_addblacknum_sms:
                        //短信拦截
                        mode = BalackNumDao.SMS;
                        break;
                    case R.id.rb_addblacknum_all:
                        //全部拦截
                        mode = BalackNumDao.ALL;
                        break;
                }
                //4.添加黑名单
                //1.添加到数据库
                blackNumDao.addBlackNum(blacknum, mode);
                //2.添加到界面显示
                //2.1添加到list集合中
                //list.add(new BlackNumInfo(blacknum, mode));
                list.add(0, new BlackNumInfo(blacknum, mode));//location : 参数2要添加到位置,参数2:添加数据
                //2.2更新界面
                myAdapter.notifyDataSetChanged();
                //隐藏对话哭给你
                dialog.dismiss();
            }
        });
        //设置取消按钮的点击事件
        btn_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //隐藏对话框
                dialog.dismiss();
            }
        });

        builder.setView(view);
        //builder.show();
        dialog = builder.create();
        dialog.show();
    }

    }



