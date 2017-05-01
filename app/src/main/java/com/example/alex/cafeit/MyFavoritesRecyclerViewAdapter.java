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

import com.example.alex.cafeit.fragments.CafesListFragment;
import com.example.alex.cafeit.fragments.FavoritesFragment;
import com.example.alex.cafeit.fragments.FavoritesFragment.OnListFragmentInteractionListener;
import com.example.alex.cafeit.fragments.HistoryFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFavoritesRecyclerViewAdapter extends RecyclerView.Adapter<MyFavoritesRecyclerViewAdapter.ViewHolder> {

    private final List<com.example.alex.cafeit.Order> mValues;
    private final OnListFragmentInteractionListener mListener;
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


        holder.cafeNameView.setText(TypefaceSpan.getSpannableString(holder.mItem.cafeName, FavoritesFragment.getFragContext()));
        holder.timeCostView.setText(Integer.toString(holder.mItem.remainingTime) +  " min"
                + "  |  $" + String.format("%.2f", holder.mItem.price));
        holder.menuOrderView.setText(holder.mItem.itemName + " " + holder.mItem.size);


        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {new AlertDialog.Builder(context, R.style.historyToFavoriteDialog)
                    .setMessage("Delete favorite?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete from database
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
                AlertDialog orderDialogue = new AlertDialog.Builder(context, R.style.historyToFavoriteDialog)
                        .setMessage("Make one-click order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete from database
                                Toast.makeText(context, "Order Made: " + holder.mItem.itemName + " "
                                        + holder.mItem.size, Toast.LENGTH_LONG).show();
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