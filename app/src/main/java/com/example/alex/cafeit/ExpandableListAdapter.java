package com.example.alex.cafeit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MenuItem>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<MenuItem>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_item, parent, false);
        }

        MenuItem cur = (MenuItem) getChild(groupPosition, childPosition);

        final EditText amt = (EditText) convertView.findViewById(R.id.quantity);
        if (cur.quantity >= 1) {
            amt.setText(String.format(Locale.US, "%d", cur.quantity));
        }
        final Button minus = (Button) convertView.findViewById(R.id.minus);
        final Button plus = (Button) convertView.findViewById(R.id.plus);

        CheckBox choice = (CheckBox) convertView.findViewById(R.id.checkBox);
        choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    amt.setVisibility(View.VISIBLE);
                    minus.setVisibility(View.VISIBLE);
                    plus.setVisibility(View.VISIBLE);
                } else {
                    amt.setVisibility(View.INVISIBLE);
                    minus.setVisibility(View.INVISIBLE);
                    plus.setVisibility(View.INVISIBLE);
                }
            }
        });
        choice.setChecked(cur.selected);
        choice.setText(cur.name);

        Spinner sizes = (Spinner) convertView.findViewById(R.id.size);
        // TODO: Populate with real menu prices
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this._context,
                R.array.sizes, R.layout.menu_item_size_spinner);

        ad.setDropDownViewResource(R.layout.menu_item_size_spinner);
        // Apply the adapter to the spinner
        sizes.setAdapter(ad);
        // TODO: Add listeners for button clicks here

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_header, parent, false);
        }

        TextView menuListHeader = (TextView) convertView
                .findViewById(R.id.menu_header);
        menuListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
