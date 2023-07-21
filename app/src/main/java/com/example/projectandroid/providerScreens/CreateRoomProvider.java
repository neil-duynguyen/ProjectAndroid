package com.example.projectandroid.providerScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.projectandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CreateRoomProvider extends AppCompatActivity {
    EditText edImage, edAddress, edLocation, edDescription, edShortDescription, edPrice;
    Button btn_Create, btn_Cancel;

    DatabaseReference databaseReference;
    List<Integer> listID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room_provider);

        edImage = findViewById(R.id.edit_Image);
        edAddress = findViewById(R.id.edit_Address);
        edLocation = findViewById(R.id.edit_Location);
        edDescription = findViewById(R.id.edit_Description);
        edShortDescription = findViewById(R.id.edit_ShortDescription);
        edPrice = findViewById(R.id.edit_Price);

        listID = new ArrayList<>();

        databaseReference =  FirebaseDatabase.getInstance().getReference().child("image");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = Objects.requireNonNull(dataSnapshot.child("id").getValue()).toString();
                    String[] parts = id.split("(?<=\\D)(?=\\d)"); // Sử dụng regex để tách theo biên giữa chữ và số
                    String prefix = parts[0]; // Phần prefix: "image"
                    String suffix = parts[1]; // Phần suffix: "1"
                    listID.add(new Integer(suffix));
                }
                
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Optional<Integer> maxNumber = listID.stream().max(Integer::compare);

        String temp = FirebaseAuth.getInstance().getCurrentUser().zzb().getUid(); //lấy user hiện tại

        btn_Create = findViewById(R.id.button_Tao);
        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("image");

            }
        });
    }
}


