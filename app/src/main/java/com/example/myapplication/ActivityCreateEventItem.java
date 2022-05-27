package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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


public class ActivityCreateEventItem extends AppCompatActivity {

    SearchView sv_searchUsers;
    boolean lv_users_search_state;
    ListView lv_users;
    TextView tv_info, tv_LoginName, tv_eventName;
    Button btn_createItem;
    EditText et_itemName, et_amount;
    FirebaseDatabase db;
    DatabaseReference refEvents, refUsers;
    FirebaseAuth mAuth;
    ArrayList<HelperUser> addedUsers;
    ArrayList<CreateEventUsers> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_item);

        String currentUser = (String) getIntent().getSerializableExtra("currentUser");
        String currentEvent = (String) getIntent().getSerializableExtra("currentEvent");

        tv_LoginName = findViewById(R.id.tv_LoginName);
        tv_info = findViewById(R.id.tv_info);
        tv_eventName = findViewById(R.id.tv_eventName);
        btn_createItem = findViewById(R.id.btn_createItem);
        lv_users = findViewById(R.id.lv_users);
        et_amount = findViewById(R.id.et_amount);
        et_itemName = findViewById(R.id.et_itemName);
        sv_searchUsers = findViewById(R.id.sv_searchUsers);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        addedUsers = new ArrayList<>();
        ArrayList<HelperUser> searchResults = new ArrayList<>();
        users = new ArrayList<>();

        tv_LoginName.setText(currentUser);
        tv_eventName.setText(currentEvent);

        refUsers = db.getReference("UsersLogins");
        refUsers.addValueEventListener(new ValueEventListener() {
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
                        searchResults.add(new HelperUser(user, false));
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

        btn_createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = et_itemName.getText().toString();
                refEvents = db.getReference("Events");
                refEvents.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HelperEventItems eventItems;
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if(ds.getKey().equals(currentEvent)){
                                HelperEvent helperEvent = ds.getValue(HelperEvent.class);
                                if(helperEvent.items != null){
                                    if(helperEvent.items.containsKey(itemName)){
                                        return;
                                    }
                                }

                                ArrayList<String> addedUsers_S = new ArrayList<String>();
                                for(HelperUser user : addedUsers){
                                    addedUsers_S.add(user.name);
                                }
                                HelperItem newItem = new  HelperItem(mAuth.getUid(), currentUser,Integer.parseInt(et_amount.getText().toString()));
                                refEvents.child(currentEvent).child("items").child(itemName).setValue(newItem);

                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

    }
}
;