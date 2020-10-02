package com.example.travelbuddy.places_model;

public class PlacesFinalContent {
    private Double lat,lon;
    private Double rating;
    private String name;
    private String vicinity;
    private String photoUrl;
    boolean open;

    public PlacesFinalContent(Double lat, Double lon, Double rating, String name, String vicinity, boolean open,String photoUrl) {
        this.lat = lat;
        this.lon = lon;
        this.rating = rating;
        this.name = name;
        this.vicinity = vicinity;
        this.open = open;
        this.photoUrl=photoUrl;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getRating() {
        return rating;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
