package com.example.alex.cafeit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
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

public final class DistanceCalculator {

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyABgEV6B6lNgGSLXpogNzWnICK9NiN_CT0";
    private String myURL;

    private LatLng from;
    private LatLng to;

    public DistanceCalculator(LatLng from, LatLng to) {
        this.from = from;
        this.to = to;
        this.myURL = createUrl();
        parseJSON();
    }

    private String createUrl() {
        String urlOrigin = this.from.latitude + "," + this.from.longitude;
        String urlDestination = this.to.latitude + "," + this.to.longitude;
        String mode = "walking";
        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&mode=" + mode + "&alternatives=false" + "&key=" + GOOGLE_API_KEY;
    }

    public int parseJSON() {
        String jsonString = "";
        int seconds = -1;

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

                JSONObject json = new JSONObject(jsonString);
                JSONArray routes = json.getJSONArray("routes");
                if (routes.length() == 0) {
                    throw new JSONException("No routes exist");
                }
                JSONObject route = routes.getJSONObject(0);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                JSONObject durationObject = leg.getJSONObject("duration");
                String duration = durationObject.getString("text");
                seconds = durationObject.getInt("value");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return seconds;
    }


    public String parseJSONDistance() {
        String jsonString = "";
        int seconds = -1;
        String duration = "";

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

                JSONObject json = new JSONObject(jsonString);
                JSONArray routes = json.getJSONArray("routes");
                if (routes.length() == 0) {
                    throw new JSONException("No routes exist");
                }
                JSONObject route = routes.getJSONObject(0);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                JSONObject durationObject = leg.getJSONObject("distance");
                duration = durationObject.getString("text");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return duration;
    }


}
