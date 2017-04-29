package com.example.alex.cafeit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpCafeActivity extends SignUpUserActivity {
    private static final int PAYMENT_SETUP_REQUEST = 1;

    // preferences
    private Context context;
    private SharedPreferences myPref;
    private SharedPreferences.Editor peditor;

    // UI elements
    private EditText email_view;
    private EditText pw_view;
    private EditText conf_pw_view;
    private EditText name_view;

    // private variables
    private boolean paymentSetUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_cafe);
    }

    @Override
    public void onClick(View v){
        validCreation();
        int i = v.getId();
        if (i == R.id.createButton) {
            if (validCreation()) {
                String useremail = email_view.getText().toString();
                String userpw = pw_view.getText().toString();
                String username = name_view.getText().toString();
                peditor.putString("USER_ID", useremail);
                peditor.putString("USER_PW", userpw);
                peditor.putString("USER_NAME", username);
                peditor.commit();
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

}