package com.pranitkulkarni.remindbylocation.database;

/**
 * Created by pranitkulkarni on 5/19/17.
 */

public class ScheduleModel {

    private int id = 0;
    private String label = "",place_name="";
    private Double longitude,latitude;
    private Boolean isCompleted = false;

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }


    public int getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }


    public String getLabel() {
        return label;
    }


    public String getPlace_name() {
        return place_name;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }
}
