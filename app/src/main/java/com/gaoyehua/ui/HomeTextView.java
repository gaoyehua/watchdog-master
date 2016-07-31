package com.gaoyehua.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 高业华 on 2016/7/17.
 */
public class HomeTextView extends TextView {


    //在代码中调用
    public HomeTextView(Context context) {
        super(context);
    }
    //在布局文件中调用

    public HomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //也是布局文件，多了一个参数

    public HomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //设置自定义的TextView

    @Override
    public boolean isFocused() {
        //true获取焦点，false不获取焦点
        return true;
    }
}

