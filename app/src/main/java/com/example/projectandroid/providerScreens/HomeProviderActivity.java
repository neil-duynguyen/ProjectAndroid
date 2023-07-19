package com.example.projectandroid.providerScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectandroid.R;
import com.example.projectandroid.fragments.AccountFragment;
import com.example.projectandroid.fragments.ChatFragment;
import com.example.projectandroid.fragments.FavoriteFragment;
import com.example.projectandroid.fragments.HomeFragment;
import com.example.projectandroid.providerFragments.AccountFragmentProvider;
import com.example.projectandroid.providerFragments.ChatFragmentProvider;
import com.example.projectandroid.providerFragments.HomeFragmentProvider;
import com.example.projectandroid.providerFragments.TransactionFragmentProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeProviderActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationViewProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_provider);

        bottomNavigationViewProvider = findViewById(R.id.bottom_navigationProvider);
        bottomNavigationViewProvider.setOnItemSelectedListener(this);
        loadFragment(new HomeFragmentProvider());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        Integer getItem = item.getItemId();

        if(getItem == R.id.menu_home_provider)
        {
            fragment = new HomeFragmentProvider();
        }

        if(getItem == R.id.menu_transction_provider)
        {
            fragment = new TransactionFragmentProvider();
        }

        if(getItem == R.id.menu_chat_provider)
        {
            fragment = new ChatFragmentProvider();
        }

        if(getItem == R.id.menu_account_provider)
        {
            fragment = new AccountFragmentProvider();
        }

        return loadFragment(fragment);
    }

    boolean loadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerProvider, fragment).commit();
            return true;
        }
        return false;
    }
}