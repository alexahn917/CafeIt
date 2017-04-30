package com.example.alex.cafeit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anthony Kim on 4/29/2017.
 * This is a tool for developers to add a certified cafe directly into the firebase db.
 */

public class NewCafePusher {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Cafe cafe;
    public static int cid = 1;

    public NewCafePusher() {
        //this.cafe = new Cafe(cid++, "Imaginary Cafe", "somewhere over the rainbow", "11AM", "11PM", "unicorn latte", 4.5f, 1, 7, "1000 somewhere street", 39.334773f, -76.620726f);
        //mDatabase.child("cafes").push().setValue(this.cafe);
    }

    public NewCafePusher(Cafe new_cafe) {
        mDatabase.child("cafes").push().setValue(new_cafe);
    }

    public Cafe getCafe() {
        return cafe;
    }

}
