package com.example.alex.cafeit.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.net.Uri;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alex.cafeit.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CafeProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CafeProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CafeProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button saveButton;
    private Button linkButton;

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cafe_profile, container, false);

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

        bulkSetOnclick();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        Toast.makeText(getActivity().getApplicationContext(), "Saved successfully!",
                Toast.LENGTH_SHORT).show();

    }
    private void linkPayment(){
        Toast.makeText(getActivity().getApplicationContext(), "Sending you to outside payment API...", Toast.LENGTH_SHORT).show();
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
}
