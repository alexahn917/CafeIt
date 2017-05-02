package com.example.alex.cafeit;


import android.support.annotation.NonNull;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order extends OrderItem implements Comparable {

    public String cafeID; // equivalent to Cafe.ID string
    public String orderTime;
    public String orderDate;
    public String purchasedDate;
    public String purchasedTime;
    public int remainingTime;
    public String cafeName;
    public String note;
    public String customerName;
    public URL image_url;
    public boolean is_favorite;

    public Order() {
    }

    public Order(String orderTime, float price, int remainingTime, String cafeName, String itemName,
                 String size) {
        this.orderTime = orderTime;
        super.itemName = itemName;
        super.price = price;
        super.size = size;
        super.remainingTime = remainingTime;
        this.purchasedDate = orderTime;
        this.cafeName = cafeName;
        this.is_favorite = false;
    }

    public Order(String itemName, String size, int quantity, String orderTime, int remainingTime,
                 String cafeName, float price, String note, String customerName, URL image_url,
                 boolean is_favorite) {
        super.itemName = itemName;
        super.size = size;
        super.quantity = quantity;
        this.orderTime = orderTime;
        this.purchasedDate = orderTime;
        this.remainingTime = remainingTime;
        this.cafeName = cafeName;
        super.price = price;
        this.note = note;
        this.customerName = customerName;
        this.image_url = image_url;
        this.is_favorite = is_favorite;
    }

    public Order(OrderItem item) {
        super.itemName = item.itemName;
        super.size = item.size;
        super.quantity = item.quantity;
        super.price = item.price;
        super.remainingTime = item.remainingTime;
        this.is_favorite = false;
    }

    @Override
    public int compareTo(@NonNull Object other) {
        if (other instanceof Order) {
            try {
                Date thisDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(this.orderDate);
                Date otherDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(((Order) other).orderDate);
                return thisDate.compareTo(otherDate);
            }
            catch(Exception E) {
                System.out.println("Error on date conversion");
                return -1;
            }
        }
        else {
            return -1;
        }
    }


    public String getItemName() {
        return super.itemName;
    }
}
