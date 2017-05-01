package com.example.alex.cafeit;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

        final MenuItem cur = (MenuItem) getChild(groupPosition, childPosition);

        final EditText amt = (EditText) convertView.findViewById(R.id.quantity);
        if (cur.quantity >= 1) {
            amt.setText(String.format(Locale.US, "%d", cur.quantity));
        }
        final Button minus = (Button) convertView.findViewById(R.id.minus);
        final Button plus = (Button) convertView.findViewById(R.id.plus);

        CheckBox choice = (CheckBox) convertView.findViewById(R.id.checkBox);
        choice.setChecked(cur.selected);
        choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    amt.setVisibility(View.VISIBLE);
                    minus.setVisibility(View.VISIBLE);
                    plus.setVisibility(View.VISIBLE);
                    cur.selected = true;
                } else {
                    amt.setVisibility(View.INVISIBLE);
                    minus.setVisibility(View.INVISIBLE);
                    plus.setVisibility(View.INVISIBLE);
                    cur.selected = false;
                    cur.quantity = 1;
                    amt.setText(String.format(Locale.US, "%d", cur.quantity));
                }
            }
        });

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                if (!builder.toString().matches(
                        "([1-9]?[0-9]?)?"
                )) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }

                return null;

            }
        };

        amt.setFilters(new InputFilter[] { filter });
        amt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = amt.getText().toString();
                    if (text.length() != 0) {
                        cur.quantity = Integer.parseInt(text);
                    }
                    amt.setText(String.format(Locale.US, "%d", cur.quantity));
                    amt.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = amt.getText().toString();
                if (text.length() != 0) {
                    int amount = Integer.parseInt(text);
                    if (amount < 99) {
                        cur.quantity++;
                        amt.setText(String.format(Locale.US, "%d", cur.quantity));
                    }
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = amt.getText().toString();
                if (text.length() != 0) {
                    int amount = Integer.parseInt(text);
                    if (amount > 1) {
                        cur.quantity--;
                        amt.setText(String.format(Locale.US, "%d", cur.quantity));
                    }
                }
            }
        });

//        choice.setChecked(cur.selected);
        choice.setText(cur.name);

        Spinner sizes = (Spinner) convertView.findViewById(R.id.size);
        // TODO: Populate with real menu prices
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this._context,
                R.array.sizes, R.layout.menu_item_size_spinner);
        ArrayAdapter<String> realAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_dropdown_item, cur.choices);

        ad.setDropDownViewResource(R.layout.menu_item_size_spinner);
        // Apply the adapter to the spinner
        sizes.setAdapter(realAdapter);
        sizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cur.choice_pos = i;
                if(!cur.oneSize){
                    switch(i){
                        case 0: cur.selectedSize = "(S)"; break;
                        case 1: cur.selectedSize = "(M)"; break;
                        case 2: cur.selectedSize = "(L)"; break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
