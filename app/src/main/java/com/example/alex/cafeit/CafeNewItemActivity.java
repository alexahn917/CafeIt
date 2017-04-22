package com.example.alex.cafeit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CafeNewItemActivity extends AppCompatActivity {

    private SpannableString s;

    private String[] categories = {"Espresso Drinks", "Non-Espresso Drinks", "Ice Blended", "Food & snacks"};
    List<String> spinnerArray = new ArrayList<String>();

    private Spinner categorySpinner;
    private Button addButton;
    private RadioButton oneSize, threeSize;
    private LinearLayout secondSize, thirdSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_new_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add an item");

        spinnerArray.add("Espresso Drinks");
        spinnerArray.add("Non-Espresso Drinks");
        spinnerArray.add("Ice Blended");
        spinnerArray.add("Food & Snacks");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner = (Spinner) findViewById(R.id.menuItemCategorySpinner);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addButton = (Button) findViewById(R.id.itemAddButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Successfully added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        oneSize = (RadioButton) findViewById(R.id.oneSizeRadio);
        threeSize = (RadioButton) findViewById(R.id.threeSizeRadio);
        secondSize = (LinearLayout) findViewById(R.id.secondSize);
        thirdSize = (LinearLayout) findViewById(R.id.thirdSize);

        oneSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(oneSize.isChecked()){
                    secondSize.setVisibility(View.GONE);
                    thirdSize.setVisibility(View.GONE);
                } else {
                    secondSize.setVisibility(View.VISIBLE);
                    thirdSize.setVisibility(View.VISIBLE);
                }
            }
        });




    }
    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(getApplicationContext(), "Bodoni 72.ttc"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setTitle(s);
    }

}
