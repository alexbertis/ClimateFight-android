package com.brontapps.climatefight;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.preference.PreferenceManager;
import android.view.MenuItem;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;
import java.util.List;

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
        viewModel.activity = MainActivity.this;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("eventos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // TODO: código de muestra de conexión a la DB
                List<ItemHome> items = new ArrayList<>();
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    items.add(new ItemHome(ds.getString("nombre"), ds.getString("descripcion"),
                            (ds.getLong("tipo")).intValue(), ds.getLong("inicio"), ds.getLong("fin"),
                            ds.getBoolean("periodico"), ds.getGeoPoint("centro"), ds.getString("nlugar"), ds.getString("url")));
                }
                viewModel.itemList.setValue(items);
            }
        });
    }

}
