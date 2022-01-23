package vn.noname.vaccineassistant;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceModel {
    String name;
    int require;
    LatLng LatLong;

    public PlaceModel(){
    }
    public PlaceModel(String name, int require, LatLng LatLong){
        this.LatLong = LatLong;
        this.name = name;
        this.require = require;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequire() {
        return require;
    }

    public void setRequire(int require) {
        this.require = require;
    }

    public LatLng getLatLong() {
        return LatLong;
    }

    public void setLatLong(LatLng latLong) {
        LatLong = latLong;
    }

}

