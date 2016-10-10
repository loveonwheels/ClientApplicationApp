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
public class MultiAppointmentAmountDialog extends DialogFragment {
    public static final String SELECTED_AMOUNT = "amountselection_amount";

    private static final String ARG_FREQUENCY = "arg_frequency";

    private boolean frequency;

    String[] amount_array;

    public MultiAppointmentAmountDialog(){

    }

    public static MultiAppointmentAmountDialog newInstance(boolean isDaily){
        MultiAppointmentAmountDialog dialog = new MultiAppointmentAmountDialog();
        Bundle args = new Bundle();
        args.putBoolean(ARG_FREQUENCY, isDaily);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        frequency = getArguments().getBoolean(ARG_FREQUENCY);
        if(frequency)
            amount_array = getResources().getStringArray(R.array.daily_amount_array);
        else
            amount_array = getResources().getStringArray(R.array.weekly_amount_array);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Amount")
                .setItems(amount_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                                getActivity().getIntent().putExtra(SELECTED_AMOUNT, Integer.valueOf(amount_array[which])));
                    }
                });
        return builder.create();
    }
}
