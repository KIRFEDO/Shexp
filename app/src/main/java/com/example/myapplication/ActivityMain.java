package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityMain extends AppCompatActivity {

    private LinearLayout LL;
    private TextView TV;
    private Button AM_Login;
    private Button AM_Register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), ActivityUserPage.class));
            finish();
        }

        LL = findViewById(R.id.AM_LL);
        TV = findViewById(R.id.AM_TV);
        AM_Login = findViewById(R.id.AM_Login);
        AM_Register = findViewById(R.id.AM_Register);

        TV.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.mp_move_logo_up
        ));
        LL.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.mp_btn_show
        ));
        AM_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
            }
        });
        AM_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityRegister.class);
                startActivity(intent);
            }
        });
    };

}