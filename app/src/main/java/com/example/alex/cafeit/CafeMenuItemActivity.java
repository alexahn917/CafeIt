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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alex.cafeit.fragments.CafeMenuFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CafeMenuItemActivity extends AppCompatActivity {

    private SpannableString s;

    private String[] categories = {"Espresso Drinks", "Non-Espresso Drinks", "Ice Blended", "Food & snacks"};
    List<String> spinnerArray = new ArrayList<String>();

    private Spinner categorySpinner;
    private Button saveButton;
    private Button deleteButton;
    private RadioButton oneSize, threeSize;
    private LinearLayout secondSize, thirdSize;
    private EditText nameField, timeField, smallField, mediumField, largeField;

    private CafeMenuItem myItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_menu_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Menu");

        myItem = CafeMenuEditPasser.item;

        nameField = (EditText) findViewById(R.id.menuItemName);
        nameField.setText(myItem.getName());

        timeField = (EditText) findViewById(R.id.menuItemTime);
        timeField.setText(myItem.getTakesTime() + "");

        smallField = (EditText) findViewById(R.id.price1);
        mediumField = (EditText) findViewById(R.id.price2);
        largeField = (EditText) findViewById(R.id.price3);

        smallField.setText(myItem.getSmallPrice() + "");
        if(myItem.getMediumPrice() != 0) {
            mediumField.setText(myItem.getMediumPrice() + "");
            largeField.setText(myItem.getLargePrice() + "");
        }



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

        categorySpinner.setSelection(myItem.getCategory(), true);

        saveButton = (Button) findViewById(R.id.itemSaveButton);
        deleteButton= (Button) findViewById(R.id.itemDeleteButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Successfully saved!", Toast.LENGTH_SHORT).show();
                updateItem();
                CafeMenuFragment.count = 0;
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Successfully deleted!", Toast.LENGTH_SHORT).show();
                deleteItem();
                CafeMenuFragment.count = 0;
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

        if(myItem.isOneSize()){
            oneSize.setChecked(true);
        } else {
            oneSize.setChecked(false);
        }




    }
    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(getApplicationContext(), "Bodoni 72.ttc"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setTitle(s);
    }

    public CafeMenuItem transformItem(){
        String name = nameField.getText() + "";
        int category = categorySpinner.getSelectedItemPosition();
        boolean isOneSize = oneSize.isChecked();
        int time = Integer.parseInt(timeField.getText() + "");
        float small, medium, large;
        if(!(smallField.getText() + "").equals("")) {
            small = Float.parseFloat(smallField.getText() + "");
        } else {
            small = 0f;
        }
        if(!(mediumField.getText() + "").equals("")) {
            medium = Float.parseFloat(mediumField.getText() + "");
        } else {
            medium = 0f;
        }
        if(!(largeField.getText() + "").equals("")){
            large = Float.parseFloat(largeField.getText() + "");
        } else {
            large = 0f;
        }


        return new CafeMenuItem(name, category, isOneSize, time, small, medium, large, 0);
    }

    public void updateItem(){
        CafeMenuItem cmi = transformItem();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("cafeMenu").child(AuthHandler.getUid()).child(myItem.getName()).removeValue();
        mDatabase.child("cafeMenu").child(AuthHandler.getUid()).child(cmi.getName()).setValue(cmi);
    }

    public void deleteItem(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("cafeMenu").child(AuthHandler.getUid()).child(myItem.getName()).removeValue();
    }

}
