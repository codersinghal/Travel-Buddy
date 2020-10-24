package com.example.travelbuddy.userprofile;

public class Profile {
    private String name;
    private String email,location;
    private String ppurl;
    private String bio;
    Profile()
    {
    }

    public Profile(String name, String email, String location,String ppurl,String bio) {
        this.name = name;
        this.email = email;
        this.location = location;
        this.ppurl=ppurl;
        this.bio=bio;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPpurl() {
        return ppurl;
    }

    public void setPpurl(String ppurl) {
        this.ppurl = ppurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
