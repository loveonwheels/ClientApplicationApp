package com.mSIHAT.client.models.Date;

import android.app.FragmentManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by ghost on 28/6/16.
 */
public class DateDialog {
    public static void getDate(FragmentManager f_manager, final EditText txt_date){

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                             @Override
                                                                             public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                                                                                 txt_date.setText((i1+1)+"/"+i2+"/"+i);
                                                                             }
                                                                         }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMaxDate(calendar);// You can add your value for YEARS_IN_THE_FUTURE.
        datePickerDialog.show(f_manager,"dob");

    }

    public static void getDate2(FragmentManager f_manager, final TextView txt_date){

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                             @Override
                                                                             public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                                                                                 txt_date.setText((i1+1)+"/"+i2+"/"+i);
                                                                             }
                                                                         }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(f_manager,"dob");

    }


}
