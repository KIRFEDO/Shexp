package com.example.myapplication;

import androidx.constraintlayout.solver.widgets.Helper;

import java.util.ArrayList;
import java.util.HashMap;

public class HelperEvent {

    String eventName;
    String ownerUid;
    ArrayList<String> addedUsersUID;
    HashMap<String, HelperItem> items;

    HelperEvent(){};

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

    public HashMap<String, HelperItem> getItems() {
        return items;
    }

    public void setItems(HashMap<String, HelperItem> items) {
        this.items = items;
    }

    public HelperEvent(String eventName, String ownerUid, ArrayList<String> addedUsersUID, HashMap<String, HelperItem> items) {
        this.eventName = eventName;
        this.ownerUid = ownerUid;
        this.addedUsersUID = addedUsersUID;
        this.items = items;
    }

    public HelperEvent(String eventName, String ownerUid, ArrayList<String> addedUsersUID) {
        this.eventName = eventName;
        this.ownerUid = ownerUid;
        this.addedUsersUID = addedUsersUID;
        this.items = new HashMap<String, HelperItem>();
    }
}
