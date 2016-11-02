package com.mSIHAT.client.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mSIHAT.client.APIServices.ServiceGenerator;
import com.mSIHAT.client.FileUploadServic;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.PractitionerDetailsFragment;
import com.mSIHAT.client.map.MapFragment;
import com.mSIHAT.client.map.MapFragment2;
import com.mSIHAT.client.models.Pratitioner2;
import com.mSIHAT.client.models.views.AppointmentDetails;
import com.mSIHAT.client.models.views.PatientAppointment;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Practitioner_Location.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Practitioner_Location#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Practitioner_Location extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARCEL_PRACTITIONER = "parcel_practitioner";
    private static final String ARG_PARCEL_APPDET = "parcel_appointment";
    private static final String ARG_PARCEL_PATDET = "parcel_patient";
    private Pratitioner2 practitioner;
    public static PatientAppointment patient;
    private AppointmentDetails appDet;
    private int userid;

    TextView prac_name,prac_time;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Practitioner_Location() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Practitioner_Location newInstance(Pratitioner2 prac, int userid, AppointmentDetails app_parcel, PatientAppointment patient) {
        Practitioner_Location fragment = new Practitioner_Location();
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
            userid = getArguments().getInt("userid");
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.alerttheme2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practitioner__location, container, false);

        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        prac_name = (TextView)view.findViewById(R.id.pracName);
        prac_time = (TextView)view.findViewById(R.id.pracTime);
        prac_name.setText(practitioner.practitioner_fullname+" is");

        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(4.2105);
        temp.setLongitude(101);

        gettime(patient.patient_address,getAddressFromLatLng(temp));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void gettime(String address,String address2) {
            System.currentTimeMillis();
            ServiceGenerator service = new ServiceGenerator();
            FileUploadServic servicePone = service.createService(FileUploadServic.class);
            Call<ResponseBody> call = servicePone.getdistance(address,address2, "AIzaSyAzv1UFN6WfXNa8DJIIhox8Z9n0u_R008Q");

            Log.e("sdsd224545", call.request().url().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int statusCode = response.code();
                    Log.e("sdsd", response.message());
                    String msg = "sucess";
                    Log.e("sdsd", String.valueOf(statusCode));


                    if (statusCode == 200) {
                        try {
                            JSONObject jsonRes = new JSONObject(response.body().string());
                            Log.e("sdsd", jsonRes.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").get("text").toString());
                            Calendar now = Calendar.getInstance();
                            String[] timedate = jsonRes.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").get("text").toString().trim().split(" ");

                            if(timedate[1].trim().equals("mins")){

                                now.add(Calendar.MINUTE,Integer.parseInt(timedate[0]));
                            }else if(timedate[1] == "hours"){

                                now.add(Calendar.MINUTE,Integer.parseInt(timedate[0])*60 +Integer.parseInt(timedate[2])) ;

                            }

                            SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy hh:mm aa");



                           prac_time.setText( jsonRes.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").get("text").toString() + " away.");
                           // txtDistance.setText( jsonRes.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").get("text").toString()  + " | ");

                        } catch (Exception e) {

                            Log.e("sdsd", e.getMessage());
                        }

                    } else {
                        msg = "error again";
                        try {
                            Log.e("sdsd", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //   progress.dismiss();
                    Log.e("sdsd", t.getMessage());

                }
            });





    }

    private String getAddressFromLatLng(Location latLng) {
        Geocoder geocoder = new Geocoder(getActivity());

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }

        return address;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try{
            MapFragment2 f = (MapFragment2) getActivity().getSupportFragmentManager().findFragmentById(R.id.map2);
            if (f != null){
                Log.e("found a map","found");
                getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();

            }
        }catch(Exception ex){

        }


    }
}
