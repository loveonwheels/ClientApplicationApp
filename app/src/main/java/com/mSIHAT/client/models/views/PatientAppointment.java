package com.mSIHAT.client.models.views;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/29/16.
 */
public class PatientAppointment implements Parcelable {
    public int patient_id;
    public String patient_fullname;
    public String patient_nric;
    public String patient_address;

    public PatientAppointment(){

    }

    public PatientAppointment(int id, String name, String nric, String address){
        this.patient_id = id;
        this.patient_fullname = name;
        this.patient_nric = nric;
        this.patient_address = address;
    }

    public PatientAppointment(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.patient_id = Integer.parseInt(data[0]);
        this.patient_fullname = data[1];
        this.patient_nric = data[2];
        this.patient_address = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.patient_id), this.patient_fullname,
                                            this.patient_nric, this.patient_address});
    }

    public static final Parcelable.Creator<PatientAppointment> CREATOR = new Parcelable.Creator<PatientAppointment>(){

        @Override
        public PatientAppointment createFromParcel(Parcel source) {
            return new PatientAppointment(source);
        }

        @Override
        public PatientAppointment[] newArray(int size) {
            return new PatientAppointment[size];
        }
    };
}
