package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ActivityLogin extends AppCompatActivity {

    private ImageView AL_backArrow;
    private Button AL_LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AL_backArrow = findViewById(R.id.AL_backArrow);
        AL_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                startActivity(intent);
            }
        });

        AL_LoginBtn = findViewById(R.id.AL_LoginBtn);
        AL_LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityUserPage.class);
                startActivity(intent);
            }
        });


    }
}