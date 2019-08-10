package com.climate.fight.ui.crear;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.climate.fight.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class Crear2Fragment extends Fragment {


    public static Crear2Fragment newInstance() {
        return new Crear2Fragment();
    }


    public TextInputLayout placeEv, startDateTimeEv, endDateTimeEv, webEv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crear2_fragment, container, false);
        placeEv = v.findViewById(R.id.placeNewEv);
        startDateTimeEv = v.findViewById(R.id.startDateNewEv);
        endDateTimeEv = v.findViewById(R.id.endDateNewEv);
        webEv = v.findViewById(R.id.webNewEv);
        ImageButton pickerI = v.findViewById(R.id.pickerStartDate);
        ImageButton pickerF = v.findViewById(R.id.pickerEndDate);
        pickerI.setOnClickListener(v1 -> mostrarPicker(0));
        pickerF.setOnClickListener(v12 -> mostrarPicker(5));
        return v;
    }


    private String pickerTotal = "";
    private void mostrarPicker(final int i) {
        final String CERO = "0";
        final String BARRA = "/";

        //Calendario para obtener fecha & hora
        final Calendar c = Calendar.getInstance();
        final int mes = c.get(Calendar.MONTH);
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int anio = c.get(Calendar.YEAR);
        final int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minuto = c.get(Calendar.MINUTE);

        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            final int mesActual = month + 1;
            String diaFormateado = (dayOfMonth < 10)? CERO + dayOfMonth :String.valueOf(dayOfMonth);
            String mesFormateado = (mesActual < 10)? CERO + mesActual :String.valueOf(mesActual);
            pickerTotal = diaFormateado + BARRA + mesFormateado + BARRA + year;

            TimePickerDialog recogerHora = new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                String horaFormateada =  (hourOfDay < 10)? CERO + hourOfDay : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? CERO + minute :String.valueOf(minute);
                pickerTotal += (" " + horaFormateada + ":" + minutoFormateado);
                if(i == 0) {
                    startDateTimeEv.getEditText().setText(pickerTotal);
                }else {
                    endDateTimeEv.getEditText().setText(pickerTotal);
                }
            }, hora, minuto, true);
            recogerHora.show();
        },anio, mes, dia);
        recogerFecha.show();
    }
}
