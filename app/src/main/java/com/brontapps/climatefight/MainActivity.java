package com.brontapps.climatefight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.brontapps.climatefight.ui.main.AccountFragment;
import com.brontapps.climatefight.ui.main.FeedFragment;
import com.brontapps.climatefight.ui.main.MainMapFragment;
import com.brontapps.climatefight.ui.main.MainSharedViewModel;
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
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MainSharedViewModel viewModel;


    final FragmentManager fm = getSupportFragmentManager();

    final Fragment fragmentF = new FeedFragment();
    final Fragment fragmentM = new MainMapFragment();
    final Fragment fragmentA = new AccountFragment();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_feed:
                        fm.beginTransaction().hide(viewModel.active).show(fragmentF).commit();
                        viewModel.active = fragmentF;
                        return true;

                    case R.id.navigation_map:
                        fm.beginTransaction().hide(viewModel.active).show(fragmentM).commit();
                        viewModel.active = fragmentM;
                        return true;

                    case R.id.navigation_account:
                        fm.beginTransaction().hide(viewModel.active).show(fragmentA).commit();
                        viewModel.active = fragmentA;
                        return true;
                }
                return false;
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // TODO: salvar los fragmentos al rotar la pantalla
        viewModel = ViewModelProviders.of(this).get(MainSharedViewModel.class);
        viewModel.activity = MainActivity.this;
        viewModel.initMain(fm, fragmentA, fragmentM, fragmentF);
        viewModel.updateFirestoreMain(FirebaseFirestore.getInstance());


        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
