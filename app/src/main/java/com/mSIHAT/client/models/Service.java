package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/22/16.
 */
public class Service implements Parcelable {
    public int service_id;
    public String service_name;

    public Service(){

    }

    public Service(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.service_id = Integer.parseInt(data[0]);
        this.service_name = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.service_id), this.service_name});
    }

    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>(){

        @Override
        public Service createFromParcel(Parcel source) {
            return new Service(source);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };
}
