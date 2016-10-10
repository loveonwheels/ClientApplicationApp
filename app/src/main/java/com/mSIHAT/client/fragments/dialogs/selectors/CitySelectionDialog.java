package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.address.City;

import java.util.ArrayList;

/**
 * Created by alamchristian on 3/15/16.
 */
public class CitySelectionDialog extends DialogFragment {
    public static final String SELECTED_CITY_ID = "cityselection_id";

    private static final String ARG_CITY_OBJECTS = "cityselection_objects";

    public CitySelectionDialog(){

    }

    public static CitySelectionDialog newInstance(ArrayList<City> cities){
        CitySelectionDialog dialog = new CitySelectionDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CITY_OBJECTS, cities);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<City> cities = getArguments().getParcelableArrayList(ARG_CITY_OBJECTS);
        String[] city_names = new String[cities.size()];
        for(int i = 0 ; i < cities.size() ; i++){
            city_names[i] = cities.get(i).city_name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.city_of_residence).setItems(city_names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, getActivity().getIntent().putExtra(SELECTED_CITY_ID, which));
            }
        });
        return builder.create();
    }
}
