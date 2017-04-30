package com.example.alex.cafeit.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.*;
import android.widget.Toast;

import com.example.alex.cafeit.Cafe;
import com.example.alex.cafeit.CurrentOrder;
import com.example.alex.cafeit.DistanceCalculator;
import com.example.alex.cafeit.DistanceCalculatorListener;
import com.example.alex.cafeit.MapsActivity;
import com.example.alex.cafeit.MyCafesRecyclerViewAdapter;
import com.example.alex.cafeit.NewCafePusher;
import com.example.alex.cafeit.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CafesListFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private List<Cafe> cafeList = new ArrayList<>();
    private Context context;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public CafesListFragment() {
        setHasOptionsMenu(true);
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    // TODO: Customize parameter initialization
    public static CafesListFragment newInstance(int columnCount) {
        CafesListFragment fragment = new CafesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
        context = getContext();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafes_list, container, false);
        populateCafeList();
        Log.d("cafelist size", cafeList.size()+"");
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //cafeList = makeDummyCafes();
            recyclerView.setAdapter(new MyCafesRecyclerViewAdapter(cafeList, mListener));

        }

        return view;
    }

    public List makeDummyCafes() {
        cafeList = new ArrayList<>();

//        cafeList.add(new Cafe(NewCafePusher.cid++, "Daily grind", "brody b", "9AM",
//                "11pm", "Cafe mocha", 1.6f, 1, 5));
//        cafeList.add(new Cafe(NewCafePusher.cid++, "Alkemia", "gilman", "9AM",
//                "5pm", "Americano", 3.4f, 1, 4));
//        cafeList.add(new Cafe(NewCafePusher.cid++, "Bird in hand", "Nine east", "9AM",
//                "6pm", "Drip coffee", 4.1f, 1, 3));
//        cafeList.add(new Cafe(NewCafePusher.cid++, "Carma's Cafe", "Near campus", "9AM",
//                "10pm", "Frappucino", 4.2f, 1, 3));
        cafeList.add(new Cafe(NewCafePusher.cid++, "my new cafe", "somewhere over the rainbow", "11AM", "11PM", "unicorn poop", 4.5f, 1, 7, "1000 somewhere street", 39.334773f, -76.620726f));
        cafeList.add(new NewCafePusher().getCafe());
        return cafeList;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            //mListener.onListFragmentInteraction(Cafe item);
        } else {
            throw new RuntimeException(context.toString()
            + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cafes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_distance) {
            Toast.makeText(context, "Sorting by distance...", Toast.LENGTH_SHORT).show();
            sortByDistance();
        }
        else if (id == R.id.sort_wait) {
            Toast.makeText(context, "Sorted by waiting time", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.sort_rating) {
            Toast.makeText(context, "Sorted by ratings", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.google_maps) {
            Toast.makeText(context, "Opening Maps View...", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, MapsActivity.class);
            i.putExtra("latitude", mLastLocation.getLatitude());
            i.putExtra("longitude", mLastLocation.getLongitude());
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Cafe item, int pos);
    }

    public void populateCafeList(){
        mDatabase.child("cafes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Cafe post = postSnapshot.getValue(Cafe.class);
                    cafeList.add(post);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    public void sortByDistance() {
        // Get current location
//        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
//        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
        Location location = mLastLocation;

        // current lat & long
        LatLng currLoc = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("DEBUG: ", "current location - (" + currLoc.latitude + "," + currLoc.longitude + ")");

        List<Pair<Cafe, Integer>> distances = new ArrayList<>();
        for (Cafe cafe : cafeList) {
            DistanceCalculator dc = new DistanceCalculator(currLoc, new LatLng(cafe.latitude, cafe.longitude));
            int time = dc.parseJSON();
            Log.d("DEBUG: ", "sortByDistance: " + cafe.toString() + ", timeTo: " + time);
            distances.add(new Pair<Cafe, Integer>(cafe, time));
        }

        Collections.sort(distances, new Comparator<Pair<Cafe, Integer>>() {
            @Override
            public int compare(Pair<Cafe, Integer> lhs, Pair<Cafe, Integer> rhs) {
                return lhs.second - rhs.second;
            }
        });

        List<Cafe> ret = new ArrayList<>();
        for (Pair<Cafe, Integer> c : distances) {
            Log.d("DEBUG: ", "SORTED : " + c.first.toString() + ", timeTo: " + c.second);
            ret.add(c.first);
        }
        cafeList = ret;

        Toast.makeText(context, "Sorted by distance", Toast.LENGTH_SHORT).show();
        // After Sorting - update cafeList
        recyclerView.setAdapter(new MyCafesRecyclerViewAdapter(cafeList, mListener));
    }

}
