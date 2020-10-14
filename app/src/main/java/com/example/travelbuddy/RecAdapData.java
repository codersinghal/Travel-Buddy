package com.example.travelbuddy;

public class RecAdapData {
    private String title;
    private String extract;
    private String type;

    public RecAdapData(String title, String extract,String type) {
        this.title = title;
        this.extract = extract;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }
}
