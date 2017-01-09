package com.mSIHAT.client.fragments.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mSIHAT.client.APIServices.RestAppointmentService;
import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.APIServices.ServiceGenerator;
import com.mSIHAT.client.FileUploadServic;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.PractitionerDetailsFragment;
import com.mSIHAT.client.map.MapFragment;
import com.mSIHAT.client.map.MapFragment2;
import com.mSIHAT.client.models.LocationUpdate2;
import com.mSIHAT.client.models.Pratitioner2;
import com.mSIHAT.client.models.views.AppointmentDetails;
import com.mSIHAT.client.models.views.AppointmentFullContent;
import com.mSIHAT.client.models.views.PatientAppointment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

    double current_lat,current_long;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARCEL = "parcel_practitioner";
    private AppointmentFullContent practitioner;
    MarkerOptions Hcpmarker;
    Marker Hcpmark;
    private RestAppointmentService restUserService = new RestAppointmentService();
    public static PatientAppointment patient;
    private AppointmentDetails appDet;
    private int userid;
   Handler handler;
    CircularImageView searchView ;
    private RestPractitionerService restPracService = new RestPractitionerService();
    TextView prac_name,prac_time;
    ProgressDialog progress;

    ImageButton callBtn;

    RelativeLayout LinearBtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Practitioner_Location() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Practitioner_Location newInstance(AppointmentFullContent app) {
        Practitioner_Location fragment = new Practitioner_Location();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARCEL, app);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        return;

        if (getArguments() != null) {
            practitioner = getArguments().getParcelable(ARG_PARCEL);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.alerttheme2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practitioner__location, container, false);

        getDialog().requestWindowFeature(STYLE_NO_TITLE);



        ImageView bckBtn = (ImageView) view.findViewById(R.id.imgBack2);
       searchView = (CircularImageView ) view.findViewById(R.id.prac_image);
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

         callBtn = (ImageButton)view.findViewById(R.id.btnCall);

         LinearBtn = (RelativeLayout)view.findViewById(R.id.LinearCall);

        LinearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        prac_name = (TextView)view.findViewById(R.id.pracName);
        prac_time = (TextView)view.findViewById(R.id.pracTime);
        prac_name.setText(practitioner.hcp_name+" is");
        progress = new ProgressDialog(getActivity());

        progress.setMessage("Wait fetching location...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        final View rootView = getActivity().getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (savedInstanceState != null){
                            Location temp = new Location(LocationManager.GPS_PROVIDER);
                            temp.setLatitude(practitioner.hcp_loc_lat);
                            temp.setLongitude(practitioner.hcp_loc_long);
                            current_lat = practitioner.hcp_loc_lat;
                            current_long = practitioner.hcp_loc_long;
                            setMaplocation();
                           // gettime2(practitioner.patient_address,getAddressFromLatLng(temp));
                            getImageUrl();
                            //by now all views will be displayed with correct values
                            startLocationSync();

                        }else{
                            Location temp = new Location(LocationManager.GPS_PROVIDER);
                            temp.setLatitude(practitioner.hcp_loc_lat);
                            temp.setLongitude(practitioner.hcp_loc_long);
                            current_lat = practitioner.hcp_loc_lat;
                            current_long = practitioner.hcp_loc_long;
                            setMaplocation();
                            gettime(practitioner.patient_address,getAddressFromLatLng(temp));
                            getImageUrl();
                            //by now all views will be displayed with correct values
                            startLocationSync();
                        }





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
    public void gettime2(String address,String address2,double lat,double lon) {
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
                        fitmapscreen2(getLocationFromAddress(getContext(),practitioner.patient_address),new LatLng(lat,lon));
                    } catch (Exception e) {

                        Log.e("sdsd", e.getMessage());
                    }
                    progress.dismiss();
                } else {
                    msg = "error again";
                    try {
                        Log.e("sdsd", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progress.dismiss();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //   progress.dismiss();
                Log.e("sdsd", t.getMessage());

            }
        });





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
                            fitmapscreen(getLocationFromAddress(getContext(),practitioner.patient_address),new LatLng(practitioner.hcp_loc_lat,practitioner.hcp_loc_long));
                        } catch (Exception e) {

                            Log.e("sdsd", e.getMessage());
                        }
                        progress.dismiss();
                    } else {
                        msg = "error again";
                        try {
                            Log.e("sdsd", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
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
        try{
        Geocoder geocoder = new Geocoder(getContext());

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }

        return address;}
        catch(Exception ex){
            return "";
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void call(){

        Log.e("in the call option","option");
        String uri = "tel:" + practitioner.hcp_phonenumber;
        Intent intentCall = new Intent(Intent.ACTION_DIAL);
        intentCall.setData(Uri.parse(uri));
        startActivity(intentCall);
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

    public void setPatientMarker(MapFragment2 f,LatLng sydney){

        // mMap.addMarker(new MarkerOptions().position(sydney).icon(bit));
        // create marke
        MarkerOptions marker = new MarkerOptions().position(sydney);

            if (f != null){
                Log.e("found a map","found");
                // Changing marker icon
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                f.getMap().addMarker(marker);

            }



    }

    public void fitmapscreen(LatLng patientLAT,LatLng customerLAT){


        try{
            MapFragment2 f = (MapFragment2) getActivity().getSupportFragmentManager().findFragmentById(R.id.map2);
            if (f != null){
                setClientMarker(f,customerLAT);
                setPatientMarker(f,patientLAT);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(patientLAT);
                builder.include(customerLAT);
                LatLngBounds bounds = builder.build();
                f.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,150));

            }
        }catch(Exception ex){

        }

    }

    public void fitmapscreen2(LatLng patientLAT,LatLng customerLAT){


        try{
            MapFragment2 f = (MapFragment2) getActivity().getSupportFragmentManager().findFragmentById(R.id.map2);
            if (f != null){

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(patientLAT);
                builder.include(customerLAT);
                LatLngBounds bounds = builder.build();
                f.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,150));
               // f.getMap().animateCamera(CameraUpdateFactory.zoomTo(20f));
            }
        }catch(Exception ex){

        }

    }

    public void setMaplocation(){

        try{
            MapFragment2 f = (MapFragment2) getActivity().getSupportFragmentManager().findFragmentById(R.id.map2);
            if (f != null){

                f.getMap().moveCamera( CameraUpdateFactory.newLatLngZoom(getLocationFromAddress(getContext(),practitioner.patient_address) , 14.0f) );

            }
        }catch(Exception ex){

        }
    }

    public void setClientMarker(MapFragment2 f,LatLng sydney){
        // mMap.addMarker(new MarkerOptions().position(sydney).icon(bit));
        // create marke
       Hcpmarker = new MarkerOptions().position(sydney).title(practitioner.hcp_name);


            if (f != null){
                Log.e("found a map","found");
                // Changing marker icon
                Hcpmarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.hcpicon));
                Hcpmark = f.getMap().addMarker(Hcpmarker);


            }

    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void getImageUrl(){


                    Picasso.with(getContext())
                            .load(practitioner.hcp_url)
                            .fit().centerCrop()
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(searchView);

    }


    public void startLocationSync(){
        try {
            MapFragment2 f = (MapFragment2) getActivity().getSupportFragmentManager().findFragmentById(R.id.map2);
            if (f != null) {


            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 20 seconds

                    if (!getLocationFromServer()) {
                        MapFragment2 g = (MapFragment2) getActivity().getSupportFragmentManager().findFragmentById(R.id.map2);
                        if (g != null) {
                           // Log.e("current lat", "current long");
                           // Log.e(String.valueOf(practitioner.hcp_loc_lat), String.valueOf(practitioner.hcp_loc_long));
                            getLocation(f);

                            handler.postDelayed(this, 10000);
                        }
                    }
                }
            }, 10000);
            }
        }catch(Exception ex){

        }
    }


    public Boolean getLocationFromServer(){
        try {
            Log.e("Checking in", "checking in");
            Toast.makeText(getContext(), "checking in", Toast.LENGTH_SHORT).show();
       return false;
        }catch(Exception ex){
            return true;
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker,MapFragment2 map) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getMap().getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 10000;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                Log.e(String.valueOf(lat),String.valueOf(lng));
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(true);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }


        });
    }


    public boolean checkIfLocationSame(double new_location_lat , double new_location_long){

        if(new_location_lat == current_lat && current_long == new_location_long){

            return false;
        }else {
            current_lat = new_location_lat;
            current_long = new_location_long;
            return true;
        }

    }
    public void getLocation(MapFragment2 f){
        Log.e("getting location","trying to get location");

        Call<LocationUpdate2> call = restUserService.getService().getLocation(practitioner.practitioner_id);
        call.enqueue(new Callback<LocationUpdate2>() {
            @Override
            public void onResponse(Call<LocationUpdate2> call, Response<LocationUpdate2> response) {
                int statusCode = response.code();
               LocationUpdate2 location = response.body();
                String msg = null;
                if (statusCode == 200) {
                    if(checkIfLocationSame(location.location_lat,location.location_long)) {
                        gettime2(practitioner.patient_address, String.valueOf(location.location_lat) + "," + String.valueOf(location.location_long), location.location_lat, location.location_long);
                        animateMarker(Hcpmark, new LatLng(location.location_lat, location.location_long), false, f);
                    }
                }
                else
                    msg = String.valueOf(statusCode);

                Log.e("getting location",String.valueOf(statusCode));

            }

            @Override
            public void onFailure(Call<LocationUpdate2> call, Throwable t) {
                Log.e("getting location","failed here");
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });






    }
}
