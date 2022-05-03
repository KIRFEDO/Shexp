package com.example.myapplication;

public class EventItemListElement {
    String eventName;
    String ownerName;
    Integer itemId;
    Integer itemCost;

    public EventItemListElement(String eventName, String ownerName, Integer itemId, Integer itemCost) {
        this.eventName = eventName;
        this.ownerName = ownerName;
        this.itemId = itemId;
        this.itemCost = itemCost;
    }
}
