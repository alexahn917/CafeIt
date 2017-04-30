package com.example.alex.cafeit;

import java.util.ArrayList;

/**
 * Created by Anthony Kim on 4/30/2017.
 */

public class CafeMenuItem {
    String name;
    int category;
    boolean isOneSize;
    int takesTime;
    float smallPrice, mediumPrice, largePrice;
    int quantity;

    public CafeMenuItem(String name, int category, boolean isOneSize, int takesTime, float smallPrice, float mediumPrice, float largePrice, int quantity) {
        this.name = name;
        this.category = category;
        this.isOneSize = isOneSize;
        this.takesTime = takesTime;
        this.smallPrice = smallPrice;
        this.mediumPrice = mediumPrice;
        this.largePrice = largePrice;
        this.quantity = quantity;
    }
}
