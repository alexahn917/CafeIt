package com.example.alex.cafeit;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CafesListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private List<Cafe> cafeList;
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public CafesListFragment() {
        setHasOptionsMenu(true);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafes_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            cafeList = makeDummyCafes();
            recyclerView.setAdapter(new MyCafesRecyclerViewAdapter(cafeList, mListener));

        }
        return view;
    }

    public List makeDummyCafes() {
        cafeList = new ArrayList<>();
        cafeList.add(new Cafe(1, "Daily grind", "brody b", "9AM",
                "11pm", "Cafe mocha", 1.6f, 1, 5));
        cafeList.add(new Cafe(2, "Alkemia", "gilman", "9AM",
                "5pm", "Americano", 3.4f, 1, 4));
        cafeList.add(new Cafe(3, "Bird in hand", "Nine east", "9AM",
                "6pm", "Drip coffee", 4.1f, 1, 3));
        cafeList.add(new Cafe(4, "Carma's Cafe", "Near campus", "9AM",
                "10pm", "Frappucino", 4.2f, 1, 3));
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
            Toast.makeText(context, "Sorted by distance", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.sort_wait) {
            Toast.makeText(context, "Sorted by waiting time", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.sort_rating) {
            Toast.makeText(context, "Sorted by ratings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
        void onListFragmentInteraction(Cafe item);
    }

}
