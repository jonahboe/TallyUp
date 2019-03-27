package com.myapplication.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.myapplication.MainActivity;
import com.myapplication.R;



/**
 * Prompts user for number of specified items they would like to move to the next section
 */
public class MoveItemDialog extends AppCompatDialogFragment {

    private static final String TAG = "MoveItemItemDialog";

    private int maxQuantity;
    private String category;
    private String item;
    private NumberPicker quantitySpinner;
    private MoveItemDialogListener listener;
    private String functionality;

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.move_item_dialog,null);

        if(MainActivity.selectedView.equals(MainActivity.SELECTED_INVENTORY)) {
            functionality = "Sell";
        }

        if(MainActivity.selectedView.equals(MainActivity.SELECTED_SOLD)) {
            functionality = "Ship";
        }


        quantitySpinner = view.findViewById(R.id.spinner);
        quantitySpinner.setMinValue(0);
        quantitySpinner.setMaxValue(maxQuantity);
        quantitySpinner.setWrapSelectorWheel(false);

        builder.setView(view)
                .setTitle(functionality + " Item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setPositiveButton(functionality, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onMoveItem(category, item, quantitySpinner.getValue());
                    }
                });

        return builder.create();
    }

    /**
     * Runs when the listener is attached to a context
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MoveItemDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * A listener for our move action
     */
    public interface MoveItemDialogListener {
        void onMoveItem(String category, String name, int quantity);
    }

}
