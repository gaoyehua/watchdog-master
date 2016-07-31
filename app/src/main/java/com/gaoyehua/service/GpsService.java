package com.gaoyehua.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.List;

/**
 * Created by gaoyehua on 2016/7/21.
 */
public class GpsService extends Service {

    private SharedPreferences sp;
    private LocationManager locationManager;
    private LocationListener myLocationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("comfig", MODE_PRIVATE);

        //获取定位管理
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);

        //获取所有的定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String string : providers) {
            Log.i("GpsService", string);
        }
        //获取最佳的定位方式
        Criteria criteria = new Criteria();//设置定位的属性，决定使用什么方式来定位
        criteria.setAltitudeRequired(true);//设置是否定义海拔
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Log.i("GpsService", "最好的定位方式：" + bestProvider);
        myLocationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);


    }

    private class MyLocationListener implements LocationListener {

        //定位位置改变的时候使用
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();//获取纬度,平行
            double longitude = location.getLongitude();//获取经度
            //给安全号码发送一个经度和纬度的信息
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sp.getString("safenum", "5556"), null,
                    "longitude:" + longitude + "   latitude:" + latitude, null, null);
            //停止服务
            stopSelf();
        }

        //定位状态改变的会后使用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        //定位可用的时候使用
        @Override
        public void onProviderEnabled(String provider) {

        }

        //定位不可用的时候使用
        @Override
        public void onProviderDisabled(String provider) {

        }

    }
    //关闭GPS，高版本已经不允许用代码实现

}
