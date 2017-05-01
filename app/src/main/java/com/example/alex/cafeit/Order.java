package com.example.alex.cafeit;


import java.net.URL;

public class Order extends OrderItem {

    public String orderTime;
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
        super.itemName = itemName;
        super.price = price;
        super.size = size;
        super.remainingTime = remainingTime;
        this.orderTime = orderTime;
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

    public String getItemName() {
        return super.itemName;
    }
}
