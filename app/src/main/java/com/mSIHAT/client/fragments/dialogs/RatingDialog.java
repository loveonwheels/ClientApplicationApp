package com.mSIHAT.client.fragments.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestUserService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.models.views.AppointmentFullContent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RatingDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RatingDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARCEL = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AppointmentFullContent appointmentDetails;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RatingBar ratingBar;
    TextView ratingRequest;
    ProgressDialog progress ;
    EditText comment;
Button ratingConfirm,ratingCancel;
    RestUserService restUserService2 = new RestUserService();
    private OnFragmentInteractionListener mListener;

    public RatingDialog() {
        // Required empty public constructor
    }

    public static RatingDialog newInstance(AppointmentFullContent app) {
        RatingDialog fragment = new RatingDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARCEL, app);
        fragment.setArguments(args);
        return fragment;
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            return;

        if (getArguments() != null) {
            appointmentDetails = getArguments().getParcelable(ARG_PARCEL);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.alerttheme2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_rating_dialog, container, false);

         ratingBar = (RatingBar)view.findViewById(R.id.ratingBarDialog) ;
        ratingRequest = (TextView) view.findViewById(R.id.ratingTexT);
        comment = (EditText) view.findViewById(R.id.ratingComment);
        ratingCancel = (Button) view.findViewById(R.id.ratingCan);
        ratingConfirm = (Button) view.findViewById(R.id.ratingCon);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingRequest.setText(String.valueOf(rating));
                if(rating <= 1){
                    ratingRequest.setText("You service was very bad!");

                }else if(rating == 2 || rating == 1.5 ){
                    ratingRequest.setText("You service was bad!");
                }else if(rating == 3 || rating == 2.5){
                    ratingRequest.setText("You service was ok!");
                }else if(rating ==4 || rating == 3.5){
                    ratingRequest.setText("You service was good!");
                }else if(rating == 5 || rating == 4.5){
                    ratingRequest.setText("You service was very good!");
                }else{
                    ratingRequest.setText("Please rate me");
                }
            }
        });


        ratingConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmServiceCompleted();
            }
        });

        ratingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void confirmServiceCompleted(){
        progress = new ProgressDialog(getActivity());

        progress.setMessage("Submiting rating...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Call<Boolean> call = restUserService2.getService().ConComsccepted(appointmentDetails.appointment_id,(int)(Math.round(ratingBar.getRating())),comment.getText().toString());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                int statusCode = response.code();
                Boolean userAppiontments = response.body();
                String msg = "here";


                if (statusCode == 200) {

                    Toast.makeText(getActivity(),"Service confirmed rendered go to completed service to rate the service",Toast.LENGTH_LONG).show();

                    getTargetFragment().onActivityResult(getTargetRequestCode(), 177899, getActivity().getIntent());
                     progress.dismiss();
                    dismiss();
                }else{
                    Log.e("dfdf2", msg);
                    msg="error again";
                    Toast.makeText(getActivity(),"Confirmation failed",Toast.LENGTH_LONG).show();
                    progress.dismiss();

                }
                Log.e("dfdf1", msg);

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                //   progress.dismiss();

                Toast.makeText(getActivity(),"request failed",Toast.LENGTH_LONG).show();
                progress.dismiss();

                //   Log.e("dfdf", t.toString());
            }
        });
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
}
