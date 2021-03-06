package com.example.alex.cafeit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alex.cafeit.fragments.FavoritesFragment;
import com.example.alex.cafeit.fragments.FavoritesFragment.OnListFragmentInteractionListener;
import com.example.alex.cafeit.fragments.HistoryFragment;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private SharedPreferences myPref;
    private SharedPreferences.Editor peditor;
    private Context context;

    public MyFavoritesRecyclerViewAdapter(List<com.example.alex.cafeit.Order> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        myPref = PreferenceManager.getDefaultSharedPreferences(context);
        peditor = myPref.edit();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        System.out.println(holder.mItem);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(MainActivity.PROF_DIR).child(
                holder.mItem.cafeID + MainActivity.PROF_PIC_SUFFIX);
        Glide.with(context /* context */).using(new FirebaseImageLoader()).load(storageReference)
                .error(ContextCompat.getDrawable(context, R.drawable.cafeit_logo)).into(holder.cafe_image);


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
                                makePurchase(order);
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
    public void makePurchase(Order order) {
        final Calendar myCalendar = Calendar.getInstance();
        String myFormat = "MM/dd/yyyy"; //Format for date choice
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String time = sdf.format(myCalendar.getTime());
        Date orderDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String purchasedDate = dateFormat.format(orderDate);
        String purchasedTime = timeFormat.format(orderDate);
        order.orderTime = time;
        order.orderDate = orderDate.toString();
        order.customerName = LoginActivity.username;
        order.purchasedDate = purchasedDate;
        order.purchasedTime = purchasedTime;
        MainActivity.dbAdapter.insertItem(order);
        mDatabase.child("orders").child(order.cafeID).push().setValue(order);
        MyHistoryRecyclerViewAdapter adapter = (MyHistoryRecyclerViewAdapter)
                ((HistoryFragment) MainActivity.HistoryFragment).mAdapter;
        ((HistoryFragment) MainActivity.HistoryFragment).updateArray(adapter);
        updateCurrentOrder(order);
        update_waittime(order.cafeID);
    }

    public void updateCurrentOrder(Order order) {
        peditor.putString("CafeId", order.cafeID);
        peditor.putString("OrderItem", order.itemName);
        peditor.putString("OrderCafe", order.cafeName);
        peditor.putString("OrderPrice", "$" + String.format("%.2f", order.price));
        peditor.putString("OrderPurchasedDate", order.purchasedDate);
        peditor.putString("OrderPurchasedTime", order.purchasedTime);
        peditor.apply();
    }

    public void update_waittime(final String CafeId) {
        mDatabase.child("orders").child(CafeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Cafe cafe = dataSnapshot.getValue(Cafe.class);
                Long OrderWaitTime = dataSnapshot.getChildrenCount() * 60;
                //OrderWaitTime = (cafe.waitTime + order_size) * 60;
                Log.d("$$$$$$$$$$$$$$$$$$$$$", OrderWaitTime+"");
                mDatabase.child("cafes").child(CafeId).child("waitTime").setValue((float) OrderWaitTime / 60);
                peditor.putLong("OrderWaitTime", OrderWaitTime);
                peditor.apply();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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
        final ImageView cafe_image;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            cafeNameView = (TextView) view.findViewById(R.id.cafe_name);
            timeCostView = (TextView) view.findViewById(R.id.time_remaining);
            menuOrderView = (TextView) view.findViewById(R.id.order_items);
            linearLayout = (LinearLayout) view.findViewById(R.id.favoriteLinearLayout);
            cafe_image = (ImageView) view.findViewById(R.id.cafe_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + cafeNameView.getText() + "'";
        }
    }
}