package com.example.travelbuddy.trips_model;

import java.io.Serializable;
import java.util.ArrayList;

public class UpcomingTripsModel implements Serializable {
    private String src,dest,budget;
    private ArrayList<String> things_to_carry,places_to_visit,events_list;
    private String stdate,endate;
    private String uid;

    public UpcomingTripsModel() {

    }

    public ArrayList<String> getEvents_list() {
        return events_list;
    }

    public void setEvents_list(ArrayList<String> events_list) {
        this.events_list = events_list;
    }

    public UpcomingTripsModel(String src, String dest, String budget, ArrayList<String> things_to_carry, ArrayList<String> places_to_visit, String stdate, String endate, String uid, ArrayList<String> events_list) {
        this.src = src;
        this.dest = dest;
        this.budget = budget;
        this.things_to_carry = things_to_carry;
        this.places_to_visit = places_to_visit;
        this.events_list=events_list;
        this.stdate = stdate;
        this.endate = endate;
        this.uid=uid;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public ArrayList<String> getThings_to_carry() {
        return things_to_carry;
    }

    public void setThings_to_carry(ArrayList<String> things_to_carry) {
        this.things_to_carry = things_to_carry;
    }

    public ArrayList<String> getPlaces_to_visit() {
        return places_to_visit;
    }

    public void setPlaces_to_visit(ArrayList<String> places_to_visit) {
        this.places_to_visit = places_to_visit;
    }

    public String getStdate() {
        return stdate;
    }

    public void setStdate(String stdate) {
        this.stdate = stdate;
    }

    public String getEndate() {
        return endate;
    }

    public void setEndate(String endate) {
        this.endate = endate;
    }
}
