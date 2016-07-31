package com.gaoyehua.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoyehau.watchdog.R;

/**
 * Created by gaoyehua on 2016/7/30.
 * 功能： 添加SD卡碎片
 *
 */
public class sdFragment extends Fragment {
    //1.初始化操作
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //2.Fragment与活动关联,设置Fagment的布局

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ////参数1:布局文件
        //参数2:容器
        //参数3:自动挂载  ,一律false
        View view =inflater.inflate(R.layout.fragment_sd,container,false);
        return view;
    }
}
