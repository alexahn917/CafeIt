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
import android.view.MenuItem;
//import android.app.Fragment;
import android.view.View;

import com.example.alex.cafeit.fragments.CafeMenuFragment;
import com.example.alex.cafeit.fragments.CafeProfileFragment;
import com.example.alex.cafeit.fragments.CafeOrdersFragment;

public class CafeMainActivity extends AppCompatActivity {

//    private TextView mTextMessage;
    private Context context;
    private SpannableString s;

    private Fragment profileFragment = new CafeProfileFragment();
    private Fragment ordersFragment = new CafeOrdersFragment();
    public Fragment menuFragment = new CafeMenuFragment();

    private FloatingActionButton addFab;
    private int fragStatus = 0;
    private static boolean nowShowing;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_orders:
                    setTitle("Orders");
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, ordersFragment).commit();
                    addFab.setVisibility(View.INVISIBLE);
                    fragStatus = 1;
                    CafeMenuFragment.count = 0;
                    nowShowing = true;
                    return true;
                case R.id.navigation_menu:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, menuFragment).commit();
                    setTitle("Menu Listing");
                    addFab.setVisibility(View.VISIBLE);
                    fragStatus = 2;
                    nowShowing = true;
                    CafeMenuFragment.count = 0;
                    return true;
                case R.id.navigation_cafe_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, profileFragment).commit();
                    setTitle("Profile");
                    addFab.setVisibility(View.INVISIBLE);
                    fragStatus = 3;
                    nowShowing = true;
                    CafeMenuFragment.count = 0;
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
        nowShowing = true;

        addFab = (FloatingActionButton) findViewById(R.id.addFAB);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CafeNewItemActivity.class);
                startActivity(i);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_cafe_layout);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, ordersFragment).commit();
        setTitle("Orders");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fragStatus == 2 && !nowShowing){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_cafe, menuFragment).commit();
            setTitle("Menu Listing");
            addFab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nowShowing = false;
    }

    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(context, "Bodoni 72.ttc"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setTitle(s);
    }
}
