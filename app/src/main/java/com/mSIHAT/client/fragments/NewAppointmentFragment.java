package com.mSIHAT.client.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mSIHAT.client.APIServices.RestForServices;
import com.mSIHAT.client.APIServices.RestPatientService;
import com.mSIHAT.client.AvailablePractitionersActivity;
import com.mSIHAT.client.MainActivity;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.SelectPract;
import com.mSIHAT.client.fragments.dialogs.selectors.PatientSelectionDialog;
import com.mSIHAT.client.fragments.dialogs.selectors.ServiceSelectionDialog;
import com.mSIHAT.client.fragments.dialogs.SingleAppointmentTimeDialog;
import com.mSIHAT.client.fragments.dialogs.selectors.SubserviceSelectionDialog;
import com.mSIHAT.client.map.MapFragment;
import com.mSIHAT.client.models.Service;
import com.mSIHAT.client.models.Subservice;
import com.mSIHAT.client.models.views.PatientSelectionItem;
import com.mSIHAT.client.utils.Constants;

import java.io.Console;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewAppointmentFragment extends Fragment implements View.OnClickListener {
    public static final int PATIENT_DIALOG = 1;
    public static final int SERVICE_DIALOG = 2;
    public static final int SUBSERVICE_DIALOG = 3;
    public static final int SINGLE_TIME_DIALOG = 4;
    public static final int MULTI_TIME_DIALOG = 5;

    public static final int ACTIVITY_RESULT_NEWAPP = 181;

    private ProgressDialog progressDialog;
    private EditText edit_patient, edit_service, edit_subservice;
    private Button button_single, button_multi;

    private static final String ARG_USER_ID = "user_id";

    private int user_id;


    private RestForServices servicesRest;
    private RestPatientService patientRest;

    private FragmentActivity callingActivity;

    private ArrayList<PatientSelectionItem> patients;
    private ArrayList<Service> services;
    private ArrayList<Subservice> subservices;

    private int subservice_id, patient_id;

    public NewAppointmentFragment() {
    }

    public static NewAppointmentFragment newInstance(int user_id) {
        NewAppointmentFragment fragment = new NewAppointmentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, user_id);
        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.e("dfgdg","its not empty");
            user_id = getArguments().getInt(ARG_USER_ID);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callingActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_appointment, container, false);

        servicesRest = new RestForServices();
        patientRest = new RestPatientService();
       // getActivity().setTitle("mSIHAT");
        findViewById(rootView);
        retrievePatients();
        retrieveServices();
       // getFirebaseKey();
        return rootView;
    }


    public void getFirebaseKey(){
        String tkn = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(getActivity(), "Current token ["+tkn+"]",
                Toast.LENGTH_LONG).show();
        Log.d("App Token", "Token ["+tkn+"]");

    }

    private void findViewById(View view) {
        edit_patient = (EditText) view.findViewById(R.id.edit_newapp_patient_name);
        edit_patient.setInputType(InputType.TYPE_NULL);
        edit_service = (EditText) view.findViewById(R.id.edit_newapp_service_type);
        edit_service.setInputType(InputType.TYPE_NULL);
        edit_subservice = (EditText) view.findViewById(R.id.edit_newapp_subservice_type);
        edit_subservice.setInputType(InputType.TYPE_NULL);

        button_single = (Button) view.findViewById(R.id.btn_newapp_single);
        button_single.setOnClickListener(this);
        button_multi = (Button) view.findViewById(R.id.btn_newapp_multiple);
        button_multi.setOnClickListener(this);
    }

    private void retrievePatients() {
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("preparing 1234");
        progressDialog.show();
        Call<ArrayList<PatientSelectionItem>> callPatients = patientRest.getService().getPatientsOfUserIdArrayList(user_id);
        callPatients.enqueue(new Callback<ArrayList<PatientSelectionItem>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientSelectionItem>> call, Response<ArrayList<PatientSelectionItem>> response) {
                if (response.code() == 200) {
                    patients = response.body();
                    edit_patient.setOnClickListener(NewAppointmentFragment.this);
                    progressDialog.dismiss();
                } else if(response.code() == 404){
                    Toast.makeText(NewAppointmentFragment.this.getContext(),
                            R.string.please_register_at_least_one_patient_under_your_account, Toast.LENGTH_SHORT).show();
                //    NewAppointmentFragment.this.getActivity().finish();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientSelectionItem>> call, Throwable t) {
                Toast.makeText(NewAppointmentFragment.this.getContext(),
                        R.string.check_network_connection, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void retrieveServices() {
        edit_service.setOnClickListener(null);
        Call<ArrayList<Service>> callServices = servicesRest.getServicesAPI().getAllServices();
        callServices.enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if (response.code() == 200) {
                    services = response.body();
                    edit_service.setOnClickListener(NewAppointmentFragment.this);
                    Log.e("dfgfdgfdg","DFgdfgdf");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                Toast.makeText(NewAppointmentFragment.this.getContext(),
                        R.string.check_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveSubservices(int service_id) {
        edit_subservice.setOnClickListener(null);
        Call<ArrayList<Subservice>> callSubservices = servicesRest.getServicesAPI().getSubservicesOfService(service_id);
        callSubservices.enqueue(new Callback<ArrayList<Subservice>>() {
            @Override
            public void onResponse(Call<ArrayList<Subservice>> call, Response<ArrayList<Subservice>> response) {
                if (response.code() == 200) {
                    subservices = response.body();
                    edit_subservice.setOnClickListener(NewAppointmentFragment.this);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Subservice>> call, Throwable t) {
                Toast.makeText(NewAppointmentFragment.this.getContext(),
                        R.string.check_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_newapp_patient_name:
                PatientSelectionDialog patientDialog = PatientSelectionDialog.newInstance(patients);
                patientDialog.setTargetFragment(NewAppointmentFragment.this, PATIENT_DIALOG);
                patientDialog.show(callingActivity.getSupportFragmentManager(), "patientDialog");
                break;
            case R.id.edit_newapp_service_type:
                ServiceSelectionDialog serviceDialog = ServiceSelectionDialog.newInstance(services);
                serviceDialog.setTargetFragment(NewAppointmentFragment.this, SERVICE_DIALOG);
                serviceDialog.show(callingActivity.getSupportFragmentManager(), "serviceDialog");
                break;
            case R.id.edit_newapp_subservice_type:
                SubserviceSelectionDialog subserviceDialog = SubserviceSelectionDialog.newInstance(subservices);
                subserviceDialog.setTargetFragment(NewAppointmentFragment.this, SUBSERVICE_DIALOG);
                subserviceDialog.show(callingActivity.getSupportFragmentManager(), "subserviceDialog");
                break;
            case R.id.btn_newapp_single:
                if(checkEntries() == -1) {


                    SelectPract singleDialog =  SelectPract.newInstance(patient_id,subservice_id,user_id);
                    singleDialog.setTargetFragment(NewAppointmentFragment.this, SINGLE_TIME_DIALOG);
                    singleDialog.show(callingActivity.getSupportFragmentManager(), "singleTimeDialog");




                } else {
                    Toast.makeText(NewAppointmentFragment.this.getContext(),
                            R.string.please_check_your_inputs, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_newapp_multiple:

                Toast.makeText(NewAppointmentFragment.this.getContext(),
                        "Multi Serivce removed for this alpha testing", Toast.LENGTH_LONG).show();
                /*
                if(checkEntries() == -1) {
                    MultiAppointmentTimeDialog multiDialog = MultiAppointmentTimeDialog.newInstance();
                    multiDialog.setTargetFragment(NewAppointmentFragment.this, MULTI_TIME_DIALOG);
                    multiDialog.show(callingActivity.getSupportFragmentManager(), "multiTimeDialog");
                } else {
                    Toast.makeText(NewAppointmentFragment.this.getContext(),
                            R.string.please_check_your_inputs, Toast.LENGTH_SHORT).show();
                }*/
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("amin new appointment","sdsd");

        Log.e("paypal got here",String.valueOf(requestCode));
        Log.e("paypal got here",String.valueOf(resultCode));
        switch (requestCode) {

            case PATIENT_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    patient_id = patients.get(data.getIntExtra(PatientSelectionDialog.SELECTED_PATIENT_ID, 0)).patient_id;
                    edit_patient.setText(patients
                            .get(data.getIntExtra(PatientSelectionDialog.SELECTED_PATIENT_ID, 0))
                            .patient_fullname);
                }
                break;
            case SERVICE_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    int service_id = services.get(data.getIntExtra(ServiceSelectionDialog.SELECTED_SERVICE_ID, 0)).service_id;
                    edit_service.setText(services
                            .get(data.getIntExtra(ServiceSelectionDialog.SELECTED_SERVICE_ID, 0))
                            .service_name);
                    edit_subservice.setText(null);
                    retrieveSubservices(service_id);
                }
                break;
            case SUBSERVICE_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    subservice_id = subservices.get(data.getIntExtra(SubserviceSelectionDialog.SELECTED_SUBSERVICE_ID, 0)).subservice_id;
                    edit_subservice.setText(subservices
                            .get(data.getIntExtra(SubserviceSelectionDialog.SELECTED_SUBSERVICE_ID, 0))
                            .subservice_name);
                }
                break;
            case SINGLE_TIME_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = new Bundle();
                    String date = data.getStringExtra(SingleAppointmentTimeDialog.SINGLE_APPOINTMENT_DATE);
                    int time = data.getIntExtra(SingleAppointmentTimeDialog.SINGLE_APPOINTMENT_TIME,0);
                    bundle.putBoolean(Constants.EXTRA_IS_MULTI_APPOINTMENT, false);
                    bundle.putString(Constants.EXTRA_APPOINTMENT_DATE, date);
                    bundle.putInt(Constants.EXTRA_APPOINTMENT_TIME,time);
                    bundle.putInt(Constants.EXTRA_PATIENT_ID, patient_id);
                    bundle.putInt(Constants.EXTRA_SUBSERVICE_ID, subservice_id);
                    Intent searchPractitioners = new Intent(this.getContext(), AvailablePractitionersActivity.class);
                    searchPractitioners.putExtras(bundle);
                    startActivityForResult(searchPractitioners, ACTIVITY_RESULT_NEWAPP);
                }
                break;
            case MULTI_TIME_DIALOG:
                if (resultCode == Activity.RESULT_OK) {

                    /*
                    Bundle bundle = new Bundle();
                    String date = data.getStringExtra(MultiAppointmentTimeDialog.MULTI_APPOINTMENT_DATE);
                    String time = data.getStringExtra(MultiAppointmentTimeDialog.MULTI_APPOINTMENT_TIME);
                    boolean frequency = data.getBooleanExtra(MultiAppointmentTimeDialog.MULTI_APPOINTMENT_FREQUENCY, true);
                    int amount = data.getIntExtra(MultiAppointmentTimeDialog.MULTI_APPOINTMENT_AMOUNT, 0);
                    bundle.putBoolean(Constants.EXTRA_IS_MULTI_APPOINTMENT, true);
                    bundle.putString(Constants.EXTRA_APPOINTMENT_DATETIME, date.concat(time));
                    bundle.putBoolean(Constants.EXTRA_MULTI_APPOINTMENT_FREQUENCY, frequency);
                    bundle.putInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT, amount);
                    bundle.putInt(Constants.EXTRA_PATIENT_ID, patient_id);
                    bundle.putInt(Constants.EXTRA_SUBSERVICE_ID, subservice_id);
                    Intent searchPractitioners = new Intent(this.getContext(), AvailablePractitionersActivity.class);
                    searchPractitioners.putExtras(bundle);
                    startActivityForResult(searchPractitioners, ACTIVITY_RESULT_NEWAPP);

                    */
                }
            default:
                break;
        }
    }

    private int checkEntries(){
        int errorAt = -1;
        String[] entries = new String[3];
        entries[0] = edit_patient.getText().toString();
        entries[1] = edit_service.getText().toString();
        entries[2] = edit_subservice.getText().toString();
        for(int i = 0 ; i < entries.length ; i++){
            if(entries[i] == null || entries[i].length() < 1){
                errorAt = i;
                break;
            }
        }
        return errorAt;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        if (f != null){
          Log.e("found a map","found");
            getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();

        }

    }
}
