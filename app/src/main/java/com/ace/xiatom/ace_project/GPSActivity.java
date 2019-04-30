package com.ace.xiatom.ace_project;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GPSActivity extends Activity {
    BaiduMap mBaiduMap;
    LocationClient mLocationClient;
    private MapView mMapView = null;
    ImageButton locatSelf;
    ImageButton locatHer;
    UserApplication userApplication ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userApplication = (UserApplication)getApplication();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_gps);
        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

       // 设置地图属性
        MyLocationConfiguration mMapConfig = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL
                ,false, BitmapDescriptorFactory.fromResource(R.drawable.map_marker),
                0xAAFFFF88,0xAA00FF00);
        mBaiduMap.setMyLocationConfiguration(mMapConfig);

        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);

    //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        mLocationClient.setLocOption(option);
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();
        locatSelf = findViewById(R.id.findSelf);
        locatSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = new LatLng(mLocationClient.getLastKnownLocation().getLatitude(), mLocationClient.getLastKnownLocation().getLongitude());
                //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //改变地图状态
                mBaiduMap.setMapStatus(msu);
            }
        });

        locatHer = findViewById(R.id.findHer);
        locatHer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http", userApplication.getIp(), 8080, "/Android_User_Database/getLoc");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setConnectTimeout(5000);
                            connection.setDoOutput(true);
                            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                            out.writeBytes("askLoc=true");
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String al ;String ln;
                            if((al= br.readLine())!=null){
                                ln  = br.readLine();
                                LatLng herlocation = new LatLng(Double.parseDouble(al), Double.parseDouble(ln));
                                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(herlocation);
                                //改变地图状态
                                BitmapDescriptor hericon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker);
                                OverlayOptions heroption = new MarkerOptions()
                                        .position(herlocation)
                                        .icon(hericon);
                                mBaiduMap.addOverlay(heroption);
                                mBaiduMap.setMapStatus(msu);
                            }
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    class MyLocationListener extends BDAbstractLocationListener {

        Boolean first = true;
        MyLocationData locData;
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            mBaiduMap.setMyLocationData(locData);

            if(first){
                first = false;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //改变地图状态
                mBaiduMap.setMapStatus(msu);
            }
        }
    }
}
