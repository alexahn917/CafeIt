package com.example.alex.cafeit;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;



public class FavoriteListAdapter extends ArrayAdapter<Order> {

    int res;

    public FavoriteListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout orderView;
        Order currOrder = getItem(position);

        if (convertView == null) {
            orderView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, orderView, true);
        } else {
            orderView = (LinearLayout) convertView;
        }

        TextView timePriceText = (TextView) orderView.findViewById(R.id.time_remaining);
        TextView cafeNameText = (TextView) orderView.findViewById(R.id.cafe_name);
        TextView orderMenuText = (TextView) orderView.findViewById(R.id.order_items);

        String timePrice = Integer.toString(currOrder.remainingTime) +  " min"
                + "  |  $" + String.format("%.2f", currOrder.price);
        String cafeName = currOrder.cafeName;
        String orderMenu = currOrder.orderMenu;

        timePriceText.setText(timePrice);
        cafeNameText.setText(cafeName);
        orderMenuText.setText(orderMenu);

        return orderView;
    }
}