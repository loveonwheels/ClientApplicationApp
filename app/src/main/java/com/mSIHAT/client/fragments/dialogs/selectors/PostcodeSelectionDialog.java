package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.address.Postcode;

import java.util.ArrayList;

/**
 * Created by alamchristian on 3/15/16.
 */
public class PostcodeSelectionDialog extends DialogFragment {
    public static final String SELECTED_POSTCODE_ID = "postcodeselection_id";

    private static final String ARG_POSTCODE_OBJECTS = "postcodeselection_objects";

    private ArrayList<Postcode> postcodes;

    public PostcodeSelectionDialog(){

    }

    public static PostcodeSelectionDialog newInstance(ArrayList<Postcode> postcodes){
        PostcodeSelectionDialog dialog = new PostcodeSelectionDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_POSTCODE_OBJECTS, postcodes);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        postcodes = getArguments().getParcelableArrayList(ARG_POSTCODE_OBJECTS);
        String[] postcode_codes = new String[postcodes.size()];
        for(int i = 0 ; i < postcodes.size() ; i++){
            postcode_codes[i] = postcodes.get(i).postcode_code;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.postcode_of_residence).setItems(postcode_codes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                        getActivity().getIntent().putExtra(SELECTED_POSTCODE_ID, which));
            }
        });
        return builder.create();
    }
}
