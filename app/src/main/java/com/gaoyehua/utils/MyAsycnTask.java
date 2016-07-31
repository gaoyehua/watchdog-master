package com.gaoyehua.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by 高业华 on 2016/7/20.
 */
public abstract class MyAsycnTask {

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
                        posttask();
            }

        };
        //在子线程之前执行的方法
        public abstract void pretask();

        //在子线程之中执行的方法
        public abstract void doingtask();

        //在子线程之后的执行方法
        public abstract void posttask();

        //执行
        public void execute() {
            pretask();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doingtask();
                    handler.sendEmptyMessage(0);
                }
            }).start();
        }
}