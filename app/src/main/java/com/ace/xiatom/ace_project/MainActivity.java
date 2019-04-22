package com.ace.xiatom.ace_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;

=======
>>>>>>> b8a77e47abcd19da0b2aee981f2deeb6526a48a7
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_bar);
        LinearLayout l = findViewById(R.id.contentLayout);
        getLayoutInflater().inflate(R.layout.content_main,l);
        //设置顶部actionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //侧边栏
        DrawerLayout drawer = findViewById(R.id.drawer_layout);





//        NavigationView s = findViewById(R.id.nav_view);
//        Button photo =  s.findViewById(R.id.btn);
//        photo.setOnClickListener(photoClick);
//        Log.i("msg",photo.toString());

        //toolbar上有点出抽屉图标
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //侧边栏的抽屉
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //进入注册界面
        View headview=navigationView.inflateHeaderView(R.layout.side_bar_head);
        ImageView head_iv= headview.findViewById(R.id.photo);
        head_iv.setOnClickListener(photoClick);


        //底部菜单选择
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public View.OnClickListener photoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    };

    //底部菜单监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.head_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();

//            startActivity(new Intent(this, class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottom_bar_items view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
<<<<<<< HEAD
            startActivity(new Intent(MainActivity.this,ChatActivity.class));
=======
>>>>>>> b8a77e47abcd19da0b2aee981f2deeb6526a48a7
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            LinearLayout l = findViewById(R.id.contentLayout);
            l.removeAllViews();
            getLayoutInflater().inflate(R.layout.content_main,l);
            Toast.makeText(this, "gallery", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "sildeshow", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "tools", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
<<<<<<< HEAD
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    DataOutputStream out;
                    String string;
                    try {
                            URL url = new URL("http", "10.240.252.96", 8080, "/Android_User_Database/Login_submit");
                            connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            out = new DataOutputStream(connection.getOutputStream());
                            out.writeBytes("name=ace&password=1023");
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            if((string=br.readLine())!=null) {
                                System.out.println(string);
                                Log.i("msg",string);
                            }
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            connection.disconnect();
                        }
                }
            }).start();
=======
            Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
>>>>>>> b8a77e47abcd19da0b2aee981f2deeb6526a48a7

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
