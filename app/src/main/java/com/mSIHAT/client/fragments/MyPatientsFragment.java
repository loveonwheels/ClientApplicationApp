package com.mSIHAT.client.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestPatientService;
import com.mSIHAT.client.DetailsActivity;
import com.mSIHAT.client.R;
import com.mSIHAT.client.UpdateDetailsActivity;
import com.mSIHAT.client.listAdapters.MyPatientsListAdapter;
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPatientsFragment extends Fragment implements ListView.OnItemClickListener {
    private ProgressDialog progressDialog;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "user_id_param";
    private int user_id;

    private RestPatientService restPatientService;

    private List<Patient> patients;
    private ListView mypatients_list;

    private LinearLayout emptyListLinear;

    private FragmentActivity callingActivity;

    public MyPatientsFragment() {
        // Required empty public constructor
    }

    public static MyPatientsFragment newInstance(int userId) {
        MyPatientsFragment fragment = new MyPatientsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            user_id = getArguments().getInt(ARG_USER_ID);
        }
        restPatientService = new RestPatientService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_patients, container, false);
        mypatients_list = (ListView) rootView.findViewById(R.id.list_mypatients);
        emptyListLinear = (LinearLayout) rootView.findViewById(R.id.linear_empty_list_mypatients);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(callingActivity.getString(R.string.retrieving_your_patients));
        progressDialog.show();


        Call<List<Patient>> call = restPatientService.getService().getPatientsOfUserId(user_id);
        call.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                MyPatientsFragment.this.patients = response.body();
                if (patients != null) {
                    emptyListLinear.setVisibility(View.INVISIBLE);
                    MyPatientsFragment.this.mypatients_list.setAdapter(new MyPatientsListAdapter(MyPatientsFragment.this.getContext(), patients));
                    MyPatientsFragment.this.mypatients_list.setOnItemClickListener(MyPatientsFragment.this);
                } else {
                    mypatients_list.setEmptyView(emptyListLinear);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Toast.makeText(MyPatientsFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_patients, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_my_patients_register_patient:
                Intent registerPatient = new Intent(this.getContext(), UpdateDetailsActivity.class);
                Bundle registerBundle = new Bundle();
                registerBundle.putString(Constants.KEY_UPDATE_DETAILS_PURPOSE,
                        Constants.EXTRA_REGISTER_PURPOSE_PATIENT);
                registerBundle.putInt(Constants.EXTRA_USER_ID, user_id);
                registerPatient.putExtras(registerBundle);

                startActivityForResult(registerPatient, Constants.ACTIVITY_RESULT_PATIENT_REGISTER);


                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == Constants.ACTIVITY_RESULT_PATIENT_REGISTER ||
                    requestCode == Constants.ACTIVITY_RESULT_PATIENT_EDIT) {
                Log.e("he is here","herer");
                reload();
              /*  Fragment frag = callingActivity.getSupportFragmentManager().findFragmentByTag(Constants.MAIN_FRAGMENT_TAG);
                FragmentTransaction ft = callingActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(frag).attach(frag).commit();
*/
            }



    }


    public void reload(){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(callingActivity.getString(R.string.retrieving_your_patients));
        progressDialog.show();
        Call<List<Patient>> call = restPatientService.getService().getPatientsOfUserId(user_id);
        call.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                MyPatientsFragment.this.patients = response.body();
                if (patients != null) {
                    emptyListLinear.setVisibility(View.INVISIBLE);
                    MyPatientsFragment.this.mypatients_list.setAdapter(new MyPatientsListAdapter(MyPatientsFragment.this.getContext(), patients));
                    MyPatientsFragment.this.mypatients_list.setOnItemClickListener(MyPatientsFragment.this);
                } else {
                    mypatients_list.setEmptyView(emptyListLinear);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Toast.makeText(MyPatientsFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callingActivity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailsIntent = new Intent(this.getContext(), DetailsActivity.class);
        Bundle detail_bundle = new Bundle();
        detail_bundle.putString(Constants.EXTRA_DETAILS_PURPOSE, Constants.EXTRA_DETAILS_PURPOSE_PATIENT);
        detail_bundle.putInt(Constants.EXTRA_PATIENT_ID, patients.get(position).patient_id);
        detailsIntent.putExtras(detail_bundle);
        startActivityForResult(detailsIntent, Constants.ACTIVITY_RESULT_PATIENT_EDIT);
    }
}
