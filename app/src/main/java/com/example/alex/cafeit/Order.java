package com.example.alex.cafeit;



public class Order extends OrderItem {

    public String orderTime;
    public int remainingTime;
    public String cafeName;
    public String note;
    public String customerName;

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
    }

    public String getItemName() {
        return super.itemName;
    }
}
