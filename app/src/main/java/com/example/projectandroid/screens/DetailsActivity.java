package com.example.projectandroid.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectandroid.R;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price, shortDescription, description;

    String pri , des;
    String img , shor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageView = findViewById(R.id.imageView);
        price = findViewById(R.id.price);
        shortDescription = findViewById(R.id.short_description);
        description = findViewById(R.id.description);

        pri = getIntent().getStringExtra("price");
        des = getIntent().getStringExtra("description");
        img = getIntent().getStringExtra("image");
        shor = getIntent().getStringExtra("shortDescription");

        price.setText("$" + pri);
        description.setText(des);
        shortDescription.setText(shor);
        Glide
                .with(this)
                .load(img)
                .centerCrop()
                .placeholder(R.drawable.ic_account)
                .into(imageView);
    }
}