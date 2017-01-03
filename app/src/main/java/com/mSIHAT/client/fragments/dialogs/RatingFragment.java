package com.mSIHAT.client.fragments.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestAppointmentService;
import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.APIServices.RestUserService;
import com.mSIHAT.client.LoginActivity;
import com.mSIHAT.client.MainActivity;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.PractitionerDetailsFragment;
import com.mSIHAT.client.models.Date.DateFormatter;
import com.mSIHAT.client.models.Date.TimeSlotUtil;
import com.mSIHAT.client.models.UserP;
import com.mSIHAT.client.models.views.AppointmentFullContent;
import com.mSIHAT.client.utils.Constants;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RatingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int SINGLE_TIME_DIALOG = 1221;
private RatingBar ratingBar;
    TextView txtdate,txttime,txtlanguage,txtname,txtgender,txtservice,txtpatient;
    TextView ratingRequest;
    CircularImageView searchView ;
    private RestPractitionerService practService = new RestPractitionerService();
    ProgressDialog progress;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    AppointmentFullContent userP;

    private RestAppointmentService restUserService;
    private OnFragmentInteractionListener mListener;

    public RatingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RatingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RatingFragment newInstance(int appointment_id) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, appointment_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.alerttheme2);

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


    /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View view = inflater.inflate(R.layout.fragment_rating, container, false);
       // ratingBar = (RatingBar)view.findViewById(R.id.ratingBar2) ;
       // ratingRequest = (TextView) view.findViewById(R.id.ratingrequest);
        txtdate = (TextView)view.findViewById(R.id.txtAppDetDate);
        txttime = (TextView)view.findViewById(R.id.txtAppDetTime);
        txtpatient = (TextView)view.findViewById(R.id.det_patient_name);
        txtservice = (TextView)view.findViewById(R.id.det_service_name);

        searchView = (CircularImageView ) view.findViewById(R.id.det_practitioner_image);
        txtlanguage = (TextView)view.findViewById(R.id.det_practitioner_language);
                txtname = (TextView)view.findViewById(R.id.det_practitioner_name);
                        txtgender = (TextView)view.findViewById(R.id.det_practitioner_gender);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                return true;
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp_toolbar_action);
toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dismiss();
    }
});

      /*  ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingRequest.setText(String.valueOf(rating));
                if(rating <= 1){
                    ratingRequest.setText("You service was very bad!");

                }else if(rating ==2 ){
                    ratingRequest.setText("You service was bad!");
                }else if(rating == 3){
                    ratingRequest.setText("You service was ok!");
                }else if(rating ==4){
                    ratingRequest.setText("You service was good!");
                }else if(rating == 5){
                    ratingRequest.setText("You service was very good!");
                }else{
                    ratingRequest.setText("Please rate me");
                }
            }
        });

        */
       // toolbar.setTitle("Appointment Detail");
        restUserService = new RestAppointmentService();
     getinfomation();
// To dismiss the dialog
        //progress.dismiss();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


public void getinfomation(){

    progress = new ProgressDialog(getActivity());
    progress.setTitle("Loading");
    progress.setMessage("Wait while loading...");
    //progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
    progress.show();
    Call<AppointmentFullContent> call = restUserService.getService().getAppointmentDetails( mParam1);
    call.enqueue(new Callback<AppointmentFullContent>() {
        @Override
        public void onResponse(Call<AppointmentFullContent> call, Response<AppointmentFullContent> response) {
            int statusCode = response.code();
            userP = response.body();
            String msg = null;
            if (statusCode == 200) {
                try {
                    setInformation(userP);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else
                msg = String.valueOf(statusCode);
           progress.dismiss();

        }

        @Override
        public void onFailure(Call<AppointmentFullContent> call, Throwable t) {
            progress.dismiss();
            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
        }
    });





}


   public void setInformation(AppointmentFullContent appointment) throws ParseException {
       txtdate.setText("Date : "+new DateFormatter(appointment.appointment_date).getDateWithDay());
       txttime.setText("Time : "+TimeSlotUtil.getTimeStringValue(appointment.appointment_time)+"     ");

       Picasso.with(getContext())
               .load(appointment.hcp_url)
               .fit().centerCrop()
               .networkPolicy(NetworkPolicy.NO_CACHE)
               .memoryPolicy(MemoryPolicy.NO_CACHE)
               .into(searchView);

       txtgender.setText(appointment.hcp_gender);
       txtlanguage.setText(appointment.hcp_qualification);
       txtname.setText(appointment.hcp_name);
       txtpatient.setText("Patient Name : "+appointment.patient_name);
       txtservice.setText("Service Name : "+appointment.service_name);

       Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Roboto-Regular.ttf");
       Typeface custom_font_bold = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/DroidSans-Bold.ttf");
       txtservice.setTypeface(custom_font);
       txtpatient.setTypeface(custom_font);
       txtdate.setTypeface(custom_font_bold);
       txttime.setTypeface(custom_font_bold);
       txtgender.setTypeface(custom_font);
       txtlanguage.setTypeface(custom_font);
       txtname.setTypeface(custom_font);



   }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void makefavorite(){
        Call<Boolean> practSer = practService.getService().makefavorite(userP.practitioner_id,userP.appointment_id);
        practSer.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 201){


                }
                else {

                   // btn_favorite.setVisibility(View.INVISIBLE);
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
     /* Practitioner_Location pracLocation =  Practitioner_Location.newInstance( practitioner, appointmentid,appDet,patient);
        pracLocation.setTargetFragment(RatingFragment.this, SINGLE_TIME_DIALOG);
        pracLocation.show(getActivity().getSupportFragmentManager(), "singleTimeDialog");
        */
    }


    public void callPractitioner(){
        String uri = "tel:" + userP.hcp_phonenumber;
        Intent intentCall = new Intent(Intent.ACTION_DIAL);
        intentCall.setData(Uri.parse(uri));
        startActivity(intentCall);
    }

}