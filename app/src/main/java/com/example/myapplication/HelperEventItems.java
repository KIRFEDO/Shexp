package com.example.myapplication;

import java.util.ArrayList;

public class HelperEventItems {
    ArrayList<String> items;

    public HelperEventItems(){};

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public HelperEventItems(ArrayList<String> items) {
        this.items = items;
    }
}
