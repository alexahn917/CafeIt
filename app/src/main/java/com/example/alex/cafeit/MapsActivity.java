package com.example.alex.cafeit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.alex.cafeit.Cafe;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCafesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private List<Cafe> cafeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCafesDatabaseReference = mFirebaseDatabase.getReference().child("cafes");
//        cafeList = new ArrayList<>();

//        Log.d("DEBUG: ", "HEREHEHRHEHREHRHEHRHERHEHRHEHR111");
//        Cafe c = new Cafe (1, "Bird in hand", "11 E 33rd St, Baltimore, MD 21218", "8:00 am", "12:00 am", "Ice coffee", 5.0f, 1, 2, 39.327941f, -76.61661f);
//        mCafesDatabaseReference.push().setValue(c);
//        Log.d("DEBUG: ", "PUSH SUCCESS");


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Cafe cafe = dataSnapshot.getValue(Cafe.class);
//                Log.d("DEBUG: ", "LOADING CAFE DATA... - " + cafe.toString());
//                cafeList.add(cafe);


                LatLng cafeLatLng = new LatLng(cafe.latitude, cafe.longitude);
                String cafeName = cafe.name;
                Log.d("DEBUG: ", "ADD MARKER for - " + cafeName + " at: (" + cafe.latitude + "," + cafe.longitude + ")");
                mMap.addMarker(new MarkerOptions()
                        .position(cafeLatLng)
                        .title(cafeName)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                );
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mCafesDatabaseReference.addChildEventListener(mChildEventListener);

    }


    public List<Pair<LatLng, String>> getCafeList() {
        List<Pair<LatLng, String>> ret = new ArrayList<>();
        Pair<LatLng, String> cafe = new Pair<>(new LatLng(39.327946, -76.616616), "Bird in Hand");
        ret.add(cafe);
        return ret;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("DEBUG: ", "cafeList: " + cafeList);

        mMap = googleMap;

        // GREAT TUTORIAL: https://www.youtube.com/watch?v=CCZPUeY94MU  (language tho...)
        // TRACK CURRENT LOCATION: https://www.youtube.com/watch?v=HCwU4E43tfw

        // Set initial map view to be centered around current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 7));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
