package com.example.alex.cafeit;

/**
 * Created by Chris Micek on 4/16/2017.
 */

public class MenuItem {
    boolean selected;
    String name;
    int quantity;
    String[] choices;

    public MenuItem (boolean selected, String name, int quantity, String[] choices) {
        this.selected = selected;
        this.name = name;
        this.quantity = quantity;
        this.choices = choices;
    }
}
