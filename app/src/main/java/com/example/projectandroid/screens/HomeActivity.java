package com.example.projectandroid.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.projectandroid.R;
import com.example.projectandroid.fragments.AccountFragment;
import com.example.projectandroid.fragments.ChatFragment;
import com.example.projectandroid.fragments.FavoriteFragment;
import com.example.projectandroid.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        Integer getItem = item.getItemId();

        if(getItem == R.id.menu_home)
        {
            fragment = new HomeFragment();
        }

        if(getItem == R.id.menu_favorite)
        {
            fragment = new FavoriteFragment();
        }

        if(getItem == R.id.menu_chat)
        {
            fragment = new ChatFragment();
        }

        if(getItem == R.id.menu_account)
        {
            fragment = new AccountFragment();
        }

        return loadFragment(fragment);
    }

    boolean loadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }
}