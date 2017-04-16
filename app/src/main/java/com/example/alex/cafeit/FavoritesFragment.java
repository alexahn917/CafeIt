package com.example.alex.cafeit;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.alex.cafeit.dummy.DummyContent;
import com.example.alex.cafeit.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private ListView FavoritesListView;
    protected static ArrayList<Order> FavoriteOrderItems;
    protected static FavoriteListAdapter adapter;
    private Context context;
    private View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoritesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG: ", "FavoritesFragment onCreateView 1");

        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_favorites, container, false);

        Log.d("DEBUG: ", "FavoritesFragment onCreateView 2");

        FavoritesListView = (ListView) rootView.findViewById(R.id.favorites_list);

        FavoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = FavoritesListView.getItemAtPosition(position);

            }
        });

        Log.d("DEBUG: ", "FavoritesFragment onCreateView 3");

        return rootView;
    }

    @Override
    public void onResume() {
        Log.d("DEBUG: ", "FavoritesFragment onResume 1");


        super.onResume();

        FavoriteOrderItems = populateData();

        Log.d("DEBUG: ", "FavoritesFragment onResume 2");


        adapter = new FavoriteListAdapter(getActivity(), R.layout.fragment_favorites, FavoriteOrderItems);
        FavoritesListView.setAdapter(adapter);

        Log.d("DEBUG: ", "FavoritesFragment onResume 3");


        getActivity().setTitle(R.string.title_favorites);

        Log.d("DEBUG: ", "FavoritesFragment onResume 4");

    }

    public ArrayList<Order> populateData() {
        ArrayList<Order> orders = new ArrayList<Order>();

        orders.add(new Order("04/01/11:13:11", 2.50f, 3, "Daily Grind @ Brody", "Americano, Iced (L)"));
        orders.add(new Order("04/01/11:13:11", 3.50f, 5, "Alkimia", "Latte, Hot (M)"));
        orders.add(new Order("04/01/11:13:11", 3.00f, 4, "Bird in Hard", "Chai Tea Latte (M)"));

        return orders;
    }


}
