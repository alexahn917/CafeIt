package com.example.alex.cafeit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private TextView mTextMessage;

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
                    getFragmentManager().beginTransaction().replace(R.id.container, CafesListFragment).commit();
                    setTitle("Cafes List");
                    return true;
                case R.id.navigation_favorites:
                    //mTextMessage.setText(R.string.title_dashboard);
//                    Toast.makeText(getApplicationContext(), "Favorites tab pressed", Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.container, FavoritesFragment).commit();
                    setTitle("Favorites");
                    return true;
                case R.id.navigation_history:
                    //mTextMessage.setText(R.string.title_history);
                    getFragmentManager().beginTransaction().replace(R.id.container, HistoryFragment).commit();
                    setTitle("History");
                    return true;
                case R.id.navigation_profile:
                    //mTextMessage.setText(R.string.title_notifications);
                    getFragmentManager().beginTransaction().replace(R.id.container, ProfileFragment).commit();
                    setTitle("Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_cafes);
    }

}
