package com.example.alex.cafeit;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 4/15/17.
 */

public class Cafe {
    private int ID;
    private String name;
    private String location;
    private String startHour;
    private String endHour;

    private String bestMenu;
    private float rating;
    private int hasWifi;
    private int waitTime;
    private Date registerDate;

    public Cafe (int ID, String name, String location, String startHour, String endHour, String bestMenu, float rating, int hasWifi, int waitTime) {
        this.ID = ID;
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

}