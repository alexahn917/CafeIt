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

import com.example.alex.cafeit.fragments.HistoryFragment;
import com.example.alex.cafeit.fragments.HistoryFragment.OnListFragmentInteractionListener;

import java.util.List;
import java.util.Locale;

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
        context = parent.getContext().getApplicationContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.dateView.setText(mValues.get(position).orderTime);
        holder.costView.setText(String.format(Locale.US, "$%.2f", mValues.get(position).price));
        holder.cafeNameView.setText(TypefaceSpan.getSpannableString(holder.mItem.cafeName, MainActivity.HistoryFragment.getContext()));
        holder.orderMenuView.setText(mValues.get(position).itemName + " " + mValues.get(position).size);

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {new AlertDialog.Builder(MainActivity.HistoryFragment.getContext(), R.style.CafeItDialogue)
                        .setMessage("Add to favorites?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Cursor curse = MainActivity.dbAdapter.getAllItems();
                                curse.move(curse.getCount() - holder.getAdapterPosition());
                                MainActivity.dbAdapter.updateIsFavorite(curse.getLong(0), true);
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

    public Context getFragContext(){
        return context;
    }
}
