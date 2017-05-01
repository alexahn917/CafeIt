package com.example.alex.cafeit;

import java.text.DecimalFormat;

/**
 * Created by Chris Micek on 4/16/2017.
 */

public class MenuItem extends CafeMenuItem{
    boolean selected;
    int completionTime;
    String[] choices;
    int choice_pos;
    String selectedSize;

    //inherits from CafeMenuItem
//    int category;
//    boolean oneSize;
//    int takesTime;
//    float smallPrice, mediumPrice, largePrice;
//    String name;
//    int quantity;

    public MenuItem(){

    }

    public MenuItem (boolean selected, String name, int completionTime, int quantity, String[] choices) {
        this.selected = selected;
        this.name = name;
        this.completionTime = completionTime;
        this.quantity = quantity;
        this.choices = choices;
        this.choice_pos = 0;
    }

    public MenuItem (CafeMenuItem cmi){
        //inherited value init
        this.category = cmi.category;
        this.oneSize = cmi.oneSize;
        this.takesTime = cmi.getTakesTime();
        this.smallPrice = cmi.getSmallPrice();
        this.mediumPrice = cmi.getMediumPrice();
        this.largePrice = cmi.getLargePrice();
        this.name = cmi.getName();
        this.quantity = 1;

        //this class' fields
        this.selected = false;
        this.completionTime = cmi.getTakesTime();
        this.choice_pos = 0;

        if(this.oneSize){
            this.choices = new String[1];
            choices[0] = "$ " + roundTwoDecimals(smallPrice);
            selectedSize = "(One Size)";
        } else {
            this.choices = new String[3];
            choices[0] = "$ " + roundTwoDecimals(smallPrice) + " (S)";
            choices[1] = "$ " + roundTwoDecimals(mediumPrice) + " (M)";
            choices[2] = "$ " + roundTwoDecimals(largePrice) + " (L)";
            selectedSize = "(SML)";
        }
    }


    public boolean isSelected() {
        return selected;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public String[] getChoices() {
        return choices;
    }

    public int getChoice_pos() {
        return choice_pos;
    }

    String roundTwoDecimals(float f) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(f);
    }
}
