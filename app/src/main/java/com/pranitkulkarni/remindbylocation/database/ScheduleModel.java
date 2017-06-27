package com.pranitkulkarni.remindbylocation.database;

/**
 * Created by pranitkulkarni on 5/19/17.
 */

public class ScheduleModel {

    private int id = 0,action_id=0,action_type=0;
    private String label = "",place_name="",created_at="";
    private Double longitude,latitude;
    private Boolean isCompleted = false;
    private MessagesModel messagesModel;

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

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMessagesModel(MessagesModel messagesModel) {
        this.messagesModel = messagesModel;
    }

    ///

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

    public int getAction_id() {
        return action_id;
    }

    public int getAction_type() {
        return action_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public MessagesModel getMessagesModel() {
        return messagesModel;
    }
}
