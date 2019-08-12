package com.climate.fight;

public class DistManager {
    public DistManager(){
        
    }
    
    public String adaptiveDistance(int meters){
        if(meters <= 4000){
            return meters + "m";
        }else{
            return meters/1000 + "km";
        }
    }
}
