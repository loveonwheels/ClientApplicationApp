package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.address.Country;

import java.util.ArrayList;

/**
 * Created by alamchristian on 3/14/16.
 */
public class CountrySelectionDialog extends DialogFragment {
    public static final String SELECTED_COUNTRY_ID = "countryselection_id";

    private static final String ARG_COUNTRY_OBJECTS = "countryselection_objects";

    private ArrayList<Country> countries;

    public CountrySelectionDialog(){

    }

    public static CountrySelectionDialog newInstance(ArrayList<Country> countries){
        CountrySelectionDialog dialog = new CountrySelectionDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_COUNTRY_OBJECTS, countries);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        countries = this.getArguments().getParcelableArrayList(ARG_COUNTRY_OBJECTS);
        String[] country_names = new String[countries.size()];
        for(int i = 0 ; i < countries.size() ; i++){
            country_names[i] = countries.get(i).country_name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.country_of_residence).setItems(country_names,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK, getActivity().getIntent().putExtra(SELECTED_COUNTRY_ID,
                                        which));
                    }
                });
        return builder.create();
    }
}
