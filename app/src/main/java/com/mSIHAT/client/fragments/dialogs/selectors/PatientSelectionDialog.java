package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.views.PatientSelectionItem;

import java.util.ArrayList;

/**
 * Created by alamchristian on 3/31/16.
 */
public class PatientSelectionDialog extends DialogFragment {
    public static final String SELECTED_PATIENT_ID = "patientselection_id";

    private static final String ARG_PATIENT_OBJECTS = "patientselection_objects";

    private ArrayList<PatientSelectionItem> patients;

    public PatientSelectionDialog(){

    }

    public static PatientSelectionDialog newInstance(ArrayList<PatientSelectionItem> patients){
        PatientSelectionDialog dialog = new PatientSelectionDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PATIENT_OBJECTS, patients);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        patients = getArguments().getParcelableArrayList(ARG_PATIENT_OBJECTS);
        String[] patient_names = new String[patients.size()];
        for(int i = 0 ; i < patients.size() ; i++){
            patient_names[i] = patients.get(i).patient_fullname;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_your_patient).setItems(patient_names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                        getActivity().getIntent().putExtra(SELECTED_PATIENT_ID, which));
            }
        });
        return builder.create();
    }
}
