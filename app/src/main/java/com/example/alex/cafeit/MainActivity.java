package com.example.alex.cafeit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        CafesListFragment.OnListFragmentInteractionListener,
        HistoryFragment.OnListFragmentInteractionListener {

    private static final int REQUEST_ORDER = 1;

    private Fragment CafesListFragment = new CafesListFragment();
    private Fragment FavoritesFragment = new FavoritesFragment();
    private Fragment HistoryFragment = new HistoryFragment();
    private Fragment ProfileFragment = new ProfileFragment();
    private Context context;
    private SpannableString s;

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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_cafes);
        setTitle("Cafes");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.current_order) {
            Intent i = new Intent(this, CurrentOrder.class);
            startActivity(i);
        }

        else if (id == R.id.sort_distance) {
            Toast.makeText(context, "Sorted by distance", Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.sort_wait) {
            Toast.makeText(context, "Sorted by waiting time", Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.sort_rating) {
            Toast.makeText(context, "Sorted by ratings", Toast.LENGTH_SHORT).show();
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
    public void onListFragmentInteraction(Cafe cafe) {
        Intent i = new Intent(this, OrderView.class);
        startActivity(i);
    }


    @Override
    public void onListFragmentInteraction(Order item) {
        Intent i = new Intent(this, OrderView.class);
        startActivity(i);
    }

}
