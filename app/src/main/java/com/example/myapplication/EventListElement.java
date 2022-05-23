package com.example.myapplication;

public class EventListElement {
    String eventName;
    String ownerName;
    Integer ownerAvatarId;

    public EventListElement(String eventName, String ownerName, Integer ownerAvatarId) {
        this.eventName = eventName;
        this.ownerName = ownerName;
        this.ownerAvatarId = ownerAvatarId;
    }
}
