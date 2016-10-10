package com.mSIHAT.client.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestAddressService;
import com.mSIHAT.client.APIServices.RestPatientService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.selectors.CitySelectionDialog;
import com.mSIHAT.client.fragments.dialogs.selectors.CountrySelectionDialog;
import com.mSIHAT.client.fragments.dialogs.selectors.PostcodeSelectionDialog;
import com.mSIHAT.client.fragments.dialogs.selectors.StateSelectionDialog;
import com.mSIHAT.client.models.address.City;
import com.mSIHAT.client.models.address.Country;
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.models.address.Postcode;
import com.mSIHAT.client.models.address.State;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientRegistrationFragment extends Fragment implements View.OnClickListener {
    public static final int COUNTRY_DIALOG = 1;
    public static final int STATE_DIALOG = 2;
    public static final int CITY_DIALOG = 3;
    public static final int POSTCODE_DIALOG = 4;
    private ProgressDialog progressDialog;

    EditText patientFullname, patientNric, patientRelationship,
            patientDob, patientAddress, patientCountry, patientState,
            patientCity, patientPostcode;

    Button confirmRegister, cancelRegister;

    private DatePickerDialog datePickerDob;
    private SimpleDateFormat dateFormatter;

    Spinner genderSpinner;

    private FragmentActivity callingActivity;

    private RestPatientService restPatientService;
    private Patient patient;

    private RestAddressService restAddressService;
    private ArrayList<Country> countries;
    private ArrayList<State> states;
    private ArrayList<City> cities;
    private ArrayList<Postcode> postcodes;
    private int selected_postcode;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "patient_user_id";

    private int user_id;

    private OnPatientRegistrationAction mListener;

    public static PatientRegistrationFragment newInstance(int user_id) {
        PatientRegistrationFragment fragment = new PatientRegistrationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getInt(ARG_USER_ID);
            dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.TAIWAN);
            restPatientService = new RestPatientService();
            restAddressService = new RestAddressService();
        }
    }

    public PatientRegistrationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_registration, container, false);
        getActivity().setTitle(getString(R.string.title_register_new_patient));

        findViewById(rootView);
        retrieveCountries();
        prepareDobField();

        return rootView;
    }

    public void findViewById(View rootView) {
        genderSpinner = (Spinner) rootView.findViewById(R.id.spinner_patient_gender);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        patientFullname = (EditText) rootView.findViewById(R.id.edit_patient_fullname);
        patientNric = (EditText) rootView.findViewById(R.id.edit_patient_nric);
        patientRelationship = (EditText) rootView.findViewById(R.id.edit_patient_relationship);

        patientDob = (EditText) rootView.findViewById(R.id.edit_patient_dob);
        patientDob.setInputType(InputType.TYPE_NULL);

        patientAddress = (EditText) rootView.findViewById(R.id.edit_patient_address);

        patientCountry = (EditText) rootView.findViewById(R.id.edit_patient_country);
        patientCountry.setInputType(InputType.TYPE_NULL);

        patientState = (EditText) rootView.findViewById(R.id.edit_patient_state);
        patientState.setInputType(InputType.TYPE_NULL);

        patientCity = (EditText) rootView.findViewById(R.id.edit_patient_city);
        patientCity.setInputType(InputType.TYPE_NULL);

        patientPostcode = (EditText) rootView.findViewById(R.id.edit_patient_postcode);
        patientPostcode.setInputType(InputType.TYPE_NULL);

        confirmRegister = (Button) rootView.findViewById(R.id.btn_patient_register);
        confirmRegister.setOnClickListener(this);
        cancelRegister = (Button) rootView.findViewById(R.id.btn_patient_cancel);
        cancelRegister.setOnClickListener(this);
    }

    private void prepareDobField() {
        patientDob.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        datePickerDob = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                patientDob.setText(dateFormatter.format(date.getTime()));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDob.getDatePicker().setMaxDate(new Date().getTime());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPatientRegistrationAction) {
            mListener = (OnPatientRegistrationAction) context;
            callingActivity = (FragmentActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPatientRegistrationAction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.btn_patient_register:
                    if(checkEntries() == -1) {
                        progressDialog = new ProgressDialog(this.getContext());
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage(callingActivity.getString(R.string.registering_patient));
                        progressDialog.show();
                        boolean gender = true;
                        if (genderSpinner.getSelectedItem().toString().equalsIgnoreCase(callingActivity.getString(R.string.male)))
                            gender = false;
                        if (genderSpinner.getSelectedItem().toString().equalsIgnoreCase(callingActivity.getString(R.string.female)))
                            gender = true;
                        patient = new Patient(patientFullname.getText().toString(), patientNric.getText().toString(),
                                patientRelationship.getText().toString(), patientDob.getText().toString(),
                                gender, patientAddress.getText().toString(), selected_postcode, user_id,"phonenumber to be added");
                        Call<Patient> call = restPatientService.getService().registerPatient(patient);
                        call.enqueue(new Callback<Patient>() {
                            @Override
                            public void onResponse(Call<Patient> call, Response<Patient> response) {
                                if (response.code() == 201) {
                                    Toast.makeText(PatientRegistrationFragment.this.getContext(),
                                            R.string.successfully_registered_a_new_patient,
                                            Toast.LENGTH_SHORT).show();
                                    mListener.onPatientRegistrationButtonClick(true);
                                } else if (response.code() == 200){
                                    Toast.makeText(PatientRegistrationFragment.this.getContext(),
                                            R.string.patient_with_this_nric_is_already_registered,
                                            Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();

                            }

                            @Override
                            public void onFailure(Call<Patient> call, Throwable t) {
                                Toast.makeText(PatientRegistrationFragment.this.getContext(),
                                        t.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(PatientRegistrationFragment.this.getContext(),
                                R.string.please_check_your_inputs, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_patient_cancel:
                    mListener.onPatientRegistrationButtonClick(false);
                    break;
                case R.id.edit_patient_dob:
                    datePickerDob.show();
                    break;
                case R.id.edit_patient_country:
                    CountrySelectionDialog countryDialog = CountrySelectionDialog.newInstance(countries);
                    countryDialog.setTargetFragment(PatientRegistrationFragment.this, COUNTRY_DIALOG);
                    countryDialog.show(callingActivity.getSupportFragmentManager(), "countryDialog");
                    break;
                case R.id.edit_patient_state:
                    StateSelectionDialog stateDialog = StateSelectionDialog.newInstance(states);
                    stateDialog.setTargetFragment(PatientRegistrationFragment.this, STATE_DIALOG);
                    stateDialog.show(callingActivity.getSupportFragmentManager(), "stateDialog");
                    break;
                case R.id.edit_patient_city:
                    CitySelectionDialog cityDialog = CitySelectionDialog.newInstance(cities);
                    cityDialog.setTargetFragment(PatientRegistrationFragment.this, CITY_DIALOG);
                    cityDialog.show(callingActivity.getSupportFragmentManager(), "cityDialog");
                    break;
                case R.id.edit_patient_postcode:
                    PostcodeSelectionDialog postcodeDialog = PostcodeSelectionDialog.newInstance(postcodes);
                    postcodeDialog.setTargetFragment(PatientRegistrationFragment.this, POSTCODE_DIALOG);
                    postcodeDialog.show(callingActivity.getSupportFragmentManager(), "postcodeDialog");
                default:
                    break;

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case COUNTRY_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    int country_id = countries.get(data.getIntExtra(CountrySelectionDialog.SELECTED_COUNTRY_ID, 0)).country_id;
                    patientCountry.setText(countries
                            .get(data.getIntExtra(CountrySelectionDialog.SELECTED_COUNTRY_ID, 0))
                            .country_name);
                    patientState.setText(null);
                    patientCity.setText(null);
                    patientPostcode.setText(null);
                    retrieveStates(country_id);
                }
                break;
            case STATE_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    int state_id = states.get(data.getIntExtra(StateSelectionDialog.SELECTED_STATE_ID, 0)).state_id;
                    patientState.setText(states
                            .get(data.getIntExtra(StateSelectionDialog.SELECTED_STATE_ID, 0))
                            .state_name);
                    patientCity.setText(null);
                    patientPostcode.setText(null);
                    retrieveCities(state_id);
                }
                break;
            case CITY_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    int city_id = cities.get(data.getIntExtra(CitySelectionDialog.SELECTED_CITY_ID, 0)).city_id;
                    patientCity.setText(cities
                            .get(data.getIntExtra(CitySelectionDialog.SELECTED_CITY_ID, 0))
                            .city_name);
                    patientPostcode.setText(null);
                    retrievePostcodes(city_id);
                }
                break;
            case POSTCODE_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    int postcode_id = postcodes.get(data.getIntExtra(PostcodeSelectionDialog.SELECTED_POSTCODE_ID, 0)).postcode_id;
                    patientPostcode.setText(postcodes
                            .get(data.getIntExtra(PostcodeSelectionDialog.SELECTED_POSTCODE_ID, 0))
                            .postcode_code);
                    selected_postcode = postcode_id;
                }
            default:
                break;
        }
    }

    private void retrieveCountries() {
        patientCountry.setOnClickListener(null);
        Call<ArrayList<Country>> call = restAddressService.getService().getAllCountries();
        call.enqueue(new Callback<ArrayList<Country>>() {
            @Override
            public void onResponse(Call<ArrayList<Country>> call, Response<ArrayList<Country>> response) {
                if (response.code() == 200) {
                    countries = response.body();
                    patientCountry.setOnClickListener(PatientRegistrationFragment.this);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Country>> call, Throwable t) {
                Toast.makeText(PatientRegistrationFragment.this.getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveStates(int country_id) {
        patientState.setOnClickListener(null);
        Call<ArrayList<State>> call = restAddressService.getService().getStatesOfCountry(country_id);
        call.enqueue(new Callback<ArrayList<State>>() {
            @Override
            public void onResponse(Call<ArrayList<State>> call, Response<ArrayList<State>> response) {
                if (response.code() == 200) {
                    states = response.body();
                    patientState.setOnClickListener(PatientRegistrationFragment.this);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<State>> call, Throwable t) {
                Toast.makeText(PatientRegistrationFragment.this.getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveCities(int state_id) {
        patientCity.setOnClickListener(null);
        Call<ArrayList<City>> call = restAddressService.getService().getCitiesOfState(state_id);
        call.enqueue(new Callback<ArrayList<City>>() {
            @Override
            public void onResponse(Call<ArrayList<City>> call, Response<ArrayList<City>> response) {
                if (response.code() == 200) {
                    cities = response.body();
                    patientCity.setOnClickListener(PatientRegistrationFragment.this);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<City>> call, Throwable t) {
                Toast.makeText(PatientRegistrationFragment.this.getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrievePostcodes(int city_id) {
        patientPostcode.setOnClickListener(null);
        Call<ArrayList<Postcode>> call = restAddressService.getService().getPostcodesOfCity(city_id);
        call.enqueue(new Callback<ArrayList<Postcode>>() {
            @Override
            public void onResponse(Call<ArrayList<Postcode>> call, Response<ArrayList<Postcode>> response) {
                if (response.code() == 200) {
                    postcodes = response.body();
                    patientPostcode.setOnClickListener(PatientRegistrationFragment.this);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Postcode>> call, Throwable t) {
                Toast.makeText(PatientRegistrationFragment.this.getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int checkEntries(){
        int errorAt = -1;
        String[] entries = new String[10];
        entries[0] = patientFullname.getText().toString();
        entries[1] = patientNric.getText().toString();
        entries[2] = patientRelationship.getText().toString();
        entries[3] = patientDob.getText().toString();
        entries[4] = genderSpinner.getSelectedItem().toString();
        entries[5] = patientAddress.getText().toString();
        entries[6] = patientCountry.getText().toString();
        entries[7] = patientState.getText().toString();
        entries[8] = patientCity.getText().toString();
        entries[9] = patientPostcode.getText().toString();
        for(int i = 0 ; i < entries.length ; i++){
            if(entries[i] == null || entries[i].length() < 1){
                errorAt = i;
                break;
            }
            if(i == 4){
                if (entries[i].equalsIgnoreCase(callingActivity.getString(R.string.gender))){
                    errorAt = i;
                    break;
                }
            }
        }
        return errorAt;
    }

    public interface OnPatientRegistrationAction {
        void onPatientRegistrationButtonClick(boolean confirm);
    }
}
