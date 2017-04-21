package com.example.alex.cafeit;



public class Order {

    public String orderTime;
    public float price;
    public int remainingTime;
    public String cafeName;
    public String orderMenu;
    public String note;
    public String name;

    public Order() {
    }

    public Order(String orderTime, float price, int remainingTime, String cafeName, String orderMenu) {
        this.orderMenu = orderMenu;
        this.orderTime = orderTime;
        this.price = price;
        this.remainingTime = remainingTime;
        this.cafeName = cafeName;
    }

    public String getOrderMenu() {
        return this.orderMenu;
    }
}
