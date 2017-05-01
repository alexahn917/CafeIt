package com.example.alex.cafeit.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.cafeit.MainActivity;
import com.example.alex.cafeit.MyHistoryRecyclerViewAdapter;
import com.example.alex.cafeit.Order;
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
public class HistoryFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private static OnListFragmentInteractionListener mListener;
    protected static Cursor cursor;
    protected static MyHistoryRecyclerViewAdapter mAdapter;
    private  RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryFragment() {
        setHasOptionsMenu(true);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HistoryFragment newInstance(int columnCount) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        mAdapter = new MyHistoryRecyclerViewAdapter(MainActivity.history, mListener);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            orders = makeDummyOrders();
            mRecyclerView.setAdapter(new MyHistoryRecyclerViewAdapter(MainActivity.history, mListener));
        }
        return view;
    }

    public void updateArray() {
        SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat to = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        cursor = MainActivity.dbAdapter.getAllItems();

        MainActivity.history.clear();
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
                MainActivity.history.add(0, o);  // puts in reverse order
            } while (cursor.moveToNext());

        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public List makeDummyOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();
        orders.add(new Order("04/11/17", 2.50f, 3, "Daily Grind @ Brody", "Americano, Iced", "(L)"));
        orders.add(new Order("04/07/17", 3.50f, 5, "Alkimia", "Latte, Hot", "(M)"));
        orders.add(new Order("04/06/17", 3.00f, 4, "Bird in Hard", "Chai Tea Latte", "(M)"));
        orders.add(new Order("04/06/17", 3.25f, 4, "Artifact Coffee", "Dirty Chai", "(M)"));
        orders.add(new Order("04/04/17", 3.75f, 4, "One World Cafe", "Coldbrew", "(L)"));
        return orders;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(Order item);
    }
}
