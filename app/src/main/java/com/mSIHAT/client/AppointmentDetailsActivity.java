package com.mSIHAT.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestAppointmentService;
import com.mSIHAT.client.APIServices.rxretrofit.RestAppointmentServiceRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestForServicesRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestPatientServiceRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestPractitionerServiceRx;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.FragmentCompletedCallback;
import com.mSIHAT.client.fragments.dialogs.FeedbackDialog;
import com.mSIHAT.client.fragments.pagerAdapters.AppointmentDetailFragmentsPagerAdapter;
import com.mSIHAT.client.models.Appointment;
import com.mSIHAT.client.models.Feedback;
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.models.Pratitioner2;
import com.mSIHAT.client.models.Subservice;
import com.mSIHAT.client.models.views.AppointmentDetails;
import com.mSIHAT.client.models.views.PatientAppointment;
import com.mSIHAT.client.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppointmentDetailsActivity extends AppCompatActivity implements FragmentCompletedCallback {
    private int appointment_id;
    private boolean feedbackSubmitted;
    private Bundle bundle;

    private ProgressDialog progressDialog;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private RestAppointmentServiceRx appointmentServiceRx;
    private RestPatientServiceRx patientServiceRx;
    private RestForServicesRx servicesRx;
    private RestPractitionerServiceRx practitionerServiceRx;

    private RestAppointmentService appointmentService;

    private int app_status;
    boolean isMulti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment_details);

        appointment_id = (int) getIntent().getExtras().getLong(Constants.EXTRA_APPOINTMENT_ID);
        isMulti = getIntent().getExtras().getBoolean(Constants.EXTRA_IS_MULTI_APPOINTMENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_activity_appointment_details));

        initializeComponents();
       getAppointmentDetails();

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp_toolbar_action);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppointmentDetailsActivity.this.finish();
            }
        });
    }

    private void initializeComponents() {
        appointmentServiceRx = new RestAppointmentServiceRx();
        patientServiceRx = new RestPatientServiceRx();
        practitionerServiceRx = new RestPractitionerServiceRx();
        servicesRx = new RestForServicesRx();
        appointmentService = new RestAppointmentService();

        bundle = new Bundle();

        viewPager = (ViewPager) findViewById(R.id.viewPager_appointment_details);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_appointment_details);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.appointment_icons_selector);
        tabLayout.getTabAt(1).setIcon(R.drawable.patient_icons_selector);
        tabLayout.getTabAt(2).setIcon(R.drawable.practitioner_icons_selector);
    }

    private void getAppointmentDetails() {
Log.e("appiontment id",String.valueOf(appointment_id));


        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();




        Observable<Appointment> callAppointmentDetails = appointmentServiceRx.appointmentService.getAppointmentById(appointment_id);
        callAppointmentDetails
                .flatMap(appointment -> Observable.zip(

                        Observable.just(appointment),
                        servicesRx.servicesAPIServiceRx.getSubserviceById(appointment.subservice_id),
                        patientServiceRx.patientService.getPatientById(appointment.patient_id),
                        practitionerServiceRx.service.getPractitionerById(appointment.practitioner_id),
                        this::saveDetails))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appo -> {
                    Log.e("AppDetails", "open appointment = " + String.valueOf(appointment_id));
                   viewPager.setAdapter(new AppointmentDetailFragmentsPagerAdapter(getSupportFragmentManager(), this, bundle));
                    Log.e("here","gere");
                    tabLayout.setupWithViewPager(viewPager);
                    Log.e("here","gere2");

                    setupTabIcons();
                    Log.e("here","gere3");
                    progressDialog.dismiss();
                }, throwable -> {
                    Log.e("app",throwable.toString());
                    Toast.makeText(AppointmentDetailsActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();

                    progressDialog.dismiss();
                    AppointmentDetailsActivity.this.finish();
                });





    }

    private Appointment saveDetails(Appointment app, Subservice sub, Patient patient, Pratitioner2 practitioner) {
        AppointmentDetails details = new AppointmentDetails(app.appointment_id, sub.subservice_name,
                app.appointment_datetime, app.status);
        PatientAppointment patientApp = new PatientAppointment(patient.patient_id, patient.patient_fullname,
                patient.patient_nric, patient.patient_address);
        this.app_status = app.status;
        if (app_status == 3) {
            checkForAppointmentFeedback();
        } else {
            refreshMenu();
        }
        bundle.putParcelable(Constants.PARCEL_APPOINTMENT_DETAILS, details);
        bundle.putParcelable(Constants.PARCEL_APPOINTMENT_DETAILS_PATIENT, patientApp);
        bundle.putParcelable(Constants.PARCEL_APPOINTMENT_DETAILS_PRACTITIONER, practitioner);

        return app;
    }

    private void checkForAppointmentFeedback() {
        Call<Feedback> callFeedback = appointmentService.getService().getFeedbackOfAppointment(appointment_id);
        callFeedback.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                if (response.code() == 404) {
                    feedbackSubmitted = false;
                } else if (response.code() == 200) {
                    feedbackSubmitted = true;
                }
                refreshMenu();
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                Toast.makeText(AppointmentDetailsActivity.this,
                        R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshMenu() {
        this.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        switch (app_status) {
            case 1:
                if (!isMulti) {
                    getMenuInflater().inflate(R.menu.menu_pending_appointment_details, menu);
                }
                break;
            case 2:
                getMenuInflater().inflate(R.menu.menu_confirmed_appointment_details, menu);
                break;
            case 3:
                if (!feedbackSubmitted) {
                    getMenuInflater().inflate(R.menu.menu_completed_appointment_details, menu);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_appointment_submit_feedback:
                FeedbackDialog feedbackDialog = FeedbackDialog.newInstance(appointment_id);
                feedbackDialog.show(getSupportFragmentManager(), "feedbackDialog");
                return true;
            case R.id.menu_appointment_cancel:
                cancelAppointment();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cancelAppointment() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.cancelling));
        progressDialog.show();

        Call<Appointment> deleteAppointment = appointmentService.getService().deleteAppointmentById(appointment_id);
        if(deleteAppointment != null) {
            deleteAppointment.enqueue(new Callback<Appointment>() {
                @Override
                public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                    if (response.code() == 200) {
                        Toast.makeText(AppointmentDetailsActivity.this,
                                R.string.youve_cancelled_the_appointment, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        setResult(Activity.RESULT_OK);
                        AppointmentDetailsActivity.this.finish();
                    }
                }

                @Override
                public void onFailure(Call<Appointment> call, Throwable t) {
                    Toast.makeText(AppointmentDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void OnPostCompleted(boolean completed) {
        if (completed) {
            checkForAppointmentFeedback();
        } else {
            Toast.makeText(AppointmentDetailsActivity.this,
                    R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}
