package com.example.alex.cafeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import com.example.alex.cafeit.MyFavoritesRecyclerViewAdapter;
import com.example.alex.cafeit.Order;
import com.example.alex.cafeit.OrderActivity;
import com.example.alex.cafeit.R;

import java.util.ArrayList;
import java.util.List;

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

    private List<Order> orderList;
    private View view;
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

        view = inflater.inflate(R.layout.fragment_favorites_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            orderList = makeDummyOrders();
            Log.d("DEBUG: ", "onCreateView Favorites Fragment" + orderList.toString());
            recyclerView.setAdapter(new MyFavoritesRecyclerViewAdapter(orderList, mListener));

        }
        return view;
    }

    public List makeDummyOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();
        orders.add(new Order("04/01/11:13:11", 2.50f, 3, "Daily Grind @ Brody", "Americano, Iced (L)"));
        orders.add(new Order("04/01/11:13:11", 3.50f, 5, "Alkimia", "Latte, Hot (M)"));
        orders.add(new Order("04/01/11:13:11", 3.00f, 4, "Bird in Hard", "Chai Tea Latte (M)"));
        orders.add(new Order("04/01/11:13:11", 3.25f, 4, "Artifact Coffee", "Dirty Chai (M)"));
        orders.add(new Order("04/01/11:13:11", 3.75f, 4, "One World Cafe", "Coldbrew (L)"));
        return orders;
    }


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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_favorites, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_favorite) {
            Intent i = new Intent(getActivity(), OrderActivity.class);
            startActivity(i);
        }
        else if (id == R.id.delete_favorite) {
            Toast.makeText(context, "Select items to delete.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Order item, int pos);
    }
}
