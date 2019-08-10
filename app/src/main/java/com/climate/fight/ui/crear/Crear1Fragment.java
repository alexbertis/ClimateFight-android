package com.climate.fight.ui.crear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.climate.fight.R;
import com.google.android.material.textfield.TextInputLayout;

public class Crear1Fragment extends Fragment {


    public static Crear1Fragment newInstance() {
        return new Crear1Fragment();
    }

    public RadioGroup rg;
    public CheckBox urg;
    public TextInputLayout name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crear1_fragment, container, false);
        rg = v.findViewById(R.id.typeGroupNewEv);
        urg = v.findViewById(R.id.checkUrgentNewEv);
        rg.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i == R.id.typeVoluntNewEv){
                urg.setVisibility(View.VISIBLE);
            }else{
                urg.setChecked(false);
                urg.setVisibility(View.GONE);
            }
        });
        name = v.findViewById(R.id.nameNewEv);
        return v;
    }

}
