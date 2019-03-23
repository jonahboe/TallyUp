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
import android.widget.Button;
import android.widget.EditText;
import com.myapplication.R;

/**
 * This has
 */
public class CategoryOptionDialog extends AppCompatDialogFragment {

    public static final String TAG = "CategoryOptionDialog";

    private EditText categoryName;
    private Button deleteCategoryButton;
    private String category;
    private CategoryDialogListener listener;

    /**
     * Sets the category to the param provided.
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * this section has the code that deletes categorys.
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_option_dialog,null);

        categoryName = view.findViewById(R.id.category_name);
        categoryName.setText(category);

        deleteCategoryButton = view.findViewById(R.id.delete_category_button);
        deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
                deleteDialog.setMessage("Are you sure you want to delete this category?");
                deleteDialog.setTitle("Delete Category");
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
                            listener.onDeleteCategory(category);
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
                .setTitle("Edit Category")
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
                            String newName = categoryName.getText().toString();
                            if (category != newName)
                                listener.onRenameCategory(category,newName);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });

        return builder.create();
    }

    /**
     * tries to use the context param to set the listener to "(CategoryMoreDialogListener) context"
     * idk what that means though.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CategoryDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * A listener for our item adder
     */
    public interface CategoryMoreDialogListener {
        void onDeleteCategory(String category);

    // An listener for our item adder
    public interface CategoryDialogListener {
        void onRenameCategory(String oldName, String newName);
        void onDeleteCategory(String category);
    }
}
