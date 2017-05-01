package com.example.alex.cafeit;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by alex on 4/29/17.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public boolean isCafe;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, boolean iscafe) {
        this.username = username;
        this.email = email;
        this.isCafe = iscafe;
    }

    @Override
    public String toString() {
        return username + " is cafe? : " + isCafe;
    }
}