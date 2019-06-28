package com.brontapps.climatefight;

import android.text.format.DateUtils;

public class HelperTiempos {

    public HelperTiempos(){
    }

    public String tiempoRestante(long millisEv){
        return DateUtils.getRelativeTimeSpanString(millisEv).toString();
    }
}
