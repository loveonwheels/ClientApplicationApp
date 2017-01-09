package com.mSIHAT.client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestUserService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.models.views.AppointmentDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetailsFragment extends Fragment {
    private static final String ARG_PARCEL_APPOINTMENT = "parcel_appointment";
    private RestUserService restUserService;
    private AppointmentDetails appointmentDetails;

    private EditText edit_subservice, edit_date, edit_time;
    private ProgressBar progress_status;
    private TextView text_status;

    Button btnConfirm ;

    public AppointmentDetailsFragment() {
        // Required empty public constructor
    }

    public static AppointmentDetailsFragment newInstance(AppointmentDetails app_parcel,int status) {
        AppointmentDetailsFragment fragment = new AppointmentDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARCEL_APPOINTMENT, app_parcel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appointmentDetails = getArguments().getParcelable(ARG_PARCEL_APPOINTMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment_details, container, false);
        findViewById(rootView);
      Log.e("appiontment id2",appointmentDetails.subservice_name);
        return rootView;
    }

    private void findViewById(View view){
        edit_subservice = (EditText) view.findViewById(R.id.edit_app_details_subservice_name);
        edit_subservice.setKeyListener(null);
        edit_date = (EditText) view.findViewById(R.id.edit_app_details_date);
        edit_date.setKeyListener(null);
        edit_time = (EditText) view.findViewById(R.id.edit_app_details_time);
        edit_time.setKeyListener(null);
        text_status = (TextView) view.findViewById(R.id.text_appointment_status);
btnConfirm = (Button)view.findViewById(R.id.button4);
        restUserService = new RestUserService();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Boolean> call = restUserService.getService().ConComsccepted(appointmentDetails.appointment_id,1,"Sdsd");
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        int statusCode = response.code();
                        Boolean userAppiontments = response.body();
                        String msg = "here";


                        if (statusCode == 200) {
btnConfirm.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(),"Service confirmed rendered go to completed service to rate the service",Toast.LENGTH_LONG).show();



                            //  progress.dismiss();
                        }else{
                            Log.e("dfdf2", msg);
                            msg="error again";
                            Toast.makeText(getActivity(),"Confirmation failed",Toast.LENGTH_LONG).show();

                        }
                        Log.e("dfdf1", msg);

                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        //   progress.dismiss();

                        Toast.makeText(getActivity(),"request failed",Toast.LENGTH_LONG).show();

                        //   Log.e("dfdf", t.toString());
                    }
                });
            }
        });


        progress_status = (ProgressBar) view.findViewById(R.id.progress_appointment_status);
        int progress = appointmentDetails.status;
        progress_status.setProgress(progress);

        switch (progress){
            case 1:
                text_status.setGravity(Gravity.START);
                text_status.setText(R.string.pending);
                break;
            case 2:
                text_status.setGravity(Gravity.CENTER_HORIZONTAL);
                text_status.setText(R.string.confirmed);

                break;
            case 4:
                text_status.setGravity(Gravity.END);
                text_status.setText(R.string.completed);
                btnConfirm.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        edit_subservice.setText(appointmentDetails.subservice_name);
        edit_date.setText(appointmentDetails.appointment_date);
        edit_time.setText(appointmentDetails.appointment_time);

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
