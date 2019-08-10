package com.climate.fight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.climate.fight.recycler.ItemHome;
import com.climate.fight.recycler.MultiAdapter;
import com.climate.fight.ui.crear.CrearActivity;
import com.climate.fight.ui.main.FeedFragment;
import com.climate.fight.ui.main.MainMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();
    private final Fragment fragmentF = new FeedFragment();
    private Fragment active = fragmentF;
    private final Fragment fragmentM = new MainMapFragment();


    private List<ItemHome> itemList = new ArrayList<>();
    private MultiAdapter adapter = null;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
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

        // TODO: salvar los fragmentos al rotar la pantalla

        fm.beginTransaction().add(R.id.main_container, fragmentM, "map").hide(fragmentM).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentF, "feed").commit();

        updateFirestoreMain();


        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void updateFirestoreMain(){
        long millis = new GregorianCalendar().getTimeInMillis();
        firestore.collection("eventos").orderBy("inicio").startAt(millis).get().addOnSuccessListener(queryDocumentSnapshots -> {
            // TODO: código de muestra de conexión a la DB
            itemList = new ArrayList<>();
            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                try {
                    itemList.add(new ItemHome(ds.getString("nombre"), ds.getString("descripcion"),
                            (ds.getLong("tipo")).intValue(), ds.getLong("inicio"), ds.getLong("fin"),
                            ds.getBoolean("periodico"), ds.getBoolean("urgent"), ds.getGeoPoint("centro"), ds.getLong("radio").intValue(), ds.getString("nlugar"), ds.getString("url")));
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(adapter != null)
                    adapter.notifyDataSetChanged();
            }
        });
    }

    public List<ItemHome> getItemsHome(){
        return itemList;
    }

    public void setAdapter(MultiAdapter adapter){
        this.adapter = adapter;
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
            i = new Intent(this, CrearActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
