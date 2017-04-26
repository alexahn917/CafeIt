package com.example.alex.cafeit;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

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
                createAndShowAlertDialog();
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

    public void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(CheckoutActivity.this, "Your order is on the way!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                setResult(OrderActivity.ORDER_SUCCESS);
                finish();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(CheckoutActivity.this, "Canceled order.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                setResult(OrderActivity.ORDER_CANCEL);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

