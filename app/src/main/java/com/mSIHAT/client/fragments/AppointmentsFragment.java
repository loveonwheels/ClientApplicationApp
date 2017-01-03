package com.mSIHAT.client.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.rxretrofit.RestAppointmentServiceRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestForServicesRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestPatientServiceRx;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.RatingFragment;
import com.mSIHAT.client.fragments.dialogs.SelectPract;
import com.mSIHAT.client.listAdapters.AppointmentsListAdapter;
import com.mSIHAT.client.models.Appointment;
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.models.Subservice;
import com.mSIHAT.client.models.views.AppointmentListItem;
import com.mSIHAT.client.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppointmentsFragment extends Fragment{
    private ProgressDialog progressDialog;
    private ListView appointments_list;

    private LinearLayout emptyListLinear;

    private List<AppointmentListItem> appointmentsList;

    private static final String ARG_USER_ID = "arg_user_id";
    private static final String ARG_APPOINTMENT_STATUS = "appointment_status";
    private static final String ARG_MULTI_APPOINTMENT_ID = "multi_appointment_id";

    private int user_id;
    private int appointment_status;
    private int multi_appointment_id;

    private RestAppointmentServiceRx appointmentServiceRx;
    private RestPatientServiceRx patientServiceRx;
    private RestForServicesRx servicesRestRx;

    private OnAppointmentsFragmentInteractionListener mListener;

    public AppointmentsFragment() {
        // Required empty public constructor
    }

    public static AppointmentsFragment newInstance(int user_id, int status) {
        AppointmentsFragment fragment = new AppointmentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, user_id);
        args.putInt(ARG_APPOINTMENT_STATUS, status);
        args.putInt(ARG_MULTI_APPOINTMENT_ID, 0);
        fragment.setArguments(args);
        return fragment;
    }

    public static AppointmentsFragment newInstance(int multi_appointment_id) {
        AppointmentsFragment fragment = new AppointmentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MULTI_APPOINTMENT_ID, multi_appointment_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getInt(ARG_USER_ID);
            appointment_status = getArguments().getInt(ARG_APPOINTMENT_STATUS);
            multi_appointment_id = getArguments().getInt(ARG_MULTI_APPOINTMENT_ID);
            appointmentServiceRx = new RestAppointmentServiceRx();
            patientServiceRx = new RestPatientServiceRx();
            servicesRestRx = new RestForServicesRx();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointments, container, false);
        appointments_list = (ListView) rootView.findViewById(R.id.list_appointments);
        appointments_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RatingFragment singleDialog =  RatingFragment.newInstance(appointmentsList.get(position).appointment_id);
                singleDialog.setTargetFragment(AppointmentsFragment.this, 10234);
                singleDialog.show(getActivity().getSupportFragmentManager(), "detailsDialog");
            }
        });


        emptyListLinear = (LinearLayout) rootView.findViewById(R.id.linear_empty_list_appointments);
        if (multi_appointment_id == 0) {
            getSingleAppointmentsResponse();
        } else {
            getAppointments(false);
        }

        return rootView;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnAppointmentsFragmentInteractionListener {
        void onAppointmentItemClick(long appointment_id);
    }

    private void getSingleAppointmentsResponse(){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.retrieving_appointments));
        progressDialog.show();

        Observable<Response<List<Appointment>>> callAppointmentsResponse = Observable.empty();
        switch (appointment_status) {
            case Constants.APPOINTMENT_STATUS_PENDING:
                callAppointmentsResponse = appointmentServiceRx.appointmentService.getPendingAppointmentsResponse(user_id);
                break;
            case Constants.APPOINTMENT_STATUS_CONFIRMED:
                callAppointmentsResponse = appointmentServiceRx.appointmentService.getConfirmedAppointmentsResponse(user_id);
                break;
            case Constants.APPOINTMENT_STATUS_COMPLETED:
                callAppointmentsResponse = appointmentServiceRx.appointmentService.getCompletedAppointmentsResponse(user_id);
                break;
            default:
                break;
        }
        callAppointmentsResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Appointment>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(Response<List<Appointment>> listResponse) {
                        if(listResponse.code() == 200){
                            getAppointments(true);
                        } else if(listResponse.code() == 404){
                            appointments_list.setEmptyView(emptyListLinear);
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        } else {
                            Toast.makeText(AppointmentsFragment.this.getContext(),
                                    "appointments fragemnt"+listResponse.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getAppointments(boolean isSingle) {
        appointmentsList = new ArrayList<>();
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.retrieving_appointments));
            progressDialog.show();
        }

        Observable<List<Appointment>> callAppointments = Observable.empty();
        if(isSingle) {
            switch (appointment_status) {
                case Constants.APPOINTMENT_STATUS_PENDING:
                    callAppointments = appointmentServiceRx.appointmentService.getPendingAppointments(user_id);
                    break;
                case Constants.APPOINTMENT_STATUS_CONFIRMED:
                    callAppointments = appointmentServiceRx.appointmentService.getConfirmedAppointments(user_id);
                    break;
                case Constants.APPOINTMENT_STATUS_COMPLETED:
                    callAppointments = appointmentServiceRx.appointmentService.getCompletedAppointments(user_id);
                    break;
                default:
                    break;
            }
        } else {
            callAppointments = appointmentServiceRx.appointmentService.getAppointmentsOfMulti(multi_appointment_id);
        }

        callAppointments
                .flatMapIterable(appointments -> appointments)
                .flatMap(appointment -> Observable.zip(
                            Observable.just(appointment),
                            patientServiceRx.patientService
                                    .getPatientById(appointment.patient_id),
                            servicesRestRx.servicesAPIServiceRx
                                    .getSubserviceById(appointment.subservice_id),
                            this::createListItem
                        ))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    emptyListLinear.setVisibility(View.INVISIBLE);
                    appointments_list
                            .setAdapter(new AppointmentsListAdapter(this.getActivity(),
                                    appointmentsList));

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }, throwable -> {
                    Toast.makeText(getContext(),
                            "i have found 2"+R.string.error,
                            Toast.LENGTH_LONG).show();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });
    }

    private AppointmentListItem createListItem(Appointment app, Patient patient, Subservice subservice) {
        AppointmentListItem item = new AppointmentListItem(app.appointment_id, subservice.subservice_name,
                patient.patient_fullname, app.appointment_datetime);
        appointmentsList.add(item);
        return item;
    }
}
