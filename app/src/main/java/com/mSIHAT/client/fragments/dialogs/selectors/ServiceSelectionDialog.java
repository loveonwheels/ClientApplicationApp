package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.Service;

import java.util.ArrayList;

/**
 * Created by alamchristian on 3/31/16.
 */
public class ServiceSelectionDialog extends DialogFragment {
    public static final String SELECTED_SERVICE_ID = "serviceselection_id";

    private static final String ARG_SERVICE_OBJECTS = "serviceselection_objects";

    private ArrayList<Service> services;

    public ServiceSelectionDialog(){

    }

    public static ServiceSelectionDialog newInstance(ArrayList<Service> services){
        ServiceSelectionDialog dialog = new ServiceSelectionDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SERVICE_OBJECTS, services);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        services = this.getArguments().getParcelableArrayList(ARG_SERVICE_OBJECTS);
        String[] service_names = new String[services.size()];
        for(int i = 0 ; i < services.size() ; i++){
            service_names[i] = services.get(i).service_name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_a_service).setItems(service_names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, getActivity().getIntent().putExtra(SELECTED_SERVICE_ID, which));
            }
        });
        return builder.create();
    }
}
