package com.example.alex.cafeit;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;

/**
 * Created by alex on 4/15/17.
 */

public class Cafe {
    protected String name;
    protected String startHour;
    protected String endHour;
    protected String bestMenu;
    protected float rating;
    protected String address;
    protected int hasWifi;
    protected float waitTime;
    public float latitude;
    public float longitude;

//    protected Date registerDate;
//    protected URL imURL;

    public Cafe(){

    }

//    protected Queue orderQueue;
    //protected float distance;
//LEGACY CONSTRUCTOR - COMMENT BY ANTHONY

//    public Cafe (int id, String name, String location, String startHour, String endHour, String bestMenu, float rating, int hasWifi, int waitTime) {
//        this.id = id;
//        this.name = name;
//        this.location = location;
//        this.startHour = startHour;
//        this.endHour = endHour;
//        this.bestMenu = bestMenu;
//        this.rating = rating;
//        this.hasWifi = hasWifi;
//        this.waitTime = waitTime;
////        registerDate = new Date();
//    }

    public Cafe(String cafe_name, String cafe_startHour, String cafe_endHour, String cafe_bestMenu,
                float cafe_rating, String cafe_address, int cafe_hasWifi, float cafe_waitTime,
                float cafe_latitude, float cafe_longitude) {
        name = cafe_name;
        startHour = cafe_startHour;
        endHour = cafe_endHour;
        bestMenu = cafe_bestMenu;
        rating = cafe_rating;
        address = cafe_address;
        hasWifi = cafe_hasWifi;
        waitTime = cafe_waitTime;
        latitude = cafe_latitude;
        longitude = cafe_longitude;
    }

    @Override
    public String toString() {
        return this.name;
    }
}