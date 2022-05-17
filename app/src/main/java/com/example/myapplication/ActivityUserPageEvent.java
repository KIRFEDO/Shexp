package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityUserPageEvent extends AppCompatActivity {

    ListView AUPE_eventList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page_event);

        AUPE_eventList = findViewById(R.id.AUPE_eventList);

        String[] items = {"1","2","3","4","5","6","7","8","9","0"};
        String[] owners = {"a", "b", "c", "d", "e", "f", "g", "j", "z", "pipa"};
        Integer[] itemIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        Integer[] itemCosts = {100, 2100, 3100100, 4100, 5100, 6, 7, 24, 9100, 0};

        ArrayList<EventItemListElement> eventItemArrayList = new ArrayList<>();

        for(int i=0;i<items.length;i++){
            EventItemListElement item = new EventItemListElement(items[i], owners[i], itemIds[i], itemCosts[i]);
            eventItemArrayList.add(item);
        }

        adapter = new AdapterEventItemList(getApplicationContext(), eventItemArrayList);
        AUPE_eventList.setAdapter(adapter);
    }
}
