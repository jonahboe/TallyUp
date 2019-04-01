package com.tallyup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.tallyup.R;
import com.tallyup.datacontainers.Inventory;
import com.tallyup.datacontainers.Item;
import com.tallyup.dialogs.AddItemDialog;
import com.tallyup.dialogs.CategoryOptionDialog;
import com.tallyup.dialogs.ItemOptionDialog;
import com.tallyup.dialogs.MoveItemDialog;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AddItemDialog.AddItemDialogListener,
        CategoryOptionDialog.CategoryOptionDialogListener,
        ItemOptionDialog.ItemOptionDialogListener,
        MoveItemDialog.MoveItemDialogListener,
        ExpandableListAdapter.CategoryOptionsButtonListener {

    public static final String SELECTED_INVENTORY = "MainActivity.SELECTED_INVENTORY";
    public static final String SELECTED_SOLD = "MainActivity.SELECTED_SOLD";
    public static final String SELECTED_SHIPPED = "MainActivity.SELECTED_SHIPPED";

    public static String selectedView = SELECTED_INVENTORY;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private static Inventory inventory;
    private static Inventory sold;
    private static Inventory shipped;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_inventory:
                    listAdapter.setDataCategory(inventory.getCategoriesList());
                    listAdapter.setDataItem(inventory.getItemsMap());
                    listAdapter.setDataItemQuantity(inventory.getItemsQuantityMap());
                    listAdapter.notifyDataSetChanged();
                    selectedView = SELECTED_INVENTORY;
                    return true;
                case R.id.navigation_sold:
                    listAdapter.setDataCategory(sold.getCategoriesList());
                    listAdapter.setDataItem(sold.getItemsMap());
                    listAdapter.setDataItemQuantity(sold.getItemsQuantityMap());
                    listAdapter.notifyDataSetChanged();
                    selectedView = SELECTED_SOLD;
                    return true;
                case R.id.navigation_shipped:
                    listAdapter.setDataCategory(shipped.getCategoriesList());
                    listAdapter.setDataItem(shipped.getItemsMap());
                    listAdapter.setDataItemQuantity(shipped.getItemsQuantityMap());
                    listAdapter.notifyDataSetChanged();
                    selectedView = SELECTED_SHIPPED;
                    return true;
            }
            return false;
        }
    };

    /**
     * Updates the list adapter to contain the information of the current inventory being viewed.
     * @param i the inventory being set to fill the view.
     */
    private void updateListAdapter(Inventory i) {
        listAdapter.setDataCategory(i.getCategoriesList());
        listAdapter.setDataItem(i.getItemsMap());
        listAdapter.setDataItemQuantity(i.getItemsQuantityMap());
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Run when the main activity is first created.
     * Loads the three inventory sets from storage in shared preferences.
     * Sets up the list view to start out with the "inventory" set loaded.
     * @param savedInstanceState A previously saved state, if applicable.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Setup our menu
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Load saved inventory
        inventory = new Inventory();
        inventory.loadInventory(this, Inventory.savedInventoryKey);

        // Load saved sold list
        sold = new Inventory();
        sold.loadInventory(this, Inventory.savedSoldKey);

        // Load saved shipped list
        shipped = new Inventory();
        shipped.loadInventory(this, Inventory.savedShippedKey);

        // Setup the list view
        listView = findViewById(R.id.expandable_list);
        listAdapter = new ExpandableListAdapter(this,inventory.getCategoriesList(),inventory.getItemsMap(),inventory.getItemsQuantityMap());
        listView.setAdapter(listAdapter);
    }

    /**
     * Run when the current activity is paused.
     * Saves the three inventory sets to the shared preferences in storage.
     */
    @Override
    protected void onPause() {
        super.onPause();

        inventory.saveInventory(this, Inventory.savedInventoryKey);
        sold.saveInventory(this, Inventory.savedSoldKey);
        shipped.saveInventory(this, Inventory.savedShippedKey);
    }


    /**
     * Inflates the menu with our custom menu bar.
     * @param menu Takes the menu to be updated.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add our custom menu items
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Runs when the item has been pressed.
     * @param item Takes the menu item to be updated.
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the item that called this function
        int id = item.getItemId();

        // Check if it was the add button that was pressed
        if (id == R.id.add_item_button) {
            AddItemDialog addItemDialog = new AddItemDialog();

            ArrayList<String> itemPrompt = new ArrayList<>();
            for (Map.Entry i : inventory.getItemsMap().entrySet()) {
                itemPrompt.addAll((ArrayList) i.getValue());
            }
            addItemDialog.setItemPrompts(itemPrompt);
            addItemDialog.setCategoryPromptList(inventory.getCategoriesList());

            addItemDialog.show(getSupportFragmentManager(),"addItemDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Runs when an item is being added.
     * Adds the item to the inventory set.
     * Updates the view if needed.
     * @param item The item to e added.
     * @param category The category name where the item is to be stored.
     * @param quantity The quantity of the item to be added.
     */
    @Override
    public void onAddedItem(String item, String category, int quantity) {

        inventory.addItem(item,category,quantity);
        inventory.sortItems();

        if(selectedView.equals(SELECTED_INVENTORY)) {
            updateListAdapter(inventory);
        }

        new Toast(this).makeText(this,"Item added",Toast.LENGTH_SHORT).show();
    }

    /**
     * Run when the category option button is pressed.
     * Sets up the dialog to be displayed.
     * @param category
     */
    @Override
    public void onOptionButtonPressed(String category) {
        CategoryOptionDialog dialog = new CategoryOptionDialog();
        dialog.setCategory(category);
        dialog.show(getSupportFragmentManager(),"categoryInfoDialog");
    }

    /**
     * Run when a category is deleted.
     * Facilitates which set to delete from.
     * @param category the category name to delete.
     */
    @Override
    public void onDeleteCategory(String category) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            inventory.deleteCategory(category);
            updateListAdapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            sold.deleteCategory(category);
            updateListAdapter(sold);
        }
        if(selectedView.equals(SELECTED_SHIPPED)) {
            shipped.deleteCategory(category);
            updateListAdapter(shipped);
        }
    }

    /**
     * Run when renaming a category
     * Facilitates which set to rename in.
     * @param oldName String of the old name.
     * @param newName String of the name to be switched to.
     */
    @Override
    public void onRenameCategory(String oldName, String newName) {
        if(newName.matches("") || newName.charAt(0) == ' ') {
            new Toast(this).makeText(this,"Error: Please fill out the field",Toast.LENGTH_LONG).show();
            return;
        }
        if(selectedView.equals(SELECTED_INVENTORY)) {
            inventory.renameCategory(oldName, newName);
            updateListAdapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            sold.renameCategory(oldName, newName);
            updateListAdapter(sold);
        }
        if(selectedView.equals(SELECTED_SHIPPED)) {
            shipped.renameCategory(oldName, newName);
            updateListAdapter(shipped);
        }
    }

    /**
     * Run when an item was pressed.
     * Sets up the items option dialog to be opened.
     * @param item
     * @param category
     */
    @Override
    public void onItemPressed(String item, String category) {
        ItemOptionDialog dialog = new ItemOptionDialog();
        dialog.setItem(item);
        dialog.setCategory(category);
        dialog.show(getSupportFragmentManager(),"itemInfoDialog");
    }

    /**
     * Run when renaming a item
     * Facilitates which set to rename in.
     * @param category Name of category where item is located.
     * @param oldName String of the old name.
     * @param newName String of the name to be switched to.
     */
    @Override
    public void onRenameItem(String category, String oldName, String newName) {
        if(newName.matches("") || category.matches("") || newName.charAt(0) == ' ' || category.charAt(0) == ' ') {
            new Toast(this).makeText(this,"Error: Please fill out the field",Toast.LENGTH_LONG).show();
            return;
        }

        if(selectedView.equals(SELECTED_INVENTORY)) {
            inventory.renameItem(category, oldName, newName);
            updateListAdapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            sold.renameItem(category, oldName, newName);
            updateListAdapter(sold);
        }
        if(selectedView.equals(SELECTED_SHIPPED)) {
            shipped.renameItem(category, oldName, newName);
            updateListAdapter(shipped);
        }
    }

    /**
     * Run when a category is deleted.
     * Facilitates which set to delete from.
     * @param category Name of category where item is located.
     * @param item The item name to delete.
     */
    @Override
    public void onDeleteItem(String category, String item) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            inventory.deleteItem(category, item);
            updateListAdapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            sold.deleteItem(category, item);
            updateListAdapter(sold);
        }
        if(selectedView.equals(SELECTED_SHIPPED)) {
            shipped.deleteItem(category, item);
            updateListAdapter(shipped);
        }
    }

    /**
     * Run when moving item button was pressed.
     * Sets up a dialog to get the info of the item being moved.
     * @param category Name of category where item is located.
     * @param item Name of the item to be moved
     */
    @Override
    public void onMoveItemPressed(String category, String item) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            MoveItemDialog dialog = new MoveItemDialog();
            dialog.setMaxQuantity(inventory.getItem(category, item).getQuantity());
            dialog.setCategory(category);
            dialog.setItem(item);
            dialog.show(getSupportFragmentManager(), "moveItemDialog");
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            MoveItemDialog dialog = new MoveItemDialog();
            dialog.setMaxQuantity(sold.getItem(category, item).getQuantity());
            dialog.setCategory(category);
            dialog.setItem(item);
            dialog.show(getSupportFragmentManager(), "moveItemDialog");
        }
    }

    /**
     * Run when moving an item between inventory sets.
     * @param category Name of category where item is located.
     * @param item Name of item being moved.
     * @param quantity Quantity of the item being moved.
     */
    @Override
    public void onMoveItem(String category, String item, int quantity) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            Item i = inventory.getItem(category, item);
            sold.addItem(item, category, quantity);
            i.setQuantity(i.getQuantity() - quantity);
            sold.sortItems();
            updateListAdapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            Item i = sold.getItem(category, item);
            shipped.addItem(item, category, quantity);
            i.setQuantity(i.getQuantity() - quantity);
            if (i.getQuantity() == 0)
                sold.deleteItem(category, item);
            shipped.sortItems();
            updateListAdapter(sold);
        }
    }
}
