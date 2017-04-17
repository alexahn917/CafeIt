package com.example.alex.cafeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderView extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<MenuItem>> listDataChild;

    static final int ORDER_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Checkout.class);
                startActivityForResult(intent, ORDER_SUCCESS);
                finish();
            }
        });

        ActionBar abar = getSupportActionBar();
        if (abar != null) {
            abar.setDisplayHomeAsUpEnabled(true);
        }

        expListView = (ExpandableListView) findViewById(R.id.menu);

        setup();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
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
                MenuItem item = new MenuItem(true, getString(R.string.menu_item), 1,
                        getResources().getStringArray(R.array.sizes));
                childItems.add(item);
            }
            listDataChild.put(listDataHeader.get(i), childItems);
        }
    }

}
