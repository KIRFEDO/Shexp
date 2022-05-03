package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ActivityUserPage extends AppCompatActivity {

    ListView AUP_eventList;
    ArrayAdapter adapter;
    ImageView AUP_avatar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        AUP_eventList = findViewById(R.id.AUP_eventList);
        AUP_avatar = findViewById(R.id.AUP_avatar);
        mAuth = FirebaseAuth.getInstance();

        AUP_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                finish();
            }
        });

        String[] events = {"1","2","3","4","5","6","7","8","9","0"};
        String[] owners = {"a", "b", "c", "d", "e", "f", "g", "j", "z", "pipa"};
        Integer[] events_ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        Integer[] avatar_ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        ArrayList<EventListElement> eventArrayList = new ArrayList<>();

        for(int i=0;i<events.length;i++){
            EventListElement event = new EventListElement(events[i], owners[i], avatar_ids[i], events_ids[i]);
            eventArrayList.add(event);
        }

        adapter = new EventListAdapter(getApplicationContext(), eventArrayList);
        AUP_eventList.setAdapter(adapter);
        AUP_eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), ActivityUserPageEvent.class);
                startActivity(intent);
            }
        });
    }
}
