package com.mSIHAT.client;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mSIHAT.client.R;import com.mSIHAT.client.fragments.AvailablePractitionersFragment;
import com.mSIHAT.client.fragments.FragmentCompletedCallback;
import com.mSIHAT.client.utils.Constants;

public class AvailablePractitionersActivity extends AppCompatActivity implements FragmentCompletedCallback {

    Bundle query_bundle;

    private FragmentTransaction fragTransaction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_practitioners);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        query_bundle = getIntent().getExtras();

        if(query_bundle != null){
            fragTransaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frag_available_practitioners,
                            AvailablePractitionersFragment.newInstance(query_bundle),
                            Constants.AVAILABLE_PRACTITIONER_FRAGMENT_TAG);
            fragTransaction.commit();
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp_toolbar_action);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Constants.RESULT_NEUTRAL);
                AvailablePractitionersActivity.this.finish();
            }
        });
    }

    @Override
    public void OnPostCompleted(boolean completed) {
        if(completed){
            setResult(Activity.RESULT_OK);
            this.finish();
        } else {
            setResult(Activity.RESULT_CANCELED);this.finish();
        }
    }
}
