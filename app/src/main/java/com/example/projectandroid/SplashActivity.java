package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.projectandroid.screens.HomeActivity;
import com.example.projectandroid.screens.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //code video 4 phút 11
        //FirebaseUser user = FirebaseAuth.getInstanse().getCurrentuser();

        //splash screen wait for 2 sec and then Launch Login activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //if(user != null) {
                //if iser is already login then it will go to home screem
                //he do not need to login again
                    //startActivities(new Intent[]{new Intent(SplashActivity.this, HomeActivity.class)});
                //} else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivities(new Intent[]{intent});
                    finish();
                //}
            }
        }, 3000);
    }
}