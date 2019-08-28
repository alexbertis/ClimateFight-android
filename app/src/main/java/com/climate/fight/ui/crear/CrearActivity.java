package com.climate.fight.ui.crear;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.climate.fight.MainActivity;
import com.climate.fight.R;
import com.climate.fight.recycler.ItemHome;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CrearActivity extends AppCompatActivity {

    private final Fragment f1 = Crear1Fragment.newInstance();
    private final Fragment f2 = Crear2Fragment.newInstance();
    private final Fragment f3 = Crear3Fragment.newInstance();
    private Fragment active = f1;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final FragmentManager fm = getSupportFragmentManager();

    private String name = "";
    private String desc = "";
    private String place = "";
    private String url = "";
    private GeoPoint center = null;
    private long start = System.currentTimeMillis();
    private long end = System.currentTimeMillis();
    private int type = -1;
    private final int range = 50;
    private boolean urgent = false;
    private AppCompatButton btnNext, btnReturn;
    private final View.OnClickListener nextDefault = view -> {
        Crear1Fragment c1f = (Crear1Fragment)f1;
        Crear2Fragment c2f = (Crear2Fragment)f2;
        Crear3Fragment c3f = (Crear3Fragment)f3;
        if(active == f1){
            fm.beginTransaction().hide(active).show(f2).commit();
            int tipo = -1;
            switch (c1f.rg.getCheckedRadioButtonId()){
                case R.id.typeProtNewEv:
                    tipo = ItemHome.TIPO_MANIF;
                    break;
                case R.id.typeRubbNewEv:
                    tipo = ItemHome.TIPO_BATIDA;
                    break;
                case R.id.typeTalkNewEv:
                    tipo = ItemHome.TIPO_REUNION;
                    break;
                case R.id.typeWorksNewEv:
                    tipo = ItemHome.TIPO_TALLER;
                    break;
            }
            setD1(c1f.name.getEditText().getText().toString(), tipo, c1f.urg.isChecked());
            active = f2;
        }else if(active == f2){
            fm.beginTransaction().hide(active).show(f3).commit();
            try {
                setD2(c2f.placeEv.getEditText().getText().toString(),
                        c2f.webEv.getEditText().getText().toString(),
                        sdf.parse(c2f.startDateTimeEv.getEditText().getText().toString()).getTime(),
                        sdf.parse(c2f.endDateTimeEv.getEditText().getText().toString()).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.err_invalid_date, Toast.LENGTH_SHORT).show();
            }
            active = f3;
            btnNext.setText(R.string.upload);
        }else if(active == f3){
            desc = c3f.til.getEditText().getText().toString();
            center = new GeoPoint(c3f.lati, c3f.longi);

            // TODO: filter with minimum and maximum characters
            if(name.trim().length() <= 3){
                Toast.makeText(this, R.string.err_m3c_name, Toast.LENGTH_SHORT).show();
                c1f.name.setError(getString(R.string.err_min_3_chars));
                return;
            }else if(desc.trim().length() <= 30){
                Toast.makeText(this, R.string.err_m30c_desc, Toast.LENGTH_SHORT).show();
                c3f.til.setError(getString(R.string.err_min_30_chars));
                return;
            }else if(type == -1){
                Toast.makeText(this, R.string.err_event_choose_type, Toast.LENGTH_SHORT).show();
                return;
            }else if(place.trim().length() <= 3){
                Toast.makeText(this, R.string.err_m3c_refloc, Toast.LENGTH_SHORT).show();
                c2f.placeEv.setError(getString(R.string.err_min_3_chars));
                return;
            }else if(center.getLatitude() == 0.0 && center.getLongitude() == 0.0){
                Toast.makeText(this, R.string.err_center_long, Toast.LENGTH_SHORT).show();
                return;
            }else if(end < System.currentTimeMillis()){
                Toast.makeText(this, R.string.err_futuredate_long, Toast.LENGTH_SHORT).show();
                c2f.endDateTimeEv.setError(getString(R.string.err_past_date));
                return;
            }else{
                String[] types = {getString(R.string.type_protest), getString(R.string.type_rubcollect),
                        getString(R.string.type_meeting_talk), getString(R.string.type_workshop),
                        getString(R.string.type_volunteering)};
                String msg = String.format(getString(R.string.revise_new_event), name, desc, types[type],
                        place, url, sdf.format(start), sdf.format(end), center.getLatitude(), center.getLongitude());
                if(start < System.currentTimeMillis()){
                    AlertDialog.Builder alertDate = new AlertDialog.Builder(this);
                    alertDate.setMessage(getString(R.string.event_already_started));
                    alertDate.setCancelable(true);
                    alertDate.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            subirEvento(msg);
                        }
                    });
                    alertDate.setNegativeButton(R.string.dialog_no, (dialog, which) -> {});
                    alertDate.create().show();
                }else{
                    subirEvento(msg);
                }
            }
        }
    };

    private void subirEvento(String mensaje) {
        AlertDialog.Builder alertDate = new AlertDialog.Builder(this);
        alertDate.setMessage(mensaje);
        alertDate.setCancelable(true);
        alertDate.setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("nombre", name);
            map.put("descripcion", desc);
            map.put("tipo", type);
            map.put("nlugar", place);
            map.put("url", url);
            map.put("inicio", start);
            map.put("fin", end);
            map.put("urgent", urgent);
            map.put("periodico", false);
            map.put("centro", center);
            map.put("radio", range);

            FirebaseFirestore.getInstance().collection("eventos").add(map).addOnSuccessListener(documentReference -> {
                Toast.makeText(CrearActivity.this, R.string.upl_success, Toast.LENGTH_SHORT).show();
                finish();
            });
        });
        alertDate.setNegativeButton(R.string.dialog_no, (dialog, which) -> {});
        alertDate.create().show();
    }

    private FusedLocationProviderClient fusedLocationClient;
    private Location location;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_activity);
        fm.beginTransaction().add(R.id.container, f3, "3").hide(f3).commit();
        fm.beginTransaction().add(R.id.container, f2, "2").hide(f2).commit();
        fm.beginTransaction().add(R.id.container, f1, "1").commit();

        btnReturn = findViewById(R.id.btn_new_ev_back);
        btnNext = findViewById(R.id.btn_new_ev_next);

        btnReturn.setOnClickListener(view -> back(fm));
        btnNext.setOnClickListener(nextDefault);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ContextCompat.checkSelfPermission(CrearActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CrearActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
        } else {
            locationUpdates();
        }
    }

    private void back(FragmentManager fm){
        if(active == f3){
            fm.beginTransaction().hide(active).show(f2).commit();
            btnNext.setText(R.string.next);
            active = f2;
        }else if(active == f2){
            fm.beginTransaction().hide(active).show(f1).commit();
            active = f1;
        }else if(active == f1){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        back(getSupportFragmentManager());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == 1002){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(CrearActivity.this, R.string.loc_perm_granted, Toast.LENGTH_SHORT).show();
                locationUpdates();
            }else{
                Toast.makeText(CrearActivity.this, R.string.loc_perm_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void locationUpdates(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || locationResult.getLastLocation() == null) {
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(loc -> {
                                location = loc;
                            });
                }else{
                    location = locationResult.getLastLocation();
                }
            }
        };
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(30000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void setD1(String name, int type, boolean urgent){
        this.name = name;
        this.type = type;
        this.urgent = urgent;
    }
    private void setD2(String place, String url, long start, long end){
        this.place = place;
        this.url = url;
        this.start = start;
        this.end = end;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
