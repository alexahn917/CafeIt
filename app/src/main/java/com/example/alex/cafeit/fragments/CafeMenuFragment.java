package com.example.alex.cafeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.app.Fragment;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.cafeit.AuthHandler;
import com.example.alex.cafeit.Cafe;
import com.example.alex.cafeit.CafeExpandableListAdapter;
import com.example.alex.cafeit.CafeMenuEditPasser;
import com.example.alex.cafeit.CafeMenuItem;
import com.example.alex.cafeit.CafeMenuItemActivity;
import com.example.alex.cafeit.MenuItem;
import com.example.alex.cafeit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CafeMenuFragment extends Fragment {

    CafeExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<CafeMenuItem>> listDataChild = new HashMap<>();
        static final int ORDER_SUCCESS = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private List<CafeMenuItem> menuList = new ArrayList<>();
    public static int count = 0;

    private ImageView prof_pic;
    private OnFragmentInteractionListener mListener;

    public CafeMenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cafe_menu, container, false);
        expListView = (ExpandableListView) v.findViewById(R.id.cafeMenu);
        populateMenuList();
        prof_pic = (ImageView) v.findViewById(R.id.CafeProfPic_menu);
        return v;
    }

    public void setPicture(Bitmap bm) {
        prof_pic.setImageBitmap(bm);
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
        void onFragmentInteraction(Uri uri);
    }

    private void setup() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        final int numHeaders = 3;
        final int numChildren = 4;
        for (int i = 0; i < numHeaders; i++) {
            listDataHeader.add(getString(R.string.menu_header) + i);
            List<CafeMenuItem> childItems = new ArrayList<>();
            for (int j = 0; j < numChildren; j++) {
                CafeMenuItem item = new CafeMenuItem("itemname", 1, true, 1, 2.0f, 0f, 0f, 1);
                childItems.add(item);
            }
            listDataChild.put(listDataHeader.get(i), childItems);
        }
    }

    private void populateMenuList(){
        Log.d("entered" , "hi");

        mDatabase.child("cafeMenu").child(AuthHandler.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuList.clear();
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    CafeMenuItem item = postSnapshot.getValue(CafeMenuItem.class);
//                    Log.d("item ****************", item.getName() + " / " + item.isOneSize());
                    menuList.add(item);
                }
                Log.d("making", "yes");

                refreshList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Log.d("entered pop method" , "hi");

    }

    public void makeList(){
        listDataHeader.clear();
        listDataHeader.add("Espresso Drinks");
        listDataHeader.add("Non-Espresso Drinks");
        listDataHeader.add("Ice Blended");
        listDataHeader.add("Food & Snacks");

        List<CafeMenuItem> espressoMenu = new ArrayList<>();
        List<CafeMenuItem> nonEspressoMenu = new ArrayList<>();
        List<CafeMenuItem> iceBlendedMenu = new ArrayList<>();
        List<CafeMenuItem> foodMenu = new ArrayList<>();

        for(CafeMenuItem i : menuList){
            switch(i.getCategory()){
                case 0: espressoMenu.add(i); break;
                case 1: nonEspressoMenu.add(i); break;
                case 2: iceBlendedMenu.add(i); break;
                case 3: foodMenu.add(i); break;
            }
        }
        listDataChild.clear();
        listDataChild.put(listDataHeader.get(0), espressoMenu);
        listDataChild.put(listDataHeader.get(1), nonEspressoMenu);
        listDataChild.put(listDataHeader.get(2), iceBlendedMenu);
        listDataChild.put(listDataHeader.get(3), foodMenu);
    }

    public void refreshList(){
        Log.d("entered refresh" , "good");
        Log.d("refreshing", " good");

        makeList();

        listAdapter = new CafeExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        listAdapter.notifyDataSetChanged();

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                    Toast.makeText(getActivity(), ((CafeMenuItem)(listAdapter.getChild(groupPosition, childPosition))).getName(), Toast.LENGTH_SHORT).show();

                CafeMenuEditPasser.item = (CafeMenuItem)(listAdapter.getChild(groupPosition, childPosition));

                Intent i = new Intent(getActivity(), CafeMenuItemActivity.class);
                startActivity(i);
                return true;
            }
        });
        count++;
    }

}
