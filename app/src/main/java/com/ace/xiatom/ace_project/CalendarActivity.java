package com.ace.xiatom.ace_project;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ace.xiatom.ace_project.view.DataView;
import com.ace.xiatom.ace_project.bean.DateEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class
CalendarActivity extends AppCompatActivity {
    private DataView dataView ;
    private TextView info ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        info =findViewById(R.id.info);
        dataView =  findViewById(R.id.week);
        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {
                info.setText("日期："+ date.date+"\n"+
                        "周几："+ date.weekName+"\n"+
                        "今日："+ date.isToday+"\n"+
                        "时间戳："+ date.million+"\n");
            }
        });
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dataView.getData(df.format(d));
    }
}
