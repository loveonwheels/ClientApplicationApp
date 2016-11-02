package com.mSIHAT.client.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.PractitionerDetailsFragment;

import java.io.IOException;
import java.util.List;

/**
 * Created by ghost on 11/8/16.
 */
public class MapFragment2 extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
GoogleMap.OnCameraMoveListener,
        com.google.android.gms.location.LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {
    LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex = 0;
    private GoogleMap mMap;


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

      /*  MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(getAddressFromLatLng(latLng));

        options.icon(BitmapDescriptorFactory.defaultMarker());
        getMap().addMarker(options);

        */
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        /*
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(getAddressFromLatLng(latLng));

        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher)));

        getMap().addMarker(options);

        */

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }catch (Exception e){

        }

        return address;
    }

    private void drawCircle(LatLng location) {
      /*  CircleOptions options = new CircleOptions();
        options.center( location );
        //Radius in meters
        options.radius( 10 );
        options.fillColor( getResources()
                .getColor( R.color.fill_color ) );
        options.strokeColor( getResources()
                .getColor( R.color.stroke_color ) );
        options.strokeWidth( 10 );
        getMap().addCircle(options);
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mCurrentLocation = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);
if(mCurrentLocation != null){
   initCamera(mCurrentLocation);
    setMarker();
}else{









    Log.e("nolocation", "cannt find location");
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    mLocationRequest.setFastestInterval(FATEST_INTERVAL);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    LocationRequest xx = new LocationRequest();

    xx.setInterval(UPDATE_INTERVAL);
    xx.setFastestInterval(FATEST_INTERVAL);
    xx.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    xx.setSmallestDisplacement(DISPLACEMENT);
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, xx, this);
    /*mCurrentLocation = LocationServices
            .FusedLocationApi
            .getLastLocation(mGoogleApiClient);
    initCamera(mCurrentLocation);*/
}

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.map2);
            mapFrag.getMapAsync(this);
           // initListeners();
        }

    }

    private void initListeners() {
       mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
     mMap.setOnMapClickListener(this);
    }

    private void initCamera(Location location) {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .zoom(15f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

      /*  getMap().animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);
*/


        // mMap.setMapType(MAP_TYPES[curMapTypeIndex]);
        getMap().setTrafficEnabled(false);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getMap().setMyLocationEnabled(true);
       // getMap().getUiSettings().setZoomControlsEnabled(true);

    }


    public void setMarker(){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(PractitionerDetailsFragment.patient.patient_address, 5);
            if (address != null) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();



                LatLng sydney = new LatLng(4.2105, 101);

                // Add a marker in Sydney and move the camera

                mMap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));;

                LatLng currentlocation = new LatLng(mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude());

                // Add a marker in Sydney and move the camera

                //mMap.addMarker(new MarkerOptions().position(currentlocation));

                // create marker
                MarkerOptions marker = new MarkerOptions().position(currentlocation);

// Changing marker icon
                 marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.hcpiconmap));
                mMap.addMarker(marker);

                //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                //  initListeners();

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude()));
                builder.include(sydney);
                LatLngBounds bounds = builder.build();


                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                Log.e("sdfdsf","map2");
                mMap.animateCamera(cu);

                /*
                mMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                    public void onCancel(){}
                    public void onFinish(){
                        CameraUpdate zout = CameraUpdateFactory.zoomBy(-3);
                         mMap.animateCamera(zout);
                       // mMap.moveCamera(zout);
                    }
                });
                */
            }



        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(5.0f);
        mMap.setMaxZoomPreference(14.0f);

        /*
Log.e("sdfdsf","map1");
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(PractitionerDetailsFragment.patient.patient_address, 5);
            if (address != null) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());

                // Add a marker in Sydney and move the camera
                Log.e("sdfdsf","map3");
                 mMap.addMarker(new MarkerOptions().position(sydney));
                // create marker
               // MarkerOptions marker = new MarkerOptions().position(sydney).title("Hello Maps");

// Changing marker icon
               // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.hcpicon));
               // mMap.addMarker(marker);

              //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
              //  initListeners();

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude()));
                builder.include(sydney);
                LatLngBounds bounds = builder.build();

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                Log.e("sdfdsf","map2");
                mMap.moveCamera(cu);
                mMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                    public void onCancel(){}
                    public void onFinish(){
                        CameraUpdate zout = CameraUpdateFactory.zoomBy(-3);
                      //  mMap.animateCamera();
                        mMap.moveCamera(zout);
                    }
                });
            }



        } catch (Exception ex) {

            ex.printStackTrace();
        }

*/




    }



    public GoogleMap getMap(){
        return mMap;
    }


    @Override
    public void onLocationChanged(Location location) {
     //   CameraUpdateFactory.newLatLngBounds(bounds, 0)
       // initCamera(location);
    }


    @Override
    public void onCameraMove() {

    }
}
