package com.mSIHAT.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestAppointmentService;
import com.mSIHAT.client.R;import com.mSIHAT.client.fragments.AppointmentsFragment;
import com.mSIHAT.client.models.MultiAppointment;
import com.mSIHAT.client.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alamchristian on 4/12/16.
 */
public class ListActivity extends AppCompatActivity implements AppointmentsFragment.OnAppointmentsFragmentInteractionListener {
    private ProgressDialog progressDialog;

    private int appointment_status;
    private int multi_appointment_id;

    private RestAppointmentService appointmentService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        appointment_status = bundle.getInt(Constants.EXTRA_APPOINTMENT_STATUS);
        multi_appointment_id = bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_ID);
        String purpose = bundle.getString(Constants.EXTRA_DETAILS_PURPOSE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(purpose.equalsIgnoreCase(Constants.EXTRA_DETAILS_PURPOSE_MULTI_APPOINTMENTS)){
            fragmentTransaction.replace(R.id.container_frag_list,
                    AppointmentsFragment.newInstance(bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_ID)));
        }

        fragmentTransaction.commit();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp_toolbar_action);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListActivity.this.finish();
            }
        });
    }

    @Override
    public void onAppointmentItemClick(long appointment_id) {
        Toast.makeText(getApplication(),String.valueOf(appointment_id),Toast.LENGTH_LONG).show();
        Intent detailIntent = new Intent(this, AppointmentDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.EXTRA_APPOINTMENT_ID, appointment_id);
        bundle.putBoolean(Constants.EXTRA_IS_MULTI_APPOINTMENT, true);
        detailIntent.putExtras(bundle);
        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch(appointment_status){
            case 1:
                getMenuInflater().inflate(R.menu.menu_pending_appointment_details, menu);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_appointment_cancel:
                cancelMultiAppointment();
                break;
            default:
                break;
        }
        return true;
    }

    private void cancelMultiAppointment(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.cancelling));
        progressDialog.show();

        appointmentService = new RestAppointmentService();
        Call<MultiAppointment> deleteMultiAppointment = appointmentService.getService().deleteMultiAppointmentById(multi_appointment_id);
        deleteMultiAppointment.enqueue(new Callback<MultiAppointment>() {
            @Override
            public void onResponse(Call<MultiAppointment> call, Response<MultiAppointment> response) {
                if(response.code() == 200){
                    Toast.makeText(ListActivity.this,
                            R.string.youve_cancelled_the_appointment, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    setResult(Activity.RESULT_OK);
                    ListActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<MultiAppointment> call, Throwable t) {
                Toast.makeText(ListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }
}
