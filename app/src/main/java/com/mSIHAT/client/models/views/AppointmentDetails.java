package com.mSIHAT.client.models.views;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alamchristian on 3/29/16.
 */
public class AppointmentDetails implements Parcelable {
    public int appointment_id;
    public String subservice_name;
    public String appointment_date;
    public String appointment_time;
    public int status;

    public AppointmentDetails(){

    }

    public AppointmentDetails(int id, String subservice_name,
                              String datetime, int status){
        this.appointment_id = id;
        this.subservice_name = subservice_name;
        assignDateTime(datetime);
        this.status = status;
    }

    public AppointmentDetails(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.appointment_id = Integer.parseInt(data[0]);
        this.subservice_name = data[1];
        this.appointment_date = data[2];
        this.appointment_time = data[3];
        this.status = Integer.parseInt(data[4]);
    }

    public void assignDateTime(String datetime){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN).parse(datetime);
            appointment_date = new SimpleDateFormat("dd/MM/yyyy", Locale.TAIWAN).format(date);
            appointment_time = new SimpleDateFormat("HH:mm", Locale.TAIWAN).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(this.appointment_id), this.subservice_name,
                                                this.appointment_date, this.appointment_time,
                                                String.valueOf(this.status)});
    }

    public static final Parcelable.Creator<AppointmentDetails> CREATOR = new Parcelable.Creator<AppointmentDetails>(){

        @Override
        public AppointmentDetails createFromParcel(Parcel source) {
            return new AppointmentDetails(source);
        }

        @Override
        public AppointmentDetails[] newArray(int size) {
            return new AppointmentDetails[size];
        }
    };
}
