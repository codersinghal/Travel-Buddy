package com.example.travelbuddy;

public class RecommendationData {
    private String name,title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RecommendationData(String name, String title) {
        this.name = name;
        this.title = title;
    }
}
