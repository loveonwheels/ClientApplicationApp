package com.mSIHAT.client.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestAppointmentService;
import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.APIServices.rxretrofit.RestAddressServiceRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestPatientServiceRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestPractitionerServiceRx;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.PatientConditionDialog;
import com.mSIHAT.client.fragments.dialogs.SlotDialogFragment;
import com.mSIHAT.client.fragments.dialogs.Suggestpersonnel;
import com.mSIHAT.client.listAdapters.AvailablePractitionersAvailListAdapter;
import com.mSIHAT.client.listAdapters.AvailablePractitionersListAdapter;
import com.mSIHAT.client.models.Appointment;
import com.mSIHAT.client.models.Appointment2;
import com.mSIHAT.client.models.ConditionPost;
import com.mSIHAT.client.models.Expertise;
import com.mSIHAT.client.models.MultiAppointment;
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.models.PractitionerAvail;
import com.mSIHAT.client.models.address.Postcode;
import com.mSIHAT.client.models.Practitioner;
import com.mSIHAT.client.models.views.PractitionerAvailable;
import com.mSIHAT.client.utils.Constants;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alamchristian on 4/1/16.
 */
public class AvailablePractitionersFragment extends Fragment {
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(Constants.PAYPAL_CLIENT_ID);
    private RestPractitionerService restPracService = new RestPractitionerService();
    public static final int CONDITION_DIALOG = 1;
    public static final int PAYMENT_REQUEST_CODE = 201;
    private static final int RequestCode2 = 13402;
    private ProgressDialog progressDialog;
    private ListView practitioners_list;
    List<PractitionerAvail> userAppiontments;
    List<Practitioner> pracList;
    private List<PractitionerAvailable> practitioners;
int listselectiontype = 0;
    private Bundle query_bundle;
    private boolean isMulti;
    int[] condition_ids;
Fragment thisfragment = this;
    private Appointment2 new_appointment;
    private ConditionPost app_condition;
    private int new_multi_app_id;
int selectionposition;
    private RestPatientServiceRx patientServiceRx;
    private RestAddressServiceRx addressServiceRx;
    private RestPractitionerServiceRx practitionerServiceRx;

    private RestAppointmentService appointmentService;

    private FragmentActivity callingActivity;

    private FragmentCompletedCallback mListener;

    public AvailablePractitionersFragment() {

    }

