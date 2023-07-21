package com.example.projectandroid.providerScreens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectandroid.R;
import com.example.projectandroid.providerFragments.StoreMapFragmentProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;

public class StoreMapActivity extends AppCompatActivity {
//    private GoogleMap _googleMap;

    private StoreMapFragmentProvider _storeMapFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.store_map_fragment);

        _storeMapFragment = new StoreMapFragmentProvider(mapFragment);
        _storeMapFragment.InitGoogleMap();


    }
}