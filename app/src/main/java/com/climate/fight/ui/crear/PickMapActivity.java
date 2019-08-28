package com.climate.fight.ui.crear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.climate.fight.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class PickMapActivity extends AppCompatActivity {

    private double lat = 0.0, lng = 0.0;
    private Marker prmk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_map);
        MapView map = findViewById(R.id.map_pick);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);


        IMapController mapController = map.getController();
        double lati = getIntent().getDoubleExtra("lat", 0.0);
        double lngi = getIntent().getDoubleExtra("lng", 0.0);
        mapController.setCenter(new GeoPoint(lati,lngi));
        mapController.setZoom(15);
        findViewById(R.id.map_pick_ok).setOnClickListener(view -> {
            Intent i = new Intent().putExtra("lat", lat).putExtra("lon", lng);
            setResult(Activity.RESULT_OK, i);
            finish();
        });
        final MapEventsReceiver mReceive = new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                lat = p.getLatitude();
                lng = p.getLongitude();
                if(prmk != null) map.getOverlays().remove(prmk);
                Marker evMarker = new Marker(map);
                evMarker.setPosition(p);
                evMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                evMarker.setIcon(ContextCompat.getDrawable(PickMapActivity.this, R.drawable.placeholder));
                map.getOverlays().add(evMarker);
                prmk = evMarker;
                Toast.makeText(getBaseContext(),lat + " - " + lng, Toast.LENGTH_LONG).show();
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));
    }
}
