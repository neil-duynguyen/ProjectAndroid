package com.example.projectandroid.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectandroid.R;
import com.example.projectandroid.model.DirectionResponse;
import com.example.projectandroid.model.DirectionService;
import com.example.projectandroid.screens.DetailsActivity;
import com.example.projectandroid.screens.DirectActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreMapActivity extends AppCompatActivity {
    private StoreMapFragment _storeMapFragment;
    MarkerOptions placeNow, palceTo;
    double latitude;
    double longitude;
    Button btn ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        splip(location);

        btn = findViewById(R.id.direction);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreMapActivity.this, DirectActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.store_map_fragment);

        _storeMapFragment = new StoreMapFragment(mapFragment);
        _storeMapFragment.STORE_LATITUDE = latitude;
        _storeMapFragment.STORE_LONGITUDE = longitude;
        _storeMapFragment.InitGoogleMap();
    }

    void splip(String location){
        String[] parts = location.split(",");
        // Lấy giá trị của hai phần tử trong mảng parts
       latitude = Double.parseDouble(parts[0].trim());
        longitude = Double.parseDouble(parts[1].trim());
    }

}