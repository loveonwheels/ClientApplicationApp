package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 4/4/16.
 */
public class AppointmentCondition implements Parcelable{
    public int condition_id;
    public String mobility;
    public String feeding;
    public String toileting;
    public String incontinent;
    public String urinary;
    public String wound;
    public String mental;

    public AppointmentCondition(){

    }

    public AppointmentCondition(Parcel in){
        String[] data = new String[8];

        in.readStringArray(data);
        this.condition_id = Integer.parseInt(data[0]);
        this.mobility = data[1];
        this.feeding = data[2];
        this.toileting = data[3];
        this.incontinent = data[4];
        this.urinary = data[5];
        this.wound = data[6];
        this.mental = data[7];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.condition_id), this.mobility,
        this.feeding, this.toileting, this.incontinent, this.urinary, this.wound, this.mental});
    }

    public static final Parcelable.Creator<AppointmentCondition> CREATOR = new Creator<AppointmentCondition>() {
        @Override
        public AppointmentCondition createFromParcel(Parcel source) {
            return new AppointmentCondition(source);
        }

        @Override
        public AppointmentCondition[] newArray(int size) {
            return new AppointmentCondition[size];
        }
    };
}
