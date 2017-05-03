package com.example.alex.cafeit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentOrder extends AppCompatActivity {

    private TextView order_item_view;
    private TextView order_cafe_view;
    private TextView order_price_view;
    private Button order_time_view;

    // preferences
    private Context context;
    private SharedPreferences myPref;
    private SharedPreferences.Editor peditor;

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
                new AlertDialog.Builder(CurrentOrder.this, R.style.historyToFavoriteDialog)
                        .setMessage("Received your order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Enjoy!", Toast.LENGTH_LONG).show();
                                MainActivity.SetResetOrder();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        update();
    }

    public void update() {
        if (MainActivity.OrderInProgress) {
            order_item_view.setText(myPref.getString("OrderItem", ""));
            order_cafe_view.setText(myPref.getString("OrderCafe", ""));
            order_price_view.setText(myPref.getString("OrderPrice", ""));
            String line = myPref.getString("OrderWaitTime", "");
            order_time_view.setText(line);
        } else {
            order_item_view.setText("");
            order_cafe_view.setText("");
            order_price_view.setText("");
            String line = "Make your purchase.";
            order_time_view.setText(line);
        }
    }

    public void ask_for_rating() {

    }
}