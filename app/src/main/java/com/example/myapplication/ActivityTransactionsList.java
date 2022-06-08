package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ActivityTransactionsList extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference refUsers;
    TextView tv_loginName;
    ImageView iv_avatar;
    ListView ll_transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        tv_loginName = findViewById(R.id.tv_loginName);
        iv_avatar = findViewById(R.id.iv_avatar);
        ll_transactionList = findViewById(R.id.ll_transactionList);

        String currentUser = (String) getIntent().getSerializableExtra("currentUser");
        int avatarId = (Integer) getIntent().getSerializableExtra("avatarId");
        ArrayList<String> keys = (ArrayList<String>) getIntent().getSerializableExtra("balance_keys");
        ArrayList<Float> values = (ArrayList<Float>) getIntent().getSerializableExtra("balance_values");

        tv_loginName.setText(currentUser);
        switch (avatarId){
            case 1:
                iv_avatar.setImageResource(R.drawable.avatar_1);
                break;
            case 2:
                iv_avatar.setImageResource(R.drawable.avatar_2);
                break;
        }

        ArrayList<Pair<String, Float>> debters = new ArrayList<>();
        ArrayList<Pair<String, Float>> payers = new ArrayList<>();
        for(int i=0; i<keys.size();i++){
            if(values.get(i) < 0){
                debters.add(new Pair<String, Float>(keys.get(i), values.get(i)));
            } else if(values.get(i) > 0){
                payers.add(new Pair<String, Float>(keys.get(i), values.get(i)));
            }
        }
        Collections.sort(debters, Comparator.comparing(p -> p.second));
        Collections.sort(payers, Comparator.comparing(p -> -p.second));
        ArrayList<TransactionListElement> transactions = new ArrayList<>();

        refUsers = db.getReference("UsersLogins");
        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> users = new HashMap<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    HelperRegisterClass user = ds.getValue(HelperRegisterClass.class);
                    users.put(user.uid, user.login);
                }
                for(int i=0; i<payers.size(); i++){
                    String payer = users.get(payers.get(i).first);
                    for(int j=0;j<debters.size();j++){
                        String debtor = users.get(debters.get(j).first);
                        if(debters.get(j).second <= payers.get(i).second){
                            transactions.add(new TransactionListElement(
                                    debtor, payer, Math.abs(debters.get(j).second)
                            ));
                            payers.set(i,
                                    new Pair<String, Float>(payers.get(i).first, payers.get(i).second + debters.get(j).second)
                            );
                            debters.set(j,
                                    new Pair<String, Float>(debters.get(j).first, 0F)
                            );
                            if(payers.get(i).second - debters.get(j).second == 0)break;
                        } else {
                            transactions.add(new TransactionListElement(
                                    debtor, payer, payers.get(i).second
                            ));
                            payers.set(i,
                                    new Pair<String, Float>(payers.get(i).first, 0F)
                            );
                            debters.set(i,
                                    new Pair<String, Float>(payers.get(i).first, debters.get(j).second + payers.get(i).second)
                            );
                            break;
                        }
                    }
                }
                ll_transactionList.setAdapter(new AdapterTransactionList(getApplicationContext(), transactions));

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
