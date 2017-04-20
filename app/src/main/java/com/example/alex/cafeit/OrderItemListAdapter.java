package com.example.alex.cafeit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class OrderItemListAdapter extends ArrayAdapter<OrderItem> {
    private Context context;
    private List<OrderItem> values;

    public OrderItemListAdapter(Context context, List<OrderItem> values, Activity mActivity) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.order_name);
        TextView price = (TextView) convertView.findViewById(R.id.order_price);

        OrderItem item = values.get(position);

        name.setText(item.name + " " + item.size);
        price.setText(String.format(Locale.US, "$%.2f", item.price));

        return convertView;
    }
}