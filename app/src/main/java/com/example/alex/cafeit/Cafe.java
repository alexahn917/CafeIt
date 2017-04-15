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
    private int hasWifi;

    private float rating;
    private Date registerDate;

    public Cafe (int ID, String name, String location, String startHour, String endHour, int hasWifi) {
        this.ID = ID;
        this.name = name;
        this.location = location;
        this.startHour = startHour;
        this.endHour = endHour;

        rating = 0.0f;
        this.hasWifi = hasWifi;

        registerDate = new Date();
    }

}