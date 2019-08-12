package com.climate.fight.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.climate.fight.MainActivity;
import com.climate.fight.MoreInfoActivity;
import com.climate.fight.R;
import com.climate.fight.recycler.ItemHome;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MainMapFragment extends Fragment {

    private MapView map;
    private IMapController mapController;
    private List<ItemHome> itemList = new ArrayList<>();

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
        // TODO: Use the ViewModel
        GeoPoint startPoint = new GeoPoint(42.1401, -0.408897);
        new Thread(() -> {
            while (((MainActivity)getActivity()).getItemsHome() == null || ((MainActivity)getActivity()).getItemsHome().size() == 0){ }
            itemList = ((MainActivity)getActivity()).getItemsHome();
            getActivity().runOnUiThread(() -> {
                for (final ItemHome itemHome : itemList) {
                    Marker evMarker = new Marker(map);
                    evMarker.setPosition(new GeoPoint(itemHome.getLocation().getLatitude(), itemHome.getLocation().getLongitude()));
                    evMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    evMarker.setTitle(itemHome.getName());
                    evMarker.setOnMarkerClickListener((marker, mapView) -> {
                        Intent eventoIntent = new Intent(getContext(), MoreInfoActivity.class);
                        Gson gson = new Gson();
                        String json = gson.toJson(itemHome);
                        eventoIntent.putExtra("object", json);
                        startActivity(eventoIntent);
                        return false;
                    });
                    map.getOverlays().add(evMarker);
                }
            });
        }).start();
        mapController.setCenter(startPoint);
    }

}
