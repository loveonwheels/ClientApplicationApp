package com.mSIHAT.client;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.PatientDetailsFragment;
import com.mSIHAT.client.utils.Constants;

public class DetailsActivity extends AppCompatActivity
        implements  PatientDetailsFragment.OnPatientDetailsFragmentInteractionListener{

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FrameLayout toolbarContent;
    private TextView toolbarMain, toolbarSecondary;
    private String purpose;
    private FragmentTransaction fragTransaction = null;

    private Bundle detailsBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_details);
        Toolbar toolbar = new Toolbar(this);

        detailsBundle = getIntent().getExtras();
        purpose = detailsBundle.getString(Constants.EXTRA_DETAILS_PURPOSE);

        fragTransaction = getSupportFragmentManager().beginTransaction();

        if(purpose.equalsIgnoreCase(Constants.EXTRA_DETAILS_PURPOSE_PATIENT)){
            LayoutInflater inflater = LayoutInflater.from(this);
            inflater.inflate(R.layout.collapsing_layout_patient_details, appBarLayout, true);

            toolbar = (Toolbar) findViewById(R.id.toolbar_patient_details);

            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.details_collapsing_toolbar);
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorTextIcon));

            int patient_id = detailsBundle.getInt(Constants.EXTRA_PATIENT_ID);
            fragTransaction.replace(R.id.details_fragment_container, PatientDetailsFragment.newInstance(patient_id));

        }
        setSupportActionBar(toolbar);
        fragTransaction.commit();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp_toolbar_action);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.this.finish();
            }
        });
    }

    @Override
    public void onFragmentCompleted(String fullname, String nric) {
        toolbarMain = (TextView) findViewById(R.id.toolbar_patient_details_fullname);
        toolbarSecondary = (TextView) findViewById(R.id.toolbar_patient_details_nric);

        toolbarMain.setText(fullname);
        toolbarSecondary.setText(nric);

        collapsingToolbarLayout.setTitle(fullname);
    }

    @Override
    public void onPatientDeleted() {
        setResult(1773);
        this.finish();
    }
}
