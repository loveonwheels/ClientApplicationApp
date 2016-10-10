package com.mSIHAT.client.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alamchristian on 3/3/16.
 */
public class Patient {
    public int patient_id;
    public String patient_fullname;
    public String patient_nric;
    public String patient_relationship;
    public String patient_dob;
    public boolean patient_gender;
    public String patient_address;
    public String patient_phonenumber;
    public int postcode_id;
    public int user_id;

    public Patient(){

    }

    public Patient(String fullname, String nric, String relationship, String dob,
                   boolean gender, String address, int postcode_id, int user_id,String phonenumber){
        this.patient_fullname = fullname;
        this.patient_nric = nric;
        this.patient_relationship = relationship;
        this.patient_dob = dob;
        this.patient_dob = convertDob();
        this.patient_gender = gender;
        this.patient_address = address;
        this.postcode_id = postcode_id;
        this.user_id = user_id;
        this.patient_phonenumber = phonenumber;
    }

    public String getParsedDob(){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN).parse(patient_dob);
            patient_dob = new SimpleDateFormat("dd/MM/yyyy", Locale.TAIWAN).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return patient_dob;
    }

    private String convertDob(){
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy", Locale.TAIWAN).parse(patient_dob);
            patient_dob = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return patient_dob;
    }
}
