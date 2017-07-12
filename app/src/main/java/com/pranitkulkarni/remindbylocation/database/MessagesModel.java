package com.pranitkulkarni.remindbylocation.database;

/**
 * Created by pranitkulkarni on 6/4/17.
 */

public class MessagesModel {

    private int id=0, schedule_id=0;
    private String message="",contact_name="",sent_at="",contact_number="";

    public void setId(int id) {
        this.id = id;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public void setSent_at(String sent_at) {
        this.sent_at = sent_at;
    }

    /////


    public int getId() {
        return id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getMessage() {
        return message;
    }

    public String getSent_at() {
        return sent_at;
    }
}
