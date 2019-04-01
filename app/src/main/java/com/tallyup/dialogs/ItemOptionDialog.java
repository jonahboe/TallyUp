package com.tallyup.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tallyup.MainActivity;
import com.tallyup.R;

/**
 * This builds the display for the options of what to do when the item is clicked.
 */
public class ItemOptionDialog extends AppCompatDialogFragment {

    public static final String TAG = "ItemOptionDialog";

    private ItemOptionDialogListener listener;
    private EditText itemName;
    private Button deleteItemButton;
    private Button sellItemButton;
    private String category;
    private String item;

    /**
     * Gives the option of changing the name of the category.
     * @param category the category to be changed.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gives the option of changing the name of the item.
     * @param item the item to be changed.
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * Allows a dialog window to appear when items or categories are interacted with
     * @param savedInstanceState The last saved stuff.
     * @return creates the dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.item_option_dialog,null);

        Button moveItem = view.findViewById(R.id.move_item_button);
        if (MainActivity.selectedView.equals(MainActivity.SELECTED_SOLD)) {
            moveItem.setText("SHIP ITEM");
        }

        itemName = view.findViewById(R.id.item_name);
        itemName.setText(item);

        sellItemButton = view.findViewById(R.id.move_item_button);
        if (MainActivity.selectedView.equals(MainActivity.SELECTED_SHIPPED))
            sellItemButton.setVisibility(View.GONE);
        else {
            sellItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoveItemPressed(category, item);
                    dismiss();
                }
            });
        }

        deleteItemButton = view.findViewById(R.id.delete_item_button);
        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
                deleteDialog.setMessage("Are you sure you want to delete this item?");
                deleteDialog.setTitle("Delete Item");
                deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            listener.onDeleteItem(category, item);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                        dismiss();
                    }
                });
                AlertDialog alertDialog = deleteDialog.create();
                alertDialog.show();
            }
        });

        builder.setView(view)
                .setTitle("Edit Item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String newName = itemName.getText().toString();
                            if (category != newName)
                                listener.onRenameItem(category, item, newName);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });

        return builder.create();
    }

    /**
     * Runs when the inflater is attached to the activity.
     * @param context the activity to be passed in.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ItemOptionDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * A listener for our item adder
     */
    public interface ItemOptionDialogListener {
        void onRenameItem(String category, String oldName, String newName);
        void onMoveItemPressed(String category, String item);
        void onDeleteItem(String category, String item);
    }
}
