package com.mSIHAT.client.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.Practitioner_Location;
import com.mSIHAT.client.fragments.dialogs.SelectPract;
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.models.Pratitioner2;
import com.mSIHAT.client.models.views.AppointmentDetails;
import com.mSIHAT.client.models.views.PatientAppointment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PractitionerDetailsFragment extends Fragment {
    private static final String ARG_PARCEL_PRACTITIONER = "parcel_practitioner";
    private static final String ARG_PARCEL_APPDET = "parcel_appointment";
    private static final String ARG_PARCEL_PATDET = "parcel_patient";
    private RestPractitionerService practService = new RestPractitionerService();
    public static final int SINGLE_TIME_DIALOG = 1221;
    private Pratitioner2 practitioner;
    public static PatientAppointment patient;
    private AppointmentDetails appDet;
private int appointmentid;
    private EditText edit_name, edit_nric, edit_phone;

   RelativeLayout btn_call,btn_favorite,btn_locate;
LinearLayout btn_location_div;
    public PractitionerDetailsFragment() {
        // Required empty public constructor
    }

    public static PractitionerDetailsFragment newInstance(Pratitioner2 prac,int userid,AppointmentDetails app_parcel,PatientAppointment patient) {
        PractitionerDetailsFragment fragment = new PractitionerDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARCEL_PRACTITIONER, prac);
        args.putParcelable(ARG_PARCEL_APPDET, app_parcel);
        args.putParcelable(ARG_PARCEL_PATDET, patient);
        Log.e("Practioner det fra",String.valueOf(userid));
        args.putInt("userid",userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            practitioner = getArguments().getParcelable(ARG_PARCEL_PRACTITIONER);
            appDet = getArguments().getParcelable(ARG_PARCEL_APPDET);
            patient = getArguments().getParcelable(ARG_PARCEL_PATDET);
            appointmentid = getArguments().getInt("userid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment_practitioner_details, container, false);

        findViewById(rootView);


        Log.e("Date",appDet.appointment_date);
        Log.e("TIme",appDet.appointment_time);
        DateFormat df = new SimpleDateFormat("mm/dd/yyyy HH:mm");
        try {
            Date appointmentdate = df.parse(appDet.appointment_date+" "+appDet.appointment_time);
            Calendar cal  = Calendar.getInstance();
            Date currentDate = cal.getTime();

            long duration  =  appointmentdate.getTime() - currentDate.getTime();

            long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

            if(diffInMinutes < 30 ){

                btn_location_div.setVisibility(View.VISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return rootView;
    }

    private void findViewById(View view){
        btn_location_div = (LinearLayout) view.findViewById(R.id.button_locate_div);
        btn_locate = (RelativeLayout) view.findViewById(R.id.button_locate);
        edit_name = (EditText) view.findViewById(R.id.edit_app_details_practitioner_name);
        edit_name.setKeyListener(null);
        edit_nric = (EditText) view.findViewById(R.id.edit_app_details_practitioner_nric);
        edit_nric.setKeyListener(null);
        edit_phone = (EditText) view.findViewById(R.id.edit_app_details_practitioner_phone);
        edit_phone.setKeyListener(null);
        btn_call = (RelativeLayout) view.findViewById(R.id.button_call_practitioner);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + edit_phone.getText().toString();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse(uri));
                startActivity(intentCall);
            }
        });

        btn_favorite = (RelativeLayout) view.findViewById(R.id.button_favorite);
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makefavorite();
            }
        });
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPracLocation();
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


    public void makefavorite(){
            Call<Boolean> practSer = practService.getService().makefavorite(practitioner.practitioner_id,appointmentid);
            practSer.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.code() == 201){


                    }
                    else {

                        btn_favorite.setVisibility(View.INVISIBLE);
                        Log.e("completed token",String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });

    }


    public void openPracLocation(){

        //SelectPract singleDialog =  SelectPract.newInstance(patient_id,subservice_id,user_id);
        Practitioner_Location pracLocation =  Practitioner_Location.newInstance( practitioner, appointmentid,appDet,patient);
        pracLocation.setTargetFragment(PractitionerDetailsFragment.this, SINGLE_TIME_DIALOG);
        pracLocation.show(getActivity().getSupportFragmentManager(), "singleTimeDialog");
    }
}
