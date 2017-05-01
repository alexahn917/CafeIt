package com.example.alex.cafeit;

import java.util.ArrayList;

/**
 * Created by Anthony Kim on 4/30/2017.
 */

public class CafeMenuItem {
    private String name;
    private int category;
    private boolean oneSize;
    private int takesTime;
    private float smallPrice, mediumPrice, largePrice;
    private int quantity;

    public CafeMenuItem(){

    }

    public CafeMenuItem(String name, int category, boolean isOneSize, int takesTime, float smallPrice, float mediumPrice, float largePrice, int quantity) {
        this.name = name;
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
