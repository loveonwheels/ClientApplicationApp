package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.address.State;

import java.util.ArrayList;

/**
 * Created by alamchristian on 3/15/16.
 */
public class StateSelectionDialog extends DialogFragment {
    public static final String SELECTED_STATE_ID = "stateselection_id";

    private static final String ARG_STATE_OBJECTS = "stateselection_objects";

    private ArrayList<State> states;

    public StateSelectionDialog(){

    }

    public static StateSelectionDialog newInstance(ArrayList<State> states){
        StateSelectionDialog dialog = new StateSelectionDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_STATE_OBJECTS, states);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        states = this.getArguments().getParcelableArrayList(ARG_STATE_OBJECTS);
        String[] state_names = new String[states.size()];
        for(int i = 0 ; i < states.size() ; i++){
            state_names[i] = states.get(i).state_name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.state_of_residence).setItems(state_names,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK, getActivity().getIntent().putExtra(SELECTED_STATE_ID, which));
                    }
                });

        return builder.create();
    }
}
