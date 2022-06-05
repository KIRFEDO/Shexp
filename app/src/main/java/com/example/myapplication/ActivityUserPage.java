package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityUserPage extends AppCompatActivity {

    ListView ll_eventList;
    ImageView iv_avatar;
    FirebaseDatabase db;
    DatabaseReference refUid, refUserEvents, refEvents;
    FirebaseAuth mAuth;
    FloatingActionButton AUP_addEventBtn;
    TextView tv_LoginName, tv_emptyEventList;
    ArrayList<CreateEventUsers> users;
    CreateEventUsers currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        ll_eventList = findViewById(R.id.ll_eventList);
        iv_avatar = findViewById(R.id.iv_avatar);
        AUP_addEventBtn = findViewById(R.id.AUP_addEventBtn);
        tv_LoginName = findViewById(R.id.tv_LoginName);
        tv_emptyEventList = findViewById(R.id.tv_emptyEventList);
        users = new ArrayList<CreateEventUsers>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                finish();
            }
        });

        refUid = db.getReference("UsersUID");
        refUid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){

                    if(ds.getValue(CreateEventUsers.class).uid.equals(mAuth.getUid())){
                        currentUser = ds.getValue(CreateEventUsers.class);
                        switch(currentUser.avatarId){
                            case 1:
                                iv_avatar.setImageResource(R.drawable.avatar_1);
                                break;
                            case 2:
                                iv_avatar.setImageResource(R.drawable.avatar_2);
                                break;
                            default:
                                break;
                        }
                        tv_LoginName.setText(currentUser.login);
                    }
                    users.add(ds.getValue(CreateEventUsers.class));
                }

                final UserEvents[] ue = new UserEvents[1];
                final boolean[] wasFound = {false};
                refUserEvents = db.getReference("UsersEvents");
                refUserEvents.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if(ds.getValue(UserEvents.class).uid.equals(mAuth.getUid())){
                                ue[0] = ds.getValue(UserEvents.class);
                                wasFound[0] = true;
                                break;
                            }
                        }
                        if(wasFound[0]) {
                            ArrayList<String> events = new ArrayList<>();
                            ArrayList<String> ownersUids = new ArrayList<>();
                            ArrayList<String> owners = new ArrayList<>();
                            ArrayList<Integer> avatarIds = new ArrayList<>();
                            ArrayList<EventListElement> eventArrayList = new ArrayList<>();
                            refEvents = db.getReference("Events");
                            refEvents.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (wasFound[0]) {
                                        for (String key : ue[0].events) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                if (ds.getKey().equals(key)) {
                                                    HelperEvent helper = ds.getValue(HelperEvent.class);
                                                    events.add(ds.getKey());
                                                    ownersUids.add(ds.getValue(HelperEvent.class).ownerUid);
                                                    break;
                                                }
                                            }
                                        }
                                        for(Object user : ownersUids){
                                            for(CreateEventUsers ceu : users) {
                                                if(ceu.uid.equals(user.toString())){
                                                    avatarIds.add(ceu.avatarId);
                                                    if(user.toString().equals(mAuth.getUid())){
                                                        owners.add("You");
                                                        break;
                                                    }
                                                    if (user.toString().equals(ceu.uid)) {
                                                        owners.add(ceu.login);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        eventArrayList.clear();
                                        for(int i=0;i<ue[0].events.toArray().length;i++){
                                            eventArrayList.add(new EventListElement(
                                                    ue[0].events.toArray()[i].toString(),
                                                    "Owner: " + owners.get(i).toString(),
                                                    avatarIds.get(i))
                                                );
                                        }
                                        ll_eventList.setAdapter(new AdapterEventList(getApplicationContext(), eventArrayList));
                                        ll_eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                                Intent intent = new Intent(getApplicationContext(), ActivityEventInside.class);
                                                intent.putExtra("currentEvent", eventArrayList.get(pos));
                                                intent.putExtra("currentUser", tv_LoginName.getText().toString());
                                                intent.putExtra("avatarId", currentUser.avatarId);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                            return;
                        } else {
                            ll_eventList.setVisibility(View.GONE);
                            tv_emptyEventList.setVisibility(View.VISIBLE);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        AUP_addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityCreateEvent.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
}
