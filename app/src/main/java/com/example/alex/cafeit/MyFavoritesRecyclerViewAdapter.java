package com.example.alex.cafeit;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.cafeit.fragments.FavoritesFragment;
import com.example.alex.cafeit.fragments.FavoritesFragment.OnListFragmentInteractionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFavoritesRecyclerViewAdapter extends RecyclerView.Adapter<MyFavoritesRecyclerViewAdapter.ViewHolder> {

    private final List<com.example.alex.cafeit.Order> mValues;
    private final OnListFragmentInteractionListener mListener;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private Context context;

    public MyFavoritesRecyclerViewAdapter(List<com.example.alex.cafeit.Order> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        System.out.println(holder.mItem);


        holder.cafeNameView.setText(TypefaceSpan.getSpannableString(holder.mItem.cafeName, MainActivity.FavoritesFragment.getContext()));
        holder.timeCostView.setText(Integer.toString(holder.mItem.remainingTime) +  " min"
                + "  |  $" + String.format(Locale.US, "%.2f", holder.mItem.price));
        holder.menuOrderView.setText(holder.mItem.itemName + " " + holder.mItem.size);


        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {new AlertDialog.Builder(context, R.style.CafeItDialogue)
                    .setMessage("Delete favorite?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete from database
                            Cursor curse = MainActivity.dbAdapter.getFavorites();
                            curse.move(curse.getCount() - holder.getAdapterPosition());
                            MainActivity.dbAdapter.updateIsFavorite(curse.getLong(0), false);
                            mValues.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                            Toast.makeText(context, "Deleted favorite", Toast.LENGTH_LONG).show();
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
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context, R.style.CafeItDialogue)
                        .setMessage("Make one-click order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete from database
                                Toast.makeText(context, "Order Made: " + holder.mItem.itemName + " "
                                        + holder.mItem.size, Toast.LENGTH_LONG).show();

//                                // MAKE THE ONE-CLICK ORDER
                                Order order = holder.mItem;

                                Date orderDate = new Date();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                                String purchasedDate = dateFormat.format(orderDate);
                                String purchasedTime = timeFormat.format(orderDate);

                                order.orderDate = orderDate.toString();

//                                mDatabase.child("cafes").child(order.cafeID).child("orders").push().setValue(order);
                                mDatabase.child("orders").child(order.cafeID).push().setValue(order);

                                mListener.onListFragmentInteraction(holder.mItem, 0);
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
        public final TextView cafeNameView;
        public final TextView timeCostView;
        public final TextView menuOrderView;
        public com.example.alex.cafeit.Order mItem;
        public LinearLayout linearLayout;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            cafeNameView = (TextView) view.findViewById(R.id.cafe_name);
            timeCostView = (TextView) view.findViewById(R.id.time_remaining);
            menuOrderView = (TextView) view.findViewById(R.id.order_items);
            linearLayout = (LinearLayout) view.findViewById(R.id.favoriteLinearLayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + cafeNameView.getText() + "'";
        }
    }
}