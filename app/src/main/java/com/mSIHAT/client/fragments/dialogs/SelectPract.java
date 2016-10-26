package com.mSIHAT.client.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestAppointmentService;
import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.APIServices.rxretrofit.RestAddressServiceRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestPatientServiceRx;
import com.mSIHAT.client.APIServices.rxretrofit.RestPractitionerServiceRx;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.AdvSearch;
import com.mSIHAT.client.fragments.FragmentCompletedCallback;
import com.mSIHAT.client.listAdapters.AvailablePractitionersAvailListAdapter;
import com.mSIHAT.client.listAdapters.AvailablePractitionersListAdapter;
import com.mSIHAT.client.models.Appointment;
import com.mSIHAT.client.models.Appointment2;
import com.mSIHAT.client.models.ConditionPost;
import com.mSIHAT.client.models.Date.DateDialog;
import com.mSIHAT.client.models.MultiAppointment;
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.models.Practitioner;
import com.mSIHAT.client.models.PractitionerAvail;
import com.mSIHAT.client.models.PractitionerPartial;
import com.mSIHAT.client.models.TimeSlots;
import com.mSIHAT.client.models.address.Postcode;
import com.mSIHAT.client.models.views.PractitionerAvailable;
import com.mSIHAT.client.utils.Constants;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectPract.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectPract#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectPract extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Fragment thisfragement;
    TimeSlots starttime;
    private static final int RequestCode1 = 14401;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView input_sch_time;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constants.PAYPAL_CLIENT_ID);
    private RestPractitionerService restPracService = new RestPractitionerService();
    public static final int CONDITION_DIALOG = 173;
    public static final int PAYMENT_REQUEST_CODE = 201;
    private static final int RequestCode2 = 13402;
Spinner gender_spinner,frq_spinner;
    int gender = 0;
    int frequency = 0;
    private ProgressDialog progressDialog;
    private ListView practitioners_list;
    List<PractitionerAvail> userAppiontments;
    List<Practitioner> pracList;
    private List<PractitionerAvailable> practitioners;
    int listselectiontype = 0;
    private Bundle query_bundle;
    private boolean isMulti;
    int[] condition_ids;
    TextView input_sch_date;
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

    public SelectPract() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectPract.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectPract newInstance(int param1, int param2) {
        SelectPract fragment = new SelectPract();
        Bundle args = new Bundle();


        args.putInt(Constants.EXTRA_PATIENT_ID, param1);
        args.putInt(Constants.EXTRA_SUBSERVICE_ID, param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            query_bundle = getArguments();
            patientServiceRx = new RestPatientServiceRx();
            addressServiceRx = new RestAddressServiceRx();
            practitionerServiceRx = new RestPractitionerServiceRx();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);

            Intent intent = new Intent(this.getActivity(), PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            getActivity().startService(intent);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.alerttheme2);


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_select_pract, container, false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        thisfragment = this;
  input_sch_date = (TextView)view.findViewById(R.id.Sch_Date);
        input_sch_time = (TextView)view.findViewById(R.id.Sch_Time);
        gender_spinner = (Spinner)view.findViewById(R.id.spinner_gender);
                frq_spinner = (Spinner)view.findViewById(R.id.spinner_frq);
        ImageView bckBtn = (ImageView) view.findViewById(R.id.imgBack);
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LinearLayout lin = (LinearLayout) view.findViewById(R.id.Lin_Date);
        thisfragement = this;
        LinearLayout linTime = (LinearLayout) view.findViewById(R.id.Lin_Time);
        practitioners_list = (ListView) view.findViewById(R.id.prac_list);
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
                    conditionDialog.setTargetFragment(SelectPract.this, CONDITION_DIALOG);
                    conditionDialog.show(getActivity().getSupportFragmentManager(), "conditionDialog");
                }
            }
        });

        FloatingActionButton search = (FloatingActionButton)view.findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvailablePractitioners();
            }
        });


        setDate(input_sch_date,view,lin);
