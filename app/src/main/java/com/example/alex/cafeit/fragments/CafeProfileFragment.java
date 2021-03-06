package com.example.alex.cafeit.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.net.Uri;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alex.cafeit.AuthHandler;
import com.example.alex.cafeit.Cafe;
import com.example.alex.cafeit.CafeMainActivity;
import com.example.alex.cafeit.LoginActivity;
import com.example.alex.cafeit.R;
import com.example.alex.cafeit.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CafeProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CafeProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CafeProfileFragment extends Fragment {
    private final int PICK_PIC = 1;

    private static final String TAG = "CafeProfileFragment";


    CafeMainActivity parent;

    private OnFragmentInteractionListener mListener;

    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private Button saveButton;
    private Button linkButton;

    private ImageView prof_pic;

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
    private Context context;
    private EditText monStart, monEnd, tueStart, tueEnd, wedStart, wedEnd,
            thuStart, thuEnd, friStart, friEnd, satStart, satEnd, sunStart, sunEnd;


    public CafeProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CafeProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CafeProfileFragment newInstance(String param1, String param2) {
        CafeProfileFragment fragment = new CafeProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.fragment_cafe_profile, container, false);

        parent = (CafeMainActivity) getActivity();

        prof_pic = (ImageView) v.findViewById(R.id.CafeProfilePic);
        prof_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        saveButton = (Button) v.findViewById(R.id.cafeProfileSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
        linkButton = (Button) v.findViewById(R.id.cafeProfilePaymentButton);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkPayment();
            }
        });


        monStart = (EditText) v.findViewById(R.id.mondayStartTime);
        tueStart = (EditText) v.findViewById(R.id.tuesdayStartTime);
        wedStart = (EditText) v.findViewById(R.id.wednesdayStartTime);
        thuStart = (EditText) v.findViewById(R.id.thursdayStartTime);
        friStart = (EditText) v.findViewById(R.id.fridayStartTime);
        satStart = (EditText) v.findViewById(R.id.saturdayStartTime);
        sunStart = (EditText) v.findViewById(R.id.sundayStartTime);

        monEnd = (EditText) v.findViewById(R.id.mondayEndTime);
        tueEnd = (EditText) v.findViewById(R.id.tuesdayEndTime);
        wedEnd = (EditText) v.findViewById(R.id.wednesdayEndTime);
        thuEnd = (EditText) v.findViewById(R.id.thursdayEndTime);
        friEnd = (EditText) v.findViewById(R.id.fridayEndTime);
        satEnd = (EditText) v.findViewById(R.id.saturdayEndTime);
        sunEnd = (EditText) v.findViewById(R.id.sundayEndTime);

        cafe_email_view = (EditText) v.findViewById(R.id.idField);
        cafe_pw_view = (EditText) v.findViewById(R.id.passwordField);
        cafe_conf_pw_view = (EditText) v.findViewById(R.id.passwordConfirmField);
        cafe_name_view = (EditText) v.findViewById(R.id.nameField);
        cafe_bestmenu_view = (EditText) v.findViewById(R.id.bestMenuField);
        cafe_haswifi_switch = (Switch) v.findViewById(R.id.hasWifiSwitch);
        cafe_address1_view = (EditText) v.findViewById(R.id.addressInput1);
        cafe_address2_view = (EditText) v.findViewById(R.id.addressInput2);
        cafe_state_view = (EditText) v.findViewById(R.id.state);
        cafe_zipcode_view = (EditText) v.findViewById(R.id.zipcode);
        if (CafeMainActivity.prof_bitmap != null) {
            prof_pic.setImageBitmap(CafeMainActivity.prof_bitmap);
            prof_pic.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        }
        populateFB();
        bulkSetOnclick();

        return v;
    }

    public void populateFB() {
        String id = LoginActivity.userId;

        mDatabase.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User current_user = dataSnapshot.getValue(User.class);
                populateData(current_user);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mDatabase.child("cafes").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cafe current_cafe = dataSnapshot.getValue(Cafe.class);
                populateData(current_cafe);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void populateData(User user) {
        cafe_email_view.setText(user.email);
        cafe_name_view.setText(user.username);
    }

    public void populateData(Cafe cafe) {
        if (cafe != null) {
            cafe_bestmenu_view.setText(cafe.bestMenu);
            if (cafe.hasWifi == 1) {
                cafe_haswifi_switch.setChecked(true);
            } else {
                cafe_haswifi_switch.setChecked(false);
            }
            cafe_address1_view.setText(cafe.address);
        }
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PIC && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Log.e(TAG, "Error: Unable to find image gallery.");
                return;
            }
            InputStream inputStream;
            try {
                inputStream = parent.getContentResolver().openInputStream(data.getData());
                final Bitmap bm = BitmapFactory.decodeStream(inputStream);
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bm != null) {
                    CafeMainActivity.prof_bitmap = bm;
                    SharedPreferences myPrefs = PreferenceManager.
                            getDefaultSharedPreferences(parent.getApplicationContext());

                    SharedPreferences.Editor peditor = myPrefs.edit();
                    String im_path = saveToInternalStorage(bm);
                    ((CafeMainActivity) getActivity()).prof_abs_path = im_path;
                    peditor.putString(CafeMainActivity.PROF_PIC_FNAME, im_path);
                    peditor.apply();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference().
                            child(CafeMainActivity.PROF_DIR).child(CafeMainActivity.PROF_PIC_FNAME);

                    UploadTask upload = storageRef.putFile(Uri.fromFile(new File(im_path, CafeMainActivity.PROF_PIC_FNAME)));
                    upload.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Profile pic upload failure. Check your Internet" +
                                    " connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    prof_pic.setImageBitmap(bm);
                    prof_pic.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(parent.getApplicationContext());
        File directory = cw.getDir(CafeMainActivity.PROF_DIR, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, CafeMainActivity.PROF_PIC_FNAME);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return directory.getAbsolutePath();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context, "Changes discarded.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void bulkSetOnclick(){
        customSetOnclick(monStart);
        customSetOnclick(tueStart);
        customSetOnclick(wedStart);
        customSetOnclick(thuStart);
        customSetOnclick(friStart);
        customSetOnclick(satStart);
        customSetOnclick(sunStart);
        customSetOnclick(monEnd);
        customSetOnclick(tueEnd);
        customSetOnclick(wedEnd);
        customSetOnclick(thuEnd);
        customSetOnclick(friEnd);
        customSetOnclick(satEnd);
        customSetOnclick(sunEnd);
    }

    private void customSetOnclick(final EditText etxt){
        etxt.setFocusable(false);
        etxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
                String s = etxt.getText().toString();
                int myhour = Integer.parseInt(s.substring(0,s.indexOf(':')));
                int mymin = Integer.parseInt(s.substring(s.indexOf(':') + 1));

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String h, m;
                        if(selectedHour < 10) {
                            h = "0" + selectedHour;
                        } else {
                            h = selectedHour + "";
                        }
                        if (selectedMinute < 10){
                            m = "0" + selectedMinute;
                        } else {
                            m = selectedMinute + "";
                        }
                        etxt.setText( h + ":" + m);
                    }
                }, myhour, mymin, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
//                String s = monEnd.getText().toString();
//                mTimePicker.updateTime(Integer.parseInt(s.substring(0,s.indexOf(':'))), Integer.parseInt(s.substring(s.indexOf(':') + 1)));
                mTimePicker.show();
            }
        });
    }

    public void updateChanges() {
        String userID = AuthHandler.getUid();
        mDatabase.child("users").child(userID).child("email").setValue(cafe_email_view.getText().toString());
        mDatabase.child("users").child(userID).child("username").setValue(cafe_name_view.getText().toString());
        mDatabase.child("cafes").child(userID).child("bestMenu").setValue(cafe_bestmenu_view.getText().toString());
        mDatabase.child("cafes").child(userID).child("address").setValue(cafe_address1_view.getText().toString());
        boolean hasWifi = cafe_haswifi_switch.isChecked();
        if (hasWifi){
            mDatabase.child("cafes").child(userID).child("hasWifi").setValue(1);
        } else {
            mDatabase.child("cafes").child(userID).child("hasWifi").setValue(0);
        }
    }
}
