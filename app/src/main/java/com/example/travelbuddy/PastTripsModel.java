package com.example.travelbuddy;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class PastTripsModel implements Serializable {
    private String src, dest;
    private String review;
    private String stdate, endate;
    private String expenditure;
    private String uid;
    private ArrayList<String> piclist;

    public ArrayList<String> getPiclist() {
        return piclist;
    }

    public void setPiclist(ArrayList<String> piclist) {
        this.piclist =new ArrayList<>(piclist);
    }

    public PastTripsModel() {

    }

    public PastTripsModel(String src, String dest, String review, String stdate, String endate, String expenditure) {
        this.src = src;
        this.dest = dest;
        this.review = review;
        this.stdate = stdate;
        this.endate = endate;
        this.expenditure = expenditure;
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public String getReview() {
        return review;
    }

    public String getStdate() {
        return stdate;
    }

    public String getEndate() {
        return endate;
    }

    public String getExpenditure() {
        return expenditure;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
