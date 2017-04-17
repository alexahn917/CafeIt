package com.example.alex.cafeit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.cafeit.FavoritesFragment.OnListFragmentInteractionListener;
import com.example.alex.cafeit.dummy.DummyContent.Order;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFavoritesRecyclerViewAdapter extends RecyclerView.Adapter<MyFavoritesRecyclerViewAdapter.ViewHolder> {

    private final List<com.example.alex.cafeit.Order> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyFavoritesRecyclerViewAdapter(List<com.example.alex.cafeit.Order> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        System.out.println(holder.mItem);



        holder.cafeNameView.setText(holder.mItem.cafeName);
        holder.timeCostView.setText(Integer.toString(holder.mItem.remainingTime) +  " min"
                + "  |  $" + String.format("%.2f", holder.mItem.price));
        holder.menuOrderView.setText(holder.mItem.orderMenu);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                    Toast.makeText(v.getContext(), holder.mItem.cafeName, Toast.LENGTH_SHORT).show();
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
        public final TextView timeCostView;
        public final TextView menuOrderView;
        public com.example.alex.cafeit.Order mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cafeNameView = (TextView) view.findViewById(R.id.cafe_name);
            timeCostView = (TextView) view.findViewById(R.id.time_remaining);
            menuOrderView = (TextView) view.findViewById(R.id.order_items);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + cafeNameView.getText() + "'";
        }
    }
}
