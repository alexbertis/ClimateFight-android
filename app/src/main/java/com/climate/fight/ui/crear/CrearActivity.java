package com.climate.fight.ui.crear;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.climate.fight.R;
import com.climate.fight.recycler.ItemHome;
import com.climate.fight.views.ButtonCompatible;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CrearActivity extends AppCompatActivity {

    Fragment f1 = Crear1Fragment.newInstance(), f2 = Crear2Fragment.newInstance(),
            f3 = Crear3Fragment.newInstance(), active = f1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");


    String name, desc, place, url;
    GeoPoint center;
    long start, end;
    public int type;
    int range;
    boolean urgent;
    ButtonCompatible btnNext, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_activity);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.container, f3, "3").hide(f3).commit();
        fm.beginTransaction().add(R.id.container, f2, "2").hide(f2).commit();
        fm.beginTransaction().add(R.id.container, f1, "1").commit();

        btnReturn = findViewById(R.id.btn_new_ev_back);
        btnNext = findViewById(R.id.btn_new_ev_next);

        btnReturn.setOnClickListener(view -> {
            back(fm);
        });
        btnNext.setOnClickListener(view -> {
            if(active == f1){
                fm.beginTransaction().hide(active).show(f2).commit();
                Crear1Fragment c1f = (Crear1Fragment)f1;
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
                Crear2Fragment c2f = (Crear2Fragment)f2;
                try {
                    setD2(c2f.placeEv.getEditText().getText().toString(),
                            c2f.webEv.getEditText().getText().toString(),
                            sdf.parse(c2f.startDateTimeEv.getEditText().getText().toString()).getTime(),
                            sdf.parse(c2f.endDateTimeEv.getEditText().getText().toString()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                active = f3;
                btnNext.setText(R.string.upload);
                btnNext.setOnClickListener(view1 -> {
                    // TODO: filter with minimum and maximum characters
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
                    map.put("radio", 50);

                    FirebaseFirestore.getInstance().collection("eventos").add(map).addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, R.string.upl_success, Toast.LENGTH_SHORT).show();
                    });
                });
            }else if(active == f3){
                Crear3Fragment c3f = (Crear3Fragment)f3;
                desc = c3f.til.getEditText().getText().toString();
                center = new GeoPoint(c3f.lati, c3f.longi);
            }
        });

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

    public void setD1(String name, int type, boolean urgent){
        this.name = name;
        this.type = type;
        this.urgent = urgent;
    }
    public void setD2(String place, String url, long start, long end){
        this.place = place;
        this.url = url;
        this.start = start;
        this.end = end;
    }

}
