package com.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.myapplication.datacontainers.Inventory;
import com.myapplication.datacontainers.Item;
import com.myapplication.dialogs.AddItemDialog;
import com.myapplication.dialogs.CategoryOptionDialog;
import com.myapplication.dialogs.ItemOptionDialog;
import com.myapplication.dialogs.MoveItemDialog;

public class MainActivity extends AppCompatActivity implements AddItemDialog.AddItemDialogListener,
        CategoryOptionDialog.CategoryOptionDialogListener,
        ItemOptionDialog.ItemOptionDialogListener,
        MoveItemDialog.MoveItemDialogListener,
        ExpandableListAdapter.CategoryOptionsButtonListener {

    public static final String SELECTED_INVENTORY = "com.myapplication.MainActivity.SELECTED_INVENTORY";
    public static final String SELECTED_SOLD = "com.myapplication.MainActivity.SELECTED_SOLD";
    public static final String SELECTED_SHIPPED = "com.myapplication.MainActivity.SELECTED_SHIPPED";

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private static String selectedView = SELECTED_INVENTORY;
    private static Inventory inventory;
    private static Inventory sold;
    private static Inventory shipped;

    private void updateListAddapter(Inventory i) {
        listAdapter.setDataCategory(i.getCategoriesList());
        listAdapter.setDataItem(i.getItemsMap());
        listAdapter.setDataItemQuantity(i.getItemsQuantityMap());
        listAdapter.notifyDataSetChanged();
    }

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
                    selectedView = SELECTED_SHIPPED;
                    return true;
                case R.id.navigation_shipped:
                    listAdapter.setDataCategory(shipped.getCategoriesList());
                    listAdapter.setDataItem(shipped.getItemsMap());
                    listAdapter.setDataItemQuantity(shipped.getItemsQuantityMap());
                    listAdapter.notifyDataSetChanged();
                    selectedView = SELECTED_SOLD;
                    return true;
            }
            return false;
        }
    };

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

    @Override
    protected void onPause() {
        super.onPause();

        inventory.saveInventory(this, Inventory.savedInventoryKey);
        sold.saveInventory(this, Inventory.savedSoldKey);
        shipped.saveInventory(this, Inventory.savedShippedKey);
    }


    // Menu bar stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add our custom menu items
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the item that called this function
        int id = item.getItemId();

        // Check if it was the add button that was pressed
        if (id == R.id.add_item_button) {
            AddItemDialog addItemDialog = new AddItemDialog();
            addItemDialog.show(getSupportFragmentManager(),"addItemDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddedItem(String item, String category, float price, int quantity) {

        inventory.addItem(item,category,price,quantity);

        if(selectedView.equals(SELECTED_INVENTORY)) {
            updateListAddapter(inventory);
        }

        new Toast(this).makeText(this,"Item added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOptionButtonPressed(String category) {
        CategoryOptionDialog dialog = new CategoryOptionDialog();
        dialog.setCategory(category);
        dialog.show(getSupportFragmentManager(),"categoryInfoDialog");
    }

    @Override
    public void onDeleteCategory(String category) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            inventory.deleteCategory(category);
            updateListAddapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            sold.deleteCategory(category);
            updateListAddapter(sold);
        }
        if(selectedView.equals(SELECTED_SHIPPED)) {
            shipped.deleteCategory(category);
            updateListAddapter(shipped);
        }
    }

    @Override
    public void onRenameCategory(String oldName, String newName) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            inventory.renameCategory(oldName, newName);
            updateListAddapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            sold.renameCategory(oldName, newName);
            updateListAddapter(sold);
        }
        if(selectedView.equals(SELECTED_SHIPPED)) {
            shipped.renameCategory(oldName, newName);
            updateListAddapter(shipped);
        }
    }

    @Override
    public void onItemPressed(String item, String category) {
        ItemOptionDialog dialog = new ItemOptionDialog();
        dialog.setItem(item);
        dialog.setCategory(category);
        dialog.show(getSupportFragmentManager(),"itemInfoDialog");
    }

    @Override
    public void onRenameItem(String category, String oldName, String newName) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            inventory.renameItem(category, oldName, newName);
            updateListAddapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            sold.renameItem(category, oldName, newName);
            updateListAddapter(sold);
        }
        if(selectedView.equals(SELECTED_SHIPPED)) {
            shipped.renameItem(category, oldName, newName);
            updateListAddapter(shipped);
        }
    }

    @Override
    public void onDeleteItem(String category, String item) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            inventory.deleteItem(category, item);
            updateListAddapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            sold.deleteItem(category, item);
            updateListAddapter(sold);
        }
        if(selectedView.equals(SELECTED_SHIPPED)) {
            shipped.deleteItem(category, item);
            updateListAddapter(shipped);
        }
    }

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

    @Override
    public void onMoveItem(String category, String item, int quantity) {

        if(selectedView.equals(SELECTED_INVENTORY)) {
            Item i = inventory.getItem(category, item);
            sold.addItem(item, category, i.getPrice(), quantity);
            i.setQuantity(i.getQuantity() - quantity);
            updateListAddapter(inventory);
        }
        if(selectedView.equals(SELECTED_SOLD)) {
            Item i = sold.getItem(category, item);
            shipped.addItem(item, category, i.getPrice(), quantity);
            i.setQuantity(i.getQuantity() - quantity);
            updateListAddapter(sold);
        }
    }
}
