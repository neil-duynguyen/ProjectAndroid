package com.example.projectandroid.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.fragments.HomeFragment;
import com.example.projectandroid.fragments.StoreMapActivity;
import com.example.projectandroid.model.Item;
import com.example.projectandroid.model.Post;
import com.example.projectandroid.model.User;
import com.example.projectandroid.providerScreens.HomeProviderActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    ImageView map;
    private TextView price, shortDescription, description, nameUser;

    de.hdodenhof.circleimageview.CircleImageView imgProfile;
    String pri , des;
    String img , shor , id;
    Post item;
    User Provider ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageView = findViewById(R.id.imageView);
        price = findViewById(R.id.price);
        shortDescription = findViewById(R.id.short_description);
        description = findViewById(R.id.description);
        nameUser = findViewById(R.id.home_frag_user_name);
        imgProfile = findViewById(R.id.profile_image_detail);
        map = (ImageView) findViewById(R.id.map);
        getProviderAccount() ;
        pri = getIntent().getStringExtra("price");
        des = getIntent().getStringExtra("description");
        img = getIntent().getStringExtra("image");
        shor = getIntent().getStringExtra("shortDescription");
        id = getIntent().getStringExtra("id");
        item= new Post();
        getPost(id);


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, StoreMapActivity.class);
                intent.putExtra("location", item.location);
                startActivity(intent);
            }
        });



//        DatabaseReference providerID = FirebaseDatabase.getInstance().getReference("image");
//        //providerID.child(id).getKey();
//        providerID.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Lấy dữ liệu từ danh sách và xử lý
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Item item = snapshot.getValue(Item.class);
//                    // Xử lý object ở đây
//                    if(snapshot.getKey().equals(id)){
//                        String name = item.getProviderId();
////                        setText(item.getName());
////                        Glide
////                                .with(DetailsActivity.this)
////                                .load(user.getImage())
////                                .centerCrop()
////                                .placeholder(R.drawable.ic_account)
////                                .into(imgProfile);
////                        return;
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý lỗi nếu có
//            }
//        });

        price.setText(pri + "VND");
        description.setText(des);
        shortDescription.setText(shor);
        Glide
                .with(this)
                .load(img)
                .centerCrop()
                .placeholder(R.drawable.ic_account)
                .into(imageView);
    }


    void getProviderAccount() {
        FirebaseUser temp = FirebaseAuth.getInstance().getCurrentUser();

        if (temp != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Lấy dữ liệu từ danh sách và xử lý
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User object = snapshot.getValue(User.class);
                        // Xử lý object ở đây
                        if (snapshot.getKey().equals(temp.getUid())) {
                            Provider = object;
                            Glide
                                    .with(DetailsActivity.this)
                                    .load(object.getImage())
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_account)
                                    .into(imgProfile);
                            nameUser.setText(object.getName());

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                }
            });
        }
    }

    private void getPost(String Id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("image");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    if(productSnapshot.getKey().equals(id)){
                        item = productSnapshot.getValue(Post.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra.
            }
        });

    }


}