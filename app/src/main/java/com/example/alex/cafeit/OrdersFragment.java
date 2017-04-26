package com.example.alex.cafeit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OrdersFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    List<Order> orders;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrdersFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OrdersFragment newInstance(int columnCount) {
        OrdersFragment fragment = new OrdersFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            orders = makeDummyOrders();
            recyclerView.setAdapter(new MyOrdersRecyclerViewAdapter(orders, mListener));
        }
        return view;
    }

    public List makeDummyOrders() {
        Order a = new Order("04/11/17", 2.50f, 3, "Daily Grind @ Brody", "Americano, Iced (L)");
        a.name = "Daniel";
        a.note = "Extra ice please";
        Order b = new Order("04/07/17", 3.50f, 5, "Alkimia", "Latte, Hot (M)");
        b.name = "Alex";
        b.note = "No ice please";
        Order c = new Order("04/06/17", 3.00f, 4, "Bird in Hard", "Chai Tea Latte (M)");
        c.name = "Anthony";
        c.note = "I'd like it less sweet";
        Order d = new Order("04/06/17", 3.25f, 4, "Artifact Coffee", "Dirty Chai (M)");
        d.name = "Chris";
        d.note = "Extra ice";
        Order e = new Order("04/04/17", 3.75f, 4, "One World Cafe", "Coldbrew (L)");
        e.name = "David";
        e.note = "n/a";
        ArrayList<Order> orders = new ArrayList<Order>();
        orders.add(a);
        orders.add(b);
        orders.add(c);
        orders.add(d);
        orders.add(e);
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
