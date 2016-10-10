package com.mSIHAT.client.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestPatientService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.DeletePatientDialog;
import com.mSIHAT.client.models.Patient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientDetailsFragment extends Fragment {
    public static final int DELETE_DIALOG = 1;

    private ProgressDialog progressDialog;
    private EditText editFullname, editNric, editRelationship,
            editDob, editGender, editAddress;

    private static final String ARG_PATIENT_ID = "param_patient_id";

    private int patient_id;

    private OnPatientDetailsFragmentInteractionListener mListener;

    private RestPatientService restPatientService;

    private FragmentActivity callingActivity;

    public PatientDetailsFragment() {
        // Required empty public constructor
    }

    public static PatientDetailsFragment newInstance(int patientId) {
        PatientDetailsFragment fragment = new PatientDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PATIENT_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            patient_id = getArguments().getInt(ARG_PATIENT_ID);
        }
        restPatientService = new RestPatientService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(this.getView() != null){
            if(patient_id != 0){
                LinearLayout emptyLinear = (LinearLayout) this.getView().findViewById(R.id.linear_empty_patients_details);
                emptyLinear.setVisibility(View.INVISIBLE);
                editFullname = (EditText) this.getView().findViewById(R.id.edit_patient_details_fullname);
                editNric = (EditText) this.getView().findViewById(R.id.edit_patient_details_nric);
                editRelationship = (EditText) this.getView().findViewById(R.id.edit_patient_details_relationship);
                editDob = (EditText) this.getView().findViewById(R.id.edit_patient_details_dob);
                editGender = (EditText) this.getView().findViewById(R.id.edit_patient_details_gender);
                editAddress = (EditText) this.getView().findViewById(R.id.edit_patient_details_address);

                editFullname.setKeyListener(null);
                editNric.setKeyListener(null);
                editRelationship.setKeyListener(null);
                editDob.setKeyListener(null);
                editGender.setKeyListener(null);
                editAddress.setKeyListener(null);

                getPatientDetails();
            } else {
                LinearLayout detailsFields = (LinearLayout) this.getView().findViewById(R.id.linear_patient_details_fields);
                detailsFields.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_patient_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_patient_details_delete:
                DeletePatientDialog deleteDialog = DeletePatientDialog.newInstance();
                deleteDialog.setTargetFragment(this, DELETE_DIALOG);
                deleteDialog.show(callingActivity.getSupportFragmentManager(), "dialog");
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPatientDetailsFragmentInteractionListener) {
            mListener = (OnPatientDetailsFragmentInteractionListener) context;
            callingActivity = (FragmentActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyPatientsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case DELETE_DIALOG:
                if(resultCode == Activity.RESULT_OK){
                    deletePatient();
                }
                else if(resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(PatientDetailsFragment.this.getContext(),
                            "Delete Cancel", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void deletePatient(){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(callingActivity.getString(R.string.deleting_patient));
        progressDialog.show();
        Call<Patient> call = restPatientService.getService().deletePatient(patient_id);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                Toast.makeText(PatientDetailsFragment.this.getContext(),
                        response.body().patient_fullname + R.string.is_deleted, Toast.LENGTH_SHORT).show();
                mListener.onPatientDeleted();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(PatientDetailsFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPatientDetails(){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(callingActivity.getString(R.string.retrieving_patients_details));
        progressDialog.show();
        Call<Patient> call = restPatientService.getService().getPatientOfId(patient_id);
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.code() == 200) {
                    PatientDetailsFragment.this.editFullname.setText(response.body().patient_fullname);
                    editNric.setText(response.body().patient_nric);
                    editRelationship.setText(response.body().patient_relationship);
                    editDob.setText(response.body().getParsedDob());
                    String gender = "Male";
                    if(response.body().patient_gender)
                        gender = "Female";
                    editGender.setText(gender);
                    editAddress.setText(response.body().patient_address);

                    if (mListener != null) {
                        mListener.onFragmentCompleted(response.body().patient_fullname,
                                response.body().patient_nric);
                    }
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(PatientDetailsFragment.this.getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    PatientDetailsFragment.this.getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(PatientDetailsFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                PatientDetailsFragment.this.getActivity().finish();
            }
        });
    }

    public interface OnPatientDetailsFragmentInteractionListener {
        void onFragmentCompleted(String fullname, String nric);
        void onPatientDeleted();
    }
}
