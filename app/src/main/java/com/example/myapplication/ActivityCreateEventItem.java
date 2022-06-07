package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;


public class ActivityCreateEventItem extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 200;
    int MY_REQUEST;
    byte image_bytes[];
    ImageView iv_camera, iv_gallery, iv_photo, iv_avatar;
    TextView tv_info, tv_LoginName, tv_eventName;
    Button btn_createItem;
    EditText et_itemName, et_amount;
    FirebaseDatabase db;
    DatabaseReference refEvents;
    FirebaseStorage storage;
    StorageReference refStorage;
    FirebaseAuth mAuth;
    ArrayList<HelperUser> addedUsers;
    ArrayList<CreateEventUsers> users;
    Uri image_uri;
    String image_key = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            switch (requestCode){
                case MY_CAMERA_REQUEST_CODE:
                    MY_REQUEST = MY_CAMERA_REQUEST_CODE;
                    Bitmap image_bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
                    image_key = UUID.randomUUID().toString();
                    image_bytes = byteStream.toByteArray();
                    iv_photo.setImageBitmap(image_bitmap);
                    break;
                case MY_GALLERY_REQUEST_CODE:
                    MY_REQUEST = MY_GALLERY_REQUEST_CODE;
                    image_uri = data.getData();
                    image_key = UUID.randomUUID().toString();
                    iv_photo.setImageURI(image_uri);
                    break;
                default:
                    Log.d("[onActivityResult]", "wrong requestCode");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_item);

        String currentUser = (String) getIntent().getSerializableExtra("currentUser");
        EventListElement currentEvent = (EventListElement) getIntent().getSerializableExtra("currentEvent");
        int avatarId = (Integer) getIntent().getSerializableExtra("avatarId");

        tv_LoginName = findViewById(R.id.tv_LoginName);
        tv_info = findViewById(R.id.tv_info);
        tv_eventName = findViewById(R.id.tv_eventName);
        btn_createItem = findViewById(R.id.btn_createItem);
        iv_camera = findViewById(R.id.iv_camera);
        iv_gallery = findViewById(R.id.iv_gallery);
        iv_photo = findViewById(R.id.iv_photo);
        iv_avatar = findViewById(R.id.iv_avatar);
        et_amount = findViewById(R.id.et_amount);
        et_itemName = findViewById(R.id.et_itemName);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        addedUsers = new ArrayList<>();
        ArrayList<HelperUser> searchResults = new ArrayList<>();
        users = new ArrayList<>();

        tv_LoginName.setText(currentUser);
        tv_eventName.setText(currentEvent.eventName);
        switch (avatarId){
            case 1:
                iv_avatar.setImageResource(R.drawable.avatar_1);
                break;
            case 2:
                iv_avatar.setImageResource(R.drawable.avatar_2);
                break;
        }


        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(
                        checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ){
                    requestPermissions(new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
                }
            }
        });

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ){
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_GALLERY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, MY_GALLERY_REQUEST_CODE);
                }
            }
        });

        btn_createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_itemName.setError(null);
                et_amount.setError(null);
                boolean error = false;

                if(et_itemName.getText().toString().isEmpty()){
                    et_itemName.setError("Add item name");
                    error = true;
                }
                try{
                    Integer.parseInt(et_amount.getText().toString());
                    if(Integer.parseInt(et_amount.getText().toString()) <= 0){
                        Toast.makeText(getApplicationContext(), "Enter amount greater then 0", Toast.LENGTH_SHORT).show();
                        error = true;
                    }
                } catch (Exception e){
                    et_amount.setError("Enter number");
                    error = true;
                }

                if(error)return;

                String itemName = et_itemName.getText().toString();
                refEvents = db.getReference("Events");
                refEvents.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HelperEventItems eventItems;
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if(ds.getKey().equals(currentEvent.eventName)){
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
                                HelperItem newItem = new  HelperItem(mAuth.getUid(), currentUser,Integer.parseInt(et_amount.getText().toString()), image_key);
                                if(!image_key.isEmpty()){
                                    refStorage = storage.getReference();
                                    refStorage = refStorage.child("images/"+image_key);
                                    switch(MY_REQUEST){
                                        case MY_GALLERY_REQUEST_CODE:
                                            refStorage.putFile(image_uri);
                                            break;
                                        case MY_CAMERA_REQUEST_CODE:
                                            refStorage.putBytes(image_bytes);
                                            break;
                                    }
                                }
                                refEvents.child(currentEvent.eventName).child("items").child(itemName).setValue(newItem);

                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                Intent intent = new Intent(getApplicationContext(), ActivityEventInside.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("currentEvent", currentEvent);
                intent.putExtra("avatarId", avatarId);
                startActivity(intent);
            }
        });
    }
}
;