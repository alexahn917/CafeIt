package com.example.alex.cafeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private Button createButton;
    private Button paymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMain();
            }
        });

        paymentButton = (Button) findViewById(R.id.signupPaymentButton);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkPayment();
            }
        });
    }

    private void launchMain(){
        Toast.makeText(this, "Welcome to Cafeit!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void linkPayment(){
        Toast.makeText(this, "Sending you to outside payment API...", Toast.LENGTH_SHORT).show();
    }
}
