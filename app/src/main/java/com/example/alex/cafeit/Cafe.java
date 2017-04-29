package com.example.alex.cafeit;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 4/15/17.
 */

public class Cafe {
    protected int id;
    protected String name;
    protected String location;
    protected String startHour;
    protected String endHour;
    protected float latitude;
    protected float longitude;

    protected String bestMenu;
    protected float rating;
    protected int hasWifi;
    protected int waitTime;
    protected Date registerDate;

    public Cafe() {
    }

    public Cafe (int id, String name, String location, String startHour, String endHour, String bestMenu, float rating, int hasWifi, int waitTime, float latitude, float longitude) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startHour = startHour;
        this.endHour = endHour;
        this.bestMenu = bestMenu;
        this.rating = rating;
        this.hasWifi = hasWifi;
        this.waitTime = waitTime;
        registerDate = new Date();
    }

    @Override
    public String toString() {
        return this.name;
    }
}