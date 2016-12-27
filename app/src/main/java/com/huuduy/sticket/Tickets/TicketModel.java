package com.huuduy.sticket.Tickets;

/**
 * Created by huudu on 22/12/2016.
 */

public class TicketModel {
    private String idTicket;
    private int idUser;
    private String idEvent;
    private String device;
    private String title;
    private String information;
    private String location;
    private String time;

    public TicketModel() {
    }

    public TicketModel(String idTicket, int idUser, String idEvent, String device, String title, String information, String location, String time) {
        this.idTicket = idTicket;
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.device = device;
        this.title = title;
        this.information = information;
        this.location = location;
        this.time = time;
    }


    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
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
