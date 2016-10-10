package com.mSIHAT.client.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.Pratitioner2;

public class PractitionerDetailsFragment extends Fragment {
    private static final String ARG_PARCEL_PRACTITIONER = "parcel_practitioner";

    private Pratitioner2 practitioner;

    private EditText edit_name, edit_nric, edit_phone;

    Button btn_call;

    public PractitionerDetailsFragment() {
        // Required empty public constructor
    }

    public static PractitionerDetailsFragment newInstance(Pratitioner2 prac) {
        PractitionerDetailsFragment fragment = new PractitionerDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARCEL_PRACTITIONER, prac);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            practitioner = getArguments().getParcelable(ARG_PARCEL_PRACTITIONER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment_practitioner_details, container, false);
        findViewById(rootView);
        return rootView;
    }

    private void findViewById(View view){
        edit_name = (EditText) view.findViewById(R.id.edit_app_details_practitioner_name);
        edit_name.setKeyListener(null);
        edit_nric = (EditText) view.findViewById(R.id.edit_app_details_practitioner_nric);
        edit_nric.setKeyListener(null);
        edit_phone = (EditText) view.findViewById(R.id.edit_app_details_practitioner_phone);
        edit_phone.setKeyListener(null);
        btn_call = (Button) view.findViewById(R.id.button_call_practitioner);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + edit_phone.getText().toString();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse(uri));
                startActivity(intentCall);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        edit_name.setText(practitioner.practitioner_fullname);
        edit_nric.setText(practitioner.practitioner_nric);
        edit_phone.setText(practitioner.practitioner_phone);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
