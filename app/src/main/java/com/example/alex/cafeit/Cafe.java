package com.example.alex.cafeit;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;

/**
 * Created by alex on 4/15/17.
 */

public class Cafe {
    protected int id;
    protected String name;
    protected String location;
    protected String startHour;
    protected String endHour;

    protected String bestMenu;
    protected float rating;
    protected int hasWifi;
    protected int waitTime;
//    protected Date registerDate;
//    protected URL imURL;

    protected String address;
    protected float latitude;
    protected float longitude;

    public Cafe(){

    }

//    protected Queue orderQueue;
    //protected float distance;
//LEGACY CONSTRUCTOR - COMMENT BY ANTHONY
    public Cafe (int id, String name, String location, String startHour, String endHour, String bestMenu, float rating, int hasWifi, int waitTime) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startHour = startHour;
        this.endHour = endHour;
        this.bestMenu = bestMenu;
        this.rating = rating;
        this.hasWifi = hasWifi;
        this.waitTime = waitTime;
//        registerDate = new Date();
    }

    public Cafe(int id, String name, String location, String startHour, String endHour, String bestMenu, float rating, int hasWifi, int waitTime, String address, float latitude, float longitude) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startHour = startHour;
        this.endHour = endHour;
        this.bestMenu = bestMenu;
        this.rating = rating;
        this.hasWifi = hasWifi;
        this.waitTime = waitTime;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return this.name;
    }
}