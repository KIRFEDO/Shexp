package com.example.myapplication;

import java.io.Serializable;

public class EventListElement implements Serializable {
    String eventName;
    String ownerName;
    Integer ownerAvatarId;

    public EventListElement(String eventName, String ownerName, Integer ownerAvatarId) {
        this.eventName = eventName;
        this.ownerName = ownerName;
        this.ownerAvatarId = ownerAvatarId;
    }
}
