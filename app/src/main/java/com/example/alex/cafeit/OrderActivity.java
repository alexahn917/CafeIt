package com.example.alex.cafeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<MenuItem>> listDataChild = new HashMap<>();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private List<MenuItem> menuList = new ArrayList<>();

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
        String cafe_name = intent.getStringExtra("cafe_name");
        String cafe_id = intent.getStringExtra("cafe_id");
        String cafe_address = intent.getStringExtra("cafe_address");
        String[] adr_lines = cafe_address.split(",", 2);
        String cafe_distance = intent.getStringExtra("cafe_distance");

        String cafe_start_hour = intent.getStringExtra("cafe_start_hour").replace("0A", "0 A").replace("0P", "0 P");
        String cafe_end_hour = intent.getStringExtra("cafe_end_hour").replace("0A", "0 A").replace("0P", "0 P");
        float cafe_rating = intent.getFloatExtra("cafe_rating", 0f);
        ((TextView) findViewById(R.id.CafeName)).setText(cafe_name);
        ((TextView) findViewById(R.id.cafe_address1)).setText(adr_lines[0] + ",");
        if (adr_lines[1].startsWith(" ")) {
            adr_lines[1] = adr_lines[1].replaceFirst(" ", "");
        }
        ((TextView) findViewById(R.id.cafe_address2)).setText(adr_lines[1]);
        ((TextView) findViewById(R.id.cafe_distance)).setText(cafe_distance);
        ((RatingBar) findViewById(R.id.cafe_rating)).setRating(cafe_rating);
        ((TextView) findViewById(R.id.hrs)).setText(cafe_start_hour + " – " + cafe_end_hour);
        Calendar cal = Calendar.getInstance();
        int cur_time = cal.get(Calendar.HOUR_OF_DAY);
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        try {
            int hr1 = Integer.parseInt(displayFormat.format(parseFormat.parse(cafe_start_hour)));
            int hr2 = Integer.parseInt(displayFormat.format(parseFormat.parse(cafe_end_hour)));

            if (!(hr1 <= cur_time && cur_time <= hr2)) {
                TextView cafe_open =  (TextView) findViewById(R.id.cafe_op_cl);
                cafe_open.setText(getResources().getText(R.string.cafe_closed));
                cafe_open.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(MainActivity.PROF_DIR).child(
                cafe_id + MainActivity.PROF_PIC_SUFFIX);
        ImageView pic = (ImageView) findViewById(R.id.CafePic);
        Glide.with(this /* context */).using(new FirebaseImageLoader()).load(storageReference)
                .error(ContextCompat.getDrawable(this, R.mipmap.ic_logo)).into(pic);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ActionBar abar = getSupportActionBar();
        if (abar != null) {
            abar.setDisplayHomeAsUpEnabled(true);
        }

        expListView = (ExpandableListView) findViewById(R.id.menu);

//        setup();

        populateMenuList();

        /*
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        */

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckoutActivity.orders = new ArrayList<>();
                for (int i = 0; i < listDataHeader.size(); i++) {
                    List<MenuItem> childList = listDataChild.get(listDataHeader.get(i));
                    for (int j = 0; j < childList.size(); j++) {
                        MenuItem item = childList.get(j);
                        if (item.selected) {
//                            String[] choice = item.choices[item.choice_pos].split("\\s+");
                            float price = 0f;

                            switch(item.choice_pos){
                                case 0: price = item.getSmallPrice(); break;
                                case 1: price = item.getMediumPrice(); break;
                                case 2: price = item.getLargePrice(); break;
                            }
                            Log.d("selected: ", item.name + " / " + item.getQuantity() + " / " + item.completionTime + " / " + item.oneSize);
//                            Toast.makeText(OrderActivity.this, item.getName(), Toast.LENGTH_SHORT).show();

                            OrderItem orderItem = new OrderItem(item.getName(), item.selectedSize, price, item.completionTime);
                            orderItem.quantity = item.getQuantity();
                            CheckoutActivity.orders.add(new Order(orderItem));
                        }
                    }
                }
                if (CheckoutActivity.orders.size() > 0) {
                    Intent new_intent = new Intent(view.getContext(), CheckoutActivity.class);
                    new_intent.putExtra("cafe_id", intent.getStringExtra("cafe_id"));
                    new_intent.putExtra("cafe_name", intent.getStringExtra("cafe_name"));
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

    private void populateMenuList(){
        Log.d("entered" , "hi");

        mDatabase.child("cafeMenu").child(intent.getStringExtra("cafe_id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    CafeMenuItem cmi = postSnapshot.getValue(CafeMenuItem.class);
                    Log.d("tem ****************", cmi.getName() + " / " + cmi.getCategory());

                    MenuItem item = transformCmiToMi(cmi);

                    menuList.add(item);
                }
                Log.d("making", "yes");

                refreshList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Log.d("entered pop method" , "hi");

    }

    public void makeList(){
        listDataHeader.clear();
        listDataHeader.add("Espresso Drinks");
        listDataHeader.add("Non-Espresso Drinks");
        listDataHeader.add("Ice Blended");
        listDataHeader.add("Food & Snacks");

        List<MenuItem> espressoMenu = new ArrayList<>();
        List<MenuItem> nonEspressoMenu = new ArrayList<>();
        List<MenuItem> iceBlendedMenu = new ArrayList<>();
        List<MenuItem> foodMenu = new ArrayList<>();

        for(MenuItem i : menuList){
            switch(i.getCategory()){
                case 0: espressoMenu.add(i); break;
                case 1: nonEspressoMenu.add(i); break;
                case 2: iceBlendedMenu.add(i); break;
                case 3: foodMenu.add(i); break;
            }
        }
        listDataChild.clear();
        listDataChild.put(listDataHeader.get(0), espressoMenu);
        listDataChild.put(listDataHeader.get(1), nonEspressoMenu);
        listDataChild.put(listDataHeader.get(2), iceBlendedMenu);
        listDataChild.put(listDataHeader.get(3), foodMenu);
    }

    public void refreshList(){
        Log.d("entered refresh" , "good");
        Log.d("refreshing", " good");

        makeList();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        listAdapter.notifyDataSetChanged();

        // setting list adapter
        expListView.setAdapter(listAdapter);
//        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
////                    Toast.makeText(getActivity(), ((CafeMenuItem)(listAdapter.getChild(groupPosition, childPosition))).getName(), Toast.LENGTH_SHORT).show();
//
//                MenuItem mi = (MenuItem) listAdapter.getChild(groupPosition, childPosition);
//
////                mi.selected = !mi.selected;
//
//
//                return true;
//            }
//        });
    }

    public MenuItem transformCmiToMi(CafeMenuItem cmi){
        MenuItem item = new MenuItem(cmi);
        return item;
    }

}
