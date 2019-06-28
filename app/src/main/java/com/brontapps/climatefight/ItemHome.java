package com.brontapps.climatefight;

import com.google.firebase.firestore.GeoPoint;

public class ItemHome {

    public static final int TIPO_BATIDA = 0;
    public static final int TIPO_MANIF = 1;
    public static final int TIPO_REUNION = 2;

    private String nombre, desc;
    private int tipo;
    private long millisStart, millisEnd;
    private boolean periodic;
    private GeoPoint ubicacion;
    private String refUbi;
    private String urlInfo;

    //TODO: parte social del evento


    public ItemHome(String nombre, String desc, int tipo, long millisStart, long millisEnd, boolean periodic, GeoPoint ubicacion, String refUbi, String urlInfo){
        this.nombre = nombre;
        this.desc = desc;
        this.tipo = tipo;
        this.millisStart = millisStart;
        this.millisEnd = millisEnd;
        this.periodic = periodic;
        this.ubicacion = ubicacion;
        this.refUbi = refUbi;
        this.urlInfo = urlInfo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDesc() {
        return desc;
    }

    public int getTipo() {
        return tipo;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

    public long getMillisStart() {
        return millisStart;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public long getMillisEnd() {
        return millisEnd;
    }

    public String getRefUbi() {
        return refUbi;
    }

    public String getUrlInfo() {
        return urlInfo;
    }
}
