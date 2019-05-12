package com.ace.xiatom.ace_project.bean;

import java.util.Date;

/**
 * Created by xiatom on 2019/5/12.
 */

public class Event {
    public int id;
    public String date;
    public String event;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event( String date, String  event){
        this.event = event;
        this.date = date;
    }

    public Event(int id, String date, String  event){

        this.id = id;
        this.event = event;
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
