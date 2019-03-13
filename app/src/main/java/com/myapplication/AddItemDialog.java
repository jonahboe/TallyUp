package com.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class AddItemDialog extends AppCompatDialogFragment {

    private static final String TAG = "AddItemDialog";

    private EditText editTextItem;
    private EditText editTextCategory;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private AddItemDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog,null);

        builder.setView(view)
                .setTitle("Add Item")
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
                            String item = editTextItem.getText().toString();
                            String category = editTextCategory.getText().toString();
                            float price = parseFloat(editTextPrice.getText().toString());
                            int quantity = parseInt(editTextQuantity.getText().toString());
                            listener.returnText(item, category, price, quantity);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });

        editTextItem = view.findViewById(R.id.itemText);
        editTextCategory = view.findViewById(R.id.categoryText);
        editTextPrice = view.findViewById(R.id.priceText);
        editTextQuantity = view.findViewById(R.id.quantityText);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddItemDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    // An listener for our item adder
    public interface AddItemDialogListener {
        void returnText(String item, String category, float price, int quantity);
    }

}
