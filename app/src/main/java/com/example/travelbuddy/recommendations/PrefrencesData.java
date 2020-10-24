package com.example.travelbuddy.recommendations;

public class PrefrencesData {
    public PrefrencesData() {
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public PrefrencesData(String type, boolean selected) {
        this.type = type;
        this.selected = selected;
    }

    private boolean selected;

}
