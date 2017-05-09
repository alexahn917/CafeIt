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
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.cafeit.fragments.CafeMenuFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

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


    private TextView smallTextLabel;
    boolean isOneSize;
    int time;
    float smallPrice, mediumPrice, largePrice;
    String name;

    private CafeMenuItem myItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_menu_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Menu");

        myItem = CafeMenuEditPasser.item;

        smallTextLabel = (TextView) findViewById(R.id.size1);

        nameField = (EditText) findViewById(R.id.menuItemName);
        nameField.setText(myItem.getNameDecoded());

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
                if(updateItem()) {
                    Toast.makeText(getApplicationContext(), "Successfully saved!", Toast.LENGTH_SHORT).show();
                    CafeMenuFragment.count = 0;
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid update!", Toast.LENGTH_SHORT).show();
                }

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
                    smallTextLabel.setText("One Size");
                    secondSize.setVisibility(View.GONE);
                    thirdSize.setVisibility(View.GONE);
                } else {
                    smallTextLabel.setText("Small");
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
        name = nameField.getText() + "";
        int category = categorySpinner.getSelectedItemPosition();
        isOneSize = oneSize.isChecked();

        if(!validateAll()){
            return null;
        }

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

    public boolean updateItem(){
        CafeMenuItem cmi = transformItem();
        if(cmi != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("cafeMenu").child(AuthHandler.getUid()).child(myItem.getName()).removeValue();
            mDatabase.child("cafeMenu").child(AuthHandler.getUid()).child(cmi.getName()).setValue(cmi);
            return true;
        } else {
           return false;
        }
    }

    public void deleteItem(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("cafeMenu").child(AuthHandler.getUid()).child(myItem.getName()).removeValue();
    }

    public boolean validateName(){
        return !name.equals("");
    }

    public boolean validateEST(){
        if(timeField.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    public boolean validatePrice(){
        if(smallField.getText().toString().equals("")){
            return false;
        }

        if(!isOneSize){
            if(mediumField.getText().toString().equals("") || largeField.getText().toString().equals(""))
                return false;
        }

        return true;
    }

    public boolean validateAll(){
        return validateName() && validateEST() && validatePrice();
    }
}
