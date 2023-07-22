package com.example.projectandroid.providerScreens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectandroid.R;
import com.example.projectandroid.fragments.HomeFragment;
import com.example.projectandroid.model.Post;
import com.example.projectandroid.model.Transaction;
import com.example.projectandroid.model.User;
import com.example.projectandroid.providerFragments.HomeFragmentProvider;
import com.example.projectandroid.screens.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CreateRoomProvider extends AppCompatActivity{
    private static final int REQUEST_CODE_MY_ACTIVITY = 1001;
    EditText  edAddress, edDescription, edShortDescription, edPrice;

    Button btn_Create, btn_Cancel, btnLocattion, btnImage;
    TextView error ;
    List<Post> listPost;
    List<Integer> listID;
    String location = "";
    String image = "";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LatLng selectedLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private AlertDialog dialog; // Khai báo biến dialog là biến toàn cục
    ProgressDialog progressDialog;
    FirebaseUser temp;
    User tempUser;

    final Integer[] priceCost = new Integer[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room_provider);
        getUser();

        //String currency = price.substring(price.length() - 3);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pricePost");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer price = (Integer) snapshot.getValue(Integer.class);
                priceCost[0] = price;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnImage = findViewById(R.id.edit_Image);
        edAddress = findViewById(R.id.edit_Address);
        btnLocattion= findViewById(R.id.edit_Location);
        edDescription = findViewById(R.id.edit_Description);
        edShortDescription = findViewById(R.id.edit_ShortDescription);
        edPrice = findViewById(R.id.edit_Price);
        error = findViewById(R.id.leave_add_error);


        final Integer[] id = {Integer.valueOf(getIntent().getStringExtra("id"))};

        String temp = FirebaseAuth.getInstance().getCurrentUser().zzb().getUid(); //lấy user hiện tại

        btn_Create = findViewById(R.id.button_Tao);
        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.equals("") || image.equals("") || edAddress.getText().toString().equals("") ||
                        edDescription.getText().toString().equals("") || edShortDescription.getText().toString().equals("") || edPrice.getText().toString().equals("")) {
                    error.setText("Vui lòng điền đầy đủ thông tin!");
                } else {
                    try {
                        long price = Long.parseLong(edPrice.getText().toString());

                    } catch (Exception ex) {
                        edPrice.setError("Number Only!");
                    }
                }


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Integer ID = ++id[0];

                Post post = new Post();
                post.id = "image"+ID;
                post.description = edDescription.getText().toString();
                post.price = Long.parseLong(edPrice.getText().toString());
                post.location = location;
                post.image= image;
                post.userID = temp.toString();
                post.address = edAddress.getText().toString();
                post.shortDescription = edShortDescription.getText().toString();
                DatabaseReference myRef = database.getReference("image" + "/" + "image" + ID + "/");
                myRef.setValue(post, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        updateWallet();
                    }
                });
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

    private void getUser() {

        temp = FirebaseAuth.getInstance().getCurrentUser();

        if(temp!=null){

        }else{
            Intent intent = new Intent(CreateRoomProvider.this, LoginActivity.class);
            intent.putExtra("Error", "Đã xảy ra lỗi trong qua trình xử lý!");
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MY_ACTIVITY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Lấy dữ liệu từ Intent
                if(data.getStringExtra("location")!=null){
                    location = data.getStringExtra("location");
                    if(!location.equals("")){
                        btnLocattion.setText("Selected");
                    }
                }
                if(data.getStringExtra("image")!=null){
                    image = data.getStringExtra("image");
                    if(!image.equals("")){
                        btnImage.setText("Selected");
                    }
                }


            }
        }
    }


    public void btn_dateSelect_click(View view){
        Intent intent = new Intent(this, PickerActivity.class);
        startActivityForResult(intent, REQUEST_CODE_MY_ACTIVITY);

    }

    public void btn_imageSelect_click(View view){
        Intent intent = new Intent(this, SelectImageActivity.class);
        startActivityForResult(intent, REQUEST_CODE_MY_ACTIVITY);
    }



    void updateWallet(){
        String nodePath = "users/" + temp.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(nodePath);
        long total= tempUser.getWallet() - priceCost[0];
        Map<String, Object> updates = new HashMap<>();
        updates.put("wallet",total );
        databaseReference.updateChildren(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(CreateRoomProvider.this, HomeProviderActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu cập nhật thất bại
                    }
                });
    }
}


