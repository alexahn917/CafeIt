package com.example.alex.cafeit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    ListView orderlist;
    OrderItemListAdapter orderAdapter;
    static List<Order> orders;
    private Intent intent;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // preferences
    private Context context;
    private SharedPreferences myPref;
    private SharedPreferences.Editor peditor;
    private String CafeId;
    private static Float OrderWaitTime = 5.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Preference set up
        context = getApplicationContext();
        myPref = PreferenceManager.getDefaultSharedPreferences(context);

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
                .setText(intent.getStringExtra("cafe_name"));
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
        Date orderDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String purchasedDate = dateFormat.format(orderDate);
        String purchasedTime = timeFormat.format(orderDate);
        String cafeName = getIntent().getStringExtra("cafe_name");
        String note = ((EditText) findViewById(R.id.notes)).getText().toString();
        Order first_item = orders.get(0);
        float total_price = 0.0f;
        CafeId = intent.getStringExtra("cafe_id");

        for (Order order : orders) {
            order.cafeID = CafeId;
            order.orderTime = time;
            order.orderDate = orderDate.toString();
            order.customerName = LoginActivity.username;
            order.cafeName = cafeName;
            order.purchasedDate = purchasedDate;
            order.purchasedTime = purchasedTime;
            order.note = note;
            MainActivity.dbAdapter.insertItem(order);
            populateOrderList(order);
            MyHistoryRecyclerViewAdapter adapter = (MyHistoryRecyclerViewAdapter)
                    ((HistoryFragment) MainActivity.HistoryFragment).mAdapter;
            ((HistoryFragment) MainActivity.HistoryFragment).updateArray(adapter);
            total_price += order.price;
        }

        peditor = myPref.edit();
        MainActivity.SetOrderInProgress();
        update_waittime(orders.size());

        if (orders.size() > 1) {
            peditor.putString("CafeId", CafeId);
            peditor.putString("OrderItem", first_item.itemName + "( + " + (orders.size()-1) + ")");
            peditor.putString("OrderCafe", cafeName);
            peditor.putString("OrderPrice", String.format("%.2f",total_price) + "$");
            peditor.putString("OrderPurchasedDate", purchasedDate);
            peditor.putString("OrderPurchasedTime", purchasedTime);
            peditor.putFloat("OrderWaitTime", OrderWaitTime);
        }
        else {
            peditor.putString("CafeId", CafeId);
            peditor.putString("OrderItem", first_item.itemName);
            peditor.putString("OrderCafe", cafeName);
            peditor.putString("OrderPrice", "$ " + String.format("%.2f",total_price));
            peditor.putString("OrderPurchasedDate", purchasedDate);
            peditor.putString("OrderPurchasedTime", purchasedTime);
            peditor.putFloat("OrderWaitTime", OrderWaitTime);
        }

        peditor.apply();
    }

    public void populateOrderList(Order order){
        mDatabase.child("orders").child(CafeId).push().setValue(order);
        System.out.println(order);
    }

    public void update_waittime(final long order_size) {
        mDatabase.child("cafes").child(CafeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cafe cafe = dataSnapshot.getValue(Cafe.class);
                OrderWaitTime = cafe.waitTime + order_size;
                Log.d("$$$$$$$$$$$$$$$$$$$$$", cafe.toString());
                //mDatabase.child("cafes").child(CafeId).child("waitTime").setValue(OrderWaitTime);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}

