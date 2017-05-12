package com.example.alex.cafeit;

import android.support.annotation.NonNull;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;

/**
 * Created by alex on 4/15/17.
 */

public class Cafe implements Comparable {
    public String ID;
    public String name;
    public String startHour;
    public String endHour;
    public String bestMenu;
    public float rating = 0f;
    public String address;
    public int hasWifi;
    public float waitTime = 0f;
    public float latitude;
    public float longitude;
    public String distance = "0.5 mi";

//    protected Date registerDate;
//    protected URL imURL;

    public Cafe(){

    }

    @Override
    public int compareTo(@NonNull Object other) {
        if (other instanceof Cafe) {
            float ret = this.waitTime - ((Cafe) other).waitTime;
            if (ret > 0) {
                return 1;
            } else {
                return -1;
            }
        }
        else {
            return -1;
        }
    }


    public Cafe(String cafe_id, String cafe_name, String cafe_startHour, String cafe_endHour, String cafe_bestMenu,
                float cafe_rating, String cafe_address, int cafe_hasWifi, float cafe_waitTime,
                float cafe_latitude, float cafe_longitude) {
        ID = cafe_id;
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