package com.huuduy.sticket.Events;

/**
 * Created by huudu on 06/12/2016.
 */

public class EventModel {
    private int idEvent;
    private String information;
    private String image;
    private int price;
    private String location;
    private String type;
    private int numberTicket;
    private String tags[];

    public EventModel(int idEvent, String information, String image, int price, String location, String type, int numberTicket, String[] tags) {
        this.idEvent = idEvent;
        this.information = information;
        this.image = image;
        this.price = price;
        this.location = location;
        this.type = type;
        this.numberTicket = numberTicket;
        this.tags = tags;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumberTicket() {
        return numberTicket;
    }

    public void setNumberTicket(int numberTicket) {
        this.numberTicket = numberTicket;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
