package com.example.alex.cafeit;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.cafeit.OrdersFragment.OnListFragmentInteractionListener;
import com.example.alex.cafeit.Order;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyOrdersRecyclerViewAdapter extends RecyclerView.Adapter<MyOrdersRecyclerViewAdapter.ViewHolder> {

    private final List<Order> mValues;
    private final OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private Context context;

    public MyOrdersRecyclerViewAdapter(List<Order> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_orders, parent, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.orders_list);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nameView.setText(mValues.get(position).name);
        holder.costView.setText("$" + String.format("%.2f", mValues.get(position).price));
        holder.noteView.setText(mValues.get(position).note);
        holder.orderMenuView.setText(mValues.get(position).orderMenu);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                Toast.makeText(v.getContext(), holder.mItem.cafeName, Toast.LENGTH_SHORT).show();
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog deleteDialogue = new AlertDialog.Builder(context)
                        .setTitle("Order Completed")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete from database
                                Toast.makeText(context, "Beverage Complete: " + holder.mItem.orderMenu, Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
        public final TextView nameView;
        public final TextView costView;
        public final TextView noteView;
        public final TextView orderMenuView;
        public com.example.alex.cafeit.Order mItem;
        public LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            cafeLogoView = (ImageView) view.findViewById(R.id.cafe_logo);
            nameView = (TextView) view.findViewById(R.id.name);
            costView = (TextView) view.findViewById(R.id.cost);
            noteView = (TextView) view.findViewById(R.id.note);
            orderMenuView = (TextView) view.findViewById(R.id.menu);
            linearLayout = (LinearLayout) view.findViewById(R.id.orderLinearLayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + orderMenuView.getText() + "'";
        }
    }


}
