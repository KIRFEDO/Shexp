package com.example.myapplication;

import java.util.ArrayList;

public class HelperEventClass {

    String eventName;
    String ownerUid;
    ArrayList<String> addedUsersUID;

    public HelperEventClass(){};

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public ArrayList<String> getAddedUsersUID() {
        return addedUsersUID;
    }

    public void setAddedUsersUID(ArrayList<String> addedUsersUID) {
        this.addedUsersUID = addedUsersUID;
    }

    public HelperEventClass(String eventName, String ownerUid, ArrayList<String> addedUsersUID) {
        this.eventName = eventName;
        this.ownerUid = ownerUid;
        this.addedUsersUID = addedUsersUID;
    }
}
