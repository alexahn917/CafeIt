package com.example.alex.cafeit;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CafeMainActivity extends AppCompatActivity {

//    private TextView mTextMessage;
    private Context context;
    private SpannableString s;

    private Fragment profileFragment = new CafeProfileFragment();
    private Fragment ordersFragment = new OrdersFragment();
    private Fragment menuFragment = new CafeMenuFragment();

    private FloatingActionButton saveFab;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_orders:
                    setTitle("Orders");
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, ordersFragment).commit();
                    saveFab.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_menu:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, menuFragment).commit();
                    setTitle("Menu Listing");
                    saveFab.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_cafe_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, profileFragment).commit();
                    setTitle("Profile");
                    saveFab.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_main);
        context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveFab = (FloatingActionButton) findViewById(R.id.saveFAB);
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Menu saved!", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_cafe_layout);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, ordersFragment).commit();
        setTitle("Orders");
    }

    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(context, "Bodoni 72.ttc"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setTitle(s);
    }
}
