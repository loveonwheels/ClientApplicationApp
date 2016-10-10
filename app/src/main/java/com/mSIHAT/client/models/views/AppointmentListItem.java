package com.mSIHAT.client.models.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alamchristian on 3/23/16.
 */
public class AppointmentListItem {
    public int appointment_id;
    public String subservice_name;
    public String patient_fullname;
    public String appointment_date;
    public String appointment_time;
    public String appointment_day;

    public AppointmentListItem(){

    }

    public AppointmentListItem(int id, String subservice_name, String patient_fullname,
                               String datetime){
        this.appointment_id = id;
        this.subservice_name = subservice_name;
        this.patient_fullname = patient_fullname;
        this.assignDateTime(datetime);
    }

    public void assignDateTime(String datetime){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(datetime);
            appointment_day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
            appointment_date = new SimpleDateFormat("MMM  dd  yyyy", Locale.ENGLISH).format(date);
            appointment_time = new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
