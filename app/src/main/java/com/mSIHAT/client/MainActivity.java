package com.mSIHAT.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mSIHAT.client.APIServices.PractitionerService;
import com.mSIHAT.client.APIServices.RestAppointmentService;
import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.fragments.MyAccountFragment;

import com.mSIHAT.client.fragments.AppointmentsFragment;

import com.mSIHAT.client.fragments.MyFavourite;
import com.mSIHAT.client.fragments.MyPatientsFragment;
import com.mSIHAT.client.fragments.NewAppointmentFragment;
import com.mSIHAT.client.fragments.ServicesFragment;
import com.mSIHAT.client.fragments.dialogs.RatingFragment;
import com.mSIHAT.client.fragments.dialogs.SelectPract;
import com.mSIHAT.client.fragments.pagerAdapters.AppointmentsFragmentPagerAdapter;
import com.mSIHAT.client.models.Appointment;
import com.mSIHAT.client.models.Practitioner;
import com.mSIHAT.client.models.Service;
import com.mSIHAT.client.utils.Constants;

import java.util.ArrayList;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        AppointmentsFragment.OnAppointmentsFragmentInteractionListener {
    public static final int ACTIVITY_RESULT_CONTENT_CHANGED = 11;

    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private Bundle main_bundle;
    private TextView nav_head_text_fullname, nav_head_text_email;
    private RestPractitionerService practService;
    private FragmentTransaction fragTransaction = null;

    private boolean refresh_fragment;

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById();

        Intent callingIntent = getIntent();
        main_bundle = callingIntent.getExtras();
        practService = new RestPractitionerService();
        main_bundle = callingIntent.getExtras();
        if (main_bundle != null) {
            nav_head_text_fullname.setText(main_bundle.getString(Constants.EXTRA_USER_FULLNAME));
            nav_head_text_email.setText(main_bundle.getString(Constants.EXTRA_USER_EMAIL));
            user_id = main_bundle.getInt(Constants.EXTRA_USER_ID);
        } else {
            Log.d("MainActivity", "Bundle is null");
        }
        fragTransaction = getSupportFragmentManager().beginTransaction();
      //  setupAppointmentsPagerAdapter(Constants.APPOINTMENT_STATUS_CONFIRMED);

        fragTransaction.replace(R.id.container_frag_main,NewAppointmentFragment.newInstance(user_id),
                "newappointmentfrag").commit();

        String tkn = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token oh24","Token ["+tkn+"]");


        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_DB, MODE_PRIVATE);
        int restoredUser = prefs.getInt("userid", 0);

        if(restoredUser == 0){
            Log.e("restoused ","empty");
            SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_DB, MODE_PRIVATE).edit();
            editor.putInt("userid", user_id);
            editor.commit();
        }

        if(tkn != null){
            String restoredText = prefs.getString("fireToken", null);
            if (restoredText != null) {
                if(tkn != restoredText){
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_DB, MODE_PRIVATE).edit();
                    editor.putInt("userid", user_id);
                    editor.putString("fireToken", tkn);
                    editor.commit();
                    updatetoken(user_id,tkn);

                }

            }else{
                SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_DB, MODE_PRIVATE).edit();
                editor.putInt("userid", user_id);
                editor.putString("fireToken", tkn);
                editor.commit();
                updatetoken(user_id,tkn);
            }


        }




    }

    private void findViewById(){
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nav_head_text_fullname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_heading_text_name);
        nav_head_text_email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_heading_text_email);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_appointment);
        fab.setOnClickListener(this);
    }

    private void setupAppointmentsPagerAdapter(int appointment_status){
        Fragment frag = this.getSupportFragmentManager().findFragmentByTag(Constants.MAIN_FRAGMENT_TAG);
        if(frag != null){
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.remove(frag).commit();
        }

        if(viewPager == null && tabLayout == null){
            LayoutInflater inflater = LayoutInflater.from(this);
            inflater.inflate(R.layout.tablayout_appointments_main, appBarLayout, true);

            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container_frag_main);
            inflater.inflate(R.layout.viewpager_appointments_main, frameLayout, true);

            viewPager = (ViewPager) findViewById(R.id.viewPager_appointments_main);
            tabLayout = (TabLayout) findViewById(R.id.tablayout_appointments_main);
        }
        Bundle bundle = new Bundle();
        Log.e("main activity userid",String.valueOf(user_id));
        bundle.putInt(Constants.EXTRA_USER_ID, user_id);
        bundle.putInt(Constants.EXTRA_APPOINTMENT_STATUS, appointment_status);

        if(viewPager.getAdapter() != null){
            AppointmentsFragmentPagerAdapter.app_status = appointment_status;
        } else {
            viewPager.setAdapter(new AppointmentsFragmentPagerAdapter(getSupportFragmentManager(), this, bundle));
        }
        viewPager.getAdapter().notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        setTabTitles();
    }

    private void setTabTitles(){
        tabLayout.getTabAt(0).setText("Single");
        tabLayout.getTabAt(1).setText("Multiple");
    }

    private void removeAppointmentsComponents(){
        if(tabLayout != null && viewPager != null) {
            ((ViewGroup) tabLayout.getParent()).removeView(tabLayout);
            ((ViewGroup) viewPager.getParent()).removeView(viewPager);
            tabLayout = null;
            viewPager = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  if(refresh_fragment){
            refresh_fragment = false;
            Fragment frag = this.getSupportFragmentManager().findFragmentByTag(Constants.MAIN_FRAGMENT_TAG);
            if(frag != null){
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.detach(frag).attach(frag).commit();
            } else {
                viewPager.getAdapter().notifyDataSetChanged();
                setTabTitles();
            }
        }

        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.x
        fragTransaction = getSupportFragmentManager().beginTransaction();
        Intent navIntent = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_appointments:
                setTitle("mSIHAT");
                removeAppointmentsComponents();
               // setupAppointmentsPagerAdapter(Constants.APPOINTMENT_STATUS_PENDING);

                NewAppointmentFragment f = (NewAppointmentFragment)getSupportFragmentManager().findFragmentByTag("Newappointment");
                if(f == null){
                    Log.e("found ur frag","found the frag");
                    removeAppointmentsComponents();
                    fragTransaction.replace(R.id.container_frag_main, NewAppointmentFragment.newInstance(user_id),
                            "Newappointment").commit();

                }

                break;
            case R.id.nav_pending_appointments:
                setTitle("Appointments");
                setupAppointmentsPagerAdapter(Constants.APPOINTMENT_STATUS_CONFIRMED);
                break;
            case R.id.nav_completed_appointments:
                setTitle(R.string.completed_appointments);
                setupAppointmentsPagerAdapter(Constants.APPOINTMENT_STATUS_COMPLETED);
                break;
            case R.id.nav_service_rates:
                setTitle(R.string.service_rates);
                removeAppointmentsComponents();
                fragTransaction.replace(R.id.container_frag_main, ServicesFragment.newInstance(),
                        Constants.MAIN_FRAGMENT_TAG).commit();
                break;
            case R.id.nav_my_account:
                setTitle(R.string.my_account);
                removeAppointmentsComponents();
                fragTransaction.replace(R.id.container_frag_main, MyAccountFragment.newInstance(user_id),
                        Constants.MAIN_FRAGMENT_TAG).commit();
                break;
            case R.id.nav_my_favourite:
                setTitle("My Favourite");
                removeAppointmentsComponents();
               fragTransaction.replace(R.id.container_frag_main, MyFavourite.newInstance(user_id),
                        Constants.MAIN_FRAGMENT_TAG).commit();
                break;
            case R.id.nav_my_patients:
                setTitle(R.string.my_patients);
                removeAppointmentsComponents();
                fragTransaction.replace(R.id.container_frag_main, MyPatientsFragment.newInstance(user_id),
                        Constants.MAIN_FRAGMENT_TAG).commit();
                break;
            case R.id.nav_logout:
               /* navIntent = new Intent(this, LoginActivity.class);
                navIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(navIntent);*/
                this.finish();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_new_appointment) {
            Intent newApp = new Intent(this, NewAppointmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.EXTRA_USER_ID, user_id);
            newApp.putExtras(bundle);
            startActivityForResult(newApp, ACTIVITY_RESULT_CONTENT_CHANGED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  if(resultCode == Activity.RESULT_OK){


            refresh_fragment = true;
        }

        */
try {
    Log.e("paypal got here in m", "here");
    Log.e("paypal got here", String.valueOf(requestCode));
    Log.e("paypal got here", String.valueOf(resultCode));


    if (resultCode == Activity.RESULT_OK) {
        Log.e("paypal got here", "here");
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("singleTimeDialog");
        fragment.onActivityResult(SelectPract.PAYMENT_REQUEST_CODE, resultCode, data);

    } else if (resultCode == Activity.RESULT_CANCELED) {
        if (requestCode == NewAppointmentFragment.ACTIVITY_RESULT_NEWAPP) {
        }
        if (resultCode == 1771 || resultCode == 1772) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.MAIN_FRAGMENT_TAG);
            fragment.onActivityResult(Constants.ACTIVITY_RESULT_PATIENT_REGISTER, Constants.ACTIVITY_RESULT_PATIENT_REGISTER, data);
        }
    }else if (resultCode == 1771 || resultCode == 1772 || resultCode == 1773) {
        Log.e("am here", "here 1232");
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.MAIN_FRAGMENT_TAG);
        fragment.onActivityResult(Constants.ACTIVITY_RESULT_PATIENT_REGISTER, Constants.ACTIVITY_RESULT_PATIENT_REGISTER, data);
    }

}catch(Exception ex){

}


    }

    @Override
    public void onAppointmentItemClick(long appointment_id) {

        Intent detailIntent = new Intent(this, AppointmentDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.EXTRA_APPOINTMENT_ID, appointment_id);
        detailIntent.putExtras(bundle);
        startActivityForResult(detailIntent, ACTIVITY_RESULT_CONTENT_CHANGED);

    }

    public int getuserid(){
        return user_id;
    }



    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

          Log.e("confiremed","sdsd");

        }
    }




    public void updatetoken(int userid,String token){

        Log.e(String.valueOf(userid),String.valueOf(token));
        Call<Boolean> practSer = practService.getService().Updatetoken(userid,token);
        practSer.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 201){



                }
                else {

                    Log.e("completed token2","completed");
                }
                Log.e("response",String.valueOf(response.code()));

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    }
