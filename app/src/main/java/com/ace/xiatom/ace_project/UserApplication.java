package com.ace.xiatom.ace_project;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationManager;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by xiatom on 2019/4/25.
 */

public class UserApplication extends Application {
    private String name;
    private String password;
    private XMPPTCPConnection connection;
    private Notification notification;
    private Bitmap photo;
    private String ip;
    private Context context;
    private LocationManager locationManager;
    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public XMPPTCPConnection getConnection() {
        return connection;
    }

    public void setConnection(XMPPTCPConnection connection) {
        this.connection = connection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
