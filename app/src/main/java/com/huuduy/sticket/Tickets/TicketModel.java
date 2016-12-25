package com.huuduy.sticket.Tickets;

/**
 * Created by huudu on 22/12/2016.
 */

public class TicketModel {
    private int idEvent;
    private String title;
    private String location;
    private String time;

    public TicketModel(int idEvent, String title, String location, String time) {
        this.idEvent = idEvent;
        this.title = title;
        this.location = location;
        this.time = time;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
