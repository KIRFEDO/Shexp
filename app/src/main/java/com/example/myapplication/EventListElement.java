package com.example.myapplication;

public class EventListElement {
    String event_name;
    Integer event_id;
    String owner_name;
    Integer owner_id;
    Integer avatar_id;

    public EventListElement(String event_name, Integer event_id, String owner_name, Integer owner_id, Integer avatar_id) {
        this.event_name = event_name;
        this.event_id = event_id;
        this.owner_name = owner_name;
        this.owner_id = owner_id;
        this.avatar_id = avatar_id;
    }
}