    public static AvailablePractitionersFragment newInstance(Bundle query_bundle) {
        AvailablePractitionersFragment fragment = new AvailablePractitionersFragment();
        fragment.setArguments(query_bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentCompletedCallback) {
            mListener = (FragmentCompletedCallback) context;
            callingActivity = (FragmentActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentCompletedCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            query_bundle = getArguments();
            isMulti = query_bundle.getBoolean(Constants.EXTRA_IS_MULTI_APPOINTMENT);
            patientServiceRx = new RestPatientServiceRx();
            addressServiceRx = new RestAddressServiceRx();
            practitionerServiceRx = new RestPractitionerServiceRx();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);

            Intent intent = new Intent(this.getActivity(), PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            getActivity().startService(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_available_practitioners, container, false);
        findViewById(rootView);
        Log.e("here1","here1");
        thisfragment = this;
        getAvailablePractitioners();
Log.e("here1","here1");
        return rootView;
    }

    private void findViewById(View view) {
        practitioners_list = (ListView) view.findViewById(R.id.list_available_practitioners);


        practitioners_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectionposition = position;
                if(listselectiontype == 1){

                    Log.e("open available time",String.valueOf(userAppiontments.get(0).starttime));
                    Log.e("open available time",String.valueOf(userAppiontments.get(0).unavailableslot.get(0)));

                    FragmentManager manager = getFragmentManager();
int unavailableslot[] = new int[userAppiontments.get(position).unavailableslot.size()];
                    for(int i = 0;i<userAppiontments.get(position).unavailableslot.size();i++){
                           unavailableslot[i] = userAppiontments.get(position).unavailableslot.get(i);

                    }
                    SlotDialogFragment slotDialogFragment = SlotDialogFragment.newInstance(RequestCode2,userAppiontments.get(position).starttime,userAppiontments.get(position).endtime,unavailableslot);
                    slotDialogFragment.setTargetFragment(thisfragment,RequestCode2);
                    slotDialogFragment.show(manager, "timeslots");


                  /*  FragmentManager fm = getActivity().getSupportFragmentManager();
                    AvailableTime availableTime = AvailableTime.newInstance(new ArrayList<AvailableTimeClass>(userAppiontments.get(0).unavailabletime);
                    availableTime.setTargetFragment(AvailablePractitionersFragment.this, 1438);
                    availableTime.show(fm, "fragment_avaiable_times");
*/


                }else {
                    new_appointment = new Appointment2();
                    new_appointment.practitioner_id = pracList.get(position).hcpid;
                    new_appointment.slot_id = Integer.parseInt(pracList.get((int) position).phonenumber);
                    Bundle bundle = new Bundle();
                    // bundle.putInt(Constants.EXTRA_SUBSERVICE_ID, query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID));
                    bundle.putInt(Constants.EXTRA_SUBSERVICE_ID, 60);
                    bundle.putInt(Constants.EXTRA_PRACTITIONER_ID, pracList.get((int) position).hcpid);
                    bundle.putInt(Constants.EXTRA_SLOT_ID, Integer.parseInt(pracList.get((int) position).phonenumber));
                    if (isMulti) {
                        bundle.putInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT, query_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT));
                    }
                    PatientConditionDialog conditionDialog = PatientConditionDialog.newInstance(bundle);
                    conditionDialog.setTargetFragment(AvailablePractitionersFragment.this, CONDITION_DIALOG);
                    conditionDialog.show(callingActivity.getSupportFragmentManager(), "conditionDialog");
                }
            }
        });
    }

    /*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new_appointment = new Appointment();
        new_appointment.practitioner_id = (int) id;
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_SUBSERVICE_ID, query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID));
        bundle.putInt(Constants.EXTRA_PRACTITIONER_ID, (int) id);
        if(isMulti){
            bundle.putInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT, query_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT));
        }
        PatientConditionDialog conditionDialog = PatientConditionDialog.newInstance(bundle);
        conditionDialog.setTargetFragment(AvailablePractitionersFragment.this, CONDITION_DIALOG);
        conditionDialog.show(callingActivity.getSupportFragmentManager(), "conditionDialog");
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RequestCode2:
                Log.e("herer124","Sdsd");
                query_bundle.putInt(Constants.EXTRA_APPOINTMENT_TIME,data.getIntExtra("timevalue", 0));
                new_appointment = new Appointment2();
                new_appointment.practitioner_id = userAppiontments.get(selectionposition).hcpid;
                new_appointment.slot_id = Integer.parseInt(userAppiontments.get((int) selectionposition).phonenumber);
                Bundle bundle = new Bundle();
                // bundle.putInt(Constants.EXTRA_SUBSERVICE_ID, query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID));
                bundle.putInt(Constants.EXTRA_SUBSERVICE_ID, 60);
                bundle.putInt(Constants.EXTRA_PRACTITIONER_ID, userAppiontments.get((int) selectionposition).hcpid);
                bundle.putInt(Constants.EXTRA_SLOT_ID, Integer.parseInt(userAppiontments.get((int) selectionposition).phonenumber));
                if (isMulti) {
                    bundle.putInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT, query_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT));
                }
                PatientConditionDialog conditionDialog = PatientConditionDialog.newInstance(bundle);
                conditionDialog.setTargetFragment(AvailablePractitionersFragment.this, CONDITION_DIALOG);
                conditionDialog.show(callingActivity.getSupportFragmentManager(), "conditionDialog");

                break;
            case CONDITION_DIALOG:
                if(resultCode == Activity.RESULT_OK){
                    /// PAYMENT_INTENT_SALE will cause the payment to complete immediately.
                    // Change PAYMENT_INTENT_SALE to
                    //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
                    //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
                    //     later via calls from your server.
                    condition_ids = data.getIntArrayExtra(PatientConditionDialog.PATIENT_CONDITIONS);

                    PayPalPayment payment = new PayPalPayment(new BigDecimal(data.getDoubleExtra(Constants.EXTRA_FINAL_RATE, 0)),
                            "MYR", "Msihat servive appointment",
                            PayPalPayment.PAYMENT_INTENT_SALE);

                    Intent intent = new Intent(this.getActivity(), PaymentActivity.class);

                    // send the same configuration for restart resiliency
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                    startActivityForResult(intent, PAYMENT_REQUEST_CODE);
                }
                break;
            case PAYMENT_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    PaymentConfirmation confirm_payment = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if(confirm_payment != null){
                        try {
                            Log.i("paymentExample", confirm_payment.toJSONObject().toString(4));
                            Log.e("fdfdfdf2323","dfdfdf1234");
                            new_appointment.patient_id = query_bundle.getInt(Constants.EXTRA_PATIENT_ID);
                            Log.e("fdfdfdf2323","dfdfdf123");
                            new_appointment.status = 1;

                            new_appointment.subservice_id = query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID);

                            Log.e("fdfdfdf2323",String.valueOf(query_bundle.getInt(Constants.EXTRA_APPOINTMENT_TIME)));
                            Log.e("fdfdfdf",query_bundle.getString(Constants.EXTRA_APPOINTMENT_DATE));

                            new_appointment.appointment_date = query_bundle.getString(Constants.EXTRA_APPOINTMENT_DATE);
                            new_appointment.appointment_start_time = query_bundle.getInt(Constants.EXTRA_APPOINTMENT_TIME,0);
                            new_appointment.appointment_end_time = (query_bundle.getInt(Constants.EXTRA_APPOINTMENT_TIME,0)+1);
                            appointmentService = new RestAppointmentService();

                            if(isMulti){
                                postNewMultiAppointment();
                            } else {
                                postNewSingleAppointment();
                            }

                        } catch (JSONException e) {
                            Log.e("payment_paypal", "error: ", e);
                        }
                    }
                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.i("payment_paypal", "The user canceled.");
                }
                else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                    Log.i("payment_paypal", "An invalid Payment or PayPalConfiguration was submitted. Refer to the documentation.");
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(this.getActivity(), PayPalService.class));
        super.onDestroy();
    }

    private void getAvailablePractitioners() {
        progressDialog.setMessage(callingActivity.getString(R.string.searching_for_practitioners));
        progressDialog.show();
        Log.e("here2","here2");
        getPatientPostcode(query_bundle.getInt(Constants.EXTRA_PATIENT_ID));
    }

    private void getPatientPostcode(int patient_id) {
        Log.e("here3","here3");
        Observable<Patient> patientObservable = patientServiceRx.patientService.getPatientById(patient_id);
        patientObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(patient -> {
                    getPatientCity(patient.postcode_id);
                }, throwable -> {
                    Log.e("here4","here4");
                    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                           "error3", Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                });
    }

    private void getPatientCity(int postcode_id) {
        Log.e("here5","here5");
        Observable<Postcode> postcodeObservable = addressServiceRx.service.getPostcodeById(postcode_id);
        postcodeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postcode -> {
                    query_bundle.putInt(Constants.EXTRA_CITY_ID, postcode.city_id);
                    getPractitioners();
                }, throwable -> {
                    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                            R.string.error, Toast.LENGTH_SHORT).show();
                    Log.e("here6",throwable.getMessage());
                    throwable.printStackTrace();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });
    }

    private void getPractitionersResponse(){
        Log.e("here7","here7");
        int subservice_id = query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID);
        int city_id = query_bundle.getInt(Constants.EXTRA_CITY_ID);
        String apiDatetime = formatDateForAPI(query_bundle.getString(Constants.EXTRA_APPOINTMENT_DATETIME));
        Observable<Response<List<Practitioner>>> availableResponse = Observable.empty();
        Log.e("here8","here8");
        if(isMulti) {
            boolean isDaily = query_bundle.getBoolean(Constants.EXTRA_MULTI_APPOINTMENT_FREQUENCY);
            int amount = query_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT);
            availableResponse = practitionerServiceRx.service.getAvailablePractitionersForMultiResponse(subservice_id, city_id,
                    apiDatetime, String.valueOf(isDaily), amount);
        } else {
            availableResponse = practitionerServiceRx.service.getAvailablePractitionersResponse(subservice_id,
                    city_id, apiDatetime);
        }
        availableResponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Practitioner>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("here9","here9");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("here10","here10");
                        e.printStackTrace();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(Response<List<Practitioner>> listResponse) {
                        if(listResponse.code() == 200){
                            Log.e("here11","here11");
                            getPractitioners();

                        } else if(listResponse.code() == 404){

                        //    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                                 //   R.string.no_available_practitioners, Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            Suggestpersonnel editNameDialog = new Suggestpersonnel();

                            editNameDialog.setTargetFragment(AvailablePractitionersFragment.this, 1337);
                            editNameDialog.show(fm, "fragment_suggestpersonnel");
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                           // mListener.OnPostCompleted(false);

                        } else {
                            Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                                    listResponse.message(), Toast.LENGTH_SHORT).show();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            mListener.OnPostCompleted(false);
                        }
                    }
                });
    }

    public void searchfordifferenttime(){
        Log.e("here121","here121");
        listselectiontype = 1;
        practitioners = new ArrayList<>();
        Log.e("here122","here122");
        int subservice_id = query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID);
        Log.e("here123","here123");
        int city_id = query_bundle.getInt(Constants.EXTRA_CITY_ID);
        Log.e("here124","here124");
        String search_date = query_bundle.getString(Constants.EXTRA_APPOINTMENT_DATE);
        Log.e("here125","here1215");
        Observable<List<Practitioner>> availablePractitioners = Observable.empty();
        Log.e("here12","here12");
        if(isMulti){
            Log.e("here13","here13");
            boolean isDaily = query_bundle.getBoolean(Constants.EXTRA_MULTI_APPOINTMENT_FREQUENCY);
            int amount = query_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT);
            availablePractitioners = practitionerServiceRx.service.getAvailablePractitionersForMulti(subservice_id, city_id,
                    search_date, String.valueOf(isDaily), amount);
        } else {
            Log.e("here14","here14");
            availablePractitioners = practitionerServiceRx.service.getAvailablePractitioners(subservice_id,
                    city_id, search_date);
        }

        Log.e("here14",String.valueOf(search_date));
        Call<List<PractitionerAvail>> call = restPracService.getService().getAvailablePractitionersForDate(subservice_id,
                city_id, search_date);

        call.enqueue(new Callback<List<PractitionerAvail>>() {
            @Override
            public void onResponse(Call<List<PractitionerAvail>> call, Response<List<PractitionerAvail>> response) {
                int statusCode = response.code();
                 userAppiontments = response.body();
                String msg = "here";



                if (statusCode == 200) {



                    if(userAppiontments.size() > 0){

                        Log.e("here21","here21 SUGGEST TIME");
                        Log.e("here21",String.valueOf(userAppiontments.get(0).starttime));
                        practitioners_list.setAdapter(new AvailablePractitionersAvailListAdapter(getActivity().getBaseContext(), userAppiontments));
                        //         practitioners_list.setOnItemClickListener(getActivity());


                        progressDialog.dismiss();
                    }else{

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                        builder.setMessage("We are sorry, but no hcp is avaliable")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();

                        Log.e("here211","here211");
                        progressDialog.dismiss();
                    }


                    //  progress.dismiss();
                }else{
                    Log.e("dfdf2", String.valueOf(statusCode));
                    msg="error again";
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"request failed 12",Toast.LENGTH_LONG).show();

                }
                Log.e("dfdf1", msg);

            }



            @Override
            public void onFailure(Call<List<PractitionerAvail>> call, Throwable t) {
                //   progress.dismiss();
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"request failed",Toast.LENGTH_LONG).show();

                  Log.e("dfdf", t.toString());
            }
        });
     /*   availablePractitioners
                .flatMapIterable(pracs -> pracs)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {

                    if(practitioners.size() > 0) {
                        Log.e("here17","here17");
                        practitioners_list.setAdapter(new AvailablePractitionersListAdapter(this.getActivity(), practitioners));
                        practitioners_list.setOnItemClickListener(this);
                    }else{

                        Log.e("here18","here18");
                    }
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }, throwable -> {
                    Log.e("here16","here16");
                    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                           throwable.toString(), Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                    Log.e("here161", throwable.toString());
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });
                */


    }

    public void searchfordifferentdate(){
        Log.e("here121","here121");
        listselectiontype = 1;
        practitioners = new ArrayList<>();
        Log.e("here122","here122");
        int subservice_id = query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID);
        Log.e("here123","here123");
        int city_id = query_bundle.getInt(Constants.EXTRA_CITY_ID);
        Log.e("here124","here124");
        int search_time = query_bundle.getInt(Constants.EXTRA_APPOINTMENT_TIME);
        Log.e("here125","here1215");
        Observable<List<Practitioner>> availablePractitioners = Observable.empty();
        Log.e("here12","here12");
        if(isMulti){
            Log.e("here13","here13");
            boolean isDaily = query_bundle.getBoolean(Constants.EXTRA_MULTI_APPOINTMENT_FREQUENCY);
            int amount = query_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT);
          //  availablePractitioners = practitionerServiceRx.service.getAvailablePractitionersForMulti(subservice_id, city_id,
                 //   search_time, String.valueOf(isDaily), amount);
        } else {
            Log.e("here14","here14");
           // availablePractitioners = practitionerServiceRx.service.getAvailablePractitioners(subservice_id,
                   // city_id, search_time);
        }

        Log.e("here14",String.valueOf(search_time));
        Call<List<PractitionerAvail>> call = restPracService.getService().getAvailablePractitionersForTime(subservice_id,
                city_id, search_time);

        call.enqueue(new Callback<List<PractitionerAvail>>() {
            @Override
            public void onResponse(Call<List<PractitionerAvail>> call, Response<List<PractitionerAvail>> response) {
                int statusCode = response.code();
                userAppiontments = response.body();
                String msg = "here";



                if (statusCode == 200) {



                    if(userAppiontments.size() > 0){

                        Log.e("here21","here21 SUGGEST TIME");
                        Log.e("here21",String.valueOf(userAppiontments.get(0).starttime));
                        practitioners_list.setAdapter(new AvailablePractitionersAvailListAdapter(getActivity().getBaseContext(), userAppiontments));
                        //         practitioners_list.setOnItemClickListener(getActivity());


                        progressDialog.dismiss();
                    }else{

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                        builder.setMessage("We are sorry, but no hcp is avaliable")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();

                        Log.e("here211","here211");
                        progressDialog.dismiss();
                    }


                    //  progress.dismiss();
                }else{
                    Log.e("dfdf2", String.valueOf(statusCode));
                    msg="error again";
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"request failed 12",Toast.LENGTH_LONG).show();

                }
                Log.e("dfdf1", msg);

            }



            @Override
            public void onFailure(Call<List<PractitionerAvail>> call, Throwable t) {
                //   progress.dismiss();
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"request failed",Toast.LENGTH_LONG).show();

                Log.e("dfdf", t.toString());
            }
        });
     /*   availablePractitioners
                .flatMapIterable(pracs -> pracs)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {

                    if(practitioners.size() > 0) {
                        Log.e("here17","here17");
                        practitioners_list.setAdapter(new AvailablePractitionersListAdapter(this.getActivity(), practitioners));
                        practitioners_list.setOnItemClickListener(this);
                    }else{

                        Log.e("here18","here18");
                    }
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }, throwable -> {
                    Log.e("here16","here16");
                    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                           throwable.toString(), Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                    Log.e("here161", throwable.toString());
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });
                */


    }


    private void getPractitioners(){
        Log.e("here121","here121");

        listselectiontype = 0;
        practitioners = new ArrayList<>();
        Log.e("here122","here122");
        int subservice_id = query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID);
        Log.e("here123","here123");
        int city_id = query_bundle.getInt(Constants.EXTRA_CITY_ID);
        Log.e("here124","here124");
        String DateReq = query_bundle.getString(Constants.EXTRA_APPOINTMENT_DATE);

        int timeReq = query_bundle.getInt(Constants.EXTRA_APPOINTMENT_TIME);
        Log.e("here125","here1215");
        Observable<List<Practitioner>> availablePractitioners = Observable.empty();
        Log.e("here12","here12");
        if(isMulti){
            Log.e("here13","here13");
            boolean isDaily = query_bundle.getBoolean(Constants.EXTRA_MULTI_APPOINTMENT_FREQUENCY);
            int amount = query_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT);
           // availablePractitioners = practitionerServiceRx.service.getAvailablePractitionersForMulti(subservice_id, city_id,
                   // apiDatetime, String.valueOf(isDaily), amount);
        } else {
            Log.e("here14","here14");
          //  availablePractitioners = practitionerServiceRx.service.getAvailablePractitioners(subservice_id,
                    //city_id, apiDatetime);
        }

        Log.e("here114","here114");

        Log.e("dfdfdf",String.valueOf(subservice_id));
        Log.e("dfdfdf",String.valueOf(city_id));
        Log.e("dfdfdf",String.valueOf(DateReq));
        Log.e("dfdfdf",String.valueOf(timeReq));


        Call<List<Practitioner>> call = restPracService.getService().getAvailablePractitioners(subservice_id,
                city_id, DateReq,timeReq,0,0);
        Log.e("here144","here144");

        call.enqueue(new Callback<List<Practitioner>>() {
            @Override
            public void onResponse(Call<List<Practitioner>> call, Response<List<Practitioner>> response) {

                Log.e("hereunkown","2324");
                int statusCode = response.code();
               pracList = response.body();
                String msg = "here";



                if (statusCode == 200) {



                    if(pracList.size() > 0){

                       Log.e("here21","here21");

                        practitioners_list.setAdapter(new AvailablePractitionersListAdapter(getActivity().getBaseContext(), pracList));

               //         practitioners_list.setOnItemClickListener(getActivity());


                        progressDialog.dismiss();
                    }else{


                        Log.e("here211","here211");
                        progressDialog.dismiss();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Suggestpersonnel editNameDialog = new Suggestpersonnel();

                        editNameDialog.setTargetFragment(AvailablePractitionersFragment.this, 1337);
                        editNameDialog.show(fm, "fragment_suggestpersonnel");
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();


                    }


                    //  progress.dismiss();
                }else{
                    Log.e("dfdf2", String.valueOf(statusCode));
                    msg="error again";
                    progressDialog.dismiss();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Suggestpersonnel editNameDialog = new Suggestpersonnel();

                    editNameDialog.setTargetFragment(AvailablePractitionersFragment.this, 1337);
                    editNameDialog.show(fm, "fragment_suggestpersonnel");
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                }
                Log.e("dfdf1", msg);

            }



            @Override
            public void onFailure(Call<List<Practitioner>> call, Throwable t) {
                //   progress.dismiss();
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"request failed",Toast.LENGTH_LONG).show();

                //   Log.e("dfdf", t.toString());
            }
        });
     /*   availablePractitioners
                .flatMapIterable(pracs -> pracs)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {

                    if(practitioners.size() > 0) {
                        Log.e("here17","here17");
                        practitioners_list.setAdapter(new AvailablePractitionersListAdapter(this.getActivity(), practitioners));
                        practitioners_list.setOnItemClickListener(this);
                    }else{

                        Log.e("here18","here18");
                    }
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }, throwable -> {
                    Log.e("here16","here16");
                    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                           throwable.toString(), Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                    Log.e("here161", throwable.toString());
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });
                */
    }

    private String formatDateForAPI(String datetime){
        String apiDatetime = null;
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.TAIWAN).parse(datetime);
            apiDatetime = new SimpleDateFormat("yyyyMMdd'time'HHmm", Locale.TAIWAN).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return apiDatetime;
    }

    private String formatDateForPostAppointment(String datetime){
        String postDatetime = null;
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.TAIWAN).parse(datetime);
            postDatetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return postDatetime;
    }

    private PractitionerAvailable createListItem(Practitioner practitioner, Expertise expertise){
        PractitionerAvailable item = new PractitionerAvailable(getContext(), practitioner.hcpid,
                practitioner.name, practitioner.level, expertise.expertise_level);
        practitioners.add(item);
        return item;
    }


    private void postNewSingleAppointment(){
        progressDialog.setMessage(callingActivity.getString(R.string.submitting_your_booking));
        progressDialog.show();
      //  Log.e("appiontme",new_appointment.additionalinformation);
        new_appointment.subservice_id = 1;
        Call<String> postAppointment = appointmentService.getService().postAppointment(new_appointment);
        postAppointment.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 201){

                 //  app_condition.appointment_id = response.body().appointment_id;
                  //  if(app_condition.appointment_id != 0)
                      //  postAppointmentCondition();
                    mListener.OnPostCompleted(true);
                }
                else {
                    mListener.OnPostCompleted(false);
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mListener.OnPostCompleted(false);
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    private void postNewMultiAppointment(){
        progressDialog.setMessage(callingActivity.getString(R.string.submitting_your_booking));
        progressDialog.show();
        boolean isDaily = query_bundle.getBoolean(Constants.EXTRA_MULTI_APPOINTMENT_FREQUENCY);
        int amount = query_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT);
        Call<MultiAppointment> postMultiAppointment = appointmentService.getService().postMultiAppointment(String.valueOf(isDaily),
                amount, new_appointment);
        postMultiAppointment.enqueue(new Callback<MultiAppointment>() {
            @Override
            public void onResponse(Call<MultiAppointment> call, Response<MultiAppointment> response) {
                if(response.code() == 201){
                    new_multi_app_id = response.body().multi_appointment_id;
                    if(new_multi_app_id != 0)
                        postMultiAppointmentCondition();
                }
                else {
                    mListener.OnPostCompleted(false);
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MultiAppointment> call, Throwable t) {
                mListener.OnPostCompleted(false);
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    private void postAppointmentCondition(){
        Call<ConditionPost> postCondition = appointmentService.getService().postAppointmentCondition(app_condition);
        postCondition.enqueue(new Callback<ConditionPost>() {
            @Override
            public void onResponse(Call<ConditionPost> call, Response<ConditionPost> response) {
                if(response.code() == 201){
                    mListener.OnPostCompleted(true);
                } else {
                    mListener.OnPostCompleted(false);
                }
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ConditionPost> call, Throwable t) {
                mListener.OnPostCompleted(false);
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    private void postMultiAppointmentCondition(){
        Call<ConditionPost> postMultiCondition = appointmentService.getService().postMultiAppointmentCondition(new_multi_app_id,
                app_condition);
        postMultiCondition.enqueue(new Callback<ConditionPost>() {
            @Override
            public void onResponse(Call<ConditionPost> call, Response<ConditionPost> response) {
                if(response.code() == 201){
                    mListener.OnPostCompleted(true);
                } else {
                    mListener.OnPostCompleted(false);
                    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                            String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ConditionPost> call, Throwable t) {
                mListener.OnPostCompleted(false);
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }
}
