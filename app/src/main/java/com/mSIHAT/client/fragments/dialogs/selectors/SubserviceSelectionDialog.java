package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.Subservice;

import java.util.ArrayList;

/**
 * Created by alamchristian on 3/31/16.
 */
public class SubserviceSelectionDialog extends DialogFragment {
    public static final String SELECTED_SUBSERVICE_ID = "subserviceselection_id";

    private static final String ARG_SUBSERVICE_OBJECTS = "subserviceselection_objects";

    private ArrayList<Subservice> subservices;

    public SubserviceSelectionDialog(){

    }

    public static SubserviceSelectionDialog newInstance(ArrayList<Subservice> subservices){
        SubserviceSelectionDialog dialog = new SubserviceSelectionDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SUBSERVICE_OBJECTS, subservices);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        subservices = getArguments().getParcelableArrayList(ARG_SUBSERVICE_OBJECTS);
        String[] subservice_names = new String[subservices.size()];
        for(int i = 0 ; i < subservices.size() ; i++){
            subservice_names[i] = subservices.get(i).subservice_name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_a_subservice).setItems(subservice_names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                        getActivity().getIntent().putExtra(SELECTED_SUBSERVICE_ID, which));
            }
        });
        return builder.create();
    }
}
