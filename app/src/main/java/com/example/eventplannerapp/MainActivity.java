package com.example.eventplannerapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bn;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        fm = getSupportFragmentManager();
        bn = findViewById(R.id.bottom_menu);

        bn.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.see_events:
                        item.setCheckable(true);
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, ListFragment.class, new Bundle());
                        transaction.commit();
                        return true;

                    case R.id.create_events:
                        item.setCheckable(true);
                        FragmentTransaction transaction2 = fm.beginTransaction();
                        transaction2.replace(R.id.nav_host_fragment, CreateFragment.class, new Bundle());
                        transaction2.commit();
                        return true;

                    case R.id.map:
                        item.setCheckable(true);
                        goToMap();
                        return true;

                    case R.id.profile:
                        item.setCheckable(true);
                        FragmentTransaction transaction4 = fm.beginTransaction();
                        transaction4.replace(R.id.nav_host_fragment, ProfileFragment.class, new Bundle());
                        transaction4.commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void goToMap() {
        Intent mdirect = new Intent(this, MapsActivity.class);
        startActivity(mdirect);
    }
}