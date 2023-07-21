package com.example.projectandroid.fragments;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StoreMapFragment {
     public  double STORE_LATITUDE = 0;
     public  double STORE_LONGITUDE = 0;
    private GoogleMap _googleMap;
    private Marker _storeMarker;
    private Marker _activatingMarker;
    private SupportMapFragment _supportMapFragment;

    public StoreMapFragment(SupportMapFragment supportMapFragment) {
        this._supportMapFragment = supportMapFragment;
    }

    public void InitGoogleMap()
    {
        _supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                _googleMap = googleMap;

                SetStoreMarkerLocation();

                _googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        CreateNewMarker(latLng);
                    }
                });

                _googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDrag(@NonNull Marker marker) {
                    }

                    @Override
                    public void onMarkerDragEnd(@NonNull Marker marker) {
                        if(marker == _storeMarker || marker != _activatingMarker) return;

                        _activatingMarker.setTitle(String.format("%.2f, %.2f", marker.getPosition().latitude, marker.getPosition().longitude));
                        Toast.makeText(_supportMapFragment.getContext(), "On Drag End", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMarkerDragStart(@NonNull Marker marker) {
                        Toast.makeText(_supportMapFragment.getContext(), "On Drag Start", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void SetStoreMarkerLocation()
    {
        LatLng storeLocation = new LatLng(STORE_LATITUDE, STORE_LONGITUDE);

        _storeMarker = _googleMap.addMarker(
                new MarkerOptions()
                        .position(storeLocation)
                        .title("")
                        .snippet("Selling .... here")
        );

        _googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15.0f));
    }

    private void CreateNewMarker(LatLng latLng)
    {
        if(_activatingMarker != null) _activatingMarker.remove();

        _activatingMarker = _googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .title(String.format("%.2f, %.2f", latLng.latitude, latLng.longitude)));

        _activatingMarker.setDraggable(true);
    }
}

