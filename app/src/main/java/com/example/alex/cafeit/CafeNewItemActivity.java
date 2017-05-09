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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.cafeit.fragments.CafeMenuFragment;

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
    private EditText estTimeText;
    private TextView smallTextLabel;


    String smallText, mediumText, largeText;
    boolean isOneSize;
    int time;
    float smallPrice, mediumPrice, largePrice;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_new_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add an item");

        estTimeText = (EditText) findViewById(R.id.menuItemTime);
        smallTextLabel = (TextView) findViewById(R.id.size1);

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

                //TODO: transform entry into cafeMenuItem, use NewMenuItemPusher to push to FB
                CafeMenuItem item = menuItemMaker();
                if(item != null) {
                    NewMenuItemPusher.pushItem(item);

                    Toast.makeText(getApplicationContext(), "Successfully added!", Toast.LENGTH_SHORT).show();
                    CafeMenuFragment.count = 0;
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid item!", Toast.LENGTH_SHORT).show();
                }
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




    }
    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(getApplicationContext(), "Bodoni 72.ttc"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setTitle(s);
    }

    public CafeMenuItem menuItemMaker(){
        String timeText = estTimeText.getText().toString();

        name = ((TextView) findViewById(R.id.menuItemName)).getText() + "";
        int category = ((Spinner) findViewById(R.id.menuItemCategorySpinner)).getSelectedItemPosition();

        RadioButton oneSizeRb = (RadioButton) findViewById(R.id.oneSizeRadio);
        if(oneSizeRb.isChecked()) isOneSize = true;
        else isOneSize = false;

        smallText = ((EditText) findViewById(R.id.price1)).getText().toString();
        mediumText = ((EditText) findViewById(R.id.price2)).getText().toString();
        largeText = ((EditText) findViewById(R.id.price3)).getText().toString();

        if(!validateAll()){
            return null;
        }
        time = Integer.parseInt(timeText);
        smallPrice = Float.parseFloat(((EditText) findViewById(R.id.price1)).getText() + "");

        if(isOneSize){
            mediumPrice = 0f;
            largePrice = 0f;
        } else {
            mediumPrice = Float.parseFloat(((EditText) findViewById(R.id.price2)).getText() + "");
            largePrice = Float.parseFloat(((EditText) findViewById(R.id.price3)).getText() + "");
        }
        return new CafeMenuItem(name, category, isOneSize, time, smallPrice, mediumPrice, largePrice, 0);
    }

    public boolean validateName(){
        return !name.equals("");
    }

    public boolean validateEST(){
        if(estTimeText.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    public boolean validatePrice(){
        if(smallText.equals("")){
            return false;
        }

        if(!isOneSize){
            if(mediumText.equals("") || largeText.equals(""))
                return false;
        }

        return true;
    }

    public boolean validateAll(){
        return validateName() && validateEST() && validatePrice();
    }

}
