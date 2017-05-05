package com.example.alex.cafeit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpUserActivity extends AppCompatActivity implements View.OnClickListener{

    private SpannableString s;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
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

    //private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        // Preference set up
        context = getApplicationContext();
        myPref = PreferenceManager.getDefaultSharedPreferences(context);
        peditor = myPref.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email_view = (EditText) findViewById(R.id.idField);
        pw_view = (EditText) findViewById(R.id.passwordField);
        conf_pw_view = (EditText) findViewById(R.id.passwordConfirmField);
        name_view = (EditText) findViewById(R.id.nameField);

        findViewById(R.id.createButton).setOnClickListener(this);
        findViewById(R.id.signupPaymentButton).setOnClickListener(this);

        setTitle("Sign up (User)");

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
        String id = email_view.getText().toString();
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
        String pw1 = pw_view.getText().toString();
        String pw2 = conf_pw_view.getText().toString();
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
                commitUserInfo();
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

    public void commitUserInfo() {
        String useremail = email_view.getText().toString();
        String userpw = pw_view.getText().toString();
        String username = name_view.getText().toString();
        peditor.putString("USER_ID", useremail);
        peditor.putString("USER_PW", userpw);
        peditor.putString("USER_NAME", username);
        peditor.commit();
    }

}