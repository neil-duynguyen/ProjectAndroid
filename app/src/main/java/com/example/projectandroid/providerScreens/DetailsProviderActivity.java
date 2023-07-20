package com.example.projectandroid.providerScreens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.providerFragments.HomeFragmentProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DetailsProviderActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText price, shortDescription, description;
    Button btnEdit, btnDelete;
    String pri , des, id;
    String img , shor;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_provider);

        btnEdit = findViewById(R.id.button_edit);
        btnDelete = findViewById(R.id.button_delete);
        imageView = findViewById(R.id.imageView);
        price = findViewById(R.id.price);
        shortDescription = findViewById(R.id.short_description);
        description = findViewById(R.id.description);

        pri = getIntent().getStringExtra("price");
        des = getIntent().getStringExtra("description");
        img = getIntent().getStringExtra("image");
        shor = getIntent().getStringExtra("shortDescription");

        price.setText(pri);
        description.setText(des);
        shortDescription.setText(shor);
        Glide
                .with(this)
                .load(img)
                .centerCrop()
                .into(imageView);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = description.getText().toString();
                Integer amount = Integer.valueOf(price.getText().toString());
                String shortD = shortDescription.getText().toString();
                updateData(des, amount, shortD, getIntent().getStringExtra("id"));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteData(getIntent().getStringExtra("id"));
            }
        });

    }

    private void onClickDeleteData(String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference("image");
        databaseReference.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(DetailsProviderActivity.this, "Delete data success", Toast.LENGTH_SHORT).show();
                Fragment fragment = new HomeFragmentProvider();
            }
        });

    }

    private void updateData(String des, Integer pri, String shortD, String id) {
        HashMap object = new HashMap();
        object.put("description", des);
        object.put("shortDescription", shortD);
        object.put("price", pri);

        databaseReference = FirebaseDatabase.getInstance().getReference("image");
        databaseReference.child(id).updateChildren(object).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(DetailsProviderActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                Fragment fragment = new HomeFragmentProvider();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailsProviderActivity.this, "Error While Updating", Toast.LENGTH_SHORT).show();
            }
        });
    }
}