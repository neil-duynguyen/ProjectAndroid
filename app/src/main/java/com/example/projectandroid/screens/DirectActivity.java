package com.example.projectandroid.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.projectandroid.R;
import com.example.projectandroid.model.DirectionResponse;
import com.example.projectandroid.model.DirectionService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.concurrent.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectActivity extends AppCompatActivity implements OnMapReadyCallback  {
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private LatLng originLatLng; // Điểm xuất phát
    private LatLng destinationLatLng; // Điểm đến
    private String apiKey = "AIzaSyDrCOrmjDp7-9AFyfqvyqSd2ijz5-nfVrE";
    Button btn;
    double latitude;
    double longitude;

    FusedLocationProviderClient fusedLocationProviderClient;
    double latitudeNow , longitudeNow;
    ;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct);
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        splip(location);
        btn =findViewById(R.id.direction);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);
        /*Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getCurrent();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();*/

        // Kiểm tra quyền truy cập vị trí của người dùng
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu không có quyền, yêu cầu quyền truy cập vị trí
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Nếu đã có quyền, lấy vị trí hiện tại một lần
            getLocationOnce();
        }
    }

    /*private void getCurrent() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
    }*/
    private void getLocationOnce() {


        // Tạo yêu cầu vị trí
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1); // Số lượng cập nhật vị trí tối đa (1 lần)

        // Kiểm tra xem có quyền truy cập vị trí hay không
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Gửi yêu cầu lấy vị trí một lần
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    // Hủy đăng ký callback sau khi nhận được vị trí
                    fusedLocationProviderClient.removeLocationUpdates(this);

                    if (locationResult != null) {
                        // Lấy vị trí hiện tại từ kết quả
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            // Lấy tọa độ Latitude và Longitude
                            latitudeNow = location.getLatitude();
                            longitudeNow = location.getLongitude();

                            onLocationReceived();
                        } else {
                            // Không lấy được vị trí, xử lý lỗi tại đây (nếu cần)
                        }
                    }
                }
            }, null);
        } else {
            // Xử lý khi không có quyền truy cập vị trí (nếu cần)
            // Ví dụ: hiển thị thông báo yêu cầu cấp quyền truy cập vị trí
        }
    }

    private void onLocationReceived() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.store_map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            direaction();
        } else {
            // Xử lý khi không tìm thấy SupportMapFragment trong layout
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền truy cập vị trí, lấy vị trí hiện tại một lần
                getLocationOnce();
            } else {
                // Người dùng không cấp quyền truy cập vị trí, xử lý lỗi tại đây
            }
        }
    }

    void splip(String location){
        String[] parts = location.split(",");
        // Lấy giá trị của hai phần tử trong mảng parts
        latitude = Double.parseDouble(parts[0].trim());
        longitude = Double.parseDouble(parts[1].trim());
    }

    private void direaction() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.store_map_fragment);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    // Gọi hàm để lấy và vẽ chỉ đường
                    getDirections();
                }
            });
        }
    }


    private void getDirections() {// Tạo đối tượng Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/") // Base URL của API
                .addConverterFactory(GsonConverterFactory.create()) // Sử dụng Gson để phân tích dữ liệu
                .build();

        // Tạo service cho API interface
        DirectionService service = retrofit.create(DirectionService.class);
        //String origin = "10.828094808060053,106.72116579141313"; // Thay thế bằng tọa độ điểm xuất phát thực tế
        //String destination = "10.827968354293938,106.72846140019244";
        String origin = String.valueOf(latitudeNow) + ","+String.valueOf(longitudeNow); // Thay thế bằng tọa độ điểm xuất phát thực tế
        String destination = latitude + "," + longitude; // Thay thế bằng tọa độ điểm đến thực tế

        // Gửi yêu cầu lấy chỉ đường
        Call<DirectionResponse> call = service.getDirections(
                origin, // Tọa độ điểm xuất phát
                destination, // Tọa độ điểm đến
                apiKey // API key của bạn
        );

        // Xử lý kết quả
        call.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                if (response.isSuccessful()) {
                    DirectionResponse directionResponse = response.body();
                    if (directionResponse != null && directionResponse.getRoutes() != null && directionResponse.getRoutes().size() > 0) {
                        // Lấy polyline từ response
                        String polyline = directionResponse.getRoutes().get(0).getOverviewPolyline().getPoints();
                        drawPolylineOnMap(polyline);
                    }
                } else {
                    // Xử lý khi có lỗi từ API
                }
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {
                // Xử lý khi có lỗi trong quá trình gửi yêu cầu hoặc nhận kết quả
            }
        });
    }

    private void drawPolylineOnMap(String polyline) {
        // Xóa tất cả các polyline và marker có sẵn trên bản đồ (nếu có)
        mMap.clear();

        // Tạo danh sách các điểm tạo thành đường polyline
        List<LatLng> points = decodePolyline(polyline);

        // Vẽ polyline lên bản đồ
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points)
                .width(8)
                .color(Color.BLUE); // Màu của đường polyline

        Polyline line = mMap.addPolyline(polylineOptions);

        // Di chuyển camera để hiển thị toàn bộ polyline
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

        // Thêm marker tại điểm xuất phát
        if (points.size() > 0) {
            originLatLng = points.get(0);
            mMap.addMarker(new MarkerOptions()
                    .position(originLatLng)
                    .title("Điểm xuất phát"));
        }

        // Thêm marker tại điểm đến
        if (points.size() > 1) {
            float temp = calculateDistance(points.get(0), points.get(1));
            btn.setText("Distance: " + temp + " km");
            destinationLatLng = points.get(points.size() - 1);
            mMap.addMarker(new MarkerOptions()
                    .position(destinationLatLng)
                    .title("Điểm đến"));
        }

    }

    // Phương thức để tính khoảng cách giữa hai điểm
    private float calculateDistance(LatLng start, LatLng end) {
        Location startLocation = new Location("");
        startLocation.setLatitude(start.latitude);
        startLocation.setLongitude(start.longitude);

        Location endLocation = new Location("");
        endLocation.setLatitude(end.latitude);
        endLocation.setLongitude(end.longitude);

        // Tính khoảng cách trong mét
        float distanceInMeters = startLocation.distanceTo(endLocation);

        // Chuyển đổi sang kilômét và trả về
        return distanceInMeters / 100.0f;
    }

    // Phương thức để giải mã chuỗi polyline thành danh sách các điểm (LatLng)
    private List<LatLng> decodePolyline(String polyline) {
        List<LatLng> decodedPolyline = new ArrayList<>();
        int index = 0;
        int len = polyline.length();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng point = new LatLng(lat / 1E5, lng / 1E5);
            decodedPolyline.add(point);
        }

        return decodedPolyline;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getDirections();
    }
}