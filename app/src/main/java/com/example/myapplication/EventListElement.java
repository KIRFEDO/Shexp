package com.example.myapplication;

public class EventListElement {
    String eventName;
    String ownerName;
    Integer ownerAvatarId;
    Integer eventId;

    public EventListElement(String eventName, String ownerName, Integer ownerAvatarId, Integer eventId) {
        this.eventName = eventName;
        this.ownerName = ownerName;
        this.ownerAvatarId = ownerAvatarId;
        this.eventId = eventId;
    }
}
