package com.mSIHAT.client;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mSIHAT.client.R;import com.mSIHAT.client.fragments.MyAccountUpdateFragment;
import com.mSIHAT.client.fragments.PatientRegistrationFragment;
import com.mSIHAT.client.models.UserP;
import com.mSIHAT.client.utils.Constants;

public class UpdateDetailsActivity extends AppCompatActivity implements PatientRegistrationFragment.OnPatientRegistrationAction,
                        MyAccountUpdateFragment.OnMyAccountUpdateFragmentListener{

    private String purpose;

    private FragmentTransaction fragTransaction = null;

    private Bundle editDetailsBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editDetailsBundle = getIntent().getExtras();
        purpose = editDetailsBundle.getString(Constants.KEY_UPDATE_DETAILS_PURPOSE);

        fragTransaction = getSupportFragmentManager().beginTransaction();
        if(purpose.equalsIgnoreCase(Constants.EXTRA_REGISTER_PURPOSE_PATIENT)){

            int user_id = editDetailsBundle.getInt(Constants.EXTRA_USER_ID);
            fragTransaction.replace(R.id.register_patient_frag_container, PatientRegistrationFragment.newInstance(user_id));

        } else if (purpose.equalsIgnoreCase(Constants.EXTRA_UPDATE_ACCOUNT_DETAILS)){
            UserP userP = editDetailsBundle.getParcelable(Constants.PARCEL_USER_OBJECT);
            fragTransaction.replace(R.id.register_patient_frag_container, MyAccountUpdateFragment.newInstance(userP));
        }
        fragTransaction.commit();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp_toolbar_action);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDetailsActivity.this.finish();
            }
        });
    }

    @Override
    public void onPatientRegistrationButtonClick(boolean confirm) {
        if(confirm){
            setResult(Activity.RESULT_OK);
            this.finish();
        } else {
            setResult(Activity.RESULT_CANCELED);
            this.finish();
        }
    }

    @Override
    public void onButtonActionBarButtonClick(boolean confirm) {
        if(confirm){
            setResult(Activity.RESULT_OK);
            this.finish();
        } else {
            setResult(Activity.RESULT_CANCELED);
            this.finish();
        }
    }
}
