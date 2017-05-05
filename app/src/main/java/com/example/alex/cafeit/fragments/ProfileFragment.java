package com.example.alex.cafeit.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.cafeit.AuthHandler;
import com.example.alex.cafeit.LoginActivity;
import com.example.alex.cafeit.MainActivity;
import com.example.alex.cafeit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment {
    private Button saveButton;
    private Button linkButton;
    private Context context;

    private EditText nameView;
    private EditText emailView;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        //MainActivity.updateCup();

        saveButton = (Button) v.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
        linkButton = (Button) v.findViewById(R.id.profilePaymentButton);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkPayment();
            }
        });

        nameView = (EditText) v.findViewById(R.id.nameField);
        emailView = (EditText) v.findViewById(R.id.emailField);
        updateView();
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void saveProfile(){
        createAndShowAlertDialog();
    }
    private void linkPayment(){
        Toast.makeText(getActivity().getApplicationContext(), "Sending you to outside payment API...", Toast.LENGTH_SHORT).show();
    }

    public void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Save changes?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateChanges();
                Toast.makeText(context, "Changes saved successfully.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                //setResult(OrderActivity.ORDER_SUCCESS);
                //finish();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context, "Changes discarded.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                //setResult(OrderActivity.ORDER_CANCEL);
                //finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updateView() {
        nameView.setText(LoginActivity.username);
        emailView.setText(LoginActivity.useremail);
    }

    public void updateChanges() {
        String userID = AuthHandler.getUid();
        mDatabase.child("users").child(userID).child("email").setValue(emailView.getText().toString());
        mDatabase.child("users").child(userID).child("username").setValue(nameView.getText().toString());
    }
}
