package com.example.alex.cafeit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    ListView orderlist;
    OrderItemListAdapter orderAdapter;
    List<OrderItem> orders;
    final int orderCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar abar = getSupportActionBar();
        if (abar != null) {
            abar.setDisplayHomeAsUpEnabled(true);
        }

        orderlist = (ListView) findViewById(R.id.order_list);
        setup();
        orderAdapter = new OrderItemListAdapter(this, orders, this);
        orderlist.setAdapter(orderAdapter);

        Button checkout = (Button) findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent done = new Intent();
                setResult(OrderView.ORDER_SUCCESS);
                finish();
            }
        });

    }

    public void setup() {
        orders = new ArrayList<>();
        for (int i = 0; i < orderCount; i++) {
            String name = getString(R.string.order_item);
            String size = getString(R.string.order_size);
            float price = Float.parseFloat(getString(R.string.sample_price_sz));
            OrderItem item = new OrderItem(name, size, price);
            orders.add(item);
        }
    }
}
