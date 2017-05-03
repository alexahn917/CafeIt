package com.example.alex.cafeit;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.alex.cafeit.fragments.CafesListFragment;
import com.example.alex.cafeit.fragments.CafesListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCafesRecyclerViewAdapter extends RecyclerView.Adapter<MyCafesRecyclerViewAdapter.ViewHolder> {

    private final List<Cafe> mValues;
    private final OnListFragmentInteractionListener mListener;
    private String TAG = "MyCafesRecyclerViewAdap";

    public MyCafesRecyclerViewAdapter(List<Cafe> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cafes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");

        holder.mItem = mValues.get(position);
        //System.out.println(holder.mItem);

        holder.cafeNameView.setText(TypefaceSpan.getSpannableString(holder.mItem.name, CafesListFragment.getFragConext()));
        holder.bestMenuView.setText(holder.mItem.bestMenu);
        holder.ratingView.setRating(holder.mItem.rating);
        String hasWifi;
        if(holder.mItem.hasWifi == 1) {
            hasWifi = "Yes";
        }
        else {
            hasWifi = "No";
        }
        holder.wifiView.setText(hasWifi);
        holder.waitView.setText(holder.mItem.waitTime + " minutes");

        //calculate distance from the user to the cafe
        String distance = "0.5 mi";
//        holder.cafeDistanceView.setText(distance);
        holder.cafeDistanceView.setText(holder.mItem.distance);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, 0);
//                    Toast.makeText(v.getContext(), holder.mItem.name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView cafeNameView;
        public final TextView cafeDistanceView;
        public final TextView bestMenuView;
        public final RatingBar ratingView;
        public final TextView wifiView;
        public final TextView waitView;
        public Cafe mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cafeNameView = (TextView) view.findViewById(R.id.cafe_name);
            cafeDistanceView = (TextView) view.findViewById(R.id.cafe_distance);
            bestMenuView = (TextView) view.findViewById(R.id.best_menu);
            ratingView = (RatingBar) view.findViewById(R.id.rating);
            wifiView = (TextView) view.findViewById(R.id.wifi);
            waitView = (TextView) view.findViewById(R.id.wait);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + cafeNameView.getText() + "'";
        }
    }
}
