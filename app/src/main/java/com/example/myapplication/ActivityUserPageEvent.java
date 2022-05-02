package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityUserPageEvent extends AppCompatActivity {

    ListView AUP_eventList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        AUP_eventList = findViewById(R.id.AUP_eventList);

        String[] events = {"1","2","3","4","5","6","7","8","9","0"};
        Integer[] events_ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        String[] owners = {"a", "b", "c", "d", "e", "f", "g", "j", "z", "pipa"};
        Integer[] owners_ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        Integer[] avatar_ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        ArrayList<EventListElement> eventArrayList = new ArrayList<>();

        for(int i=0;i<events.length;i++){
            EventListElement event = new EventListElement(events[i], events_ids[i], owners[i], owners_ids[i], avatar_ids[i]);
            eventArrayList.add(event);
        }

        adapter = new EventListAdapter(getApplicationContext(), eventArrayList);
        AUP_eventList.setAdapter(adapter);
    }
}
