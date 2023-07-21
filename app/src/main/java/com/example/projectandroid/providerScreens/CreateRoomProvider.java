package com.example.projectandroid.providerScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectandroid.R;
import com.example.projectandroid.model.Post;
import com.example.projectandroid.providerFragments.HomeFragmentProvider;
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
    List<Post> listPost;
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

        final Integer[] id = {Integer.valueOf(getIntent().getStringExtra("id"))};



        String temp = FirebaseAuth.getInstance().getCurrentUser().zzb().getUid(); //lấy user hiện tại

        btn_Create = findViewById(R.id.button_Tao);
        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Integer ID = ++id[0];

                DatabaseReference myRef = database.getReference("image"+"/"+"image"+ID+"/"+"address");
                myRef.setValue(edAddress.getText().toString());

                DatabaseReference myRef1 = database.getReference("image"+"/"+"image"+ID+"/"+"description");
                myRef1.setValue(edDescription.getText().toString());

                DatabaseReference myRef2 = database.getReference("image"+"/"+"image"+ID+"/"+"id");
                myRef2.setValue("image"+ID);

                DatabaseReference myRef3 = database.getReference("image"+"/"+"image"+ID+"/"+"image");
                myRef3.setValue(edImage.getText().toString());

                DatabaseReference myRef4 = database.getReference("image"+"/"+"image"+ID+"/"+"location");
                myRef4.setValue(edLocation.getText().toString());

                DatabaseReference myRef5 = database.getReference("image"+"/"+"image"+ID+"/"+"price");
                myRef5.setValue(Integer.valueOf(edPrice.getText().toString()));

                DatabaseReference myRef6 = database.getReference("image"+"/"+"image"+ID+"/"+"shortDescription");
                myRef6.setValue(Integer.valueOf(edPrice.getText().toString()));

                DatabaseReference myRef7 = database.getReference("image"+"/"+"image"+ID+"/"+"userID");
                myRef7.setValue(temp.toString());
            }
        });

        btn_Cancel = findViewById(R.id.button_Huy);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateRoomProvider.this, "Tạo sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                loadFragment(new HomeFragmentProvider());
            }
        });
    }
    boolean loadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerProvider, fragment).commit();
            return true;
        }
        return false;
    }
}


