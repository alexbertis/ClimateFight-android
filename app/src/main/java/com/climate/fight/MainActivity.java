package com.climate.fight;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.climate.fight.recycler.ItemHome;
import com.climate.fight.recycler.MultiAdapter;
import com.climate.fight.ui.crear.CrearActivity;
import com.climate.fight.ui.main.FeedFragment;
import com.climate.fight.ui.main.MainMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final FragmentManager fm = getSupportFragmentManager();
    private final Fragment fragmentF = new FeedFragment();
    private Fragment active = fragmentF;
    private final Fragment fragmentM = new MainMapFragment();

    SharedPreferences prefs;

    private FusedLocationProviderClient fusedLocationClient;
    private Location location;
    private LocationCallback locationCallback;

    private List<ItemHome> itemList = new ArrayList<>();
    private MultiAdapter adapter = null;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_feed:
                Log.e("AFD", "Paso 1 feed");
                fm.beginTransaction().hide(active).show(fragmentF).commit();
                active = fragmentF;
                Log.e("AFDaE", active.getClass().getName());
                return true;

            case R.id.navigation_map:
                Log.e("AFD", "Paso 1 map");
                fm.beginTransaction().hide(active).show(fragmentM).commit();
                active = fragmentM;
                Log.e("AFDaE", active.getClass().getName());
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        firestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());

        // TODO: salvar los fragmentos al rotar la pantalla

        fm.beginTransaction().add(R.id.main_container, fragmentM, "map").hide(fragmentM).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentF, "feed").commit();

        Context ctx = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        Configuration.getInstance().load(ctx, prefs);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        updateFirestoreMain(adapter, false);

        if(prefs.getBoolean("location", true)) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
            } else {
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            return;
                        }
                        for (Location locat : locationResult.getLocations()) {
                            location = locat;
                        }
                    }
                };
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setInterval(60000);
                locationRequest.setFastestInterval(30000);
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void updateFirestoreMain(MultiAdapter adapter, boolean fallback) {
        long millis = new Date().getTime();
        if(fallback || !prefs.getBoolean("location", true)){
            getFirestore(adapter, null, millis);
        }else {
            if(location == null) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
                } else fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(loc -> {
                            location = loc;
                            getFirestore(adapter, location, millis);
                        })
                        .addOnCanceledListener(() -> getFirestore(adapter, null, millis))
                        .addOnFailureListener(e -> getFirestore(adapter, null, millis));
            }else{
                getFirestore(adapter, location, millis);
            }
        }
    }

    private void getFirestore(MultiAdapter adapter, Location loc, long millis){
        firestore.collection("eventos").orderBy("fin").startAt(millis)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            // TODO: código de muestra de conexión a la DB
            itemList.clear();
            if(loc != null) {
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    try {
                        GeoPoint gp = ds.getGeoPoint("centro");
                        Location loco = new Location("");
                        loco.setLatitude(gp.getLatitude());
                        loco.setLongitude(gp.getLongitude());
                        int metrosDistA = ((int) (loco.distanceTo(loc) / 50)) * 50;
                        itemList.add(new ItemHome(ds.getString("nombre"), ds.getString("descripcion"),
                                (ds.getLong("tipo")).intValue(), ds.getId(), ds.getLong("inicio"), ds.getLong("fin"),
                                ds.getBoolean("periodico"), ds.getBoolean("urgent"), gp,
                                ds.getLong("radio").intValue(), ds.getString("nlugar"), ds.getString("url"), metrosDistA));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(itemList, (o1, o2) -> {
                    int metrosDistA = o1.getDistToUser();
                    int metrosDistB = o2.getDistToUser();
                    if (metrosDistA == metrosDistB) return 0;
                    else if (metrosDistA < metrosDistB) return -1;
                    else return 1;
                });
            }else{
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    try {
                        GeoPoint gp = ds.getGeoPoint("centro");
                        itemList.add(new ItemHome(ds.getString("nombre"), ds.getString("descripcion"),
                                (ds.getLong("tipo")).intValue(), ds.getId(), ds.getLong("inicio"), ds.getLong("fin"),
                                ds.getBoolean("periodico"), ds.getBoolean("urgent"), gp,
                                ds.getLong("radio").intValue(), ds.getString("nlugar"), ds.getString("url"), -1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (adapter != null) {
                MainActivity.this.adapter = adapter;
                adapter.notifyDataSetChanged();
            }
        });
    }

    public List<ItemHome> getItemsHome(){
        return itemList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == 1002){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, R.string.loc_perm_granted, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, R.string.loc_perm_denied, Toast.LENGTH_SHORT).show();
            }
            updateFirestoreMain(adapter, true);
        }else if(requestCode == 1003){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, R.string.loc_perm_granted, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, R.string.loc_perm_denied, Toast.LENGTH_SHORT).show();
            }
            updateFirestoreMain(adapter, true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent i;
        if (id == R.id.main_settings) {
            i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }else if(id == R.id.main_account){
            i = new Intent(this, AccountActivity.class);
            startActivity(i);
        }else if(id == R.id.main_add){
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                Toast.makeText(MainActivity.this, R.string.new_event_login_required, Toast.LENGTH_SHORT).show();
            }else {
                i = new Intent(this, CrearActivity.class);
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
