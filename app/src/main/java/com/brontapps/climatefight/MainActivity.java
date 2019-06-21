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

    final FragmentManager fm = getSupportFragmentManager();

    final Fragment fragmentF = new FeedFragment();
    /*final*/ Fragment fragmentM /*= new DashboardFragment()*/;
    /*final*/ Fragment fragmentA /*= new NotificationsFragment()*/;
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

    }

}
