package com.ace.xiatom.ace_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.location.LocationClient;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by xiatom on 2019/4/25.
 */

public class Login_init {

    UserApplication userApplication;

    public void Connect(UserApplication application) {

        this.userApplication = application;
        Thread connectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
                try {
                    builder.setXmppDomain("localhost");
                    builder.setHostAddress(InetAddress.getByName(userApplication.getIp()));
                    builder.setPort(5222);
                    builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                    builder.setCompressionEnabled(true);
                    builder.setSendPresence(true);
                    XMPPTCPConnection xmpptcpConnection = new XMPPTCPConnection(builder.build());
                    if (!xmpptcpConnection.isConnected()) {
                        Log.i("msg", "connect");
                        xmpptcpConnection.connect();
                    } else {
                        Log.i("msg", "already connect");
                    }
                    Presence presence = new Presence(Presence.Type.available);
                    presence.setStatus("在线");
                    //设置在线
                    xmpptcpConnection.sendStanza(presence);
                    xmpptcpConnection.login(userApplication.getName(), userApplication.getPassword());
                    userApplication.setConnection(xmpptcpConnection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        connectThread.start();


        Thread changeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = userApplication.getContext();
                    LocationManager locationManager = userApplication.getLocationManager();
                    String provider = LocationManager.GPS_PROVIDER;// 指定LocationManager的定位方法
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        Log.i("msg", "not");
                    Location location;
                    while ((location = locationManager.getLastKnownLocation(provider)) == null) {
                        Log.i("msg", "location null");
                        Thread.sleep(2000);
                    }
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    Log.i("msg", lat+" "+Math.abs(lon)+"");
                    changeLocation(lat,Math.abs(lon));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        changeThread.start();


        userApplication.getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 200, locationListener);

    }

    private void changeLocation(double latt,double lonn) {
        final double lat = latt;
        final double lon = lonn;
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
                    out.writeBytes("latitude=" + lat + "&longitude=" + lon+"&askLoc=null");
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String string;
                    if ((string = br.readLine()) != null) {
                        Log.i("msg", string);
                    }
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i("msg", "changed");
            if(location!=null)
                 changeLocation(location.getAltitude(),Math.abs(location.getLongitude()));
        }

        @Override
        public void onProviderDisabled(String arg0) {
        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        }

    };
}
