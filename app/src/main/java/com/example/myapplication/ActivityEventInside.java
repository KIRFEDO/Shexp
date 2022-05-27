package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyCallback;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ActivityEventInside extends AppCompatActivity {

    ListView lv_items;
    ArrayAdapter adapter;
    TextView tv_LoginName, tv_eventName, tv_owner, tv_noActivities;
    FloatingActionButton fbtn_addEventItem;
    FirebaseDatabase db;
    DatabaseReference refEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_inside);

        EventListElement currentEvent = (EventListElement) getIntent().getSerializableExtra("currentEvent");
        String currentUser = (String) getIntent().getSerializableExtra("currentUser");
        tv_LoginName = findViewById(R.id.tv_LoginName);
        tv_eventName = findViewById(R.id.tv_eventName);
        tv_owner = findViewById(R.id.tv_owner);
        tv_noActivities = findViewById(R.id.tv_noActivities);
        fbtn_addEventItem = findViewById(R.id.fbtn_addEventItem);
        lv_items = findViewById(R.id.lv_items);
        db = FirebaseDatabase.getInstance();

        tv_LoginName.setText(currentUser);
        tv_eventName.setText(currentEvent.eventName);
        tv_owner.setText(currentEvent.ownerName);

        refEvents = db.getReference("Events");
        refEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.getKey().equals(currentEvent.eventName)){
                        HelperEvent event = ds.getValue(HelperEvent.class);
                        if(event.items == null){
                            lv_items.setVisibility(View.GONE);
                            tv_noActivities.setVisibility(View.VISIBLE);
                        } else {
                            lv_items.setVisibility(View.VISIBLE);
                            tv_noActivities.setVisibility(View.GONE);

                            ArrayList<String> itemsNames = new ArrayList<>();
                            ArrayList<String> owners = new ArrayList<>();
                            ArrayList<Integer> costs = new ArrayList<>();

                            for(Map.Entry<String, HelperItem> item : event.items.entrySet()){
                                itemsNames.add(item.getKey());
                                owners.add("Owner: "+item.getValue().owner_login);
                                costs.add(item.getValue().amount);
                            }

                            ArrayList<EventItemListElement> eventItemArrayList = new ArrayList<>();
                            for(int i=0;i<itemsNames.toArray().length;i++){
                                EventItemListElement item = new EventItemListElement(itemsNames.get(i), owners.get(i), 1, costs.get(i));
                                eventItemArrayList.add(item);
                            }
                            lv_items.setAdapter(new AdapterEventItemList(getApplicationContext(), eventItemArrayList));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        fbtn_addEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityCreateEventItem.class);
                intent.putExtra("currentUser", tv_LoginName.getText().toString());
                intent.putExtra("currentEvent", tv_eventName.getText().toString());
                startActivity(intent);
            }
        });
    }
}
