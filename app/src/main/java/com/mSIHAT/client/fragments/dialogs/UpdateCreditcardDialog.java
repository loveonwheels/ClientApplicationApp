package com.mSIHAT.client.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mSIHAT.client.R;

/**
 * Created by alamchristian on 3/22/16.
 */
public class UpdateCreditcardDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String NEW_CREDITCARD = "new_creditcard";

    private EditText editCreditcard;

    public UpdateCreditcardDialog(){

    }

    public static UpdateCreditcardDialog newInstance(){
        UpdateCreditcardDialog dialog = new UpdateCreditcardDialog();
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_update_creditcard, null);
        editCreditcard = (EditText) dialogView.findViewById(R.id.edit_update_account_creditcard_new);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.update, this)
                .setNegativeButton(R.string.cancel, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == -1){
            String newCard = editCreditcard.getText().toString();
            if(newCard.length() == 16) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                        getActivity().getIntent().putExtra(NEW_CREDITCARD, newCard));
            } else {
                Toast.makeText(UpdateCreditcardDialog.this.getContext(),
                        R.string.invalid_card, Toast.LENGTH_SHORT).show();
            }
        } else if(which == -2){
            UpdateCreditcardDialog.this.getDialog().cancel();
        }
    }
}
