package com.mSIHAT.client.models.views;

import android.os.Parcel;
import android.os.Parcelable;

import com.mSIHAT.client.models.Appointment;

/**
 * Created by ghost on 23/12/16.
 */
public class AppointmentFullContent implements Parcelable{
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
    public String patient_address;

    @Override
    public int describeContents() {
        return 0;
    }

    public AppointmentFullContent(Parcel in){
        String[] data = new String[22];
        in.readStringArray(data);

        this.appointment_id= Integer.parseInt(data[0]);
        this.patient_id=Integer.parseInt(data[1]);
        this.subservice_id =Integer.parseInt(data[2]);
        this.practitioner_id =Integer.parseInt(data[3]);
        this.slot_id =Integer.parseInt(data[4]);
        this.slot_breakdown_id =Integer.parseInt(data[5]);
        this.appointment_date = data[6];
        this.appointment_time = Integer.parseInt(data[7]);
        this.is_favorite =Integer.parseInt(data[8]);
        this.is_rated =Integer.parseInt(data[9]);
        this.status =Integer.parseInt(data[10]);
        this.rating =Integer.parseInt(data[11]);
        this.service_name = data[12];
        this.patient_name = data[13];
        this.hcp_url = data[14];
        this.hcp_name = data[15];
        this.hcp_qualification = data[16];
        this.hcp_gender = data[17];
        this.hcp_phonenumber = data[18];
        this.hcp_loc_lat= Double.parseDouble(data[19]);
        this.hcp_loc_long = Double.parseDouble(data[20]);
        this.patient_address = data[21];
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(this.appointment_id), String.valueOf(this.patient_id),
                String.valueOf(this.subservice_id), String.valueOf(this.practitioner_id),
                String.valueOf(this.slot_id), String.valueOf(this.slot_breakdown_id),
                this.appointment_date, String.valueOf(this.appointment_time),
                String.valueOf(this.is_favorite), String.valueOf(this.is_rated),
                String.valueOf(this.status), String.valueOf(this.rating),
               this.service_name,this.patient_name,this.hcp_url,this.hcp_name,this.hcp_qualification,
        this.hcp_gender,this.hcp_phonenumber,String.valueOf(this.hcp_loc_lat),String.valueOf(this.hcp_loc_long),
                String.valueOf(this.patient_address)});
    }

    public static final Parcelable.Creator<AppointmentFullContent> CREATOR = new Parcelable.Creator<AppointmentFullContent>(){

        @Override
        public AppointmentFullContent createFromParcel(Parcel source) {
            return new AppointmentFullContent(source);
        }

        @Override
        public AppointmentFullContent[] newArray(int size) {
            return new AppointmentFullContent[size];
        }
    };
}
