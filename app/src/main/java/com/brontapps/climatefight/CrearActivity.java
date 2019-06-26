package com.brontapps.climatefight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.brontapps.climatefight.ui.crear.Crear1Fragment;

public class CrearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Crear1Fragment.newInstance())
                    .commitNow();
        }
    }
}
