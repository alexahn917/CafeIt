package com.example.alex.cafeit;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.alex.cafeit.fragments.CafesListFragment;
import com.example.alex.cafeit.fragments.FavoritesFragment;
import com.example.alex.cafeit.fragments.HistoryFragment;
import com.example.alex.cafeit.fragments.ProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        com.example.alex.cafeit.fragments.CafesListFragment.OnListFragmentInteractionListener,
        com.example.alex.cafeit.fragments.FavoritesFragment.OnListFragmentInteractionListener {

    static final int ORDER_SUCCESS = 1;

    private Fragment CafesListFragment = new CafesListFragment();
    private Fragment FavoritesFragment = new FavoritesFragment();
    static Fragment HistoryFragment = com.example.alex.cafeit.fragments.HistoryFragment.newInstance(1);
    private Fragment ProfileFragment = new ProfileFragment();
    private Context context;
    private SpannableString s;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static HashMap<String, String> CafeIdHash = new HashMap();

    public static LocalDBAdapter dbAdapter;
    public static ArrayList<Order> history = new ArrayList<>();
    public static ArrayList<Order> favorites = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_cafes:
                    setTitle("Cafes");
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, CafesListFragment).commit();
                    return true;
                case R.id.navigation_favorites:
                    setTitle("Favorites");
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, FavoritesFragment).commit();
                    return true;
                case R.id.navigation_history:
                    setTitle("History");
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, HistoryFragment).commit();
                    return true;
                case R.id.navigation_profile:
                    setTitle("Profile");
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, ProfileFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        dbAdapter = new LocalDBAdapter(this);
        dbAdapter.open();
        history.clear();
        favorites.clear();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_cafes);
        setTitle("Cafes");

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_CONTACTS},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.


            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.current_order) {
            Intent i = new Intent(this, CurrentOrder.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setTitle(CharSequence title) {
        s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(context, "Bodoni 72.ttc"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setTitle(s);
    }

    @Override
    public void onListFragmentInteraction(Cafe cafe, int pos) {
        Intent i = new Intent(this, OrderActivity.class);
        i.putExtra("CafeId", cafe.ID);
        i.putExtra("cafe", cafe.name);
        startActivityForResult(i, ORDER_SUCCESS);
    }

    @Override
    public void onListFragmentInteraction(Order item, int pos) {
        // Make the order!
        // Add to customer DB
        // Call SqureApp API
    }

    @Override
    public void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }

}
