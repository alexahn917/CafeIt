package com.example.alex.cafeit;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.cafeit.fragments.HistoryFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<Order> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyHistoryRecyclerViewAdapter(List<Order> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
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
        holder.orderMenuView.setText(mValues.get(position).itemName + " " + mValues.get(position).size);

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog deleteDialogue = new AlertDialog.Builder(context)
                        .setMessage("Add to favorites?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete from database
                                Toast.makeText(context, "Added to favorites: " + holder.mItem.itemName
                                        + " " + holder.mItem.size + " from " + holder.mItem.cafeName,
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
//                Toast.makeText(context, "Clicked " + holder.mItem.cafeName, Toast.LENGTH_LONG).show();
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
        public LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            cafeLogoView = (ImageView) view.findViewById(R.id.cafe_logo);
            dateView = (TextView) view.findViewById(R.id.date);
            costView = (TextView) view.findViewById(R.id.cost);
            cafeNameView = (TextView) view.findViewById(R.id.cafeName);
            orderMenuView = (TextView) view.findViewById(R.id.menu);
            linearLayout = (LinearLayout) view.findViewById(R.id.historyLinearLayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + cafeNameView.getText() + "'";
        }
    }
}
