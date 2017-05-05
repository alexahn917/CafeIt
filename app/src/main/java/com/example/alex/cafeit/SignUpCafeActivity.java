package com.example.alex.cafeit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpCafeActivity extends AppCompatActivity implements View.OnClickListener {

    private SpannableString s;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final int PAYMENT_SETUP_REQUEST = 1;

    // preferences
    private Context context;
    private SharedPreferences myPref;
    private SharedPreferences.Editor peditor;

    // UI elements
    private EditText cafe_email_view;
    private EditText cafe_pw_view;
    private EditText cafe_conf_pw_view;
    private EditText cafe_name_view;
    private EditText cafe_address1_view;
    private EditText cafe_address2_view;
    private EditText cafe_state_view;
    private EditText cafe_zipcode_view;
    private EditText cafe_bestmenu_view;
    private Switch cafe_haswifi_switch;
    private EditText cafe_address_view;

    // values
    private String cafe_starthour = "9:00 AM";
    private String cafe_endhour = "8:00 PM";
    private float cafe_rating = 2.5f;
    private float cafe_waittime = 0.0f;
    private float cafe_latitude = 0f;
    private float cafe_longitude = 0f;
    private boolean validAddress = false;

    // private variables
    private boolean paymentSetUp = false;

    //private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_cafe);

        // Preference set up
        context = getApplicationContext();
        myPref = PreferenceManager.getDefaultSharedPreferences(context);
        peditor = myPref.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cafe_email_view = (EditText) findViewById(R.id.idField);
        cafe_pw_view = (EditText) findViewById(R.id.passwordField);
        cafe_conf_pw_view = (EditText) findViewById(R.id.passwordConfirmField);
        cafe_name_view = (EditText) findViewById(R.id.nameField);
        cafe_bestmenu_view = (EditText) findViewById(R.id.bestMenuField);
        cafe_haswifi_switch = (Switch) findViewById(R.id.hasWifiSwitch);
        cafe_address1_view = (EditText) findViewById(R.id.addressInput1);
        cafe_address2_view = (EditText) findViewById(R.id.addressInput2);
        cafe_state_view = (EditText) findViewById(R.id.state);
        cafe_zipcode_view = (EditText) findViewById(R.id.zipcode);

        findViewById(R.id.createButton).setOnClickListener(this);
        findViewById(R.id.signupPaymentButton).setOnClickListener(this);

        setTitle("Sign up (Cafe)");
    }

    private void linkPayment(){
        Toast.makeText(this, "Sending you to outside payment API...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(context, "Bodoni 72.ttc"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setTitle(s);
    }

    public boolean validateID() {
        String id = cafe_email_view.getText().toString();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(id);

        if (!matcher.find()) {
            Toast.makeText(this, "Invalid email.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    public boolean validatePW() {
        String pw1 = cafe_pw_view.getText().toString();
        String pw2 = cafe_conf_pw_view.getText().toString();
        if (pw1 == null && pw2 == null) {
            Toast.makeText(this, "Invalid password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pw1.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!pw1.equals(pw2)) {
            Toast.makeText(this, "Confirmation password does not match with password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    public boolean validCreation() {
        if (validateID() && validatePW()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v){
        validCreation();
        int i = v.getId();
        if (i == R.id.createButton) {
            if (validCreation()) {
                commitCafeInfo();
                if (!this.validAddress) {
                    Toast.makeText(context, "Invalid Creation of Cafe", Toast.LENGTH_SHORT).show();
                    return;
                }
                setResult(RESULT_OK);
                finish();
            }
            else {
                Toast.makeText(this, "Invalid ID / PW", Toast.LENGTH_SHORT).show();
            }
        } else if (i == R.id.signupPaymentButton) {
            if (validCreation()) {
                // Lanuch activity
                //Intent intent = new Intent(this, PaymentSetUpActivity.class);
                //startActivityForResult(intent, PAYMENT_SETUP_REQUEST);
                paymentSetUp = true;
                if (paymentSetUp) {
                    findViewById(R.id.createButton).setEnabled(true);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PAYMENT_SETUP_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //String CREDIT_CARD_INFO= myPref.getString("CREDIT_CARD_INFO", "");
                paymentSetUp = true;
            }
        }
    }

    public void commitCafeInfo() {
        peditor.putString("CAFE_ID", cafe_email_view.getText().toString());
        peditor.putString("CAFE_PW", cafe_pw_view.getText().toString());
        peditor.putString("CAFE_NAME", cafe_name_view.getText().toString());
        peditor.putString("CAFE_STARTHOUR", cafe_starthour);
        peditor.putString("CAFE_ENDHOUR", cafe_endhour);
        peditor.putString("CAFE_BESTMENU", cafe_bestmenu_view.getText().toString());
        peditor.putFloat("CAFE_RATING", cafe_rating);
        peditor.putString("CAFE_ADDRESS",getAddress());
        peditor.putInt("CAFE_HASWIFI", hasWifi());
        peditor.putFloat("CAFE_WAITTIME", cafe_waittime);
        peditor.putFloat("CAFE_LATITUDE", cafe_latitude);
        peditor.putFloat("CAFE_LONGITUDE", cafe_longitude);
        peditor.commit();
    }

    public String getAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(cafe_address1_view.getText().toString() + ", ");
        sb.append(cafe_address2_view.getText().toString() + ", ");
        sb.append(cafe_state_view.getText().toString().toUpperCase() + " ");
        sb.append(cafe_zipcode_view.getText().toString());
        String address = sb.toString();

        try {
            Log.d("DEBUG: ", "address: " + address);
            Geocoder geo = new Geocoder(this);
            Address adr = geo.getFromLocationName(address, 1).get(0);
            cafe_latitude = (float) adr.getLatitude();
            cafe_longitude = (float) adr.getLongitude();
            this.validAddress = true;
        } catch (IOException e) {
            this.validAddress = false;
        }

        return address;
    }


    public int hasWifi() {
        if (cafe_haswifi_switch.isChecked()) {
            return 1;
        }
        else {
            return 0;
        }
    }

}