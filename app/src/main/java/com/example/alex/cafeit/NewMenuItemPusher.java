package com.example.alex.cafeit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anthony Kim on 4/30/2017.
 */

public class NewMenuItemPusher {
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public NewMenuItemPusher(){

    }

    public static void pushItem(CafeMenuItem item){
        mDatabase.child("cafeMenu").child(AuthHandler.getUid()).child(item.getName()).setValue(item);
    }
}
