package com.example.alex.cafeit;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CafesListFragment.OnListFragmentInteractionListener {

    //private TextView mTextMessage;
    private static final int REQUEST_ORDER = 1;

    private Fragment CafesListFragment = new CafesListFragment();
    private Fragment FavoritesFragment = new FavoritesFragment();
    private Fragment HistoryFragment = new HistoryFragment();
    private Fragment ProfileFragment = new ProfileFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_cafes:
                    //mTextMessage.setText(R.string.title_home);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, CafesListFragment).commit();
                    //setTitle("Cafes List");
                    return true;
                case R.id.navigation_favorites:
                    //mTextMessage.setText(R.string.title_dashboard);
//                    Toast.makeText(getApplicationContext(), "Favorites tab pressed", Toast.LENGTH_LONG).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, FavoritesFragment).commit();
                    //setTitle("Favorites");
                    return true;
                case R.id.navigation_history:
                    //mTextMessage.setText(R.string.title_history);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, HistoryFragment).commit();
                    //setTitle("History");
                    return true;
                case R.id.navigation_profile:
                    //mTextMessage.setText(R.string.title_notifications);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, ProfileFragment).commit();
                    //setTitle("Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_cafes);
    }
    @Override
    public void onListFragmentInteraction(Cafe cafe) {
        Intent i = new Intent(this, OrderView.class);
        startActivity(i);
    }

}
