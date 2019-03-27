package com.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
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

    private CategoryOptionsButtonListener listener;
    private Context context;
    private List<String> listDataCategory;
    private HashMap<String, List<String>> listDataItem;
    private HashMap<String, List<Integer>> listItemQuantity;

    /**
     * Non-default expandable list adapter creates a new adapter instance using the following
     * parameters. It also sets an more button listener for each of the categories.
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

        try {
            listener = (CategoryOptionsButtonListener) context;
        } catch (ClassCastException e) {
            Log.e("ExpandableListAdapter", e.getMessage());
        }
    }

    /**
     * Gets the category data, the title of the category
     * @return the category data
     */
    public List<String> getDataCategory() {
        return listDataCategory;
    }

    /**
     * Allows us to set the data header, which is the category title
     * @param listDataHeader the header from before
     */
    public void setDataCategory(List<String> listDataHeader) {
        this.listDataCategory = listDataHeader;
    }

    /**
     * A hash map for getting the item in the data
     * @return returns the item data
     */
    public HashMap<String, List<String>> getDataItem() {
        return listDataItem;
    }

    /**
     * Sets the item ehader, which are the items
     * @param listHashMap the header from before
     */
    public void setDataItem(HashMap<String, List<String>> listHashMap) {
        this.listDataItem = listHashMap;
    }

    /**
     * How many of a particular item we have
     * @return gets the quantity of how many we have
     */
    public HashMap<String, List<Integer>> getDataItemQuantity() {
        return listItemQuantity;
    }

    /**
     * Sets the quantity of the particular item
     * @param listItemQuantity the quantity from before
     */
    public void setDataItemQuantity(HashMap<String, List<Integer>> listItemQuantity) {
        this.listItemQuantity = listItemQuantity;
    }

    /**
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        return listDataCategory.size();
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public int getChildrenCount(int i) {
        return listDataItem.get(listDataCategory.get(i)).size();
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Object getGroup(int i) {
        return listDataCategory.get(i);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Object getChild(int i, int i1) {
        return listDataItem.get(listDataCategory.get(i)).get(i1);
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     *
     * @param i
     * @param b
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_category,null);
        }
        final TextView listHeader = view.findViewById(R.id.category_name);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);

        ImageButton optionButton = view.findViewById(R.id.option_button);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOptionButtonPressed(listHeader.getText().toString());
            }
        });

        return view;
    }

    /**
     *
     * @param i
     * @param i1
     * @param b
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    @SuppressLint("SetTextI18n")
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String)getChild(i,i1);
        final String categoryText = (String)getGroup(i);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_item,null);
        }

        final TextView txtListChild = view.findViewById(R.id.item_name);
        txtListChild.setText(childText);

        TextView itemQty = view.findViewById(R.id.item_quantity);
        itemQty.setText("Qty: " + Integer.toString(listItemQuantity.get(listDataCategory.get(i)).get(i1)));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemPressed(childText, categoryText);
            }
        });

        view.setBackgroundResource(android.R.drawable.menuitem_background);
        return view;
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     *
     */
    public interface CategoryOptionsButtonListener {
        void onOptionButtonPressed(String infoCategory);
        void onItemPressed(String item, String category);
    }

}
