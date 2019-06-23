package com.brontapps.climatefight;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    final Fragment fragmentF = new FeedFragment();
    final Fragment fragmentM = new MainMapFragment();
    final Fragment fragmentA = new AccountFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentF;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    fm.beginTransaction().hide(active).show(fragmentF).commit();
                    active = fragmentF;
                    return true;

                case R.id.navigation_map:
                    fm.beginTransaction().hide(active).show(fragmentM).commit();
                    active = fragmentM;
                    return true;

                case R.id.navigation_account:
                    fm.beginTransaction().hide(active).show(fragmentA).commit();
                    active = fragmentA;
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.main_container, fragmentA, "account").hide(fragmentA).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentM, "map").hide(fragmentM).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentF, "feed").commit();
    }

}
