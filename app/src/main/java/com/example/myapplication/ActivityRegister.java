package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.drive.events.CompletionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class ActivityRegister extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    TextInputLayout AR_loginET, AR_emailET, AR_passwordET, AR_passwordRepET;
    ImageView AR_backArrow;
    Button AR_registerBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AR_loginET = findViewById(R.id.AR_LoginET);
        AR_emailET = findViewById(R.id.AR_EmailET);
        AR_passwordET = findViewById(R.id.AR_PasswordET);
        AR_passwordRepET = findViewById(R.id.AR_PasswordRepET);
        AR_registerBTN = findViewById(R.id.AR_registerBTN);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        AR_backArrow = findViewById(R.id.AR_backArrow);
        AR_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                startActivity(intent);
            }
        });

        AR_registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = AR_loginET.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(login)){
                    AR_loginET.setError("Login is required");
                    return;
                }
                AR_loginET.setError(null);

                String email = AR_emailET.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    AR_emailET.setError("Email is required");
                    return;
                }
                AR_emailET.setError(null);


                String password = AR_passwordET.getEditText().getText().toString().trim();
                String passwordRep = AR_passwordRepET.getEditText().getText().toString().trim();

                int len = 2;
                if(password.length() < len){
                    AR_passwordET.setError("Password must be longer then 10 characters");
                    return;
                }
                AR_passwordET.setError(null);

                if(password.compareTo(passwordRep) != 0){
                    AR_passwordRepET.setError("Passwords are not equal");
                } else {
                    AR_passwordRepET.setError(null);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Registrations successful!", Toast.LENGTH_SHORT).show();
                                db = FirebaseDatabase.getInstance();

                                //Users table
                                ref = db.getReference("UsersUID");
                                ref.child(mAuth.getCurrentUser().getUid()).setValue(new HelperRegisterClass(login, email, mAuth.getCurrentUser().getUid()), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if(error != null){
                                            Toast.makeText(getApplicationContext(), "Registrations failed!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                                //Logins table
                                ref = db.getReference("UsersLogins");
                                ref.child(login).setValue(new HelperRegisterClass(login, email, mAuth.getCurrentUser().getUid()), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if(error != null){
                                            Toast.makeText(getApplicationContext(), "Registrations failed!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });

                                startActivity(new Intent(getApplicationContext(), ActivityMain.class));


                            }else{
                                Toast.makeText(getApplicationContext(), "Registrations failed!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }
            }
        });
    }
}