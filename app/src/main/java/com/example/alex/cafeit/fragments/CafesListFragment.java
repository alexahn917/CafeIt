package com.example.alex.cafeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.alex.cafeit.DistanceCalculator;
import com.example.alex.cafeit.MapsActivity;
import com.example.alex.cafeit.MyCafesRecyclerViewAdapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private static Context context;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private String TAG = "DEBUG:CafesListFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public CafesListFragment() {
        setHasOptionsMenu(true);
    }

    public void onStart() {
        Log.d(TAG, "onStart: ");
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        Log.d(TAG, "onStop: ");
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
        Log.d(TAG, "onCreate: ");
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
        Log.d(TAG, "onCreateView: ");
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

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
            + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.menu_cafes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
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
        Log.d(TAG, "onConnected: ");
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // GET DISTANCES AND PUT THEM IN CAFE CLASS TO DISPLAY AGAIN
        if (mLastLocation == null) { // If no location (emulator) -> set default to JHU
            Location defaultLocation = new Location("");
            defaultLocation.setLatitude(39.329148d);
            defaultLocation.setLongitude(-76.618461d);
            mLastLocation = defaultLocation;
        }

        Location location = mLastLocation;
        LatLng currLoc = new LatLng(location.getLatitude(), location.getLongitude());
        for (Cafe c : cafeList) {
            DistanceCalculator dc = new DistanceCalculator(currLoc, new LatLng(c.latitude, c.longitude));
            String distance = dc.parseJSONDistance();
            c.distance = distance;
        }

        recyclerView.setAdapter(new MyCafesRecyclerViewAdapter(cafeList, mListener));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");

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
        Log.d(TAG, "populateCafeList: ");
        mDatabase.child("cafes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                cafeList.clear();

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
        Log.d(TAG, "sortByDistance: ");
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

    public static Context getFragConext() {
        return context;
    }
}
