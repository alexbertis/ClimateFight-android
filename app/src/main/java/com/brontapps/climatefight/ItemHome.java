package com.brontapps.climatefight;

import com.google.firebase.firestore.GeoPoint;

public class ItemHome {

    public static final int TIPO_BATIDA = 0;

    private String nombre, desc;
    private long tipo;
    private GeoPoint ubicacion;
    public ItemHome(String nombre, String desc, long tipo, GeoPoint ubicacion){
        this.nombre = nombre;
        this.desc = desc;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDesc() {
        return desc;
    }

    public long getTipo() {
        return tipo;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

}
