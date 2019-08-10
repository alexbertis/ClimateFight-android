package com.climate.fight;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.climate.fight.R;
import com.climate.fight.recycler.ItemHome;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MoreInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView map = null;
    private ItemHome evento;
    private boolean fav = false;

    private TextView tvTitulo, tvTipo, tvUbic, tvFecha, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        evento = new Gson().fromJson(getIntent().getStringExtra("objeto"), ItemHome.class);

        tvTitulo = findViewById(R.id.infoev_tit);
        tvUbic = findViewById(R.id.infoev_ubi);
        tvFecha = findViewById(R.id.infoev_fecha);
        tvDesc = findViewById(R.id.infoev_des);
        // TODO: cambiar programáticamente TextView del tipo de evento
        final FloatingActionButton fab = findViewById(R.id.fab);

        tvTitulo.setText(evento.getNombre());
        tvUbic.setText(evento.getRefUbi());
        tvDesc.setText(evento.getDesc());
        tvFecha.setText(new HelperTiempos(MoreInfoActivity.this).tiempoRestante(evento.getMillisStart()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fav = !fav;
                if(fav){
                    fab.setImageDrawable(ContextCompat.getDrawable(MoreInfoActivity.this, R.drawable.ic_favorite_black));
                }else {
                    fab.setImageDrawable(ContextCompat.getDrawable(MoreInfoActivity.this, R.drawable.ic_favorite_border_black));
                }
            }
        });

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(false);

        IMapController mapController = map.getController();
        mapController.setZoom(18);
        GeoPoint centro = new GeoPoint(evento.getUbicacion().getLatitude(), evento.getUbicacion().getLongitude());
        mapController.setCenter(centro);
        Marker evMarker = new Marker(map);
        evMarker.setPosition(centro);
        evMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        evMarker.setIcon(ContextCompat.getDrawable(MoreInfoActivity.this, R.drawable.pin_place));
        map.getOverlays().add(evMarker);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.infoev_calendar:
                intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Events.TITLE, evento.getNombre());
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, evento.getMillisStart());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, evento.getMillisEnd());
                intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
                intent.putExtra(CalendarContract.Events.DESCRIPTION, evento.getDesc());
                startActivity(intent);
                break;
            case R.id.infoev_link:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(evento.getUrlInfo()));
                startActivity(intent);
                break;
        }
    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }
    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}
