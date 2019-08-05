package com.brontapps.climatefight.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brontapps.climatefight.ItemHome;
import com.brontapps.climatefight.MoreInfoActivity;
import com.brontapps.climatefight.R;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class MainMapFragment extends Fragment {

    private MainSharedViewModel mViewModel;
    private MapView map;
    private IMapController mapController;

    public static MainMapFragment newInstance() {
        return new MainMapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_map_fragment, container, false);
        map = v.findViewById(R.id.map_main);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);


        mapController = map.getController();
        mapController.setZoom(15);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainSharedViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.itemList.observe(this, itemHomes -> {
            for(final ItemHome itemHome : itemHomes){
                Marker evMarker = new Marker(map);
                evMarker.setPosition(new GeoPoint(itemHome.getUbicacion().getLatitude(), itemHome.getUbicacion().getLongitude()));
                evMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                evMarker.setTitle(itemHome.getNombre());
                evMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        Intent eventoIntent = new Intent(mViewModel.activity, MoreInfoActivity.class);
                        Gson gson = new Gson();
                        String json = gson.toJson(itemHome);
                        eventoIntent.putExtra("objeto", json);
                        startActivity(eventoIntent);
                        return false;
                    }
                });
                map.getOverlays().add(evMarker);
            }

        });
        mapController.setCenter(mViewModel.startPoint);
    }

}
