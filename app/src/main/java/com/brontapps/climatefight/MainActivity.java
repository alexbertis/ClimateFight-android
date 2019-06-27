package com.brontapps.climatefight;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.osmdroid.config.Configuration;

import java.util.Collections;

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

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        final MainSharedViewModel viewModel = ViewModelProviders.of(this).get(MainSharedViewModel.class);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("eventos").document("cI5TuJL5kXjikv4cfTVk").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                // TODO: código de muestra de conexión a la DB
                viewModel.itemList.setValue(Collections.singletonList(new ItemHome(ds.getString("nombre"), ds.getString("descripcion"), ds.getLong("tipo"), ds.getGeoPoint("centro"))));
            }
        });
    }

}
