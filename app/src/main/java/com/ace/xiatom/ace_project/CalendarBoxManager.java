package com.ace.xiatom.ace_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ace.xiatom.ace_project.bean.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xiatom on 2019/4/24.
 */

public class CalendarBoxManager {

    SQLiteDatabase db;
    public CalendarBoxManager(SQLiteDatabase db){
        this.db = db;
    }
    public List<Event> getEvent(){
        List<Event> events = new LinkedList<>();
        Cursor cursor = db.query("event",null,null,null,null,null,null);
        if(cursor.moveToFirst())
            do{
                String event = cursor.getString(cursor.getColumnIndex("event"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                events.add(new Event(id,date,event));
            }while(cursor.moveToNext());
        return events;
    }

    public void insertMeg(Event m){
        ContentValues values = new ContentValues();
        values.put("date",m.getDate());
        values.put("event",m.getEvent());
        db.insert("event",null,values);
        Log.i("db","insert suc");
    }

    public void delMsg(String m){
        db.delete("event","date=?",new String[]{m});
    }
}
