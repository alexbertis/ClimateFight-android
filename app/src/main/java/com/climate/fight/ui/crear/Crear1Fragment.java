package com.climate.fight.ui.crear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public TextInputLayout name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crear1_fragment, container, false);
        rg = v.findViewById(R.id.typeGroupNewEv);
        name = v.findViewById(R.id.nameNewEv);
        return v;
    }

}
