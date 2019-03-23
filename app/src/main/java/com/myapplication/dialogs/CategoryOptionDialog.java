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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import com.myapplication.R;


public class CategoryOptionDialog extends AppCompatDialogFragment {

    public static final String TAG = "CategoryOptionDialog";

    private AutoCompleteTextView categoryName;
    private String category;
    private CategoryMoreDialogListener listener;

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_option_dialog,null);

        categoryName = view.findViewById(R.id.category_name);
        categoryName.setText(category);

        builder.setView(view)
                .setTitle("Edit Item")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            listener.onDeleteCategory(category);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CategoryMoreDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    // An listener for our item adder
    public interface CategoryMoreDialogListener {
        void onDeleteCategory(String category);
        void onRenameCategory(String oldName, String newName);
    }
}
