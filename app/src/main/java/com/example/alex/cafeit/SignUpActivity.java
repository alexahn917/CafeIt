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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private SpannableString s;

    // preferences
    private Context context;
    private SharedPreferences myPref;
    private SharedPreferences.Editor peditor;

    // UI elements
    private EditText id_view;
    private EditText pw_view;
    private EditText conf_pw_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Preference set up
        context = getApplicationContext();
        myPref = PreferenceManager.getDefaultSharedPreferences(context);
        peditor = myPref.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id_view = (EditText) findViewById(R.id.idField);
        pw_view = (EditText) findViewById(R.id.passwordField);
        conf_pw_view = (EditText) findViewById(R.id.passwordConfirmField);

        findViewById(R.id.createButton).setOnClickListener(this);
        findViewById(R.id.signupPaymentButton).setOnClickListener(this);

        setTitle("Sign up");
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
        String id = id_view.getText().toString();
        if (id != null && id.contains("@")) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean validatePW() {
        String pw1 = pw_view.getText().toString();
        String pw2 = conf_pw_view.getText().toString();
        if (pw1 != null && pw2 != null && pw1.equals(pw2)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.createButton) {
            if (validateID() && validatePW()) {
                peditor.putString("USER_ID", id_view.getText().toString());
                peditor.putString("USER_PW", pw_view.getText().toString());
                peditor.commit();
                setResult(RESULT_OK);
                finish();
            }
            else {
                Toast.makeText(this, "Invalid ID / PW", Toast.LENGTH_SHORT).show();
            }
        } else if (i == R.id.signupPaymentButton) {
            linkPayment();
            findViewById(R.id.createButton).setEnabled(true);
        }
    }
}