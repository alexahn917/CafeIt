package com.example.alex.cafeit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity
        implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private static final int SIGNUP_USER_REQUEST = 1;
    private static final int SIGNUP_CAFE_REQUEST = 2;

    // preferences
    private Context context;
    private SharedPreferences myPref;
    private SharedPreferences.Editor peditor;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    // Firebase
    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private boolean signInAsCafe;
    private boolean signUpAsCafe;

    public static String username;
    public static String useremail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Preference set up
        context = getApplicationContext();
        myPref = PreferenceManager.getDefaultSharedPreferences(context);
        peditor = myPref.edit();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        // Activate Buttons
        findViewById(R.id.email_log_in_button).setOnClickListener(this);
        findViewById(R.id.email_sign_up_user_button).setOnClickListener(this);
        findViewById(R.id.email_sign_up_cafe_button).setOnClickListener(this);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void launchMainActivity() {
//        Toast.makeText(context, signInAsCafe + " given", Toast.LENGTH_SHORT).show();
        if(signInAsCafe) {
            Intent intent = new Intent(this, CafeMainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void setTypeFace(Button v) {
        //Typeface font = Typeface.createFromAsset(getResources().getAssets(), "Bodoni 72.ttc");
        //v.setTypeface(font);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User loginUser = dataSnapshot.getValue(User.class);
                                    signInAsCafe = loginUser.isCafe;
                                    LoginActivity.username = loginUser.username;
                                    LoginActivity.useremail = loginUser.email;
                                    launchMainActivity();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });

                            mDatabase.child(user.getUid());
//                            Toast.makeText(context, "signInAsCafe: " + signInAsCafe, Toast.LENGTH_SHORT).show();
//                            launchMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void createUserAccount(String email, String password) {
        //showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Successfully signed up to CafeIt!",
                                    Toast.LENGTH_SHORT).show();
                            writeNewUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createCafeAccount(String email, String password) {
        //showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Successfully signed up to CafeIt!",
                                    Toast.LENGTH_SHORT).show();
                            writeNewCafe();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeNewUser() {
        String userId = mAuth.getCurrentUser().getUid();
        String name = myPref.getString("USER_NAME", "");
        String email = myPref.getString("USER_ID", "");
        User user = new User(name, email, signUpAsCafe);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private void writeNewCafe() {
        String cafeId = mAuth.getCurrentUser().getUid();
        String name = myPref.getString("CAFE_NAME", "");
        String email = myPref.getString("CAFE_ID", "");
        User user = new User(name, email, signUpAsCafe);
        mDatabase.child("users").child(cafeId).setValue(user);

        Cafe new_cafe = new Cafe(
                myPref.getString("CAFE_NAME", ""),
                myPref.getString("CAFE_STARTHOUR", ""),
                myPref.getString("CAFE_ENDHOUR", ""),
                myPref.getString("CAFE_BESTMENU", ""),
                myPref.getFloat("CAFE_RATING", 0.0f),
                myPref.getString("CAFE_ADDRESS", ""),
                myPref.getInt("CAFE_HASWIFI", 0),
                myPref.getFloat("CAFE_WAITTIME", 0.0f),
                myPref.getFloat("CAFE_LATITUDE", 0.0f),
                myPref.getFloat("CAFE_LONGITUDE", 0.0f));
        mDatabase.child("cafes").child(cafeId).setValue(new_cafe);
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Required.");
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Required.");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == SIGNUP_USER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String USER_ID = myPref.getString("USER_ID", "");
                String USER_PW = myPref.getString("USER_PW", "");
                createUserAccount(USER_ID, USER_PW);
                signUpAsCafe = false;
            }
        }
        else if (requestCode == SIGNUP_CAFE_REQUEST) {
            if (resultCode == RESULT_OK) {
                String CAFE_ID = myPref.getString("CAFE_ID", "");
                String CAFE_PW = myPref.getString("CAFE_PW", "");
                createCafeAccount(CAFE_ID, CAFE_PW);
                signUpAsCafe = true;
            }
        }
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.email_log_in_button) {
            signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());
        } else if (i == R.id.email_sign_up_user_button) {
            peditor.putString("USER_ID", "");
            peditor.putString("USER_PW", "");
            peditor.putString("USER_NAME", "");
            peditor.commit();
            Intent intent = new Intent(this, SignUpUserActivity.class);
            startActivityForResult(intent, SIGNUP_USER_REQUEST);
        } else if (i == R.id.email_sign_up_cafe_button) {
            peditor.putString("CAFE_ID", "");
            peditor.putString("CAFE_PW", "");
            peditor.putString("CAFE_NAME", "");
            peditor.putString("CAFE_LOCATION", "");
            peditor.putString("CAFE_STARTHOUR", "");
            peditor.putString("CAFE_ENDHOUR", "");
            peditor.putString("CAFE_BESTMENU", "");
            peditor.putString("CAFE_RATING", "");
            peditor.putString("CAFE_HASWIFI", "");
            peditor.putString("CAFE_WAITTIME", "");
            peditor.putString("CAFE_ADDRESS", "");
            peditor.putString("CAFE_LATITUDE", "");
            peditor.putString("CAFE_LONGITUDE", "");
            peditor.commit();
            Intent intent = new Intent(this, SignUpCafeActivity.class);
            startActivityForResult(intent, SIGNUP_CAFE_REQUEST);
        }
    }
}