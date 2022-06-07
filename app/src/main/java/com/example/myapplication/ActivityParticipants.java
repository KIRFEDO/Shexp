package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityParticipants extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView tv_loginName, tv_eventName;
    ImageView iv_avatar;
    ListView lv_users;
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        String userLogin = (String) getIntent().getSerializableExtra("currentUser");
        String eventName = (String) getIntent().getSerializableExtra("currentEventName");
        ArrayList<String> usersUids = (ArrayList<String>) getIntent().getSerializableExtra("currentEventUsers");
        int avatarId = (int) getIntent().getSerializableExtra("avatarId");

        tv_loginName = findViewById(R.id.tv_loginName);
        tv_eventName = findViewById(R.id.tv_eventName);
        lv_users = findViewById(R.id.lv_users);
        iv_avatar = findViewById(R.id.iv_avatar);

        tv_eventName.setText("Event: "+eventName);
        tv_loginName.setText(userLogin);
        switch(avatarId){
            case 1:
                iv_avatar.setImageResource(R.drawable.avatar_1);
                break;
            case 2:
                iv_avatar.setImageResource(R.drawable.avatar_2);
                break;
        }

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("UsersUID");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HelperUser> users = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String currentUid = ds.getKey();
                    if(usersUids.indexOf(currentUid) != -1){
                        CreateEventUsers user = ds.getValue(CreateEventUsers.class);
                        users.add(new HelperUser(user, false));
                    }
                }
                lv_users.setAdapter(new AdapterUserList(getApplicationContext(), users));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
