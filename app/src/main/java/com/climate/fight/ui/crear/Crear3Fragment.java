package com.climate.fight.ui.crear;

import android.app.Activity;
import android.content.Intent;
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
            startActivityForResult(new Intent(getContext(), PickMapActivity.class), 117);
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 117){
            if(resultCode == Activity.RESULT_OK){
                b.setText(R.string.change_center_map_new_event);
                lati = data.getDoubleExtra("lat", 0.0);
                longi = data.getDoubleExtra("lon", 0.0);
            }
        }
    }
}
