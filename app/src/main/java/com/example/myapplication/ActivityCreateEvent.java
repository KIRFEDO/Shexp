package com.example.myapplication;

import android.content.Intent;
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

public class ActivityCreateEvent extends AppCompatActivity {

    SearchView sv_searchUsers;
    boolean lv_users_search_state;
    ListView lv_users;
    TextView tv_info;
    Button btn_createEvent;
    EditText et_eventName;
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btn_createEvent = findViewById(R.id.btn_createEvent);
        lv_users = findViewById(R.id.lv_users);
        tv_info = findViewById(R.id.tv_info);
        et_eventName = findViewById(R.id.et_eventName);
        sv_searchUsers = findViewById(R.id.sv_searchUsers);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ArrayList<UserListItem> addedUsers = new ArrayList<>();
        ArrayList<UserListItem> searchResults = new ArrayList<>();
        ArrayList<CreateEventUsers> users = new ArrayList<>();
        ref = db.getReference("UsersLogins");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
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
                lv_users_search_state = true;
                tv_info.setVisibility(View.INVISIBLE);
                searchResults.clear();
                for(CreateEventUsers user : users){
                    if(user.login.toLowerCase().contains(newText.toLowerCase())) {
                        searchResults.add(new UserListItem(user, false));
                    }
                }

                lv_users.setVisibility(View.VISIBLE);
                lv_users.setAdapter(new AdapterUserList(getApplicationContext(), searchResults));

                lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        if(lv_users_search_state) {
                            lv_users_search_state = false;
                            UserListItem buff = searchResults.get(pos);
                            buff.remove_option = true;
                            addedUsers.add(buff);
                            lv_users.setAdapter(new AdapterUserList(getApplicationContext(), addedUsers));
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
                ref = db.getReference("Events");
                ArrayList<Integer> a = new ArrayList<Integer>();
                a.add(1);
                a.add(2);
                a.add(3);

                String eventName = et_eventName.getText().toString();
                String owner = mAuth.getCurrentUser().getUid();

                HelperEventClass helperClass = new HelperEventClass(eventName, owner, a);
                ref.child(eventName).setValue(helperClass);
                startActivity(new Intent(getApplicationContext(), ActivityUserPage.class));
            }
        });
    }
}
;