package com.climate.fight.recycler;

import com.google.firebase.firestore.GeoPoint;

public class ItemHome {

    public static final int TIPO_BATIDA = 0;
    public static final int TIPO_MANIF = 1;
    public static final int TIPO_REUNION = 2;
    public static final int TIPO_TALLER = 3;
    public static final int TIPO_VOLUNTARIADO = 4;

    private String name, desc;
    private int type;
    private String id;
    private long millisStart, millisEnd;
    private boolean periodic, urgent;
    private GeoPoint location;
    private int radius, distToUser;
    private String refLoc;
    private String urlInfo;

    //TODO: parte social del evento


    public ItemHome(String name, String desc, int type, String id, long millisStart, long millisEnd, boolean periodic, boolean urgent, GeoPoint location, int radius, String refLoc, String urlInfo, int distToUser){
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.id = id;
        this.millisStart = millisStart;
        this.millisEnd = millisEnd;
        this.periodic = periodic;
        this.urgent = urgent;
        this.location = location;
        this.radius = radius;
        this.refLoc = refLoc;
        this.urlInfo = urlInfo;
        this.distToUser = distToUser;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getType() {
        return type;
    }

    public GeoPoint getLocation() {
        return location;
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

    public String getRefLoc() {
        return refLoc;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public String getId() {
        return id;
    }

    public int getDistToUser() {
        return distToUser;
    }

    public void setDistToUser(int distToUser) {
        this.distToUser = distToUser;
    }
}
