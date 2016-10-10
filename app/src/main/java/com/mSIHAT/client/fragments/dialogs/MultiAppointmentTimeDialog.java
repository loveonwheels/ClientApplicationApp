package com.mSIHAT.client.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.selectors.MultiAppointmentAmountDialog;
import com.mSIHAT.client.fragments.dialogs.selectors.MultiAppointmentFrequencyDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by alamchristian on 4/7/16.
 */
public class MultiAppointmentTimeDialog extends DialogFragment implements View.OnClickListener {
    public static final String MULTI_APPOINTMENT_DATE = "multi_date";
    public static final String MULTI_APPOINTMENT_TIME = "multi_time";
    public static final String MULTI_APPOINTMENT_FREQUENCY = "multi_frequency";
    public static final String MULTI_APPOINTMENT_AMOUNT = "multi_amount";

    public static final int FREQUENCY_DIALOG = 11;
    public static final int AMOUNT_DIALOG = 12;

    private EditText edit_date, edit_time;
    private EditText edit_frequency, edit_amount;
    private Button btn_confirm, btn_cancel;

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private String dateString, timeString;
    private boolean isDaily;
    private int amount;

    private FragmentActivity callingActivity;

    public MultiAppointmentTimeDialog(){

    }

    public static MultiAppointmentTimeDialog newInstance(){
        return new MultiAppointmentTimeDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callingActivity = (FragmentActivity) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.TAIWAN);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_multi_appointment_time, null);
        findViewById(dialogView);

        builder.setView(dialogView);

        return builder.create();
    }

    private void findViewById(View view){
        prepareDateTimePicker();
        edit_date = (EditText) view.findViewById(R.id.edit_newapp_multi_start_date);
        edit_date.setInputType(InputType.TYPE_NULL);
        edit_date.setOnClickListener(this);
        edit_time = (EditText) view.findViewById(R.id.edit_newapp_multi_time);
        edit_time.setInputType(InputType.TYPE_NULL);
        edit_time.setOnClickListener(this);
        edit_frequency = (EditText) view.findViewById(R.id.edit_newapp_multi_frequency);
        edit_frequency.setInputType(InputType.TYPE_NULL);
        edit_frequency.setOnClickListener(this);
        edit_amount = (EditText) view.findViewById(R.id.edit_newapp_multi_amount);
        edit_amount.setInputType(InputType.TYPE_NULL);

        btn_confirm = (Button) view.findViewById(R.id.btn_newapp_multi_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel = (Button) view.findViewById(R.id.btn_newapp_multi_cancel);
        btn_cancel.setOnClickListener(this);
    }

    private void prepareDateTimePicker(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = 0;

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                dateString = dateFormatter.format(date.getTime());
                edit_date.setText(dateString);
            }
        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        datePickerDialog.setTitle(getString(R.string.starting_date));

        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeString = String.valueOf(hourOfDay) + ":00";
                edit_time.setText(timeString);
            }
        }, hour, minute, true);
        timePickerDialog.setTitle(getString(R.string.appointment_time));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_newapp_multi_start_date:
                datePickerDialog.show();
                break;
            case R.id.edit_newapp_multi_time:
                timePickerDialog.show();
                break;
            case R.id.edit_newapp_multi_frequency:
                MultiAppointmentFrequencyDialog frequencyDialog = MultiAppointmentFrequencyDialog.newInstance();
                frequencyDialog.setTargetFragment(MultiAppointmentTimeDialog.this, FREQUENCY_DIALOG);
                frequencyDialog.show(callingActivity.getSupportFragmentManager(), "frequencyDialog");
                break;
            case R.id.edit_newapp_multi_amount:
                if(edit_frequency.getText().toString().equalsIgnoreCase("daily"))
                    isDaily = true;
                else
                    isDaily = false;
                MultiAppointmentAmountDialog amountDialog = MultiAppointmentAmountDialog.newInstance(isDaily);
                amountDialog.setTargetFragment(MultiAppointmentTimeDialog.this, AMOUNT_DIALOG);
                amountDialog.show(callingActivity.getSupportFragmentManager(), "amountDialog");
                break;
            case R.id.btn_newapp_multi_cancel:
                this.dismiss();
                break;
            case R.id.btn_newapp_multi_confirm:
                if(checkEntries() == -1) {
                    Intent intent = getActivity().getIntent();
                    intent.putExtra(MULTI_APPOINTMENT_DATE, dateString);
                    intent.putExtra(MULTI_APPOINTMENT_TIME, timeString);
                    intent.putExtra(MULTI_APPOINTMENT_FREQUENCY, isDaily);
                    intent.putExtra(MULTI_APPOINTMENT_AMOUNT, amount);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                            intent);
                    this.dismiss();
                } else {
                    Toast.makeText(MultiAppointmentTimeDialog.this.getContext(),
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
            case FREQUENCY_DIALOG:
                if(resultCode == Activity.RESULT_OK){
                    String frequency = data.getStringExtra(MultiAppointmentFrequencyDialog.SELECTED_FREQUENCY);
                    edit_frequency.setText(frequency);
                    edit_amount.setOnClickListener(this);
                }
                break;
            case AMOUNT_DIALOG:
                if(resultCode == Activity.RESULT_OK){
                    amount = data.getIntExtra(MultiAppointmentAmountDialog.SELECTED_AMOUNT,0);
                    edit_amount.setText(String.valueOf(amount));
                }
                break;
            default:
                break;
        }
    }

    private int checkEntries(){
        int errorAt = -1;
        String[] entries = new String[4];
        entries[0] = edit_date.getText().toString();
        entries[1] = edit_time.getText().toString();
        entries[2] = edit_frequency.getText().toString();
        entries[3] = edit_amount.getText().toString();
        for(int i = 0 ; i < entries.length ; i++){
            if(entries[i] == null && entries[i].length() < 1){
                errorAt = i;
                break;
            }
        }
        return errorAt;
    }
}