linTime.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

            FragmentManager manager = getFragmentManager();

            SlotDialogFragment slotDialogFragment = SlotDialogFragment.newInstance(RequestCode1,0,23);
            slotDialogFragment.setTargetFragment(thisfragement,RequestCode1);
            slotDialogFragment.show(manager, "timeslots");

    }
});


        setSpinners();

        return view;
    }



    private void getAvailablePractitioners() {
        progressDialog.setMessage("searching for practitioner");
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
                    Toast.makeText(getActivity(),
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
                    Toast.makeText(getContext(),
                            R.string.error, Toast.LENGTH_SHORT).show();
                    Log.e("here6",throwable.getMessage());
                    throwable.printStackTrace();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });
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
        String DateReq = input_sch_date.getText().toString();

        int timeReq = starttime.getTimevalue();
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
                city_id, DateReq,timeReq,gender,frequency);
        Log.e("gender4",String.valueOf(gender));

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
                        listselectiontype = 2;
                        practitioners_list.setAdapter(new AvailablePractitionersListAdapter(getActivity().getBaseContext(), pracList));

                        //         practitioners_list.setOnItemClickListener(getActivity());


                        progressDialog.dismiss();
                    }else{

                        Log.e("no appointmnet","no appointment found");
                        /*
                        Log.e("here211","here211");
                        progressDialog.dismiss();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Suggestpersonnel editNameDialog = new Suggestpersonnel();

                        editNameDialog.setTargetFragment(SelectPract.this, 1337);
                        editNameDialog.show(fm, "fragment_suggestpersonnel");
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
*/

                    }


                    //  progress.dismiss();
                }else{

                    Log.e("dfdf2", String.valueOf(statusCode));

                    Log.e("here211","here211");

                    if(frequency == 0){
                        progressDialog.dismiss();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Suggestpersonnel editNameDialog = new Suggestpersonnel();

                        editNameDialog.setTargetFragment(SelectPract.this, 1337);
                        editNameDialog.show(fm, "fragment_suggestpersonnel");
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }else{

                        //check for partial match
                        partialmatch();
                    }



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



    public void setDate(final TextView input_dob, View view, LinearLayout linear){
        input_dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    DateDialog.getDate2(getActivity().getFragmentManager(), input_dob);

                }
            }
        });
        input_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DateDialog.getDate2(getActivity().getFragmentManager(),input_dob);

            }
        });

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DateDialog.getDate2(getActivity().getFragmentManager(),input_dob);

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("paypal got here","here2");
        Log.e("paypal got here",String.valueOf(requestCode));
        Log.e("paypal got here",String.valueOf(resultCode));
        switch (requestCode){

            case RequestCode1:
                starttime = new TimeSlots(data.getIntExtra("timevalue", 0), true);
                input_sch_time.setText(starttime.getTimeStringvalue());

                break;
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
                conditionDialog.setTargetFragment(SelectPract.this, CONDITION_DIALOG);
                conditionDialog.show(getActivity().getSupportFragmentManager(), "conditionDialog");

                break;
            case CONDITION_DIALOG:
                Log.e("paypal is here","com3");

                if(resultCode == Activity.RESULT_OK){
                    Log.e("completeddf","cofdf");
                    // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
                    // Change PAYMENT_INTENT_SALE to
                    //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
                    //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
                    //     later via calls from your server.
                    condition_ids = data.getIntArrayExtra(PatientConditionDialog.PATIENT_CONDITIONS);

                    PayPalPayment payment = new PayPalPayment(new BigDecimal(data.getDoubleExtra(Constants.EXTRA_FINAL_RATE, 0)),
                            "USD", "Love on Wheels Appointment",
                            PayPalPayment.PAYMENT_INTENT_SALE);

                    Intent intent = new Intent(this.getActivity(), PaymentActivity.class);

                    // send the same configuration for restart resiliency
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                    startActivityForResult(intent, PAYMENT_REQUEST_CODE);
                }
                break;
            case PAYMENT_REQUEST_CODE:
                Log.e("completed","com3");
                if(resultCode == Activity.RESULT_OK){
                    Log.e("completed","com4");
                    PaymentConfirmation confirm_payment = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if(confirm_payment != null){
                        Log.e("completed","completed");
                       try {
                            Log.i("paymentExample", confirm_payment.toJSONObject().toString(4));
                            Log.e("fdfdfdf2323","dfdfdf1234");
                            new_appointment.patient_id = query_bundle.getInt(Constants.EXTRA_PATIENT_ID);
                            Log.e("fdfdfdf2323","dfdfdf123");
                            new_appointment.status = 1;

                            new_appointment.subservice_id = query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID);

                           new_appointment.patient_id = query_bundle.getInt(Constants.EXTRA_PATIENT_ID);

                           Log.e("sub",String.valueOf(new_appointment.subservice_id));
                           Log.e("patient",String.valueOf(query_bundle.getInt(Constants.EXTRA_PATIENT_ID)));
;

                            new_appointment.appointment_date = input_sch_date.getText().toString();
                            new_appointment.appointment_start_time = starttime.getTimevalue();
                            new_appointment.appointment_end_time = (starttime.getTimevalue()+1);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void postNewSingleAppointment(){

        Log.e("completed","com2");
        progressDialog.setMessage(getActivity().getString(R.string.submitting_your_booking));
        progressDialog.show();
        //  Log.e("appiontme",new_appointment.additionalinformation);

        Call<Appointment> postAppointment = appointmentService.getService().postAppointment(new_appointment);
        postAppointment.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if(response.code() == 201){

                    //  app_condition.appointment_id = response.body().appointment_id;
                    //  if(app_condition.appointment_id != 0)
                    //  postAppointmentCondition();
                   // mListener.OnPostCompleted(true);

                    Log.e("completed",String.valueOf(response.code()));
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                    builder.setMessage( "YOur appointment has been confirmed")
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                    dismiss();

                }
                else {
                   /* mListener.OnPostCompleted(false);*/
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("completed","completed");
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
              //  mListener.OnPostCompleted(false);

                Log.e("failed",t.getMessage());
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    private void postNewMultiAppointment(){

    }


    private void setSpinners(){
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = position;
                Log.e("spinner position",String.valueOf(position));
                Log.e("spinner position",String.valueOf(id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = 0;
            }


        });

        frq_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frequency = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                frequency = 0;
            }


        });


    }

    public void partialmatch(){

        progressDialog.dismiss();
        progressDialog.setMessage("No direct match found, Searching for partial match");
        progressDialog.show();
            Log.e("here121","here121");

            listselectiontype = 0;
            practitioners = new ArrayList<>();
            Log.e("here122","here122");
            int subservice_id = query_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID);
            Log.e("here123","here123");
            int city_id = query_bundle.getInt(Constants.EXTRA_CITY_ID);
            Log.e("here124","here124");
            String DateReq = input_sch_date.getText().toString();

            int timeReq = starttime.getTimevalue();
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


            Call<List<PractitionerPartial>> call = restPracService.getService().getPartialAvailPractitioners(subservice_id,
                    city_id, DateReq,timeReq,gender,frequency);
            Log.e("gender4",String.valueOf(gender));

            call.enqueue(new Callback<List<PractitionerPartial>>() {
                @Override
                public void onResponse(Call<List<PractitionerPartial>> call, Response<List<PractitionerPartial>> response) {

                    Log.e("hereunkown","2324");
                    int statusCode = response.code();
                    List<PractitionerPartial> practialList = response.body();
                    String msg = "here";



                    if (statusCode == 200) {



                        if(practialList.size() > 0){

                           /* Log.e("here21","here21");
                            listselectiontype = 2;
                            practitioners_list.setAdapter(new AvailablePractitionersListAdapter(getActivity().getBaseContext(), pracList));

                            //         practitioners_list.setOnItemClickListener(getActivity());

*/

                            progressDialog.dismiss();
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            builder.setMessage( "Sorry we could not find you a pratitioner that fits your schedule but we found "+ String.valueOf(practialList.size())+" practitioner that partial match fixs your needs.please ok to proceed to advanced search to customize booking")
                                    .setCancelable(false)
                                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            FragmentManager manager = getFragmentManager();

                                            AdvSearch advDialogFragment = AdvSearch.newInstance((ArrayList) practialList,DateReq,timeReq,frequency);
                                            advDialogFragment.setTargetFragment(thisfragement,RequestCode1);
                                            advDialogFragment.show(manager, "advsearch");
                                        }
                                    });

                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                            TextView messageText = (TextView)alert.findViewById(android.R.id.message);
                            messageText.setGravity(Gravity.CENTER);


                        }else{

                            Log.e("no appointmnet","no appointment found");



                        }


                        //  progress.dismiss();
                    }else{

                        Log.e("dfdf2", String.valueOf(statusCode));

                       //no match found at all
                        progressDialog.dismiss();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                        builder.setMessage("wW are sorry but no practitioner could be found ")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();



                    }
                    Log.e("dfdf1", msg);

                }



                @Override
                public void onFailure(Call<List<PractitionerPartial>> call, Throwable t) {
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


}
