package com.example.projectandroid.providerScreens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectandroid.R;
import com.example.projectandroid.fragments.HomeFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SelectImageActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    ImageView img ;
    Button btnCon , btnAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        img = findViewById(R.id.selectImage_img);
        btnCon = findViewById(R.id.con);
         btnAgain = findViewById(R.id.again);
         btnCon.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });

         btnAgain.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 pickImageFromGallery();
             }
         });

        pickImageFromGallery();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // Tạo tên duy nhất cho hình ảnh
            String uniqueImageName = "image_" + System.currentTimeMillis() + ".jpg";

            StorageReference imageRef = storageRef.child("images/" + uniqueImageName);
            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Tải lên thành công, lấy link của hình ảnh từ Firebase Storage
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    RequestOptions requestOptions = new RequestOptions()
                            .centerCrop() // Hiển thị ảnh với kiểu scale là centerCrop
                            .placeholder(R.drawable.ic_account) // Hình placeholder sẽ được hiển thị trong lúc đang tải ảnh
                            .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh

                    Glide.with(SelectImageActivity.this)
                            .load(imageUrl)
                            .apply(requestOptions) // Áp dụng RequestOptions vào Glide
                            .into(img); // ImageView mà bạn muốn hiển thị ảnh
                    Intent intent = new Intent();
                    intent.putExtra("image",imageUrl);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                });
            }).addOnFailureListener(e -> {
                // Xử lý nếu có lỗi xảy ra trong quá trình tải lên
                showToast("Lỗi khi tải ảnh lên Firebase Storage");
            });
        } else {
            showToast("Không có hình ảnh được chọn");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            uploadImageToFirebase(selectedImageUri);
        }
    }
}