package com.example.alex.cafeit;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Anthony Kim on 4/30/2017.
 */

public class CafeMenuItem {
    String name;
    int category;
    boolean oneSize;
    int takesTime;
    float smallPrice, mediumPrice, largePrice;
    int quantity;

    public CafeMenuItem(){

    }

    public CafeMenuItem(String name, int category, boolean isOneSize, int takesTime, float smallPrice, float mediumPrice, float largePrice, int quantity) {
        this.name = Uri.encode(name).replace("\\.", "%2E");
        this.category = category;
        this.oneSize = isOneSize;
        this.takesTime = takesTime;
        this.smallPrice = smallPrice;
        this.mediumPrice = mediumPrice;
        this.largePrice = largePrice;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getNameDecoded() {
        return Uri.decode(name.replace("%2E", "\\."));
    }

    public int getCategory() {
        return category;
    }

    public boolean isOneSize() {
        return oneSize;
    }

    public int getTakesTime() {
        return takesTime;
    }

    public float getSmallPrice() {
        return smallPrice;
    }

    public float getMediumPrice() {
        return mediumPrice;
    }

    public float getLargePrice() {
        return largePrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
