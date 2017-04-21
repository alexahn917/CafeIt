package com.example.alex.cafeit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.app.Fragment;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CafeMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CafeMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CafeMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<MenuItem>> listDataChild;

    static final int ORDER_SUCCESS = 1;


    private OnFragmentInteractionListener mListener;

    public CafeMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CafeMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CafeMenuFragment newInstance(String param1, String param2) {
        CafeMenuFragment fragment = new CafeMenuFragment();
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
        View v = inflater.inflate(R.layout.fragment_cafe_menu, container, false);

//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


//        ActionBar abar = getSupportActionBar();
//        if (abar != null) {
//            abar.setDisplayHomeAsUpEnabled(true);
//        }

        expListView = (ExpandableListView) v.findViewById(R.id.cafeMenu);

        setup();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);




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


    private void setup() {

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        final int numHeaders = 3;
        final int numChildren = 4;
        for (int i = 0; i < numHeaders; i++) {
            listDataHeader.add(getString(R.string.menu_header) + i);
            List<MenuItem> childItems = new ArrayList<>();
            for (int j = 0; j < numChildren; j++) {
                MenuItem item = new MenuItem(true, getString(R.string.menu_item), 1,
                        getResources().getStringArray(R.array.sizes));
                childItems.add(item);
            }
            listDataChild.put(listDataHeader.get(i), childItems);
        }
    }
}
