package com.example.alex.cafeit;

public class OrderItem {
    String itemName;
    String size;
    float price;
    int remainingTime;
    int quantity;

    public OrderItem() {

    }

    public OrderItem(String name, String size, float price, int remainingTime) {
        this.itemName = name;
        this.size = size;
        this.price = price;
        this.remainingTime = remainingTime;
    }
}
