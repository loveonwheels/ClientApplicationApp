package com.mSIHAT.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import com.mSIHAT.client.fragments.MyAccountFragment;

import com.mSIHAT.client.fragments.AppointmentsFragment;

import com.mSIHAT.client.fragments.MyPatientsFragment;
import com.mSIHAT.client.fragments.ServicesFragment;
import com.mSIHAT.client.fragments.pagerAdapters.AppointmentsFragmentPagerAdapter;
import com.mSIHAT.client.utils.Constants;

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

        main_bundle = callingIntent.getExtras();
        if (main_bundle != null) {
            nav_head_text_fullname.setText(main_bundle.getString(Constants.EXTRA_USER_FULLNAME));
            nav_head_text_email.setText(main_bundle.getString(Constants.EXTRA_USER_EMAIL));
            user_id = main_bundle.getInt(Constants.EXTRA_USER_ID);
        } else {
            Log.d("MainActivity", "Bundle is null");
        }
        fragTransaction = getSupportFragmentManager().beginTransaction();
        setupAppointmentsPagerAdapter(Constants.APPOINTMENT_STATUS_CONFIRMED);
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
        if(refresh_fragment){
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
        // Handle navigation view item clicks here.
        fragTransaction = getSupportFragmentManager().beginTransaction();
        Intent navIntent = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_appointments:
                setTitle(R.string.title_activity_main);
                setupAppointmentsPagerAdapter(Constants.APPOINTMENT_STATUS_CONFIRMED);
                break;
            case R.id.nav_pending_appointments:
                setTitle(R.string.pending_appointments);
                setupAppointmentsPagerAdapter(Constants.APPOINTMENT_STATUS_PENDING);
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
        if(resultCode == Activity.RESULT_OK){
            refresh_fragment = true;
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
}
