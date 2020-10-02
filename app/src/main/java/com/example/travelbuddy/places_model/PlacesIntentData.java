package com.example.travelbuddy.places_model;

import java.io.Serializable;

public class PlacesIntentData implements Serializable {
    private double latitude;
    private double longitude;
    private String place_type;

    public PlacesIntentData(double latitude, double longitude, String place_type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.place_type = place_type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlace_type() {
        return place_type;
    }

    public void setPlace_type(String place_type) {
        this.place_type = place_type;
    }
}
