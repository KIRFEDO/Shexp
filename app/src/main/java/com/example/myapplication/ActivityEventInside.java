package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyCallback;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityEventInside extends AppCompatActivity {

    ListView lv_items;
    ImageView iv_total, iv_balance;
    TextView tv_LoginName, tv_eventName, tv_owner, tv_noActivities, tv_total, tv_balance;
    FloatingActionButton fbtn_addEventItem, fbtn_calculateTransactions;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference refEvents;
    Map<String, Float> balance;

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
        tv_total = findViewById(R.id.tv_total);
        tv_balance = findViewById(R.id.tv_balance);
        iv_total = findViewById(R.id.iv_total);
        iv_balance = findViewById(R.id.iv_balance);
        fbtn_addEventItem = findViewById(R.id.fbtn_addEventItem);
        fbtn_calculateTransactions = findViewById(R.id.fbtn_calculateTransactions);
        lv_items = findViewById(R.id.lv_items);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

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
                            tv_total.setText("--");
                            iv_total.setVisibility(View.GONE);
                            iv_balance.setVisibility(View.GONE);
                            fbtn_calculateTransactions.setVisibility(View.GONE);
                        } else {
                            lv_items.setVisibility(View.VISIBLE);
                            tv_noActivities.setVisibility(View.GONE);

                            ArrayList<String> itemsNames = new ArrayList<>();
                            ArrayList<String> owners = new ArrayList<>();
                            ArrayList<Float> costs = new ArrayList<>();
                            float total = 0F;

                            for(Map.Entry<String, HelperItem> item : event.items.entrySet()){
                                itemsNames.add(item.getKey());
                                owners.add("Owner: "+item.getValue().owner_login);
                                costs.add(item.getValue().amount);
                                total += item.getValue().amount;
                            }

                            tv_total.setText(Float.toString(total));

                            ArrayList<EventItemListElement> eventItemArrayList = new ArrayList<>();
                            for(int i=0;i<itemsNames.toArray().length;i++){
                                EventItemListElement item = new EventItemListElement(itemsNames.get(i), owners.get(i), costs.get(i));
                                eventItemArrayList.add(item);
                            }

                            lv_items.setAdapter(new AdapterEventItemList(getApplicationContext(), eventItemArrayList));

                            balance = new HashMap<>();
                            for(String uid : event.addedUsersUID){
                                balance.put(uid, (float) 0);
                            }

                            HashMap<String, HelperItem> items = event.items;
                            for(Map.Entry<String, HelperItem> item : items.entrySet()){
                                String owner = item.getValue().owner_uid;
                                for(String user : event.addedUsersUID){
                                    float valueForSingleUser = item.getValue().amount/event.addedUsersUID.size();
                                    if(user.equals(owner)){
                                        balance.put(user, balance.get(user)+item.getValue().amount-valueForSingleUser);
                                    } else {
                                        balance.put(user, balance.get(user)-valueForSingleUser);
                                    }
                                }
                            }

                            float balanceForCurrentUser = balance.get(mAuth.getUid());
                            if(balanceForCurrentUser > 0){
                                tv_balance.setText("Your total: " + Float.toString(balanceForCurrentUser));
                            } else if(balanceForCurrentUser < 0){
                                tv_balance.setText("Your total: " + Float.toString(balanceForCurrentUser));
                            } else {
                                iv_balance.setVisibility(View.GONE);
                            }
                            return;
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        fbtn_calculateTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityTransactionsList.class);
                intent.putExtra("currentUser", tv_LoginName.getText().toString());
                ArrayList<String> balance_keys = new ArrayList<>();
                ArrayList<Float> balance_values = new ArrayList<>();
                for(Map.Entry<String, Float> user : balance.entrySet()){
                    balance_keys.add(user.getKey());
                    balance_values.add(user.getValue());
                }
                intent.putExtra("balance_keys", balance_keys);
                intent.putExtra("balance_values", balance_values);
                startActivity(intent);
            }
        });

        fbtn_addEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityCreateEventItem.class);
                intent.putExtra("currentUser", tv_LoginName.getText().toString());
                intent.putExtra("currentEvent", currentEvent);
                startActivity(intent);
            }
        });
    }
}
