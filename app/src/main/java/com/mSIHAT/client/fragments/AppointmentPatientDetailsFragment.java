package com.mSIHAT.client.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.AppointmentCondition;
import com.mSIHAT.client.models.views.PatientAppointment;

/**
 * Created by alamchristian on 3/30/16.
 */
public class AppointmentPatientDetailsFragment extends Fragment {
    private final static String ARG_PARCEL_PATIENT = "parcel_patient";
    private final static String ARG_PARCEL_CONDITION = "parcel_condition";

    private PatientAppointment patientDetails;
    private AppointmentCondition condition;

    private EditText edit_name, edit_nric, edit_address,
                    edit_mobility, edit_feeding,
                    edit_toileting, edit_incontinent,
                    edit_urinary, edit_wound,
                    edit_mental;

    public AppointmentPatientDetailsFragment(){

    }

    public static AppointmentPatientDetailsFragment newInstance(PatientAppointment patient, AppointmentCondition condition){
        AppointmentPatientDetailsFragment fragment = new AppointmentPatientDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARCEL_PATIENT, patient);
        args.putParcelable(ARG_PARCEL_CONDITION, condition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            patientDetails = getArguments().getParcelable(ARG_PARCEL_PATIENT);
            condition = getArguments().getParcelable(ARG_PARCEL_CONDITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment_patient_details, container, false);
        findViewById(rootView);
        return rootView;
    }

    private void findViewById(View view){
        edit_name = (EditText) view.findViewById(R.id.edit_app_details_patient_name);
        edit_name.setKeyListener(null);
        edit_nric = (EditText) view.findViewById(R.id.edit_app_details_patient_nric);
        edit_nric.setKeyListener(null);
        edit_address = (EditText) view.findViewById(R.id.edit_app_details_patient_address);
        edit_address.setKeyListener(null);

        edit_mobility = (EditText) view.findViewById(R.id.edit_app_details_patient_mobility);
        edit_mobility.setKeyListener(null);
   /*     edit_feeding = (EditText) view.findViewById(R.id.edit_app_details_patient_feeding);
        edit_feeding.setKeyListener(null);
        edit_toileting = (EditText) view.findViewById(R.id.edit_app_details_patient_toileting);
        edit_toileting.setKeyListener(null);
        edit_incontinent = (EditText) view.findViewById(R.id.edit_app_details_patient_incontinent);
        edit_incontinent.setKeyListener(null);
        edit_urinary = (EditText) view.findViewById(R.id.edit_app_details_patient_urinary_catheter);
        edit_urinary.setKeyListener(null);
        edit_wound = (EditText) view.findViewById(R.id.edit_app_details_patient_wound_dressing);
        edit_wound.setKeyListener(null);
        edit_mental = (EditText) view.findViewById(R.id.edit_app_details_patient_mental_status);
        edit_mental.setKeyListener(null);
        */
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        edit_name.setText(patientDetails.patient_fullname);
        edit_nric.setText(patientDetails.patient_nric);
        edit_address.setText(patientDetails.patient_address);
/*
        edit_mobility.setText(condition.mobility);
        edit_feeding.setText(condition.feeding);
        edit_toileting.setText(condition.toileting);
        edit_incontinent.setText(condition.incontinent);
        edit_urinary.setText(condition.urinary);
        edit_wound.setText(condition.wound);
        edit_mental.setText(condition.mental);
        */
    }
}
