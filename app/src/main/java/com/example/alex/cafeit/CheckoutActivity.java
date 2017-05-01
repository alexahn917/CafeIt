package com.example.alex.cafeit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.cafeit.fragments.HistoryFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    ListView orderlist;
    OrderItemListAdapter orderAdapter;
    static List<Order> orders;
    private Intent intent;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

//    final int orderCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar abar = getSupportActionBar();
        if (abar != null) {
            abar.setDisplayHomeAsUpEnabled(true);
        }

        orderlist = (ListView) findViewById(R.id.order_list);
        ((TextView) findViewById(R.id.total))
                .setText(String.format(Locale.US, "$%.2f", calc_price()));
        ((TextView) findViewById(R.id.CafeName_checkout))
                .setText(getIntent().getStringExtra("cafe"));
        orderAdapter = new OrderItemListAdapter(this, orders);
        orderlist.setAdapter(orderAdapter);

        Button checkout = (Button) findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndShowAlertDialog();
            }
        });

    }

    public float calc_price() {
        float result = 0f;
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            result += o.price * o.quantity;
        }
        return result;
    }

    public void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(CheckoutActivity.this, "Your order is on the way!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                setResult(OrderActivity.ORDER_SUCCESS);
                updateAppData();
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

    private void updateAppData() {
        final Calendar myCalendar = Calendar.getInstance();
        String myFormat = "MM/dd/yyyy"; //Format for date choice
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String time = sdf.format(myCalendar.getTime());

        String cafe = getIntent().getStringExtra("cafe");
        String note = ((EditText) findViewById(R.id.notes)).getText().toString();

        for (Order order : orders) {
            order.orderTime = time;
            order.cafeName = cafe;
            order.note = note;
            MainActivity.dbAdapter.insertItem(order);
            populateOrderList(order);
            ((HistoryFragment) MainActivity.HistoryFragment).updateArray();
        }
    }

    public void populateOrderList(Order order){
        String CafeId = intent.getStringExtra("CafeId");
        System.out.println("IHIHIHIHIHI:  " + CafeId);
        //mDatabase.child("cafes").child(CafeId).child("orders").push().setValue(order);
    }
}
