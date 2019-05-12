package com.ace.xiatom.ace_project;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.xiatom.ace_project.bean.Event;
import com.ace.xiatom.ace_project.view.DataView;
import com.ace.xiatom.ace_project.bean.DateEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class
CalendarActivity extends AppCompatActivity {
    private DataView dataView ;
    private TextView info ;
    private SQLiteDatabase db;
    private calendarEventSQLite dbHelper;
    CalendarBoxManager calendarBoxManager;List<Event> events;
    LinearLayout eventLayout;
    LinearLayout addLayout;
    DateEntity selectedDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        dbHelper = new calendarEventSQLite(this,"homework.db",null,1);
        db = dbHelper.getWritableDatabase();
        calendarBoxManager = new CalendarBoxManager(db);
        events = calendarBoxManager.getEvent();

        info =findViewById(R.id.info);
        dataView =  findViewById(R.id.week);

        eventLayout= findViewById(R.id.events);
        addLayout = findViewById(R.id.addeventform);
        Button add = findViewById(R.id.addevent);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLayout.setVisibility(View.VISIBLE);
                eventLayout.setVisibility(View.INVISIBLE);
            }
        });

        Button submit = findViewById(R.id.submitEvent);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText year = findViewById(R.id.getYear);
                EditText month = findViewById(R.id.getMouth);
                EditText day = findViewById(R.id.getDay);
                String y = year.getText().toString();
                String m = month.getText().toString();
                String d = day.getText().toString();
                EditText event = findViewById(R.id.event);
                String eventString = event.getText().toString();

                String date = y+(m.length()==1?"-0":"-")+m+"-"+d;
                Event newEvent = new Event(date,eventString);
                year.setText("");
                month.setText("");
                day.setText("");
                events.add(newEvent);
                calendarBoxManager.insertMeg(newEvent);
                addLayout.setVisibility(View.INVISIBLE);
                eventLayout.setVisibility(View.VISIBLE);

                if(selectedDate.date.equals(date)){
                    info.setText("日期："+ selectedDate.date+"\n"+
                            "星期："+ selectedDate.weekName+"\n"+
                            "纪念日：\n"+getEventByDate(selectedDate.date));
                }

            }
        });

        Button del = findViewById(R.id.delevent);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarBoxManager.delMsg(selectedDate.date);
                events = calendarBoxManager.getEvent();
                info.setText("日期："+ selectedDate.date+"\n"+
                        "星期："+ selectedDate.weekName+"\n"+
                        "纪念日：\n"+getEventByDate(selectedDate.date));
            }
        });


        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {
                selectedDate = date;
                info.setText("日期："+ date.date+"\n"+
                        "星期："+ date.weekName+"\n"+
                        "纪念日：\n"+getEventByDate(date.date));
            }
        });
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dataView.getData(df.format(d));
    }

    public StringBuilder getEventByDate(String date){
        StringBuilder result = new StringBuilder();
        for(Event e:events){
            if(e.date.equals(date))
                result.append(e.event+"\n");
        }
        return result;
    }
}
