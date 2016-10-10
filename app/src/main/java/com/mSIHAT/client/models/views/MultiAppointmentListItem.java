package com.mSIHAT.client.models.views;

/**
 * Created by alamchristian on 4/11/16.
 */
public class MultiAppointmentListItem {
    public int multi_appointment_id;
    public String subservice_name;
    public String patient_fullname;
    public int multi_appointment_total;

    public MultiAppointmentListItem(){

    }

    public MultiAppointmentListItem(int id, String subservice_name, String patient_fullname, int total){
        this.multi_appointment_id = id;
        this.subservice_name = subservice_name;
        this.patient_fullname = patient_fullname;
        this.multi_appointment_total = total;
    }
}
