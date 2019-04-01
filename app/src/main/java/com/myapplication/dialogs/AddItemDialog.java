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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;


/**
 * This is the part where the user is able to add items and categories.
 */
public class AddItemDialog extends AppCompatDialogFragment {

    private static final String TAG = "AddItemDialog";

    private AutoCompleteTextView editTextItem;
    private AutoCompleteTextView editTextCategory;
    private EditText editTextQuantity;
    private AddItemDialogListener listener;

    private List<String> itemPromptList;
    private List<String> categoryPromptList;

    /**
     * Set the item list for the dropdown prompt.
     * @param itemPromptList
     */
    public void setItemPrompts(List<String> itemPromptList) {
        this.itemPromptList = itemPromptList;
    }

    /**
     * Set the category list for the dropdown prompt.
     * @param categoryPromptList
     */
    public void setCategoryPromptList(List<String> categoryPromptList) {
        this.categoryPromptList = categoryPromptList;
    }

    /**
     * uses the instance to add more onto the instance according to what the user types in.
     * @param savedInstanceState The last built instance.
     * @return the dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog,null);

        editTextItem = view.findViewById(R.id.itemText);
        ArrayAdapter itemAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, itemPromptList);
        editTextItem.setThreshold(1);
        editTextItem.setAdapter(itemAdapter);

        editTextCategory = view.findViewById(R.id.categoryText);
        ArrayAdapter categoryAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, categoryPromptList);
        editTextCategory.setThreshold(1);
        editTextCategory.setAdapter(categoryAdapter);

        editTextQuantity = view.findViewById(R.id.quantityText);

        builder.setView(view)
                .setTitle("Add Item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        String category = "";
                        float price = 0;
                        int quantity = 0;
                        try {
                            item = editTextItem.getText().toString();
                            category = editTextCategory.getText().toString();
                            quantity = parseInt(editTextQuantity.getText().toString());
                            Log.d("testing", item + ", " + category);
                            if (item.matches("") || category.matches("") || item.charAt(0) == ' ' || category.charAt(0) == ' ') {
                                throw new Exception("Empty fields");
                            }
                            listener.onAddedItem(item, category, quantity);
                        } catch (Exception e) {
                            new Toast(getContext()).makeText(getContext(),R.string.add_item_error,Toast.LENGTH_LONG).show();
                            Log.e(TAG, e.getMessage());
                        }

                    }
                });

        return builder.create();
    }

    /**
     * sets listener = (AddItemDialogListener) context
     * there is some more stuff but thats the important line I think.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddItemDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * A listener for our item adder
     */
    public interface AddItemDialogListener {
        void onAddedItem(String item, String category, int quantity);
    }

}
