package com.mSIHAT.client.models.views;

import com.mSIHAT.client.models.Appointment;

/**
 * Created by ghost on 23/12/16.
 */
public class AppointmentFullContent {
    public int appointment_id;
    public int patient_id;
    public int subservice_id ;
    public int practitioner_id ;
    public int slot_id ;
    public int slot_breakdown_id ;
    public String appointment_date ;
    public int appointment_time ;
    public int is_favorite ;
    public int is_rated ;
    public int status ;
    public int rating ;
    public String service_name ;
    public String patient_name ;
    public String hcp_url ;
    public String hcp_name ;
    public String hcp_qualification ;
    public String hcp_gender ;
    public String hcp_phonenumber ;
    public double hcp_loc_lat;
    public double hcp_loc_long ;
}
