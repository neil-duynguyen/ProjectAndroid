package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.projectandroid.adapters.UserAdapter;
import com.example.projectandroid.adaptersProvider.TransactionAdapter;
import com.example.projectandroid.model.Transaction;
import com.example.projectandroid.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserManagerActivity extends AppCompatActivity {
    UserAdapter adapter;
    RecyclerView transRV;
    List<User> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        transRV = findViewById(R.id.trans_rv);
        adapter = new UserAdapter(UserManagerActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserManagerActivity.this, RecyclerView.VERTICAL, false);
        transRV.setLayoutManager(linearLayoutManager);

        loadList();
    }

    private void loadList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference objectCRef = database.getReference("users");
        objectCRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot bSnapshot : dataSnapshot.getChildren()) {
                    User tempTransaction = bSnapshot.getValue(User.class);
                    list.add(tempTransaction);
                }
                adapter.setData(list);
                transRV.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

}