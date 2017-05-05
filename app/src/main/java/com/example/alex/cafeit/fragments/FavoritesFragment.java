package com.example.alex.cafeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import com.example.alex.cafeit.MainActivity;
import com.example.alex.cafeit.MyFavoritesRecyclerViewAdapter;
import com.example.alex.cafeit.MyHistoryRecyclerViewAdapter;
import com.example.alex.cafeit.Order;
import com.example.alex.cafeit.OrderActivity;
import com.example.alex.cafeit.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavoritesFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    protected Cursor cursor;
    protected MyFavoritesRecyclerViewAdapter mAdapter;
    public  RecyclerView mRecyclerView;

    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public FavoritesFragment() {
        setHasOptionsMenu(true);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavoritesFragment newInstance(int columnCount) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG: ", "onCreate Favorites Fragment");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG: ", "onCreate Favorites Fragment");

        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);
        //MainActivity.updateCup();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            List<Order> orderList = MainActivity.favorites;
            Log.d("DEBUG: ", "onCreateView Favorites Fragment" + orderList.toString());
            mAdapter = new MyFavoritesRecyclerViewAdapter(orderList, mListener);
            mRecyclerView.setAdapter(mAdapter);
            updateArray(mAdapter);

        }
        return view;
    }

    public void updateArray(MyFavoritesRecyclerViewAdapter mAdapter) {
        SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat to = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        cursor = MainActivity.dbAdapter.getFavorites();

        MainActivity.favorites.clear();
        if (cursor.moveToFirst())
            do {
                String date = "01/01/2017";
                try {
                    date = to.format(from.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                URL im_url;
                try {
                    im_url = new URL(cursor.getString(10));
                } catch (MalformedURLException e) {
                    im_url = null;
                }
                Order o =  new Order(cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        date, cursor.getInt(5), cursor.getString(6), cursor.getFloat(7),
                        cursor.getString(8), cursor.getString(9), im_url, cursor.getInt(11) != 0);
                o.cafeID = cursor.getString(12);
                MainActivity.favorites.add(0, o);  // puts in reverse order
            } while (cursor.moveToNext());

        mAdapter.notifyDataSetChanged();

    }


//    public List makeDummyOrders() {
//        ArrayList<Order> orders = new ArrayList<Order>();
//        orders.add(new Order("04/01/11:13:11", 2.50f, 3, "Daily Grind @ Brody", "Americano, Iced", "(L)"));
//        orders.add(new Order("04/01/11:13:11", 3.50f, 5, "Alkimia", "Latte, Hot", "(M)"));
//        orders.add(new Order("04/01/11:13:11", 3.00f, 4, "Bird in Hard", "Chai Tea Latte", "(M)"));
//        orders.add(new Order("04/01/11:13:11", 3.25f, 4, "Artifact Coffee", "Dirty Chai", "(M)"));
//        orders.add(new Order("04/01/11:13:11", 3.75f, 4, "One World Cafe", "Coldbrew", "(L)"));
//        return orders;
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //+ " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Order item, int pos);
    }

    public Context getFragContext(){
        return context;
    }
}
