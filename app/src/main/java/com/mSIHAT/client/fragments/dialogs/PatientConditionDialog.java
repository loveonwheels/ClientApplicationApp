package com.mSIHAT.client.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.models.ServiceRate;
import com.mSIHAT.client.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alamchristian on 4/4/16.
 */
public class PatientConditionDialog extends DialogFragment implements View.OnClickListener {
    public static final int MOBILITY_DIALOG = 1;
    public static final int FEEDING_DIALOG = 2;
    public static final int TOILETING_DIALOG = 3;
    public static final int INCONTINENT_DIALOG = 4;
    public static final int URINARYCATHETER_DIALOG = 5;
    public static final int WOUNDDRESSING_DIALOG = 6;
    public static final int MENTAL_DIALOG = 7;

    public static final String PATIENT_CONDITIONS = "patient_condition";

    private ProgressDialog progressDialog;

    private double rate;
    private Bundle forRate_bundle;

    private EditText edit_mobility, edit_feeding, edit_toileting,
                    edit_incontinent, edit_urinary, edit_wound,
                    edit_mental;
    private TextView text_rate;
    private Button btn_confirm, btn_cancel;

    private FragmentActivity callingActivity;

    private RestPractitionerService practitionerService;

    private String patient_condition;

    private String[] condition_ids;

    public PatientConditionDialog(){

    }

    public static PatientConditionDialog newInstance(Bundle forRate_bundle){
        PatientConditionDialog dialog = new PatientConditionDialog();
        dialog.setArguments(forRate_bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        practitionerService = new RestPractitionerService();
        forRate_bundle = getArguments();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.briefly_describe_patient_conditions);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_appointment_condition, null);
        findViewById(dialogView);
        retrieveConditions();
        getFinalRate();

        builder.setView(dialogView);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callingActivity = (FragmentActivity) context;
        condition_ids = new String[7];
    }

    private void findViewById(View view){
        edit_mobility = (EditText) view.findViewById(R.id.edit_patient_condition_mobility);

        text_rate = (TextView) view.findViewById(R.id.text_rate_final);

        btn_confirm = (Button) view.findViewById(R.id.btn_patient_condition_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel = (Button) view.findViewById(R.id.btn_patient_condition_cancel);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btn_patient_condition_cancel:
                this.dismiss();
                break;
            case R.id.btn_patient_condition_confirm:
                if(checkEntries() == -1) {
                    condition_ids[0] = edit_mobility.getText().toString();

                  /*
                     getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                            getActivity().getIntent().putExtra(PATIENT_CONDITIONS, condition_ids)
                                    .putExtra(Constants.EXTRA_FINAL_RATE, rate));
                  */

                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                            getActivity().getIntent().putExtra(PATIENT_CONDITIONS, condition_ids)
                                    .putExtra(Constants.EXTRA_FINAL_RATE, 60.00));
                    this.dismiss();
                } else {
                    Toast.makeText(PatientConditionDialog.this.getContext(),
                            R.string.please_check_your_inputs, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){

        }
    }

    private void retrieveConditions(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(callingActivity.getString(R.string.preparing));
        progressDialog.show();

    }

    private void getFinalRate(){
        Call<ServiceRate> callRate = practitionerService.getService().getRateOfPractitionerForSubservice(
                forRate_bundle.getInt(Constants.EXTRA_SUBSERVICE_ID), forRate_bundle.getInt(Constants.EXTRA_PRACTITIONER_ID));
        callRate.enqueue(new Callback<ServiceRate>() {
            @Override
            public void onResponse(Call<ServiceRate> call, Response<ServiceRate> response) {
                if(response.code() == 200){
                    rate = response.body().rate;
                    if(forRate_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT) != 0){
                        rate = rate * forRate_bundle.getInt(Constants.EXTRA_MULTI_APPOINTMENT_AMOUNT);
                    }
                    text_rate.setText(String.valueOf(rate));
                }
            }

            @Override
            public void onFailure(Call<ServiceRate> call, Throwable t) {
                Toast.makeText(PatientConditionDialog.this.getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private int checkEntries(){
        int errorAt = -1;
        String[] entries = new String[7];
        entries[0] = edit_mobility.getText().toString();
            if(entries[0] == null || entries[0].length() < 1){
                errorAt = 0;

            }

        return errorAt;
    }
}
