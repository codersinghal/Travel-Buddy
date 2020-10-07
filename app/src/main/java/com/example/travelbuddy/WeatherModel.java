package com.example.travelbuddy;

public class WeatherModel {
    private String date;
    private String temp;
    private String desc;
    private String weather_icon;

    public String getDesc() {
        return desc;
    }

    public String getWeather_icon() {
        return weather_icon;
    }

    public void setWeather_icon(String weather_icon) {
        this.weather_icon = weather_icon;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public WeatherModel(String date, String temp,String desc,String weather_icon) {
        this.date = date;
        this.temp = temp;
        this.desc=desc;
        this.weather_icon=weather_icon;
    }
}
