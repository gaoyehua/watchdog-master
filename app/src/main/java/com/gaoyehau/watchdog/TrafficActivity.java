package com.gaoyehau.watchdog;

import android.app.Activity;
import android.os.Bundle;

import java.security.AccessControlContext;

/**
 * Created by gaoyehua on 2016/7/30.
 * 功能：实现手机流量统计的功能
 *
 */
public class TrafficActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
    }
}
