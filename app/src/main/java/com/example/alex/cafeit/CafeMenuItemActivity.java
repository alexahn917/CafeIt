package com.example.alex.cafeit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CafeMenuItemActivity extends AppCompatActivity {

    private SpannableString s;

    private String[] categories = {"Espresso Drinks", "Non-Espresso Drinks", "Ice Blended", "Food & snacks"};
    List<String> spinnerArray = new ArrayList<String>();

    private Spinner categorySpinner;
    private Button saveButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_menu_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Menu");

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

        saveButton = (Button) findViewById(R.id.itemSaveButton);
        deleteButton= (Button) findViewById(R.id.itemDeleteButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Successfully saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Successfully deleted!", Toast.LENGTH_SHORT).show();
                finish();
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
