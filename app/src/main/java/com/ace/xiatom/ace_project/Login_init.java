package com.ace.xiatom.ace_project;

import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.net.InetAddress;

/**
 * Created by xiatom on 2019/4/25.
 */

public class Login_init {

    UserApplication userApplication;
    public void Connect(UserApplication application){
        this.userApplication = application;
        new Thread(new Runnable() {
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
        }).start();
    }
}
