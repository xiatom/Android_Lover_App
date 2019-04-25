package com.ace.xiatom.ace_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatActivityOld extends AppCompatActivity {

    TextView tv ;
    String content = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tv = findViewById(R.id.chat_content);
        Button server = findViewById(R.id.server);
        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            ServerSocket ss = new ServerSocket(6667);
                            Socket s;
                            BufferedReader bis = null;
                            String str;
                            Log.i("msg","s");
                                s = ss.accept();
                                Log.i("msg","run");
                                try {
                                    bis = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                    str = bis.readLine();
                                    content+=str+"\n";
                                    tv.setText(content);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            bis.close();
                            ss.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText msg = findViewById(R.id.msg);
                final String message = msg.getText().toString();
                content+=message+"\n";
                tv.setText(content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Socket socket = new Socket("10.240.252.96", 6666);
                            OutputStream ops = socket.getOutputStream();
                            OutputStreamWriter osw = null;
                            osw = new OutputStreamWriter(ops);
                            osw.write(message);
                            osw.close();
                            socket.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });
    }



}
