package com.mSIHAT.client.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.TimeSlots;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by alamchristian on 4/1/16.
 */
public class SingleAppointmentTimeDialog extends DialogFragment implements View.OnClickListener {
    public static final String SINGLE_APPOINTMENT_DATE = "single_date";
    public static final String SINGLE_APPOINTMENT_TIME = "single_time";
    private static final int RequestCode2 = 14402;
    private EditText edit_date, edit_time;
    private Button btn_confirm, btn_cancel;
    TimeSlots endtime;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter,dateFormatter2;
Fragment thisfragment;
    private String dateString;
    private int timeString;

    public SingleAppointmentTimeDialog(){

    }

    public static SingleAppointmentTimeDialog newInstance(){
        return new SingleAppointmentTimeDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.TAIWAN);
        dateFormatter2 = new SimpleDateFormat("MM/dd/yyyy", Locale.TAIWAN);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_single_appointment_time, null);
        findViewById(dialogView);

        builder.setView(dialogView);
thisfragment = this;
        return builder.create();
    }

    private void findViewById(View view){
        edit_date = (EditText) view.findViewById(R.id.edit_newapp_single_date);
        edit_date.setInputType(InputType.TYPE_NULL);
        edit_date.setOnClickListener(this);
        edit_time = (EditText) view.findViewById(R.id.edit_newapp_single_time);
        edit_time.setInputType(InputType.TYPE_NULL);
        edit_time.setOnClickListener(this);
        prepareDateTimePicker();

        btn_confirm = (Button) view.findViewById(R.id.btn_newapp_single_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel = (Button) view.findViewById(R.id.btn_newapp_single_cancel);
        btn_cancel.setOnClickListener(this);

    }

    private void prepareDateTimePicker(){
        Calendar cal = Calendar.getInstance();
      //  cal.add(Calendar.DAY_OF_YEAR, 2);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = 0;

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                dateString = dateFormatter2.format(date.getTime());
                edit_date.setText(dateString);
            }
        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        datePickerDialog.setTitle(getString(R.string.appointment_date));

        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                edit_time.setText(timeString);
            }
        }, hour, minute, true);
        timePickerDialog.setTitle(getString(R.string.appointment_time));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.edit_newapp_single_date:
                datePickerDialog.show();
                break;
            case R.id.edit_newapp_single_time:
Log.e("am here bitch","sdfsdf");
                FragmentManager manager = getFragmentManager();
                SlotDialogFragment slotDialogFragment = SlotDialogFragment.newInstance(RequestCode2,0,23);
                slotDialogFragment.setTargetFragment(thisfragment,RequestCode2);
                slotDialogFragment.show(manager, "timeslots");
                break;
            case R.id.btn_newapp_single_cancel:
                this.dismiss();
                break;
            case R.id.btn_newapp_single_confirm:
                if(checkEntries() == -1) {
                    Intent intent = getActivity().getIntent();
                    intent.putExtra(SINGLE_APPOINTMENT_DATE, dateString);
                    intent.putExtra(SINGLE_APPOINTMENT_TIME, timeString);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                            intent);
                    this.dismiss();
                } else {
                    Toast.makeText(SingleAppointmentTimeDialog.this.getContext(),
                            R.string.please_check_your_inputs, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private int checkEntries(){
        int errorAt = -1;
        String[] entries = new String[2];
        entries[0] = edit_date.getText().toString();
        entries[1] = edit_time.getText().toString();
        for(int i = 0 ; i < entries.length ; i++){
            if(entries[i] == null || entries[i].length() < 1){
                errorAt = i;
                break;
            }
        }
        return errorAt;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //do what ever you want here, and get the result from intent like below
        // String myData = data.getStringExtra("listdata");
        // Toast.makeText(getActivity(),data.getStringExtra("listdata"), Toast.LENGTH_SHORT).show();
 if(requestCode == RequestCode2){
Log.e("herer124","Sdsd");
   endtime = new TimeSlots(data.getIntExtra("timevalue", 0), true);
     timeString = endtime.getTimevalue();
     edit_time.setText(endtime.getTimeStringvalue());


        }
    }


}
