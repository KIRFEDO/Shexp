package com.example.myapplication;

import java.util.ArrayList;

public class UserEvents {
    String uid;
    ArrayList<String> events;
    UserEvents(){};

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public UserEvents(String uid, ArrayList<String> events) {
        this.uid = uid;
        this.events = events;
    }
}
