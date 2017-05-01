package com.example.alex.cafeit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CurrentOrder extends AppCompatActivity {

    private TextView order_item_view;
    private TextView order_cafe_view;
    private TextView order_price_view;
    private TextView order_time_view;

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
        order_time_view= (TextView) findViewById(R.id.current_order_time);
        update();
    }

    public void update() {
        order_item_view.setText(myPref.getString("OrderItem",""));
        order_cafe_view.setText(myPref.getString("OrderCafe",""));
        order_price_view.setText(myPref.getString("OrderPrice",""));
        order_time_view.setText(myPref.getString("OrderTime",""));
    }
}