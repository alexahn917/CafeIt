package com.example.alex.cafeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<MenuItem>> listDataChild;

    private Intent intent;
    static final int ORDER_SUCCESS = 1;
    static final int ORDER_CANCEL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_order_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ActionBar abar = getSupportActionBar();
        if (abar != null) {
            abar.setDisplayHomeAsUpEnabled(true);
        }

        expListView = (ExpandableListView) findViewById(R.id.menu);

        setup();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckoutActivity.orders = new ArrayList<>();
                for (int i = 0; i < listDataHeader.size(); i++) {
                    List<MenuItem> childList = listDataChild.get(listDataHeader.get(i));
                    for (int j = 0; j < childList.size(); j++) {
                        MenuItem item = childList.get(j);
                        if (item.selected) {
                            String[] choice = item.choices[item.choice_pos].split("\\s+");
                            float price = Float.parseFloat(
                                    (new StringBuilder(choice[0])).deleteCharAt(0).toString());
                            OrderItem orderItem = new OrderItem(item.name, choice[1],price, item.completionTime);
                            orderItem.quantity = item.quantity;
                            CheckoutActivity.orders.add(new Order(orderItem));
                        }
                    }
                }
                if (CheckoutActivity.orders.size() > 0) {
                    Intent new_intent = new Intent(view.getContext(), CheckoutActivity.class);
                    new_intent.putExtra("CafeId", intent.getStringExtra("CafeId"));
                    startActivityForResult(new_intent, ORDER_SUCCESS);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.no_orders, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ORDER_SUCCESS) {
            if (resultCode == RESULT_OK) {
                setResult(ORDER_SUCCESS);
                finish();
            }
        }
    }

    private void setup() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        final int numHeaders = 3;
        final int numChildren = 4;
        for (int i = 0; i < numHeaders; i++) {
            listDataHeader.add(getString(R.string.menu_header) + i);
            List<MenuItem> childItems = new ArrayList<>();
            for (int j = 0; j < numChildren; j++) {
                MenuItem item = new MenuItem(false, getString(R.string.menu_item), 3, 1,
                        getResources().getStringArray(R.array.sizes));
                childItems.add(item);
            }
            listDataChild.put(listDataHeader.get(i), childItems);
        }
    }

}
