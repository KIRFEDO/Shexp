package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityCreateEvent extends AppCompatActivity {

    ImageView iv_avatar;
    SearchView sv_searchUsers;
    boolean lv_users_search_state;
    ListView lv_users;
    TextView tv_info;
    Button btn_createEvent;
    EditText et_eventName;
    FirebaseDatabase db;
    DatabaseReference ref;
    DatabaseReference ref2;
    FirebaseAuth mAuth;
    ArrayList<HelperUser> addedUsers;
    ArrayList<CreateEventUsers> users;
    TextView tv_LoginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        tv_LoginName = findViewById(R.id.tv_LoginName);
        btn_createEvent = findViewById(R.id.btn_createEvent);
        lv_users = findViewById(R.id.lv_users);
        tv_info = findViewById(R.id.tv_info);
        et_eventName = findViewById(R.id.et_eventName);
        sv_searchUsers = findViewById(R.id.sv_searchUsers);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        addedUsers = new ArrayList<>();
        ArrayList<HelperUser> searchResults = new ArrayList<>();
        users = new ArrayList<>();
        iv_avatar = findViewById(R.id.iv_avatar);

        CreateEventUsers currentUser = (CreateEventUsers) getIntent().getSerializableExtra("currentUser");
        tv_LoginName.setText(currentUser.login);
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

        ref = db.getReference("UsersLogins");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.getValue(CreateEventUsers.class).uid.equals(mAuth.getUid())){
                        tv_LoginName.setText(ds.getValue(CreateEventUsers.class).login);
                        continue;
                    }
                    users.add(ds.getValue(CreateEventUsers.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //FOR IMPROVEMENT
            }
        });

        sv_searchUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //FOR IMPROVEMENT
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    lv_users_search_state = false;
                    AdapterUserList adapter_userList = new AdapterUserList(getApplicationContext(), addedUsers);
                    adapter_userList.setOnAddListener(new AdapterUserList.OnAddListener() {
                        @Override
                        public void onAdd(int position, String text) {
                            addedUsers.remove(position);
                            lv_users.setAdapter(adapter_userList);
                        }
                    });
                    lv_users.setAdapter(adapter_userList);
                    return false;
                }
                lv_users_search_state = true;
                tv_info.setVisibility(View.INVISIBLE);
                searchResults.clear();
                for(CreateEventUsers user : users){
                    if(user.login.toLowerCase().contains(newText.toLowerCase())) {
                        boolean wasFound = false;
                        HelperUser buff = new HelperUser(user, false);
                        for(HelperUser buffUser : addedUsers){
                            if(buffUser.name.equals(user.login)){
                                wasFound = true;
                            }
                        }
                        if(!wasFound){
                            searchResults.add(buff);
                        }
                    }
                }

                lv_users.setVisibility(View.VISIBLE);
                lv_users.setAdapter(new AdapterUserList(getApplicationContext(), searchResults));

                lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        if(lv_users_search_state) {
                            lv_users_search_state = false;
                            HelperUser buff = searchResults.get(pos);
                            buff.remove_option = true;
                            addedUsers.add(buff);
                            AdapterUserList adapter_userList = new AdapterUserList(getApplicationContext(), addedUsers);
                            adapter_userList.setOnAddListener(new AdapterUserList.OnAddListener() {
                                @Override
                                public void onAdd(int position, String text) {
                                    addedUsers.remove(position);
                                    lv_users.setAdapter(adapter_userList);
                                }
                            });
                            lv_users.setAdapter(adapter_userList);
                        } else {

                        }
                    }
                });
                Log.d("[debug]", searchResults.toString());
                return false;
            }
        });

        btn_createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_eventName.getText().toString().isEmpty()){
                    et_eventName.setError("Enter event name");
                    return;
                }

                if(addedUsers.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Add at least one user", Toast.LENGTH_LONG).show();
                    return;
                }

                ref = db.getReference("Events");

                String eventName = et_eventName.getText().toString();
                String ownerUid = mAuth.getCurrentUser().getUid();
                ArrayList<String> addedUsersUID = new ArrayList<String>();

                addedUsersUID.add(ownerUid);
                for(HelperUser uli : addedUsers){
                    for(CreateEventUsers ceu : users){
                        if(uli.name == ceu.login){
                            addedUsersUID.add(ceu.uid);
                            break;
                        }
                    }
                }

                ref2 = db.getReference("UsersEvents");
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean wasFound = false;
                        for(String uid : addedUsersUID){
                            for(DataSnapshot ds : snapshot.getChildren()){
                                UserEvents userEvents;
                                userEvents = ds.getValue(UserEvents.class);
                                if(userEvents.uid.equals(uid)){
                                    if(userEvents.events == null){
                                        userEvents.events = new ArrayList<>();
                                    }
                                    if(userEvents.events.indexOf(eventName) != -1){
                                        return;
                                    };
                                    userEvents.events.add(eventName);
                                    Map<String, Object> updateVal = new HashMap<String, Object>();
                                    updateVal.put("uid", uid);
                                    updateVal.put("events", userEvents.events);
                                    ref2.child(uid).updateChildren(updateVal);
                                    wasFound = true;
                                    break;
                                }
                            }
                            if(!wasFound){
                                ArrayList<String> buff = new ArrayList<String>();
                                buff.add(eventName);
                                ref2.child(uid).setValue(new UserEvents(uid, buff));
                            }
                            wasFound = false;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                HelperEvent helperClass = new HelperEvent(eventName, ownerUid, addedUsersUID);
                ref.child(eventName).setValue(helperClass);
                startActivity(new Intent(getApplicationContext(), ActivityUserPage.class));
            }
        });

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                finish();
            }
        });
    }
}
;