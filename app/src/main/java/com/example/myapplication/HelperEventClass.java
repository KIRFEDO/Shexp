package com.example.myapplication;

import java.util.ArrayList;

public class HelperEventClass {

    String eventName;
    String owner;
    ArrayList<Integer> addedUsers;

    public HelperEventClass(){};

    public HelperEventClass(String eventName, String owner, ArrayList<Integer> addedUsers) {
        this.eventName = eventName;
        this.owner = owner;
        this.addedUsers = addedUsers;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Integer> getAddedUsers() {
        return addedUsers;
    }

    public void setAddedUsers(ArrayList<Integer> addedUsers) {
        this.addedUsers = addedUsers;
    }
}
