package com.example.alex.cafeit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CurrentOrder extends AppCompatActivity {

    private TextView order_item_view;
    private TextView order_cafe_view;
    private TextView order_price_view;
    private Button order_time_view;

    // preferences
    private Context context;
    private SharedPreferences myPref;
    private SharedPreferences.Editor peditor;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Cafe ordered_cafe;
    private String CafeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        // Preference set up
        context = getApplicationContext();
        myPref = PreferenceManager.getDefaultSharedPreferences(context);
        peditor = myPref.edit();

        order_item_view = (TextView) findViewById(R.id.current_order_item);
        order_cafe_view = (TextView) findViewById(R.id.current_order_cafe);
        order_price_view= (TextView) findViewById(R.id.current_order_price);
        order_time_view= (Button) findViewById(R.id.current_order_time);
        order_time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CurrentOrder.this, R.style.CafeItDialogue)
                        .setMessage("Received your order?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(context, "Enjoy!", Toast.LENGTH_LONG).show();
                                MainActivity.SetResetOrder();
                                ask_for_rating();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        if (MainActivity.OrderInProgress) {
            order_time_view.setEnabled(true);
        } else {
            order_time_view.setEnabled(false);
        }

        update();

        CafeId = myPref.getString("CafeId", "");
        mDatabase.child("cafes").child(CafeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ordered_cafe = dataSnapshot.getValue(Cafe.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                update();
                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };
        t.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    public void ask_for_rating() {
        new AlertDialog.Builder(CurrentOrder.this, R.style.CafeItDialogue)
                .setMessage("How was the service?")
                .setPositiveButton("Good", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Thank you.", Toast.LENGTH_LONG).show();
                        update_rating(true);
                        finish();
                    }
                })
                .setNegativeButton("Bad", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Thank you.", Toast.LENGTH_LONG).show();
                        update_rating(false);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void update() {
        if (MainActivity.OrderInProgress) {
            order_item_view.setText(myPref.getString("OrderItem", ""));
            order_cafe_view.setText(myPref.getString("OrderCafe", ""));
            order_price_view.setText(myPref.getString("OrderPrice", ""));
            long elapsed_time_sec = 0;
            long remain_time_sec = 0;

            Date orderDate = new Date();
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String purchasedTime = myPref.getString("OrderPurchasedTime", "");
            String currentTime= timeFormat.format(orderDate);
            try {
                Date current = timeFormat.parse(purchasedTime);
                Date purchased = timeFormat.parse(currentTime);
                elapsed_time_sec = getDateDiff(purchased, current);
                remain_time_sec = myPref.getLong("OrderWaitTime", 0) - elapsed_time_sec;
            }
            catch (Exception E) {

            }
            Log.d("!@!@!@!@!@!currentTime", currentTime + "");
            Log.d("!@!@!@!@purchasedTime", purchasedTime + "");
            Log.d("!@!!@!elapsed_time_sec", elapsed_time_sec + "");
            Log.d("!@@!@!@!remain_time_sec", remain_time_sec + "");
            long mins = 0;
            long secs = 0;
            if (remain_time_sec > 0) {
                mins = remain_time_sec / 60;
                secs = remain_time_sec % 60;
                order_time_view.setText(mins + ":" + String.format("%02d", secs) + " remaining!");
            } else {
                order_time_view.setText("Your order is ready!");
            }
        } else {
            order_item_view.setText("");
            order_cafe_view.setText("");
            order_price_view.setText("");
            String line = "Make your purchase.";
            order_time_view.setText(line);
        }
    }

    public void update_rating(Boolean good) {
        if (good) {
            ordered_cafe.rating += 1.0;
            mDatabase.child("cafes").child(CafeId).setValue(ordered_cafe);
        } else {
            ordered_cafe.rating -= 1.0;
            mDatabase.child("cafes").child(CafeId).setValue(ordered_cafe);
        }
    }

    public static long getDateDiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long diffSeconds = diff / 1000;
        return diffSeconds;
    }
}