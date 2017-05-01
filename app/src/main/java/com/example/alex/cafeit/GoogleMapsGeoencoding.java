package com.example.alex.cafeit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.alex.cafeit.Cafe;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by jinyoungsohn on 4/29/17.
 */

public final class GoogleMapsGeoencoding {

    private static final String DIRECTION_URL_API = "http://maps.google.com/maps/api/geocode/json?address=";
    private static final String GOOGLE_API_KEY = "AIzaSyABgEV6B6lNgGSLXpogNzWnICK9NiN_CT0";
    private static final String GOOGLE_GEOENCODING_KEY = "AIzaSyB5Y6evumo9MAo-7UkiFTQjHmDcLpkbWGU";

    private String myURL;

    private String address;

    public GoogleMapsGeoencoding(String address) {
        this.address = address;
        this.myURL = createUrl();
        parseJSON();
    }

    private String createUrl() {
        return DIRECTION_URL_API + this.address + "&sensor=false";
    }

    public LatLng parseJSON() {
        String jsonString = "";
        int seconds = -1;
        double lat = 0;
        double lng = 0;

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {

                URL url = new URL(this.myURL);
                StringBuilder sb = new StringBuilder();
                HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        sb.append(strLine);
                    }
                    input.close();
                }
                jsonString = sb.toString();
                Log.d("DEBUG: ", "parseJSON: jsonString - " + jsonString);

                JSONObject json = new JSONObject(jsonString);

                JSONArray results = json.getJSONArray("results");
                JSONObject result = results.getJSONObject(0);
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                lat = location.getDouble("lat");
                lng = location.getDouble("lng");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return new LatLng(lat, lng);
    }


}
