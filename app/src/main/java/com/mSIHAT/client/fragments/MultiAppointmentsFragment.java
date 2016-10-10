package com.mSIHAT.client.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.mSIHAT.client.ListActivity;
import com.mSIHAT.client.MainActivity;
import com.mSIHAT.client.R;
import com.mSIHAT.client.models.MultiAppointment;
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.models.Subservice;
import com.mSIHAT.client.models.views.MultiAppointmentListItem;
import com.mSIHAT.client.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alamchristian on 4/11/16.
 */
public class MultiAppointmentsFragment extends Fragment implements ListView.OnItemClickListener {
    private ProgressDialog progressDialog;
    private ListView appointments_list;

    private LinearLayout emptyListLinear;

    private List<MultiAppointmentListItem> appointmentsList;

    private static final String ARG_USER_ID = "arg_user_id";
    private static final String ARG_APPOINTMENT_STATUS = "appointment_status";

    private int user_id;
    private int appointment_status;

    private RestAppointmentServiceRx appointmentServiceRx;
    private RestPatientServiceRx patientServiceRx;
    private RestForServicesRx servicesRestRx;

    public MultiAppointmentsFragment() {

    }

    public static MultiAppointmentsFragment newInstance(int user_id, int status) {
        MultiAppointmentsFragment fragment = new MultiAppointmentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, user_id);
        args.putInt(ARG_APPOINTMENT_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getInt(ARG_USER_ID);
            appointment_status = getArguments().getInt(ARG_APPOINTMENT_STATUS);
            appointmentServiceRx = new RestAppointmentServiceRx();
            patientServiceRx = new RestPatientServiceRx();
            servicesRestRx = new RestForServicesRx();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointments, container, false);
        appointments_list = (ListView) rootView.findViewById(R.id.list_appointments);
        emptyListLinear = (LinearLayout) rootView.findViewById(R.id.linear_empty_list_appointments);
        getMultiAppointmentsResponse();
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getActivity(), ListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_MULTI_APPOINTMENT_ID, (int) id);
        bundle.putInt(Constants.EXTRA_APPOINTMENT_STATUS, appointment_status);
        bundle.putString(Constants.EXTRA_DETAILS_PURPOSE, Constants.EXTRA_DETAILS_PURPOSE_MULTI_APPOINTMENTS);
        intent.putExtras(bundle);
        startActivityForResult(intent, MainActivity.ACTIVITY_RESULT_CONTENT_CHANGED);
    }

    private void getMultiAppointmentsResponse(){
        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.retrieving_appointments));
        progressDialog.show();

        Observable<Response<List<MultiAppointment>>> callAppointmentsResponse = Observable.empty();
        switch (appointment_status) {
            case Constants.APPOINTMENT_STATUS_PENDING:
                callAppointmentsResponse = appointmentServiceRx.appointmentService.getPendingMultiAppointmentsResponse(user_id);
                break;
            case Constants.APPOINTMENT_STATUS_CONFIRMED:
                callAppointmentsResponse = appointmentServiceRx.appointmentService.getConfirmedMultiAppointmentsResponse(user_id);
                break;
            case Constants.APPOINTMENT_STATUS_COMPLETED:
                callAppointmentsResponse = appointmentServiceRx.appointmentService.getCompletedMultiAppointmentsResponse(user_id);
                break;
            default:
                break;
        }
        callAppointmentsResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<MultiAppointment>>>() {
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
                    public void onNext(Response<List<MultiAppointment>> listResponse) {
                        if(listResponse.code() == 200){
                            emptyListLinear.setVisibility(View.INVISIBLE);
                            getMultiAppointments();
                        } else if(listResponse.code() == 404){
                            appointments_list.setEmptyView(emptyListLinear);
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        } else {
                            Toast.makeText(MultiAppointmentsFragment.this.getContext(),
                                    listResponse.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getMultiAppointments() {
        appointmentsList = new ArrayList<>();

        Observable<List<MultiAppointment>> callAppointments = Observable.empty();
        switch (appointment_status) {
            case Constants.APPOINTMENT_STATUS_PENDING:
                callAppointments = appointmentServiceRx.appointmentService.getPendingMultiAppointments(user_id);
                break;
            case Constants.APPOINTMENT_STATUS_CONFIRMED:
                callAppointments = appointmentServiceRx.appointmentService.getConfirmedMultiAppointments(user_id);
                break;
            case Constants.APPOINTMENT_STATUS_COMPLETED:
                callAppointments = appointmentServiceRx.appointmentService.getCompletedMultiAppointments(user_id);
                break;
            default:
                break;
        }
/*
        callAppointments
                .flatMapIterable(apps -> apps)
                .flatMap(app -> Observable.zip(
                        Observable.just(app),
                        servicesRestRx.servicesAPIServiceRx.getSubserviceById(app.subservice_id),
                        patientServiceRx.patientService.getPatientById(app.patient_id),
                        this::createListItem
                ))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    appointments_list.setAdapter(new MultiAppointmentsListAdapter(this.getActivity(), appointmentsList));
                    appointments_list.setOnItemClickListener(this);
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }, throwable -> {
                    Toast.makeText(MultiAppointmentsFragment.this.getContext(), "multi"+R.string.error, Toast.LENGTH_SHORT).show();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });

                */
    }

    private MultiAppointmentListItem createListItem(MultiAppointment app, Subservice sub, Patient pat) {
        MultiAppointmentListItem item = new MultiAppointmentListItem(app.multi_appointment_id,
                sub.subservice_name, pat.patient_fullname, app.total);
        appointmentsList.add(item);
        return item;
    }
}
