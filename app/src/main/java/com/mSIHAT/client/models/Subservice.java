package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/22/16.
 */
public class Subservice implements Parcelable{
    public int subservice_id;
    public String subservice_name;
    public int service_id;

    public Subservice(){

    }

    public Subservice(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.subservice_id = Integer.parseInt(data[0]);
        this.subservice_name = data[1];
        this.service_id = Integer.parseInt(data[2]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(this.subservice_id), this.subservice_name,
                                            String.valueOf(this.service_id)});
    }

    public static final Parcelable.Creator<Subservice> CREATOR = new Parcelable.Creator<Subservice>(){

        @Override
        public Subservice createFromParcel(Parcel source) {
            return new Subservice(source);
        }

        @Override
        public Subservice[] newArray(int size) {
            return new Subservice[size];
        }
    };
}
