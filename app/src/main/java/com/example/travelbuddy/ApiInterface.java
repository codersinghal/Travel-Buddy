package com.example.travelbuddy;

import com.example.travelbuddy.places_model.PlacesBase;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDYoQybddM6c-Daz0bHVe7h2tuyzxHmW1k")
    Call<PlacesBase> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}
