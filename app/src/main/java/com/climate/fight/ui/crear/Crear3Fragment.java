package com.climate.fight.ui.crear;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.climate.fight.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Crear3Fragment extends Fragment {


    public static Crear3Fragment newInstance() {
        return new Crear3Fragment();
    }

    MaterialButton b;
    TextInputLayout til;
    public double lati, longi;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crear3_fragment, container, false);
        b = v.findViewById(R.id.launchPickMapNewEv);
        til = v.findViewById(R.id.descNewEv);

        b.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), PickMapActivity.class);
            Location location = ((CrearActivity)getActivity()).getLocation();
            if(location != null){
                i.putExtra("lat", location.getLatitude());
                i.putExtra("lng", location.getLongitude());
            }else{
                String locale = getContext().getResources().getConfiguration().locale.getDisplayCountry();
                try {
                    Address address = new Geocoder(getContext()).getFromLocationName(locale, 1).get(0);
                    i.putExtra("lat", address.getLatitude());
                    i.putExtra("lng", address.getLongitude());
                } catch (IOException e) {
                    e.printStackTrace();
                    i.putExtra("lat", 0);
                    i.putExtra("lng", 0);
                }

            }
            startActivityForResult(i, 117);
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 117){
            if(resultCode == Activity.RESULT_OK){
                b.setText(R.string.change_center_map_new_event);
                lati = (double) Math.round(data.getDoubleExtra("lat", 0.0)* 1000000d) / 1000000d;
                longi = (double)Math.round(data.getDoubleExtra("lon", 0.0)* 1000000d) / 1000000d;
            }
        }
    }
}
