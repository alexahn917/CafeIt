package com.example.alex.cafeit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.cafeit.HistoryFragment.OnListFragmentInteractionListener;
import com.example.alex.cafeit.Order;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<Order> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyHistoryRecyclerViewAdapter(List<Order> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.dateView.setText(mValues.get(position).orderTime);
        holder.costView.setText("$" + String.format("%.2f", mValues.get(position).price));
        holder.cafeNameView.setText(mValues.get(position).cafeName);
        holder.orderMenuView.setText(mValues.get(position).orderMenu);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
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
//        public final ImageView cafeLogoView;
        public final TextView dateView;
        public final TextView costView;
        public final TextView cafeNameView;
        public final TextView orderMenuView;
        public com.example.alex.cafeit.Order mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            cafeLogoView = (ImageView) view.findViewById(R.id.cafe_logo);
            dateView = (TextView) view.findViewById(R.id.date);
            costView = (TextView) view.findViewById(R.id.cost);
            cafeNameView = (TextView) view.findViewById(R.id.cafeName);
            orderMenuView = (TextView) view.findViewById(R.id.menu);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + cafeNameView.getText() + "'";
        }
    }
}
