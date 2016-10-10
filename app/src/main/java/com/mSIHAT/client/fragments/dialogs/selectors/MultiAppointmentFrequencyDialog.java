package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mSIHAT.client.R;

/**
 * Created by alamchristian on 4/13/16.
 */
public class MultiAppointmentFrequencyDialog extends DialogFragment {
    public static final String SELECTED_FREQUENCY = "frequencyselection_frequency";

    String[] frequency_array;

    public MultiAppointmentFrequencyDialog(){

    }

    public static MultiAppointmentFrequencyDialog newInstance(){
        return new MultiAppointmentFrequencyDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        frequency_array = getResources().getStringArray(R.array.frequency_array);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Frequency")
                .setItems(frequency_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                                getActivity().getIntent().putExtra(SELECTED_FREQUENCY, frequency_array[which]));
                    }
                });
        return builder.create();
    }
}
