package com.mSIHAT.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mSIHAT.client.R;import com.mSIHAT.client.fragments.NewAppointmentFragment;
import com.mSIHAT.client.utils.Constants;

public class NewAppointmentActivity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction = null;

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user_id = getIntent().getExtras().getInt(Constants.EXTRA_USER_ID);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp_toolbar_action);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewAppointmentActivity.this.finish();
            }
        });

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_frag_new_appointment, NewAppointmentFragment.newInstance(user_id),
                Constants.NEW_APPOINTMENT_FRAGMENT_TAG).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            setResult(Activity.RESULT_OK);
            this.finish();
        } else if (resultCode == Activity.RESULT_CANCELED){
            if(requestCode == NewAppointmentFragment.ACTIVITY_RESULT_NEWAPP) {
                Toast.makeText(NewAppointmentActivity.this,
                        R.string.please_try_again, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
