package com.example.alex.cafeit;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public interface DistanceCalculatorListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Cafe> sortedCafes);
}
