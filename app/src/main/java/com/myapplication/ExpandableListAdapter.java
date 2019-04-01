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
     * @param listDataItem The hash holds the individual items, the keys being the categories in the listDataCategory.
     * @param listItemQuantity A hash holding the quantities of each item, the keys are in listDataCategory.
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
     * Gets the category data, the title of the category.
     * @return the category data.
     */
    public List<String> getDataCategory() {
        return listDataCategory;
    }

    /**
     * Allows us to set the data header, which is the category title.
     * @param listDataHeader the header from before.
     */
    public void setDataCategory(List<String> listDataHeader) {
        this.listDataCategory = listDataHeader;
    }

    /**
     * A hash map for getting the item in the data.
     * @return returns the item data.
     */
    public HashMap<String, List<String>> getDataItem() {
        return listDataItem;
    }

    /**
     * Sets the item ehader, which are the items.
     * @param listHashMap the header from before.
     */
    public void setDataItem(HashMap<String, List<String>> listHashMap) {
        this.listDataItem = listHashMap;
    }

    /**
     * How many of a particular item we have.
     * @return gets the quantity of how many we have.
     */
    public HashMap<String, List<Integer>> getDataItemQuantity() {
        return listItemQuantity;
    }

    /**
     * Sets the quantity of the particular item.
     * @param listItemQuantity the quantity from before.
     */
    public void setDataItemQuantity(HashMap<String, List<Integer>> listItemQuantity) {
        this.listItemQuantity = listItemQuantity;
    }

    /**
     * How many categories there are in that inventory.
     * @return an integer of the quantity.
     */
    @Override
    public int getGroupCount() {
        return listDataCategory.size();
    }

    /**
     * Get however many items are in the specific category.
     * @param i index of the category.
     * @return an integer of the quantity.
     */
    @Override
    public int getChildrenCount(int i) {
        return listDataItem.get(listDataCategory.get(i)).size();
    }

    /**
     * Gets the name of a specific category.
     * @param i takes the index.
     * @return a string of the name of the category.
     */
    @Override
    public Object getGroup(int i) {
        return listDataCategory.get(i);
    }

    /**
     * Gets the name of the item.
     * @param i Index of category.
     * @param i1 Index of the item.
     * @return the name of the child of a specified category as a string.
     */
    @Override
    public Object getChild(int i, int i1) {
        return listDataItem.get(listDataCategory.get(i)).get(i1);
    }

    /**
     * Gets the group id using an Index.
     * @param i index of the category.
     * @return Index i.
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }

    /**
     * Gets the item id using an Index.
     * @param i index of the category.
     * @param i1 index of the item.
     * @return the item.
     */
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    /**
     * Items id's aren't modified on item added.
     * @return false.
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Inflates a new view for a category, and gets the individual category view.
     * @param i index of the category.
     * @param b not used.
     * @param view the view to build into.
     * @param viewGroup not used.
     * @return an individual category view
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
     * Inflates a new view for a item, and gets the individual item view.
     * @param i index of the category.
     * @param i1 index of the item.
     * @param b not used.
     * @param view the view to build into.
     * @param viewGroup not used.
     * @return an individual item view.
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
     * Can you click the item?
     * @param i the category index.
     * @param i1 the item index.
     * @return if the child is clickable.
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * The interface to be overwritten allowing an activity to receive information
     * about user activity.
     */
    public interface CategoryOptionsButtonListener {
        void onOptionButtonPressed(String infoCategory);
        void onItemPressed(String item, String category);
    }

}
