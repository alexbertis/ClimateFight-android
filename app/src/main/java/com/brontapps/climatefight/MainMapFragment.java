package com.brontapps.climatefight;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
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
        GeoPoint startPoint = new GeoPoint(42.1401, -0.408897);
        mapController.setCenter(startPoint);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainSharedViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.itemList.observe(this, new Observer<List<ItemHome>>() {
            @Override
            public void onChanged(List<ItemHome> itemHomes) {
                for(ItemHome itemHome : itemHomes){
                    Marker startMarker = new Marker(map);
                    startMarker.setPosition(new GeoPoint(itemHome.getUbicacion().getLatitude(), itemHome.getUbicacion().getLongitude()));
                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    startMarker.setTitle(itemHome.getNombre());
                    map.getOverlays().add(startMarker);
                }

            }
        });
    }

}
