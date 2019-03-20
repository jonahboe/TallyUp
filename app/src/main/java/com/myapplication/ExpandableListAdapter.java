package com.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * An expandable list adapter implementing a custom expandableListView.xml. This adapter holds
 * categories (headers), and items (sub-items).
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataCategory;
    private HashMap<String, List<String>> listDataItem;
    private HashMap<String, List<Integer>> listItemQuantity;

    /**
     * Non-default expandable list adapter creates a new adapter instance using the following
     * parameters.
     * @param context The context of the view.
     * @param listDataCategory The header holds a list of strings (the categories).
     * @param listDataItem The hash holds the individual items, the keys being the categories in the listDataCategory
     * @param listItemQuantity A hash holding the quantities of each item, the keys are in listDataCategory
     */
    public ExpandableListAdapter(Context context, List<String> listDataCategory, HashMap<String, List<String>> listDataItem, HashMap<String, List<Integer>> listItemQuantity) {
        this.context = context;
        this.listDataCategory = listDataCategory;
        this.listDataItem = listDataItem;
        this.listItemQuantity = listItemQuantity;
    }

    public List<String> getDataCategory() {
        return listDataCategory;
    }

    public void setDataCategory(List<String> listDataHeader) {
        this.listDataCategory = listDataHeader;
    }

    public HashMap<String, List<String>> getDataItem() {
        return listDataItem;
    }

    public void setDataItem(HashMap<String, List<String>> listHashMap) {
        this.listDataItem = listHashMap;
    }

    public HashMap<String, List<Integer>> getDataItemQuantity() {
        return listItemQuantity;
    }

    public void setDataItemQuantity(HashMap<String, List<Integer>> listItemQuantity) {
        this.listItemQuantity = listItemQuantity;
    }

    @Override
    public int getGroupCount() {
        return listDataCategory.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listDataItem.get(listDataCategory.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataCategory.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listDataItem.get(listDataCategory.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_category,null);
        }
        final TextView listHeader = (TextView)view.findViewById(R.id.category_name);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);

        ImageButton button = (ImageButton)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.onInfoButtonPressed(listHeader.getText().toString());
            }
        });

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String)getChild(i,i1);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_item,null);
        }

        TextView txtListChild = (TextView)view.findViewById(R.id.item_name);
        txtListChild.setText(childText);

        TextView itemQty = (TextView)view.findViewById(R.id.item_quantity);
        itemQty.setText("Qty: " + Integer.toString(listItemQuantity.get(listDataCategory.get(i)).get(i1)));

        view.setBackgroundResource(android.R.drawable.menuitem_background);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
