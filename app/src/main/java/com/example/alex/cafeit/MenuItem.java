package com.example.alex.cafeit;

/**
 * Created by Chris Micek on 4/16/2017.
 */

public class MenuItem {
    boolean selected;
    String name;
    int quantity;
    int completionTime;
    String[] choices;
    int choice_pos;

    public MenuItem (boolean selected, String name, int completionTime, int quantity, String[] choices) {
        this.selected = selected;
        this.name = name;
        this.completionTime = completionTime;
        this.quantity = quantity;
        this.choices = choices;
        this.choice_pos = 0;
    }
}
