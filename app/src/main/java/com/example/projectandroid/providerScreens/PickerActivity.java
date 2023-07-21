package com.example.projectandroid.providerScreens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projectandroid.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class PickerActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LatLng selectedLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private AlertDialog dialog; // Khai báo biến dialog là biến toàn cục

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        // Kiểm tra quyền truy cập vị trí của người dùng
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu không có quyền, yêu cầu quyền truy cập vị trí
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Nếu đã có quyền, lấy tọa độ hiện tại và hiển thị dialog chọn vị trí
            showMapPickerDialog();
        }
    }

    private void showMapPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_map_picker, null);
        builder.setView(dialogView);
        builder.setTitle("Chọn vị trí trên bản đồ");

        // Khởi tạo SupportMapFragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        // Ánh xạ các view trong dialog
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Xử lý sự kiện click nút "Lưu"
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng nhấn nút "Lưu"
                if (selectedLocation != null) {
                    // Hiển thị Toast chứa tọa độ của điểm được chọn
                    Toast.makeText(PickerActivity.this, "Đã chọn vị trí: " + selectedLocation.latitude + ", " + selectedLocation.longitude, Toast.LENGTH_SHORT).show();
                    // Đóng dialog sau khi lưu thành công
                    dialog.dismiss();
                } else {
                    Toast.makeText(PickerActivity.this, "Vui lòng chọn vị trí trên bản đồ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Hiển thị vị trí hiện tại của thiết bị lên bản đồ
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        // Lấy vị trí hiện tại từ FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng currentLocation = new LatLng(latitude, longitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                }
            }
        });

        // Lắng nghe sự kiện click trên bản đồ
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Lưu tọa độ của điểm được chọn
                selectedLocation = latLng;

                // Xóa marker cũ (nếu có)
                mMap.clear();

                // Hiển thị marker tại vị trí được chọn
                mMap.addMarker(new MarkerOptions().position(selectedLocation));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle("MapViewBundle");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("MapViewBundle", mapViewBundle);
        }
        mapFragment.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }
}