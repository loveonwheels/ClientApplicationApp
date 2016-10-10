package com.mSIHAT.client.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mSIHAT.client.R;

/**
 * Created by alamchristian on 3/8/16.
 */
public class DeletePatientDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public DeletePatientDialog(){

    }

    public static DeletePatientDialog newInstance(){
        DeletePatientDialog dialog = new DeletePatientDialog();
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.are_you_sure_you_want_to_delete_patient)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //Toast.makeText(DeletePatientDialog.this.getActivity(), String.valueOf(which), Toast.LENGTH_SHORT).show();
        if(which == -1)
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
        else if (which == -2)
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
    }

}
