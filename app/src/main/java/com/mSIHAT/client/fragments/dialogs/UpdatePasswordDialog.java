package com.mSIHAT.client.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mSIHAT.client.R;

/**
 * Created by alamchristian on 3/22/16.
 */
public class UpdatePasswordDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String NEW_PASSWORD = "new_password";

    private EditText editCurrent, editNew, editConfirm;

    private static final String ARG_CURRENT_PASSWORD = "current_password";

    private String currentPassword;

    public static UpdatePasswordDialog newInstance(String currentPassword){
        UpdatePasswordDialog dialog = new UpdatePasswordDialog();
        Bundle args = new Bundle();
        args.putString(ARG_CURRENT_PASSWORD, currentPassword);
        dialog.setArguments(args);
        return dialog;
    }

    public UpdatePasswordDialog(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentPassword = this.getArguments().getString(ARG_CURRENT_PASSWORD);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_update_password, null);
        findViewById(dialogView);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.update, this)
                .setNegativeButton(R.string.cancel, this);
        return builder.create();
    }

    private void findViewById(View dialogView){
        editCurrent = (EditText) dialogView.findViewById(R.id.edit_update_account_password_current);
        editNew = (EditText) dialogView.findViewById(R.id.edit_update_account_password_new);
        editConfirm = (EditText) dialogView.findViewById(R.id.edit_update_account_password_confirm);
    }

    private String validateNewPassword(String newPassword, String confirmPassword){
        String msg = null;
        if(!newPassword.equals(confirmPassword)) {
            msg = getString(R.string.password_does_not_match);
        }
        if(newPassword.length() < 8 || newPassword.trim().length() < 8){
            msg = getString(R.string.password_needs_to_be_at_least_8_chars_long);
        }

        return msg;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == -1) {
            String inputCurrent = editCurrent.getText().toString();
            String inputNew = editNew.getText().toString();
            String inputConfirm = editConfirm.getText().toString();
            if(inputCurrent.equals(currentPassword)){
                String validateMsg = validateNewPassword(inputNew, inputConfirm);
                if(validateMsg == null){
                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                            Activity.RESULT_OK, getActivity().getIntent().putExtra(NEW_PASSWORD, inputConfirm));
                }else {
                    Toast.makeText(UpdatePasswordDialog.this.getContext(),
                            validateMsg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UpdatePasswordDialog.this.getContext(),
                        R.string.current_password_is_invalid, Toast.LENGTH_SHORT).show();
            }
        } else if (which == -2){
            UpdatePasswordDialog.this.getDialog().cancel();
        }
    }
}
