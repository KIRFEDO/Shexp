package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ActivityEventItem extends AppCompatActivity {

    ImageView iv_photo, iv_avatar;
    TextView tv_info, tv_LoginName, tv_eventName, tv_itemName, tv_amount;
    Button btn_createItem;
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference refStorage;
    FirebaseAuth mAuth;
    ArrayList<HelperUser> addedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_item);

        String currentUser = (String) getIntent().getSerializableExtra("currentUser");
        String currentEvent = (String) getIntent().getSerializableExtra("currentEventName");
        String itemName = (String) getIntent().getSerializableExtra("currentItem");
        int avatarId = (Integer) getIntent().getSerializableExtra("avatarId");

        tv_LoginName = findViewById(R.id.tv_LoginName);
        tv_info = findViewById(R.id.tv_info);
        tv_eventName = findViewById(R.id.tv_eventName);
        tv_itemName = findViewById(R.id.tv_itemName);
        tv_amount = findViewById(R.id.tv_amount);
        btn_createItem = findViewById(R.id.btn_createItem);
        iv_avatar = findViewById(R.id.iv_avatar);
        iv_photo = findViewById(R.id.iv_photo);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        addedUsers = new ArrayList<>();

        tv_LoginName.setText(currentUser);
        tv_eventName.setText("Event: "+currentEvent);
        tv_itemName.setText("Activity: "+itemName);
        switch (avatarId) {
            case 1:
                iv_avatar.setImageResource(R.drawable.avatar_1);
                break;
            case 2:
                iv_avatar.setImageResource(R.drawable.avatar_2);
                break;
        }

        ref = db.getReference("Events");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageKey = "";
                float amount;
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.getKey().equals(currentEvent)) {
                        HelperEvent buffEvent = ds.getValue(HelperEvent.class);
                        imageKey = buffEvent.items.get(itemName).image_key;
                        amount = buffEvent.items.get(itemName).amount;
                        tv_amount.setText("Amount: "+Float.toString(amount));
                        break;
                    }
                }
                if(imageKey.isEmpty())return;
                refStorage = storage.getReference().child("images/"+imageKey);
                try {
                    File buff = File.createTempFile("buff", ".jpg");
                    refStorage.getFile(buff).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            Bitmap bitmap = BitmapFactory.decodeFile(buff.getAbsolutePath());
                            iv_photo.setImageBitmap(bitmap);
                        }
                    });
                }catch(IOException ie){};
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
;