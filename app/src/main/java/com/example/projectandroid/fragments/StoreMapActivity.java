package com.example.projectandroid.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projectandroid.R;
import com.google.android.gms.maps.SupportMapFragment;

public class StoreMapActivity extends AppCompatActivity {
    private StoreMapFragment _storeMapFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.store_map_fragment);

        _storeMapFragment = new StoreMapFragment(mapFragment);
        _storeMapFragment.InitGoogleMap();
    }

}