package com.example.alex.cafeit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Anthony Kim on 4/30/2017.
 */

public class AuthHandler {

    public static FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    public static FirebaseUser user = fbAuth.getCurrentUser();;
    public static String uid = user.getUid() + "";

    public AuthHandler(){
    }

    public static String getUid(){
        return uid;
    }

}
